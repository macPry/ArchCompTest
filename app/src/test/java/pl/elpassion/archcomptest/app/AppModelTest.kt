package pl.elpassion.archcomptest.app

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.subjects.SingleSubject
import pl.elpassion.archcomptest.common.TreeSpec
import pl.elpassion.archcomptest.common.assertLastValue
import pl.elpassion.archcomptest.app.App.*

class AppModelTest : TreeSpec() {

    private val apiSubject = SingleSubject.create<List<Item>>()
    private val api = mock<Api> {
        on { getItems() } doReturn apiSubject
    }
    private val model = AppModel(api)
    private val states = model.states.test()

    init {
        nest("On start") {
            assert("should be idle") {
                states.assertLastValue(States.Idle)
            }
        }
        nest("On get items") {
            before {
                getItems()
            }
            assert("should call api get items") {
                verify(api).getItems()
            }
            assert("should show loader") {
                states.assertLastValue(States.Loading)
            }
        }
        nest("On get items when api returns items") {
            val items = listOf(Item("1"), Item("2"))
            before {
                getItems()
                apiSubject.onSuccess(items)
            }
            assert("should return items") {
                states.assertLastValue(States.Items(items))
            }
        }
        nest("On get items when api returns error") {
            before {
                getItems()
                apiSubject.onError(RuntimeException())
            }
            assert("should return error") {
                states.assertLastValue(States.Error)
            }
        }
    }

    private fun getItems() = model.events.accept(Events.GetItems)
}