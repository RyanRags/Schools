package com.example.nycschools.model

import com.example.nycschools.common.END_POINT_SAT
import com.example.nycschools.common.END_POINT_SCHOOLS
import retrofit2.Response
import retrofit2.http.GET

//API to get the School and Scholastic Assessment Test (SAT)
interface NycApi {

    @GET(END_POINT_SCHOOLS)
    suspend fun getSchoolList(): Response<List<SchoolListResponse>>

    @GET(END_POINT_SAT)
    suspend fun getSchoolSat(): Response<List<SchoolSatResponse>>
}