package com.example.nycschools.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nycschools.common.OnSchoolSelected
import com.example.nycschools.databinding.SchoolItemLayoutBinding
import com.example.nycschools.model.SchoolListResponse

// update data, creating viewHolders and bind the viewHolder
class Adapter(
    private val onSchoolSelected: OnSchoolSelected,
    private val items: MutableList<SchoolListResponse> = mutableListOf()
) : RecyclerView.Adapter<Adapter.NYCViewHolder>() {

    class NYCViewHolder(val binding: SchoolItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    fun updateData(newSchools: List<SchoolListResponse>) {
        items.addAll(newSchools)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NYCViewHolder {
        return NYCViewHolder(
            SchoolItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NYCViewHolder, position: Int) {
        holder.binding.tvSchoolName.text = items[position].school_name
        holder.binding.tvSchoolLocation.text = items[position].neighborhood
        holder.binding.tvSchoolPhone.text = items[position].phone_number
        holder.binding.cardView.setOnClickListener {
            onSchoolSelected.schoolSelected(items[position])
        }
    }

    override fun getItemCount(): Int = items.size
}