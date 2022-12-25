package uoc.tfm.escapethecity

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import uoc.tfm.escapethecity.data.UserEscape

internal class BaseActivityTest {

    lateinit var SUT: BaseActivity
    lateinit var currentERUser: UserEscape

    @Before
    fun setUpClass(){
        getInstrumentation().runOnMainSync {
            SUT = BaseActivity()
        }
    }

    @Before
    fun setUpCurrentERUser(){
        currentERUser = UserEscape()
        currentERUser.user_date_selected = 1671934432
    }

    @Test
    fun checkERStartTest() {
        val result: Boolean = SUT.checkERStart()
        assertTrue(result)
    }

}