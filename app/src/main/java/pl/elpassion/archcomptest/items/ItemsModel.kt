package pl.elpassion.archcomptest.items

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import pl.elpassion.archcomptest.items.Items.*

class ItemsModel(private val api: Api) {
    val states: BehaviorRelay<State> = BehaviorRelay.createDefault<State>(State.Idle)
    val events: Relay<Event> = PublishRelay.create()
    private val disposable = itemsModel().subscribe(states)

    private fun itemsModel(): Observable<State> = Observable.merge(
            onCreate(),
            onErrorClick())

    private fun onErrorClick(): Observable<State> = events.ofType(Event.ErrorClick::class.java)
            .map { State.Idle }

    private fun onCreate(): Observable<State> = events.ofType(Event.Create::class.java).flatMap {
        api.call().toObservable()
                .map { State.Items(it) as State }
                .startWith(State.Loading)
                .onErrorReturn { State.Error }
    }
}