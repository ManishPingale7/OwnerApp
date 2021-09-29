package com.example.ownerapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ownerapp.data.Plan
import com.example.ownerapp.databinding.PlanlistitemBinding

class ViewPlansAdapter : ListAdapter<Plan, ViewPlansAdapter.PlansViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlansViewHolder {
        return PlansViewHolder(
            PlanlistitemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PlansViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlansViewHolder(val binding: PlanlistitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var text = ""
        fun bind(plan: Plan) {
            text = if (plan.pt) {
                "Personal Training"
            } else {
                "Normal"
            }
            binding.apply {
                cardPlanName.text = plan.name
                cardPlanDesc.text = plan.desc
                cardDuration.text = plan.timeNumber
                isPersonal.text = text
                cardFees.text = plan.fees
            }

        }
    }

}

class DiffCallBack : DiffUtil.ItemCallback<Plan>() {
    override fun areItemsTheSame(oldItem: Plan, newItem: Plan) =
        oldItem.name == newItem.name


    override fun areContentsTheSame(oldItem: Plan, newItem: Plan) =
        oldItem == newItem

}