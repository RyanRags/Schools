package com.example.nycschools.common

//Actions to alert app users if there are issues loading the data remotely
sealed class StateAction {
    object LOADING : StateAction()
    class SUCCESS<T>(val response : T) : StateAction()
    class ERROR(val error: Exception) : StateAction()
}