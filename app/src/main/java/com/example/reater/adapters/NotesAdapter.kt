package com.example.reater.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.reater.databinding.NotesRowLayoutBinding
import com.example.reater.models.StudyMaterial

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    private var notes = emptyList<StudyMaterial>()

    companion object {
        fun sampleNotes(): List<StudyMaterial> {
            return listOf(
                StudyMaterial(
                    filename = "Unit 1 - Introduction to Networks.pdf",
                    timestamp = "Apr 29, 2026",
                    authorName = "Dr. Sudhir Sharma",
                    authorDP = "",
                    fileLocation = "https://example.com/network-notes"
                ),
                StudyMaterial(
                    filename = "Chapter 4 - Routing Basics.pdf",
                    timestamp = "Apr 28, 2026",
                    authorName = "Dr. Pankaj Vyas",
                    authorDP = "",
                    fileLocation = "https://example.com/routing-notes"
                ),
                StudyMaterial(
                    filename = "MIDSEM Revision Notes.pdf",
                    timestamp = "Apr 27, 2026",
                    authorName = "Veena Khandelwal",
                    authorDP = "",
                    fileLocation = "https://example.com/revision-notes"
                )
            )
        }
    }

    class NotesViewHolder(val binding: NotesRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: StudyMaterial) {
            binding.NoteTitle.text = note.filename
            binding.SubCodeValue.text = if (note.timestamp.isNotBlank()) note.timestamp else "Material"
            binding.uploaderValue.text = note.authorName
        }
        
        companion object {
            fun from(parent: ViewGroup): NotesViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = NotesRowLayoutBinding.inflate(inflater, parent, false)
                return NotesViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun setData(newNotes: List<StudyMaterial>) {
        val notesDiffUtil = NotesDiffUtil(notes, newNotes)
        val diffUtilResult = DiffUtil.calculateDiff(notesDiffUtil)
        notes = newNotes
        diffUtilResult.dispatchUpdatesTo(this)
    }

    fun loadSampleData() {
        setData(sampleNotes())
    }

    class NotesDiffUtil(
        private val oldList: List<StudyMaterial>,
        private val newList: List<StudyMaterial>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].filename == newList[newItemPosition].filename
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}