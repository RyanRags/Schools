package com.example.nycschools.common

import com.example.nycschools.model.SchoolListResponse
//Interface to click on a particular school to get details about the school
interface OnSchoolClicked {
    fun schoolClicked(school: SchoolListResponse)

}