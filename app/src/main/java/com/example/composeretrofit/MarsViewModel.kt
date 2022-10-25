package com.example.composeretrofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeretrofit.network.MarsApi
import com.example.composeretrofit.network.MarsApiFilter
import com.example.composeretrofit.network.MarsProperty
import kotlinx.coroutines.launch


enum class MarsApiStatus { LOADING, ERROR, DONE }

class MarsViewModel : ViewModel() {
    private val _status = MutableLiveData<MarsApiStatus>()
    val status: LiveData<MarsApiStatus>
        get() = _status

    private val _properties = MutableLiveData<List<MarsProperty>>()
    val properties: LiveData<List<MarsProperty>>
        get() = _properties

    init {
        getMarsRealEstateProperties(MarsApiFilter.SHOW_ALL)
    }

    private fun getMarsRealEstateProperties(filter: MarsApiFilter) {
        viewModelScope.launch {
            _status.value = MarsApiStatus.LOADING
            try {
                _properties.value = MarsApi.retrofitService.getProperties(filter.value)
                _status.value = MarsApiStatus.DONE
            } catch (e: Exception) {
                _status.value = MarsApiStatus.ERROR
                _properties.value = ArrayList()
            }
        }
    }

    fun updateFilter(filter: MarsApiFilter) {
        getMarsRealEstateProperties(filter)
    }

    fun getProperty(propertyIndex: Int): MarsProperty? {
        return _properties.value!![propertyIndex]
    }
}