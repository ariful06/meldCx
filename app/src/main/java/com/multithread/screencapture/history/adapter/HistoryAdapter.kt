package com.multithread.screencapture.history.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.multithread.screencapture.databinding.ItemImageBinding
import com.multithread.screencapture.history.HistoryViewModel
import com.multithread.screencapture.history.model.ImageDataModel
import com.multithread.screencapture.listener.ItemClickListener


class HistoryAdapter(
    private var items: MutableList<ImageDataModel>, private val viewModel: HistoryViewModel?,
    private val mActivity: Activity?
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>(),
    ItemClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemImageBinding =
            ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.item = item
        holder.binding.listener = this
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(var binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onItemClick(vararg data: Any) {
        val item = data[0] as ImageDataModel
        viewModel?.onItemClickEvent?.value = item

    }

    override fun onDeleteClick(vararg data: Any) {
        val item = data[0] as ImageDataModel
        viewModel?.deleteItem(item)
    }


    fun replaceData(items: List<ImageDataModel>?) {
        items?.let { setList(items) }
    }

    private fun setList(items: List<ImageDataModel>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

}