package pl.elpassion.archcomptest.items

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.Scheduler
import pl.elpassion.archcomptest.items.Items.*

class ItemsModel(private val api: Api, private val backgroundScheduler: Scheduler, private val uiScheduler: Scheduler) {
    val states: BehaviorRelay<State> = BehaviorRelay.createDefault<State>(State.Idle)
    val events: Relay<Event> = PublishRelay.create()
    private val disposable = itemsModel().subscribe(states)

    private fun itemsModel(): Observable<State> = Observable.merge(
            callApi(),
            handleErrorClick(),
            handleItemClick())

    private fun callApi(): Observable<State> = events.filter { it is Event.Create || it is Event.Refresh }
            .flatMap {
                api.call().toObservable()
                        .subscribeOn(backgroundScheduler)
                        .observeOn(uiScheduler)
                        .map { State.Items(it) as State }
                        .startWith(State.Loading)
                        .onErrorReturn { State.Error }
            }

    private fun handleErrorClick(): Observable<State> = events.ofType(Event.ErrorClick::class.java)
            .map { State.Idle }

    private fun handleItemClick(): Observable<State> = events.ofType(Event.ItemClick::class.java)
            .map { State.OpenDetails(it.item) }
}