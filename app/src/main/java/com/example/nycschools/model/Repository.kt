package com.example.nycschools.model

import android.util.Log
import com.example.nycschools.common.StateAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface Repository {
    fun NYCSchoolCatched(): Flow<StateAction>
    fun NYCSatCatched(): Flow<StateAction>
}

//Implementation to get School and SAT data loaded successfully otherwise provide an error
//Separate objects from network call to provide cleaner architecture
class RepositoryImpl @Inject constructor(
    private val service: NycApi
) : Repository {

    override fun NYCSchoolCatched() = flow {
        emit(StateAction.LOADING)
        try {
            val respose = service.getSchoolList()
            if (respose.isSuccessful) {
                respose.body()?.let {
                    emit(StateAction.SUCCESS(it))
                } ?: throw Exception("Error null")
            } else {
                throw Exception("Error failure")
            }
        } catch (e: Exception) {
            emit(StateAction.ERROR(e))
        }
    }

    override fun NYCSatCatched() = flow {
        emit(StateAction.LOADING)
        try {
            val respose = service.getSchoolSat()
            if (respose.isSuccessful) {
                respose.body()?.let {
                    emit(StateAction.SUCCESS(it))
                    Log.i("Repository", "NYCSatCatched: $it ")
                } ?: throw Exception("Error null")
            } else {
                throw Exception("Error failure")
            }
        } catch (e: Exception) {
            emit(StateAction.ERROR(e))
        }
    }
}