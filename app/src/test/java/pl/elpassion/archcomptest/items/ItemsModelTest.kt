package pl.elpassion.archcomptest.items

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.subjects.SingleSubject
import pl.elpassion.archcomptest.common.TreeSpec
import pl.elpassion.archcomptest.common.assertLastValue
import pl.elpassion.archcomptest.items.Items.*

class ItemsModelTest : TreeSpec() {

    private val apiSubject = SingleSubject.create<Api.Response>()
    private val api = mock<Api> {
        on { call() } doReturn apiSubject
    }
    private val model = ItemsModel(api)
    private val states = model.states.test()

    init {
        nest("When start") {
            assert("should be idle") {
                states.assertLastValue(State.Idle)
            }
        }
        nest("On create") {
            before {
                onCreate()
            }
            assert("should call api") {
                verify(api).call()
            }
            assert("should show loader") {
                states.assertLastValue(State.Loading)
            }
        }
        nest("On create when api returns items") {
            val response = Api.Response(listOf(Item("1"), Item("2")))
            before {
                onCreate()
                apiSubject.onSuccess(response)
            }
            assert("then items should be displayed") {
                states.assertLastValue(State.Items(response))
            }
        }
        nest("On create when api returns error") {
            before {
                onCreate()
                apiSubject.onError(RuntimeException())
            }
            assert("should show error") {
                states.assertLastValue(State.Error)
            }
        }
    }

    private fun onCreate() = model.events.accept(Event.OnCreate)
}