package com.example.nycschools.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nycschools.common.State
import com.example.nycschools.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NYCViewModel @Inject constructor(
    private val repository: Repository,
    private val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _schoolResponse = MutableLiveData<State>()
    val schoolResponse: MutableLiveData<State>
        get() = _schoolResponse

    private val _schoolSatResponse = MutableLiveData<State>()
    val schoolSatResponse: MutableLiveData<State>
        get() = _schoolSatResponse

    fun getSchoolList() {
        coroutineScope.launch {
            repository.NYCSchoolCatched().collect {
                _schoolResponse.postValue(it)
            }
        }
    }

    fun getSatList() {
        coroutineScope.launch {
            repository.NYCSatCatched().collect {
                _schoolSatResponse.postValue(it)
            }
        }
    }
}