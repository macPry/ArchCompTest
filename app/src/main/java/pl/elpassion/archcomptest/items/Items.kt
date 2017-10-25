package pl.elpassion.archcomptest.items

import pl.elpassion.archcomptest.app.App

interface Items {

    data class State(
            val items: List<App.Item>? = null,
            val exception: Throwable? = null,
            val isLoading: Boolean = false)

    sealed class Event {
        object GetItems : Event()
        object Refresh : Event()
    }
}