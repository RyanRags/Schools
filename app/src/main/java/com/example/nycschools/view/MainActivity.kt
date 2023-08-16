package com.example.nycschools.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nycschools.databinding.ActivityMainBinding
import com.example.nycschools.common.OnSchoolSelected
import com.example.nycschools.common.SCHOOL_ITEM
import com.example.nycschools.common.State
import com.example.nycschools.model.SchoolListResponse
import com.example.nycschools.viewmodel.NYCViewModel
import dagger.hilt.android.AndroidEntryPoint

//Setting up the models that present a list of schools that
// the user can interact with by an option to click and see each schools details
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnSchoolSelected {
    private lateinit var activityMainBinding: ActivityMainBinding
    private val viewModel: NYCViewModel by lazy {
        ViewModelProvider(this).get(NYCViewModel::class.java)
    }

    private lateinit var adapterSchool: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
    }

    override fun onResume() {
        super.onResume()
        recyclerView()
        initObservables()
        viewModel.getSchoolList()
    }

    private fun recyclerView() {
        adapterSchool = Adapter(this)
        activityMainBinding.listSchool.apply {
            layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
            adapter = adapterSchool
        }
    }

    private fun initObservables() {
        viewModel.schoolResponse.observe(this) { action ->
            when (action) {
                is State.LOADING -> {
                    Toast.makeText(baseContext, "loading schools...", Toast.LENGTH_SHORT).show()
                }
                is State.SUCCESS<*> -> {
                    val newSchools = action.response as? List<SchoolListResponse>
                    newSchools?.let {
                        adapterSchool.updateData(it)
                        Log.i("MainActivity", "initIbservablesSchoolResponse $it ")
                    } ?: showError("Error at casting")
                }
                is State.ERROR -> {
                    showError(action.error.localizedMessage)
                }
            }
        }
    }

    private fun showError(message: String) {
        AlertDialog.Builder(baseContext)
            .setTitle("Error occurred")
            .setMessage(message)
            .setNegativeButton("CLOSE") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun schoolSelected(school: SchoolListResponse) {
        Intent(baseContext, DetailActivity::class.java).apply {
            putExtra(SCHOOL_ITEM, school)
            startActivity(this)
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.schoolResponse.removeObservers(this)
    }
}

