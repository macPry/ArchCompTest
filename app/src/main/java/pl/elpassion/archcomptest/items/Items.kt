package pl.elpassion.archcomptest.items

import pl.elpassion.archcomptest.app.App

interface Items {

    sealed class State {
        data class Items(val items: List<App.Item>) : State()
        data class Error(val exception: Throwable) : State()
    }

    sealed class Event {
        object Create : Event()
        /*object Refresh : Event()
        object ErrorClick : Event()
        object ItemClick : Event()*/
    }
}