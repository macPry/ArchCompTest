package pl.elpassion.archcomptest.items

import io.reactivex.Single

interface Items {

    sealed class State {
        object Idle : State()
        object Loading : State()
        data class Items(val response: Api.Response) : State()
        object Error : State()
        data class OpenDetails(val item: Item) : State()
    }

    sealed class Event {
        object Create : Event()
        object Refresh : Event()
        object ErrorClick : Event()
        data class ItemClick(val item: Item) : Event()
    }

    interface Api {
        data class Response(val items: List<Item>)

        fun call(): Single<Response>
    }

    data class Item(val name: String)
}