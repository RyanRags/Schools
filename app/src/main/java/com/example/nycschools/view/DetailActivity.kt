package com.example.nycschools.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.nycschools.common.SCHOOL_ITEM
import com.example.nycschools.common.State
import com.example.nycschools.databinding.ActivityDetailBinding
import com.example.nycschools.model.SchoolListResponse
import com.example.nycschools.model.SchoolSatResponse
import com.example.nycschools.viewmodel.NYCViewModel
import dagger.hilt.android.AndroidEntryPoint

// This activity provides details about each school with SAT scores if they are available
@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var activityDetailBinding: ActivityDetailBinding
    private val viewModel: NYCViewModel by lazy {
        ViewModelProvider(this).get(NYCViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(activityDetailBinding.root)
        intent.apply {
            val school = getParcelableExtra<SchoolListResponse>(SCHOOL_ITEM)
            activityDetailBinding.tvSchoolName.text = school?.school_name
            activityDetailBinding.tvAddress.text = school?.location
            activityDetailBinding.tvEmail.text = school?.school_email
            activityDetailBinding.tvWebsite.text = school?.website
            activityDetailBinding.tvOverview.text = school?.overview_paragraph
            initObservables(school?.dbn)
        }
        viewModel.getSatList()
    }

    private fun initObservables(schDbn: String?) {
        viewModel.schoolSatResponse.observe(this) { action ->
            when (action) {
                is State.LOADING -> {
                    Toast.makeText(baseContext, "loading SAT schools...", Toast.LENGTH_SHORT).show()
                }
                is State.SUCCESS<*> -> {
                    val newSchools = action.response as? List<SchoolSatResponse>
                    newSchools?.let {
                        Log.i("MainActivity2", "initObservablesSAT: $it ")
                        schDbn?.let { schoolDbn ->
                            satDetails(it, schoolDbn)
                        } ?: showError("Error at school dbn null")
                    } ?: showError("Error at casting SAT")
                }
                is State.ERROR -> {
                    showError(action.error.localizedMessage)
                }
            }
        }
    }

    private fun satDetails(satDetails: List<SchoolSatResponse>, schDbn: String) {
        satDetails.firstOrNull { it.dbn == schDbn }?.let {
            if (it.mathAvg.isEmpty()) {
                activityDetailBinding.scoreInfo.visibility = View.INVISIBLE
            } else {
                activityDetailBinding.scoreInfo.visibility = View.VISIBLE
            }
            activityDetailBinding.tvMathScores.text = it.mathAvg
            activityDetailBinding.tvReadingScores.text = it.readingAvg
            activityDetailBinding.tvWritingScores.text = it.writingAvg
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