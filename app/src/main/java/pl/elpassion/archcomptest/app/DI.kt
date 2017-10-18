package pl.elpassion.archcomptest.app

import io.reactivex.Single
import pl.elpassion.archcomptest.app.App.*

object DI {

    private val appModel by lazy { AppModel(provideApi()) }

    private var provideApi: () -> Api = { apiProvider }

    var provideAppModel: () -> Model = { appModel }

    private val apiProvider = object : Api {
        override fun getItems(): Single<List<Item>> {
            return Single.just(listOf(Item("ItemName1"), Item("ItemName2")))
        }
    }
}