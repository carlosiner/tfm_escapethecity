package uoc.tfm.escapethecity.game

import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import uoc.tfm.escapethecity.BaseActivity
import uoc.tfm.escapethecity.R
import uoc.tfm.escapethecity.data.GameItems
import uoc.tfm.escapethecity.data.GameTrials
import uoc.tfm.escapethecity.data.User
import uoc.tfm.escapethecity.data.UserRanking
import java.util.*
import kotlin.collections.HashMap

class GInvestigationActivity : BaseActivity(),
    NavigationView.OnNavigationItemSelectedListener{

    // Common Drawer definition
    private lateinit var drawerL: DrawerLayout
    private lateinit var currentPopupView: View
    // Game Trial selected
    var currentGameTrialKey = ""
    var lastPopupWindow = PopupWindow()
    var lastSuperPopupWindow = PopupWindow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ginvestigation)

        trialSelection()
    }

    /* --- Operations section --- */
    private fun trialSelection(){
        val trialMap = currentERUser.trials
        var flagFinish = false

        // Sort trial map
        var trialMapSorted = trialMap.toSortedMap()
        // Get the lower id with 0 points (Trial not finished)
        for (i in trialMapSorted){
            if(!i.value.t_finished){
                currentGameTrialValue = i.value
                currentGameTrialKey = i.key
                flagFinish = false
                break
            }
            else{
                flagFinish = true
            }
        }

        // Game is finished
        if (flagFinish){
            Toast.makeText(this, R.string.tv_game_investigation_correct_end, Toast.LENGTH_SHORT).show()

            if(currentERUser.user_status < 4){
                // Update the user current information to the final state
                currentERUser.user_status = 4 // Final status

                // Log event
                setUserLog(
                    getString(R.string.tv_game_userlog_title_EER),
                    getString(R.string.tv_game_userlog_desc_EER)
                )

                // Update Ranking
                updateRanking()

                // Update db
                updateUserEscapeRoom()
            }

            // Return to ER menu
            goEscapeRoom()
        }
        else{
            // And load the view information for trial
            loadTrialView()
        }
    }

    private fun updateRanking() {
        val db = FirebaseFirestore.getInstance()
        var userMap: HashMap<String, UserRanking> = hashMapOf()
        var userR = UserRanking()
        userR.user_name = userInfo.username!!
        userR.user_points = currentERUser.user_points
        // Encode needed as some characters (".", "/", ...) generates more objects than
        // expected in Firebase Firestore (when using it as values)
        var email = Uri.encode(userInfo.email!!).replace(".", "%2E")
        userMap[email] = userR

        try {
            db.collection("ranking").document(currentERId).get()
                .addOnSuccessListener {
                    if (it.data?.size != null) {
                        // Update the user Rankin
                        db.collection("ranking")
                            .document(currentERId)
                            .update(userMap as Map<String, Any>)
                    }
                    else{
                        //First time for Escape Room
                        db.collection("ranking")
                            .document(currentERId)
                            .set(userMap)
                    }
                }
        }
        catch (err: Exception){
            Log.d("error-db", "Cannot access to the DB to load the ranking info")
        }
    }

    private fun loadTrialView(){
        /* Load the view information */
        // Name/Title
        val name: TextView = findViewById(R.id.tv_game_investigation_title)
        name.text = currentGameTrialValue.t_name
        // Description
        val description: TextView = findViewById(R.id.tv_game_investigation_description)
        description.text = currentGameTrialValue.t_description
        // Image
        val image: ImageView = findViewById(R.id.iv_game_investigation_image)
        getImageFromURL(this, currentGameTrialValue.t_resource!!, image)
    }

    /* --- PopUp section --- */

    fun PopupWindow.dimBehind() {
        val container = contentView.rootView
        val context = contentView.context
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val p = container.layoutParams as WindowManager.LayoutParams
        p.flags = p.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.3f
        wm.updateViewLayout(container, p)
    }

    private fun showPopUp(view_tag: String){
        val notiInf: LinearLayout = findViewById(R.id.ll_popup)

        var popupLayout: Int? = null
        when(view_tag){
            "solve" -> popupLayout = R.layout.popup_investigation_solve
            "clues" -> popupLayout = R.layout.popup_investigation_clues
            "correct" -> popupLayout = R.layout.popup_investigation_correct
        }

        val notiView: View = LayoutInflater.from(this).inflate(
            popupLayout!!,notiInf,false)

        val popupW = PopupWindow(
            notiView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true)
        popupW.showAtLocation(notiInf, Gravity.CENTER, 0, 0)
        popupW.dimBehind()

        if (lastPopupWindow.contentView == null){
            lastPopupWindow = popupW
        }
        else{
            lastSuperPopupWindow = popupW
        }
        currentPopupView = notiView

        // Update Popup content
        if (view_tag == "clues"){
            var idTV = 0
            var idCB = 0

            // Set text for description
            var clueDescr: TextView = currentPopupView.findViewById(R.id.tv_game_investigation_clue1_description)
            clueDescr.text = currentERUser.trials[currentGameTrialKey]!!.t_clue1
            clueDescr = currentPopupView.findViewById(R.id.tv_game_investigation_clue2_description)
            clueDescr.text = currentERUser.trials[currentGameTrialKey]!!.t_clue2
            clueDescr = currentPopupView.findViewById(R.id.tv_game_investigation_clue3_description)
            clueDescr.text = currentERUser.trials[currentGameTrialKey]!!.t_clue3

            // Check if checkboxes were activated
            if(currentERUser.trials[currentGameTrialKey]!!.t_clue1_activated){
                idTV = R.id.tv_game_investigation_clue1_description
                idCB = R.id.cb_game_investigation_clue1
                activateClues(idTV, idCB, false)
            }
            if (currentERUser.trials[currentGameTrialKey]!!.t_clue2_activated){
                idTV = R.id.tv_game_investigation_clue2_description
                idCB = R.id.cb_game_investigation_clue2
                activateClues(idTV, idCB, false)
            }
            if (currentERUser.trials[currentGameTrialKey]!!.t_clue3_activated){
                idTV = R.id.tv_game_investigation_clue3_description
                idCB = R.id.cb_game_investigation_clue3
                activateClues(idTV, idCB, false)
            }
        }
        else if (view_tag == "solve"){
            var etSolve: EditText = currentPopupView.findViewById(R.id.et_game_investigation_check_hint)
            etSolve.hint = currentERUser.trials[currentGameTrialKey]!!.t_solution_tip
        }
    }

    private fun activateClues(idTV: Int, idCB: Int, clicked: Boolean){
        var clueDescr: TextView = currentPopupView.findViewById(idTV)
        clueDescr.visibility = View.VISIBLE
        var clueCheckBox: CheckBox = currentPopupView.findViewById(idCB)
        if (!clicked){
            // When is from a previous state
            clueCheckBox.isChecked = true
        }
        clueCheckBox.isEnabled = false

    }


    /* --- Actions section --- */

    // Actions selector
    fun selectActions(view: View){
        // Select a destination function based on the tag
        when(view.tag){
            "goBack" -> goBack()
            "b_game_investigation_solve" -> showPopUp("solve")
            "b_game_investigation_check" -> checkAnswer(view)
            "b_game_investigation_goBack" -> goPopUpBack()
            "b_game_investigation_correct_continue" -> goGame()
            "b_game_investigation_clues" -> showPopUp("clues")
            "cb_game_investigation_clue1" -> showClues("clue1")
            "cb_game_investigation_clue2" -> showClues("clue2")
            "cb_game_investigation_clue3" -> showClues("clue3")
            "b_game_investigation_end" -> endTrial()
        }
    }



    private fun showClues(clue: String) {
        var idTV = 0
        var idCB = 0
        var nameClue = ""
        when (clue){
            "clue1" -> {
                nameClue = getString(R.string.cb_game_investigation_clue1)
                idTV = R.id.tv_game_investigation_clue1_description
                idCB = R.id.cb_game_investigation_clue1
                currentERUser.trials[currentGameTrialKey]!!.t_clue1_activated = true}
            "clue2" -> {
                nameClue = getString(R.string.cb_game_investigation_clue2)
                idTV = R.id.tv_game_investigation_clue2_description
                idCB = R.id.cb_game_investigation_clue2
                currentERUser.trials[currentGameTrialKey]!!.t_clue2_activated = true}
            "clue3" -> {
                nameClue = getString(R.string.cb_game_investigation_clue3)
                idTV = R.id.tv_game_investigation_clue3_description
                idCB = R.id.cb_game_investigation_clue3
                currentERUser.trials[currentGameTrialKey]!!.t_clue3_activated = true}
        }

        activateClues(idTV, idCB, true)

        // Log event
        setUserLog(
            getString(R.string.tv_game_userlog_title_UC) + nameClue,
            getString(R.string.tv_game_userlog_desc_UC),
            -20
        )
        // Update ER User info
        updateUserEscapeRoom()

    }

    private fun checkAnswer(view: View){
        try{
            // If the item is used then END this trial
            if(currentGameTrialValue.t_id_item_used != ""
                && currentERUser.items[currentGameTrialValue.t_id_item_used]!!.i_used){
                // Log event
                setUserLog(
                    getString(R.string.tv_game_userlog_title_FT) + currentERContent.trials[currentGameTrialKey]!!.t_name,
                    getString(R.string.tv_game_userlog_desc_FT),
                    currentERUser.trials[currentGameTrialKey]!!.t_totalPoints
                )
                goEndThisTrial()
            }
            else{
                // Check the answer from the box
                var tVAnswer: TextView = currentPopupView.findViewById(R.id.et_game_investigation_check_hint)
                closeKeyBoard(view)
                if (tVAnswer.text.toString().trim().lowercase()
                    == currentERContent.trials[currentGameTrialKey]!!.t_solution){
                    // Log event
                    setUserLog(
                        getString(R.string.tv_game_userlog_title_FT) + currentERContent.trials[currentGameTrialKey]!!.t_name,
                        getString(R.string.tv_game_userlog_desc_FT),
                        currentERUser.trials[currentGameTrialKey]!!.t_totalPoints
                    )
                    goEndThisTrial()
                }
                else{
                    Toast.makeText(this, R.string.tv_game_investigation_incorrect, Toast.LENGTH_SHORT).show()
                }
            }

        } catch(e: Exception){
            // Handles if the Popup is lost at this point
            goGame()
        }
    }

    private fun endTrial(){
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.tv_game_investigation_end_confirmation_title))
            .setMessage(getString(R.string.tv_game_investigation_end_confirmation_message))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.tv_game_options_confirmation_yes),
                DialogInterface.OnClickListener{ _, _ ->
                    // Log event (half points)
                    setUserLog(
                        getString(R.string.tv_game_userlog_title_FFT) + currentERContent.trials[currentGameTrialKey]!!.t_name,
                        getString(R.string.tv_game_userlog_desc_FFT),
                        currentERUser.trials[currentGameTrialKey]!!.t_totalPoints /2
                    )
                    goEndThisTrial()
                })
            .setNegativeButton(getString(R.string.tv_game_options_confirmation_no),
                DialogInterface.OnClickListener{ _, _ ->
                    // Do nothing
                })
            .show()
    }

    private fun goEndThisTrial() {
        showPopUp("correct")
        currentERUser.trials[currentGameTrialKey]!!.t_finished = true
        getAchievements(currentGameTrialValue)
        getItems(currentGameTrialValue)
        updateUserEscapeRoom()
    }


    private fun goPopUpBack() {
        if (lastSuperPopupWindow.contentView == null){
            lastPopupWindow.dismiss()
            lastPopupWindow = PopupWindow()
        }
        else{
            lastSuperPopupWindow.dismiss()
            lastSuperPopupWindow = PopupWindow()
        }
    }


    /* --------------- COMMON --------------- */

    // Override back
    override fun onBackPressed() {
        goBack()
    }

    // Common item selection for navigationMenu
    private fun loadTopBar() {
        var topBar: androidx.appcompat.widget.Toolbar = findViewById(R.id.top_bar)
        topBar.title = "Juego: " + currentERContent.name
        drawerL = topBarActivation()
        lateralBarActivation(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Button selection
        when (item.itemId){
            R.id.lateralmenu_home -> goMain()
            R.id.lateralmenu_mygames -> goMyGames()
            R.id.lateralmenu_logout_button -> logout()
        }
        // Lateral menu closure
        drawerL.closeDrawer(GravityCompat.START)
        return true
    }
}