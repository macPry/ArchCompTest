package pl.elpassion.archcomptest.items

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.items_activity.*
import com.jakewharton.rxbinding2.support.design.widget.dismisses
import pl.elpassion.archcomptest.R
import pl.elpassion.archcomptest.app.App

class ItemsActivity : AppCompatActivity() {

    private val items = mutableListOf<App.Item>()
    private val errorSnackBar by lazy {
        Snackbar.make(itemsLayout, "", Snackbar.LENGTH_SHORT).setAction(getString(R.string.refresh), { })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.items_activity)
        itemsRecycler.adapter = ItemsAdapter(items)
        initViewModel()
    }

    private fun initViewModel() {
        val itemsViewModel = ViewModelProviders.of(this).get(ItemsViewModel::class.java)
        itemsViewModel.state.observe(this, Observer {
            updateViews(it)
        })
        errorSnackBar.dismisses().map { Items.Event.Refresh }.subscribe(itemsViewModel.event)
    }

    private fun updateViews(state: Items.State?) {
        state?.let { showIfIsLoading(it) }
        state?.items?.let { updateList(it) }
        state?.exception?.let { errorSnackBar.setText("${it.message}").show() }
    }

    private fun showIfIsLoading(state: Items.State) {
        itemsLoader.visibility = if (state.isLoading) View.VISIBLE else View.GONE
    }

    private fun updateList(newItems: List<App.Item>) {
        items.clear()
        items.addAll(newItems)
        itemsRecycler.adapter.notifyDataSetChanged()
    }
}