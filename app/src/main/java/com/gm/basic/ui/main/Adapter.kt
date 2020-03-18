package com.gm.basic.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gm.basic.R
import com.gm.basic.data.AutoUpdater
import com.gm.basic.room.DataEntity
import kotlinx.android.synthetic.main.item_layout.view.*
import kotlin.properties.Delegates

/**
 * Author     : Gowtham
 * Email      : goutham.gm11@gmail.com
 * Github     : https://github.com/goutham106
 * Created on : 2020-01-02.
 */
class Adapter(var listner: OnItemClickListener) : RecyclerView.Adapter<Adapter.ViewHolder>(),
    AutoUpdater {

    var items: List<DataEntity> by Delegates.observable(emptyList()) { _, oldValue, newValue ->
        autoNotify(oldValue, newValue) { o, n -> o.id == n.id }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView.rootView) {
        /*fun bind(
            data: Data,
            position: Int,
            listner: OnItemClickListener
        ) = with(itemView) {
            Glide.with(this.context)
                .load(data.image_url_tumbnail)
                .into(img_profile)

            text_title.text = data.title
            text_description.text = data.description
            this.setOnClickListener { listner.onItemClicked(position, data) }

            img_profile.setOnClickListener { listner.onItemClicked(position, data) }
        }*/


        fun bind(data: DataEntity, position: Int) {

            Glide.with(itemView.context)
                .load(data.image_url_tumbnail)
                .into(itemView.img_profile)

            itemView.text_title.text = data.title
            itemView.text_description.text = data.description
            itemView.cardView.setOnClickListener { listner.onItemClicked(position, data) }

        }
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int, data: DataEntity)
    }

}