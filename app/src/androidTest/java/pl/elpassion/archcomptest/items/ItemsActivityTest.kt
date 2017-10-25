package pl.elpassion.archcomptest.items

import android.app.Activity
import android.app.Instrumentation
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
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
import pl.elpassion.archcomptest.details.DetailsActivity

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

    @JvmField @Rule
    val intents = InitIntentsRule()

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
        onText("Some error").isDisplayed()
    }

    @Test
    fun shouldShowRefreshButtonOnAppError() {
        modelStates.accept(App.States.Items(exception = RuntimeException()))
        onText(R.string.refresh).isDisplayed()
    }

    @Test
    fun shouldCallGetItemsSecondTimeOnRefreshClicked() {
        modelStates.accept(App.States.Items(exception = RuntimeException()))
        onText(R.string.refresh).click()
        testObserver.assertValueAt(1, { it is App.Events.GetItems })
    }

    @Test
    fun shouldOpenDetailsScreenOnItemClick() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null))
        modelStates.accept(App.States.Items(listOf(App.Item("321"), App.Item("666"))))
        onRecyclerViewItem(R.id.itemsRecycler, 0, R.id.itemView).click()
        Intents.intended(IntentMatchers.hasComponent(DetailsActivity::class.java.name))
    }
}