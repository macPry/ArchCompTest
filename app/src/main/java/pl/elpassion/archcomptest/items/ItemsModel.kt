package pl.elpassion.archcomptest.items

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import pl.elpassion.archcomptest.items.Items.*

class ItemsModel : Items.Model {
    override val states: BehaviorRelay<State> = BehaviorRelay.create()
    override val events: PublishRelay<Event> = PublishRelay.create()


}