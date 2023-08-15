package com.example.nycschools.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.nycopendata.databinding.ActivityMain2Binding
import com.example.nycschools.common.SCHOOL_ITEM
import com.example.nycschools.common.StateAction
import com.example.nycschools.databinding.ActivityMain2Binding
import com.example.nycschools.model.SchoolListResponse
import com.example.nycschools.model.SchoolSatResponse
import com.example.nycschools.model.remote.SchoolListResponse
import com.example.nycschools.model.remote.SchoolSatResponse
import com.example.nycschools.viewmodel.NYCViewModel
import dagger.hilt.android.AndroidEntryPoint

//Activity that provides detailed information about each school plus SAT scores if available
//if no SAT information present, element does not display.
//validation of data is also coded.
@AndroidEntryPoint
class MainActivity2 : AppCompatActivity() {
    private lateinit var bindingMain2Binding: ActivityMain2Binding

    private val viewModel: NYCViewModel by lazy {
        ViewModelProvider(this).get(NYCViewModel::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain2Binding = ActivityMain2Binding.inflate(layoutInflater)

        setContentView(bindingMain2Binding.root)

        intent.apply {
            val school = getParcelableExtra<SchoolListResponse>(SCHOOL_ITEM)

            bindingMain2Binding.tvSchoolName.text = school?.school_name
            bindingMain2Binding.tvAddress.text = school?.location
            bindingMain2Binding.tvEmail.text = school?.school_email
            bindingMain2Binding.tvWebsite.text = school?.website
            bindingMain2Binding.tvOverview.text = school?.overview_paragraph

            initObservables(school?.dbn)
        }

        viewModel.getSatList()

        bindingMain2Binding.backButton.setOnClickListener{
            val intentBack = Intent(this, MainActivity::class.java)
            startActivity(intentBack)
        }
    }

    private fun initObservables(schDbn: String?) {
        viewModel.schoolSatResponse.observe(this) { action ->
            when (action) {
                is StateAction.LOADING -> {
                    Toast.makeText(baseContext, "loading SAT schools...", Toast.LENGTH_SHORT).show()
                }
                is StateAction.SUCCESS<*> -> {
                    val newSchools = action.response as? List<SchoolSatResponse>
                    newSchools?.let {
                        Log.i("MainActivity2", "initObservablesSAT: $it ")
                        schDbn?.let { schoolDbn ->
                            populateSatDetails(it, schoolDbn)
                        } ?: showError("Error at school dbn null")
                    } ?: showError("Error at casting SAT")
                }
                is StateAction.ERROR -> {
                    showError(action.error.localizedMessage)
                }
            }
        }
    }

    private fun populateSatDetails(satDetails: List<SchoolSatResponse>, schDbn: String) {
        satDetails.firstOrNull { it.dbn == schDbn }?.let {
            if (it.mathAvg.isEmpty()) {
                bindingMain2Binding.scoreInfo.visibility = View.INVISIBLE
            } else {
                bindingMain2Binding.scoreInfo.visibility = View.VISIBLE
            }

            bindingMain2Binding.tvMathScores.text = it.mathAvg
            bindingMain2Binding.tvReadingScores.text = it.readingAvg
            bindingMain2Binding.tvWritingScores.text = it.writingAvg
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

    override fun onStop() {
        super.onStop()
        viewModel.schoolSatResponse.removeObservers(this)
    }
}