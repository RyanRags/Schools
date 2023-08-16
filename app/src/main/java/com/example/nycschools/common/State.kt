package com.example.nycschools.common

//Actions to alert app users if there are issues loading the data remotely
sealed class State {
    object LOADING : State()
    class SUCCESS<T>(val response : T) : State()
    class ERROR(val error: Exception) : State()
}