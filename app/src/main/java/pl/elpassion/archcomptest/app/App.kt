package pl.elpassion.archcomptest.app

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Single

interface App {

    interface Model {
        val states: BehaviorRelay<States>
        val events: PublishRelay<Events>
    }

    sealed class States {
        object Idle : States()
        object Loading : States()
        data class Items(val items: List<Item>) : States()
        data class Error(val exception: Throwable) : States()
    }

    sealed class Events {
        object GetItems : Events()
    }

    interface Api {
        fun getItems(): Single<List<Item>>
    }

    data class Item(val name: String)
}