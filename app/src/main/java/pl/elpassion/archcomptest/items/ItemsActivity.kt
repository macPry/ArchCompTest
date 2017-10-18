package pl.elpassion.archcomptest.items

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.support.design.widget.Snackbar
import kotlinx.android.synthetic.main.items_activity.*
import pl.elpassion.archcomptest.R

class ItemsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.items_activity)
        val itemsViewModel = ViewModelProviders.of(this).get(ItemsViewModel::class.java)
        itemsViewModel.state.observe(this, Observer {
            when (it) {
                is Items.State.Items -> it.items.forEach {
                    itemsLayout.addView(TextView(this).apply { text = it.name })
                }
                is Items.State.Error -> showError(it)
            }
        })
        itemsViewModel.event.accept(Items.Event.Create)
    }

    private fun showError(error: Items.State.Error) {
        Snackbar.make(itemsLayout, error.exception.message.toString(), Snackbar.LENGTH_SHORT)
                .setAction("Refresh", { })
                .show()
    }
}