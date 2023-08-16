package com.example.nycschools.common

import com.example.nycschools.model.SchoolListResponse

//Interface to get details about the school
interface OnSchoolSelected {
    fun schoolSelected(school: SchoolListResponse)
}