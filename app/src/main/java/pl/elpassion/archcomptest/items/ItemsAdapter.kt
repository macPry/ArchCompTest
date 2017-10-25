package pl.elpassion.archcomptest.items

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item.view.*
import pl.elpassion.archcomptest.R
import pl.elpassion.archcomptest.app.App

class ItemsAdapter(private val items: List<App.Item>) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    private val clicksSubject = PublishSubject.create<View>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        val viewHolder = ViewHolder(itemView)

        RxView.clicks(itemView)
                .takeUntil(RxView.detaches(parent))
                .map { itemView }
                .subscribe(clicksSubject)

        return viewHolder
    }

    fun clickEvents(): Observable<View> = clicksSubject.hide()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: App.Item) {
            itemView.name.text = item.name
        }
    }
}

