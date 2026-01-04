package com.mottled.quell.jqlait.aq

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mottled.quell.jqlait.dmothi.JunkCategory
import com.mottled.quell.kill.R
import com.mottled.quell.kill.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val categories: List<JunkCategory>
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: JunkCategory) {
            binding.apply {
                imageView.setImageResource(category.type.iconRes)
                tvTitle.text = category.type.title
                tvSize.text = category.getSizeFormatted()

                // 更新展开/收起图标
                updateExpandIcon(category.isExpanded)

                // 更新选中状态
                updateSelectionIcon(category.isAllSelected())

                // 设置文件列表
                setupFileList(category)

                // 点击展开/收起
                llCategory.setOnClickListener {
                    category.isExpanded = !category.isExpanded
                    updateExpandIcon(category.isExpanded)
                    rvItemFile.visibility = if (category.isExpanded) View.VISIBLE else View.GONE
                }

                // 点击选中整个分类
                imgSelect.setOnClickListener {
                    val newState = !category.isAllSelected()
                    category.selectAll(newState)
                    updateSelectionIcon(newState)
                    // 刷新文件列表以更新子项选中状态
                    rvItemFile.adapter?.notifyDataSetChanged()
                }
            }
        }

        private fun setupFileList(category: JunkCategory) {
            binding.rvItemFile.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = FileItemAdapter(category.files) {
                    // 文件选中状态改变时，更新分类选中图标
                    binding.updateSelectionIcon(category.isAllSelected())
                }
                visibility = if (category.isExpanded) View.VISIBLE else View.GONE
            }
        }

        private fun updateExpandIcon(isExpanded: Boolean) {
            binding.imgInstruct.setImageResource(
                if (isExpanded) R.drawable.ic_wednes_top
                else R.drawable.ic_wednes_bottom
            )
        }

        private fun ItemCategoryBinding.updateSelectionIcon(isSelected: Boolean) {
            imgSelect.setImageResource(
                if (isSelected) R.drawable.ic_wednes_check
                else R.drawable.ic_wednes_discheck
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount() = categories.size
}