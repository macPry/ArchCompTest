package pl.elpassion.archcomptest.items

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import pl.elpassion.archcomptest.items.Items.*

class ItemsModel(private val api: Api) {
    val state: BehaviorRelay<State> = BehaviorRelay.createDefault<State>(State.Idle)
    val event: Relay<Event> = PublishRelay.create()
    private val disposable = callApiOnCreate().subscribe(state)

    private fun callApiOnCreate(): Observable<State> {
        return event.ofType(Event.OnCreate::class.java).flatMap {
            api.call().toObservable()
                    .map { State.Items(it) }
        }
    }
}