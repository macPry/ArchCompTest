package pl.elpassion.archcomptest

import android.support.test.rule.ActivityTestRule
import com.elpassion.android.commons.espresso.InitIntentsRule
import com.elpassion.android.commons.espresso.isDisplayed
import com.elpassion.android.commons.espresso.onText
import com.jakewharton.rxrelay2.BehaviorRelay
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.Rule
import org.junit.Test
import pl.elpassion.archcomptest.items.DI
import pl.elpassion.archcomptest.items.Items
import pl.elpassion.archcomptest.items.Items.*
import pl.elpassion.archcomptest.items.ItemsActivity

class ItemsActivityTest {

    private val modelStates: BehaviorRelay<State> = BehaviorRelay.create<State>()
    private val model: Items.Model = mock { on { states } doReturn modelStates }

    @JvmField
    @Rule
    val intents = InitIntentsRule()

    @JvmField
    @Rule
    val rule = ActivityTestRule(ItemsActivity::class.java)

    init {
        //DI.provideModel = { model }
    }

    /*@Test
    fun should() {
        modelStates.accept(State.Items(Api.Response(listOf(Item("1")))))
        onText("1").isDisplayed()
    }*/

    /*init {
        nest("On activity create when api returns items") {
            before {
                startActivity()
                apiSubject.onSuccess(Items.Api.Response(listOf(Items.Item("1"))))
            }
            assert("should show items") {
                onText("1").isDisplayed()
            }
        }
    }*/
}