package com.example.ownerapp.Adapters

import android.view.LayoutInflater
import android.view.View
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
        var RESULT = View.VISIBLE
        fun bind(plan: Plan) {
            if (plan.pt!!) {
                text = "PT"
                RESULT = View.VISIBLE

            } else {
                text = "Normal"
                RESULT = View.INVISIBLE
            }
            binding.apply {
                cardPlanName.text = plan.name
                // cardPlanDesc.text = plan.desc
                cardDuration.text = plan.timeNumber
                isPersonal.text = text
                cardFees.text = plan.fees
                badgeGold.visibility = RESULT
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