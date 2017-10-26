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

        data class Items(
                val items: List<App.Item>? = null,
                val isLoading: Boolean = false,
                val exception: Throwable? = null) : States()
    }

    sealed class Events {
        object GetItems : Events()
    }

    interface Api {
        fun getItems(): Single<List<Item>>
    }

    data class Item(val id: Long, val name: String)
}