package pl.elpassion.archcomptest.items

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import pl.elpassion.archcomptest.items.Items.*

class ItemsModel(private val api: Api) {
    val states: BehaviorRelay<State> = BehaviorRelay.createDefault<State>(State.Idle)
    val events: Relay<Event> = PublishRelay.create()
    private val disposable = callApiOnCreate().subscribe(states)

    private fun callApiOnCreate(): Observable<State> {
        return events.ofType(Event.OnCreate::class.java).flatMap {
            api.call().toObservable()
                    .map { State.Items(it) as State }
                    .startWith(State.Loading)
                    .onErrorReturn { State.Error }
        }
    }
}