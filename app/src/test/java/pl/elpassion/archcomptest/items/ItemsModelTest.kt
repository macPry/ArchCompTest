package pl.elpassion.archcomptest.items

import com.nhaarman.mockito_kotlin.*
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
        nest("On start") {
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
            assert("should display items") {
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
        nest("On error click") {
            before {
                model.events.accept(Event.ErrorClick)
            }
            assert("should not show error") {
                states.assertLastValue(State.Idle)
            }
        }
        nest("On refresh") {
            before {
                model.events.accept(Event.Refresh)
            }
            assert("should call api") {
                verify(api).call()
            }
        }
        nest("On item click") {
            before {
                model.events.accept(Event.ItemClick(Item("3")))
            }
            assert("should pass item data") {
                states.assertLastValue(State.OpenDetails(Item("3")))
            }
        }
    }

    private fun onCreate() = model.events.accept(Event.Create)
}