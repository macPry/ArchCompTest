package pl.elpassion.archcomptest.items

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.subjects.SingleSubject
import org.junit.Assert
import pl.elpassion.archcomptest.common.TreeSpec
import pl.elpassion.archcomptest.items.Items.*

class ItemsModelTest : TreeSpec() {

    private val apiSubject = SingleSubject.create<Api.Response>()
    private val api = mock<Api> {
        on { call() } doReturn apiSubject
    }
    private val model = ItemsModel(api)
    private val state = model.state.test()

    init {
        nest("When start") {
            assert("should be idle") {
                state.assertValue(State.Idle)
            }
        }
        nest("On create") {
            before {
                model.event.accept(Event.OnCreate)
            }
            assert("should call api") {
                verify(api).call()
            }
            assert("should show loader") {
                Assert.assertEquals(State.Loading, state.values().last())
            }
        }
        nest("On create when api returns items") {
            val response = Api.Response(listOf(Item("1"), Item("2")))
            before {
                model.event.accept(Event.OnCreate)
                apiSubject.onSuccess(response)
            }
            assert("then items should be displayed") {
                Assert.assertEquals(State.Items(response), state.values().last())
            }
        }
    }
}