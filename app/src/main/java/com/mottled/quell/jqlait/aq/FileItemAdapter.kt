package com.mottled.quell.jqlait.aq

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mottled.quell.jqlait.dmothi.JunkFile
import com.mottled.quell.kill.R
import com.mottled.quell.kill.databinding.WednesFileItemBinding

class FileItemAdapter(
    private val files: List<JunkFile>,
    private val onSelectionChanged: () -> Unit
) : RecyclerView.Adapter<FileItemAdapter.FileViewHolder>() {

    inner class FileViewHolder(private val binding: WednesFileItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(file: JunkFile) {
            binding.tvFileName.text = file.name
            binding.tvFileSize.text = file.getSizeFormatted()
            updateSelectionState(file)

            binding.root.setOnClickListener {
                file.isSelected = !file.isSelected
                updateSelectionState(file)
                onSelectionChanged()
            }

            binding.imgFileSelect.setOnClickListener {
                file.isSelected = !file.isSelected
                updateSelectionState(file)
                onSelectionChanged()
            }
        }

        private fun updateSelectionState(file: JunkFile) {
            binding.imgFileSelect.setImageResource(
                if (file.isSelected) R.drawable.ic_wednes_check
                else R.drawable.ic_wednes_discheck
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val binding = WednesFileItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(files[position])
    }

    override fun getItemCount() = files.size
}