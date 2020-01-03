package com.gm.basic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * Author     : Gowtham
 * Email      : goutham.gm11@gmail.com
 * Github     : https://github.com/goutham106
 * Created on : 2020-01-02.
 */
class MainViewModel : ViewModel() {

    private val repo: Repository by lazy { Repository() }

    private var _data: MutableLiveData<List<Data>> = MutableLiveData()
    val data: LiveData<List<Data>> = _data

    fun checkWebservice() {
        viewModelScope.launch {
            _data.postValue(repo.getFeed())
        }

    }


}