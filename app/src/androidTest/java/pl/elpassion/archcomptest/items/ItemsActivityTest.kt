package pl.elpassion.archcomptest.items

import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import com.elpassion.android.commons.espresso.*
import com.elpassion.android.commons.espresso.recycler.onRecyclerViewItem
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.observers.TestObserver
import org.junit.Rule
import org.junit.Test
import pl.elpassion.archcomptest.R
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
    fun shouldShowLoaderOnAppLoadingState() {
        modelStates.accept(App.States.Items(isLoading = true))
        onId(R.id.itemsLoader).isDisplayed()
    }

    @Test
    fun shouldNotShowLoaderOnAppErrorState() {
        modelStates.accept(App.States.Items(exception = RuntimeException()))
        onId(R.id.itemsLoader).isNotDisplayed()
    }

    @Test
    fun shouldNotShowLoaderOnAppItemsState() {
        modelStates.accept(App.States.Items(listOf(App.Item("321"), App.Item("666"))))
        onId(R.id.itemsLoader).isNotDisplayed()
    }

    @Test
    fun shouldShowItemsOnAppItemsState() {
        modelStates.accept(App.States.Items(listOf(App.Item("321"), App.Item("666"))))
        onRecyclerViewItem(R.id.itemsRecycler, 0, R.id.itemView).hasChildWithText("321")
        onRecyclerViewItem(R.id.itemsRecycler, 1, R.id.itemView).hasChildWithText("666")
    }

    @Test
    fun shouldShowErrorMessageOnAppError() {
        modelStates.accept(App.States.Items(exception = Exception("Some error")))
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        onText("Some error").isDisplayed()
    }

    @Test
    fun shouldShowRefreshButtonOnAppError() {
        modelStates.accept(App.States.Items(exception = RuntimeException()))
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        onText("Refresh").isDisplayed()
    }
}