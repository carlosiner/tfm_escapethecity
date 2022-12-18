package uoc.tfm.escapethecity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.transition.Visibility
import com.google.android.material.navigation.NavigationView
import uoc.tfm.escapethecity.data.GameTrials

class GInvestigationActivity : BaseActivity(),
    NavigationView.OnNavigationItemSelectedListener{

    // Common Drawer definition
    private lateinit var drawerL: DrawerLayout
    private lateinit var currentPopupView: View
    // Game Trial selected
    var currentGameTrialValue = GameTrials()
    var currentGameTrialKey = ""
    var lastPopupWindow = PopupWindow()
    var lastSuperPopupWindow = PopupWindow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ginvestigation)

        // TODO Hide all popups
        trialSelection()
    }

    /* --- Operations section --- */
    private fun trialSelection(){
        val trialMap = currentERUser.trials
        var selectedGame = GameTrials()
        var flagFinish = false

        // Get the lower id with 0 points (Trial not finished)
        for (i in trialMap){
            if(!i.value.t_finished){
                currentGameTrialValue = i.value
                currentGameTrialKey = i.key
                flagFinish = false
            }
            else{
                flagFinish = true
            }
        }

        // Game is finished
        if (flagFinish){
            Toast.makeText(this,R.string.tv_game_investigation_correct_end, Toast.LENGTH_SHORT).show()
            goEscapeRoom()
        }
        else{
            // And load the view information for trial
            loadTrialView()
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
        // Todo check!
        val container = contentView.rootView
        val context = contentView.context
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val p = container.layoutParams as WindowManager.LayoutParams
        p.flags = p.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.3f
        wm.updateViewLayout(container, p)
    }

    private fun showPopUp(view_tag: String){
        // Disable background layout
//        var llpop: LinearLayout = findViewById(R.id.ll_game_investigation_popup_Check)
//        llBack.isEnabled = false

//        val popup = PopupWindow(this)
        val notiInf: LinearLayout = findViewById(R.id.ll_popup)

        var popupLayout: Int? = null
        when(view_tag){
            "solve" -> popupLayout = R.layout.popup_investigation_solve
            "clues" -> popupLayout = R.layout.popup_investigation_clues
            "correct" -> popupLayout = R.layout.popup_investigation_correct
            "incorrect" -> popupLayout = R.layout.popup_investigation_incorrect
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

            // Check if checkboxes where activated
            if(currentERUser.trials[currentGameTrialKey]!!.t_clue1_activated){
                idTV = R.id.tv_game_investigation_clue1_description
                idCB = R.id.cb_game_investigation_clue1
                activateClues(idTV, idCB, false)
            }
            if (currentERUser.trials[currentGameTrialKey]!!.t_clue1_activated){
                idTV = R.id.tv_game_investigation_clue2_description
                idCB = R.id.cb_game_investigation_clue2
                activateClues(idTV, idCB, false)
            }
            if (currentERUser.trials[currentGameTrialKey]!!.t_clue1_activated){
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
        }
    }

    private fun showClues(clue: String) {
        var idTV = 0
        var idCB = 0
        when (clue){
            "clue1" -> {
                idTV = R.id.tv_game_investigation_clue1_description
                idCB = R.id.cb_game_investigation_clue1
                currentERUser.trials[currentGameTrialKey]!!.t_clue1_activated = true}
            "clue2" -> {
                idTV = R.id.tv_game_investigation_clue2_description
                idCB = R.id.cb_game_investigation_clue2
                currentERUser.trials[currentGameTrialKey]!!.t_clue2_activated = true}
            "clue3" -> {
                idTV = R.id.tv_game_investigation_clue3_description
                idCB = R.id.cb_game_investigation_clue3
                currentERUser.trials[currentGameTrialKey]!!.t_clue3_activated = true}
        }

        activateClues(idTV, idCB, true)
        // Update ER User info
        updateUserEscapeRoom()

    }

    private fun checkAnswer(view: View){
        var tVAnswer: TextView = currentPopupView.findViewById(R.id.et_game_investigation_check_hint)
        closeKeyBoard(view)
        if (tVAnswer.text.toString().trim().lowercase()
            == currentERContent.trials[currentGameTrialKey]!!.t_solution){
            showPopUp("correct")
            currentERUser.trials[currentGameTrialKey]!!.t_finished = true
            updateUserEscapeRoom()
        }
        else{
            Toast.makeText(this,R.string.tv_game_investigation_incorrect, Toast.LENGTH_SHORT).show()
        }
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

    private fun goGame() {
        /* After finishing a trial, go back to the Game menu */
        var intent = Intent(this,GameActivity::class.java)
        startActivity(intent)
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