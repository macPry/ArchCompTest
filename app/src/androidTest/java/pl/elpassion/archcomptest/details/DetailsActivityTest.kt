package pl.elpassion.archcomptest.details

import android.support.test.rule.ActivityTestRule
import com.elpassion.android.commons.espresso.InitIntentsRule
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.observers.TestObserver
import org.junit.Rule
import pl.elpassion.archcomptest.app.App
import pl.elpassion.archcomptest.app.DI

class DetailsActivityTest {

    private val modelStates: BehaviorRelay<App.States> = BehaviorRelay.create()
    private val modelEvents = PublishRelay.create<App.Events>()
    private val model = mock<App.Model> {
        on { states } doReturn modelStates
        on { events } doReturn modelEvents
    }
    private val testObserver = TestObserver<App.Events>()

    @JvmField @Rule
    val intents = InitIntentsRule()

    @JvmField
    @Rule
    val rule = ActivityTestRule(DetailsActivity::class.java)

    init {
        DI.provideAppModel = { model }
        modelEvents.subscribe(testObserver)
    }
}