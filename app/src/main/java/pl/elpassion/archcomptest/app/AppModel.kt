package pl.elpassion.archcomptest.app

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import pl.elpassion.archcomptest.app.App.*

class AppModel(private val api: Api) : Model {
    override val states: BehaviorRelay<States> = BehaviorRelay.createDefault(States.Idle)
    override val events: PublishRelay<Events> = PublishRelay.create()

    init {
        events.subscribe(this::onEvent)
    }

    private fun onEvent(event: Events) {
        when (event) {
            Events.GetItems -> callApi()
        }
    }

    private fun callApi() =
            api.getItems().toObservable()
                    .map { states.accept(States.Items(it)) }
                    .startWith(states.accept(States.Loading))
                    .onErrorReturn { states.accept(States.Error) }
                    .subscribe()
}