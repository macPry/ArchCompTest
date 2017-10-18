package pl.elpassion.archcomptest.app

import io.reactivex.Single

object DI {

    private val model by lazy { AppModel(provideApi()) }

    private var provideApi: () -> App.Api = { apiProvider }

    var provideModel: () -> App.Model = { model }

    private val apiProvider = object : App.Api {
        override fun getItems(): Single<List<App.Item>> {
            return Single.just(listOf(App.Item("ItemName")))
        }
    }
}