package pl.elpassion.archcomptest.items

import io.reactivex.Single

interface Items {

    sealed class State {
        object Idle : State()
        data class Items(val response: Api.Response) : State()
        object Loading : State()
        object Error : State()
    }

    sealed class Event {
        object Create : Event()
        object ErrorClick : Event()
    }

    interface Api {
        data class Response(val items: List<Item>)

        fun call(): Single<Response>
    }

    data class Item(val name: String)
}