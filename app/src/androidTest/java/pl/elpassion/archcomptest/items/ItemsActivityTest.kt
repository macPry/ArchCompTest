package pl.elpassion.archcomptest.items

import android.support.test.rule.ActivityTestRule
import com.elpassion.android.commons.espresso.isDisplayed
import com.elpassion.android.commons.espresso.onText
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.observers.TestObserver
import org.junit.Rule
import org.junit.Test
import pl.elpassion.archcomptest.app.App
import pl.elpassion.archcomptest.app.DI

class ItemsActivityTest {

    private val modelStates: BehaviorRelay<App.States> = BehaviorRelay.create()
    private val modelEvents = PublishRelay.create<App.Events>()
    private val model = mock<App.Model> {
        on { states } doReturn modelStates
        on { events } doReturn modelEvents
    }
    private val testObserver = TestObserver<App.Events>()

    @JvmField
    @Rule
    val rule = ActivityTestRule(ItemsActivity::class.java)

    init {
        DI.provideAppModel = { model }
        modelEvents.subscribe(testObserver)
    }

    @Test
    fun shouldShowItemsOnAppItemsState() {
        modelStates.accept(App.States.Items(listOf(App.Item("321"), App.Item("666"))))
        onText("321").isDisplayed()
        onText("666").isDisplayed()
    }
}