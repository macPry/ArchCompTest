package pl.elpassion.archcomptest.items

import com.nhaarman.mockito_kotlin.*
import io.reactivex.schedulers.Schedulers
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

    init {
        nest("On api call") {
            val backgroundScheduler = TestScheduler()
            val model = ItemsModel(api, backgroundScheduler, Schedulers.trampoline())
            val states = model.states.test()
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
        nest("On api call") {
            val uiScheduler = TestScheduler()
            val model = ItemsModel(api, Schedulers.trampoline(), uiScheduler)
            val states = model.states.test()
            before {
                model.events.accept(Event.Create)
                apiSubject.onError(RuntimeException())
            }
            assert("should observe on given scheduler") {
                states.assertLastValue(State.Loading)
                uiScheduler.triggerActions()
                states.assertLastValue(State.Error)
            }
        }
    }
}