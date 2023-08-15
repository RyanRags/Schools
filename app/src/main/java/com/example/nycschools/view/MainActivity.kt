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
import com.example.nycschools.common.OnSchoolClicked
import com.example.nycschools.common.SCHOOL_ITEM
import com.example.nycschools.common.StateAction
import com.example.nycschools.model.SchoolListResponse
import com.example.nycschools.viewmodel.NYCViewModel
import dagger.hilt.android.AndroidEntryPoint
//Setting up the models that present a list of schools and
//provide an option to click and to each school details.
//Validation is also provided.

//Initializing the data
//No need to find findviewbyID
//Observing the data for changes and making changes to UI
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnSchoolClicked {

    private lateinit var bindingMain: ActivityMainBinding

    private val viewModel: NYCViewModel by lazy {
        ViewModelProvider(this).get(NYCViewModel::class.java)
    }

    private lateinit var adapterSchool: NYCAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)
    }

    override fun onResume() {
        super.onResume()

        initializeRecyclerView()
        initObservables()
        viewModel.getSchoolList()
    }

    private fun initializeRecyclerView() {
        adapterSchool = NYCAdapter(this)

        bindingMain.listSchool.apply {
            layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
            adapter = adapterSchool
        }
    }

    private fun initObservables() {
        viewModel.schoolResponse.observe(this) { action ->
            when (action) {
                is StateAction.LOADING -> {
                    Toast.makeText(baseContext, "loading schools...", Toast.LENGTH_SHORT).show()
                }
                is StateAction.SUCCESS<*> -> {
                    val newSchools = action.response as? List<SchoolListResponse>
                    newSchools?.let {
                        adapterSchool.updateData(it)
                        Log.i("MainActivity", "initIbservablesSchoolResponse $it ")
                    } ?: showError("Error at casting")
                }
                is StateAction.ERROR -> {
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

    override fun schoolClicked(school: SchoolListResponse) {
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

