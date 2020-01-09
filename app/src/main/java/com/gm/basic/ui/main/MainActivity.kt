package com.gm.basic.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import com.gm.basic.R
import com.gm.basic.data.Data
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), Adapter.OnItemClickListener {

    lateinit var vieModel: MainViewModel
    private var adapter: Adapter = Adapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vieModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        initRecyclerView()

        button_check.setOnClickListener {
            vieModel.checkWebservice()
        }

        vieModel.data.observe(this, Observer {
            adapter.items = it
        })
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = TopDissolveLayoutManager()//LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
    }

    override fun onItemClicked(position: Int, data: Data) {
        Toast.makeText(this, data.title, Toast.LENGTH_SHORT).show()
    }
}
