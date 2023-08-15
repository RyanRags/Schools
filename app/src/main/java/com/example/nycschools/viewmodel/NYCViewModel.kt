package com.example.nycschools.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nycschools.common.StateAction
import com.example.nycschools.model.Repository
import com.example.nycschools.model.remote.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject


//Bridge between data and the view.
// Will be holding the logic to retrieve the data from repository
@HiltViewModel
class NYCViewModel @Inject constructor(
    private val repository: Repository,
    private val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _schoolResponse = MutableLiveData<StateAction>()
    val schoolResponse: MutableLiveData<StateAction>
        get() = _schoolResponse

    private val _schoolSatResponse = MutableLiveData<StateAction>()
    val schoolSatResponse: MutableLiveData<StateAction>
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