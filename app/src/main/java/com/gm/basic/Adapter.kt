package com.gm.basic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_layout.view.*
import kotlin.properties.Delegates

/**
 * Author     : Gowtham
 * Email      : goutham.gm11@gmail.com
 * Github     : https://github.com/goutham106
 * Created on : 2020-01-02.
 */
class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>(), AutoUpdatableAdapter {

    var items: List<Data> by Delegates.observable(emptyList()) { _, oldValue, newValue ->
        autoNotify(oldValue, newValue) { o, n -> o.title == n.title }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Data) = with(itemView) {
            Glide.with(this.context)
                .load(data.thumbnail)
                .into(img_profile)

            text_title.text = data.title
            text_description.text = data.description
        }

    }


}