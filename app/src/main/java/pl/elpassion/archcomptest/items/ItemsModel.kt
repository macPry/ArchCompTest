package pl.elpassion.archcomptest.items

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable

class ItemsModel(private val api: Items.Api) {
    val state: BehaviorRelay<Items.State> = BehaviorRelay.createDefault<Items.State>(Items.State.Idle)
    val event: Relay<Items.Event> = PublishRelay.create()
    private val disposable = callApiOnCreate().subscribe(state)

    private fun callApiOnCreate(): Observable<Items.State> {
        return event.ofType(Items.Event.OnCreate::class.java).flatMap {
            api.call().toObservable()
                    .map { Items.State.Items(it) }
        }
    }
}