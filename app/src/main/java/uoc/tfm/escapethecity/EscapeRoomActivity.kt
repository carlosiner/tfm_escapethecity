package uoc.tfm.escapethecity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentTransaction

class EscapeRoomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_escape_room)

    }

    fun er_selection(view:View){
        when(view.tag){
            "t_er_info" -> goERInfoFragment()
        }
    }

    private fun goERInfoFragment(){
        val fragment = ERInfoFragment()
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_main,fragment)
        ft.commit()
    }

}