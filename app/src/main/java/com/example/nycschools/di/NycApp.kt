package com.example.nycschools.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//Dependency injection library that reduces creating manual dependency injections
//Objects need to go or where needed
@HiltAndroidApp
class NycApp: Application() {
}