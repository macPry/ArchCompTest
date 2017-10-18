package pl.elpassion.archcomptest.items

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay

interface Items {

    interface Model {
        val states: BehaviorRelay<State>
        val events: PublishRelay<Event>
    }

    sealed class State {

    }

    sealed class Event {
        object Create : Event()
        object Refresh : Event()
        object ErrorClick : Event()
        object ItemClick : Event()
    }
}