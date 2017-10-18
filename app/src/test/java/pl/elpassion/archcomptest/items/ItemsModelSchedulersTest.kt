package pl.elpassion.archcomptest.items

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.SingleSubject
import org.junit.Test
import pl.elpassion.archcomptest.common.assertLastValue
import pl.elpassion.archcomptest.items.Items.Event
import pl.elpassion.archcomptest.items.Items.State

class ItemsModelSchedulersTest {

    /*private val apiSubject = SingleSubject.create<Items.Api.Response>()
    private val api = mock<Items.Api> {
        on { call() } doReturn apiSubject
    }

    @Test
    fun shouldSubscribeOnGivenScheduler() {
        val backgroundScheduler = TestScheduler()
        val model = ItemsModel(api, backgroundScheduler, Schedulers.trampoline())
        val states = model.states.test()
        model.events.accept(Event.Create)
        apiSubject.onError(RuntimeException())
        states.assertLastValue(State.Loading)
        backgroundScheduler.triggerActions()
        states.assertLastValue(State.Error)
    }

    @Test
    fun shouldObserveOnGivenScheduler() {
        val uiScheduler = TestScheduler()
        val model = ItemsModel(api, Schedulers.trampoline(), uiScheduler)
        val states = model.states.test()
        model.events.accept(Event.Create)
        apiSubject.onError(RuntimeException())
        states.assertLastValue(State.Loading)
        uiScheduler.triggerActions()
        states.assertLastValue(State.Error)
    }*/
}