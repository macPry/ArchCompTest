package pl.elpassion.archcomptest

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.SingleSubject
import pl.elpassion.archcomptest.common.TreeSpec

class MainListModelTest : TreeSpec() {

    private val apiSubject = SingleSubject.create<MainList.Api.Response>()
    private val api = mock<MainList.Api> {
        on { call() } doReturn apiSubject
    }
    private val model = MainListModel(api)
    private val state = model.state.test()

    init {
        nest("When start") {
            assert("should be idle") {
                state.assertValue(MainList.State.Idle)
            }
        }
        nest("When activity starts") {
            before {
                model.event.accept(MainList.Event.OnCreate)
            }
            assert("should call api") {
                verify(api).call()
            }
        }
    }
}

interface MainList {

    sealed class State {
        object Idle : State()
        data class Items(val response: Api.Response) : State()
    }

    sealed class Event {
        object OnCreate : Event()
    }

    interface Api {
        data class Response(val items: List<Item>)

        fun call(): Single<Response>
    }

    data class Item(val name: String)
}

class MainListModel(private val api: MainList.Api) {
    val state: Relay<MainList.State> = BehaviorRelay.createDefault<MainList.State>(MainList.State.Idle)
    val event: Relay<MainList.Event> = PublishRelay.create()
    private var disposable: Disposable? = null

    init {
        disposable = callApiOnCreate().subscribe(state)
    }

    private fun callApiOnCreate(): Observable<MainList.State> {
        return event.ofType(MainList.Event.OnCreate::class.java).flatMap {
            api.call().toObservable().map {
                MainList.State.Items(it)
            }
        }
    }
}