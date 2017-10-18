package pl.elpassion.archcomptest.app

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay

class AppModel(private val api: App.Api) : App.Model {
    override val states: BehaviorRelay<App.States> = BehaviorRelay.createDefault(App.States.Idle)
    override val events: PublishRelay<App.Events> = PublishRelay.create()

    init {
        events.subscribe(this::onEvent)
    }

    private fun onEvent(event: App.Events) {
        when (event) {
            App.Events.GetItems -> callApi()
        }
    }

    private fun callApi() =
            api.getItems().toObservable()
                    .map { states.accept(App.States.Items(it)) }
                    .startWith(states.accept(App.States.Loading))
                    .onErrorReturn { states.accept(App.States.Error) }
                    .subscribe()
}