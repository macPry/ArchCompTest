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
            callApi(),
            handleErrorClick())

    private fun callApi(): Observable<State> = events.filter { it is Event.Create || it is Event.Refresh }
            .flatMap {
                api.call().toObservable()
                        .map { State.Items(it) as State }
                        .startWith(State.Loading)
                        .onErrorReturn { State.Error }
            }

    private fun handleErrorClick(): Observable<State> = events.ofType(Event.ErrorClick::class.java)
            .map { State.Idle }
}