package pl.elpassion.archcomptest.items

import com.nhaarman.mockito_kotlin.*
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.SingleSubject
import pl.elpassion.archcomptest.common.TreeSpec
import pl.elpassion.archcomptest.common.assertLastValue
import pl.elpassion.archcomptest.items.Items.*

class ItemsModelSchedulersTest : TreeSpec() {

    private val apiSubject = SingleSubject.create<Items.Api.Response>()
    private val api = mock<Items.Api> {
        on { call() } doReturn apiSubject
    }
    private val backgroundScheduler = TestScheduler()
    private val model = ItemsModel(api, backgroundScheduler)
    private val states = model.states.test()

    init {
        nest("On api call") {
            before {
                model.events.accept(Event.Create)
                apiSubject.onError(RuntimeException())
            }
            assert("should subscribe on given scheduler") {
                states.assertLastValue(State.Loading)
                backgroundScheduler.triggerActions()
                states.assertLastValue(State.Error)
            }
        }
    }
}