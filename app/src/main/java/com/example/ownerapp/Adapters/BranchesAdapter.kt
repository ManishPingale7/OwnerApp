package com.example.ownerapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ownerapp.data.Branch
import com.example.ownerapp.databinding.BranchListItemBinding

class BranchesAdapter : ListAdapter<Branch, BranchesAdapter.MyViewHolder>(DiffCallBack()) {

    inner class MyViewHolder(private val binding: BranchListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(branch: Branch) {
            binding.apply {
                password.setText(branch.branchPass)
                hideIt.isPasswordVisibilityToggleEnabled = false
                branchNameItem.text = branch.branchName
                branchIDItem.text = branch.branchID
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            BranchListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallBack : DiffUtil.ItemCallback<Branch>() {
        override fun areItemsTheSame(oldItem: Branch, newItem: Branch) =
            oldItem.branchID == newItem.branchID

        override fun areContentsTheSame(oldItem: Branch, newItem: Branch) = oldItem == newItem

    }

}

