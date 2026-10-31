package com.example.reater.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reater.databinding.ItemAvailableClassBinding
import com.example.reater.models.AvailableClass

class AvailableClassAdapter(
    private val onRegisterClick: (AvailableClass) -> Unit
) : RecyclerView.Adapter<AvailableClassAdapter.ViewHolder>() {

    private var items: List<AvailableClass> = emptyList()

    fun setItems(newItems: List<AvailableClass>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAvailableClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemAvailableClassBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AvailableClass) {
            binding.subjectNameText.text = item.subjectName
            binding.subjectCodeBadge.text = item.subjectCode
            binding.creditsText.text = "${item.credits} Credits • ${item.semester}"
            binding.teacherNameText.text = "Teacher: ${item.teacherName}"

            if (!item.streamName.isNullOrEmpty()) {
                binding.streamBadge.visibility = View.VISIBLE
                val formattedStream = item.streamName.replace("_", "/")
                binding.streamBadge.text = "$formattedStream Stream"
            } else {
                binding.streamBadge.visibility = View.GONE
            }

            if (item.isRegistered) {
                binding.registerButton.isEnabled = false
                binding.registerButton.text = "Registered"
                binding.registerButton.alpha = 0.6f
            } else if (item.isStreamLocked) {
                binding.registerButton.isEnabled = false
                binding.registerButton.text = "Locked"
                binding.registerButton.alpha = 0.45f
            } else {
                binding.registerButton.isEnabled = true
                binding.registerButton.text = "Register"
                binding.registerButton.alpha = 1.0f
                binding.registerButton.setOnClickListener {
                    onRegisterClick(item)
                }
            }
        }
    }
}
