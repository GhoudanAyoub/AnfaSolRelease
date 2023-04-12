package chari.groupewib.com.ui.command.items

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import chari.groupewib.com.app_models.ItemStock
import chari.groupewib.com.common_ui.product.ItemViewListener
import ghoudan.anfaSolution.com.common_ui.order.data.ProductOrderViewData
import ghoudan.anfaSolution.com.databinding.ItemsviewBinding
import java.util.Locale


class StockAdapter(
    private val commandListener: ItemViewListener
) : RecyclerView.Adapter<StockAdapter.ItemsAdapterItemViewHolder>(), Filterable {
    var cmdList: MutableList<ItemStock> = arrayListOf()

    inner class ItemsAdapterItemViewHolder(private val itemBinding: ItemsviewBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(command: ItemStock) {
            itemBinding.root.bind(
                ProductOrderViewData(
                    orderLine = command,
                    listener = commandListener
                )
            )
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<ItemStock>() {
        override fun areItemsTheSame(oldItem: ItemStock, newItem: ItemStock): Boolean {
            return oldItem.code == newItem.code
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ItemStock, newItem: ItemStock): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemsAdapterItemViewHolder {
        val layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        return ItemsAdapterItemViewHolder(
            ItemsviewBinding.inflate(
                LayoutInflater.from(parent.context)
            ).apply {
                this.root.layoutParams = layoutParams
            }
        )
    }

    override fun onBindViewHolder(holder: ItemsAdapterItemViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setList(cmdList: MutableList<ItemStock>) {
        this.cmdList = cmdList
        notifyDataSetChanged()
    }

    @Suppress("UNCHECKED_CAST")
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList: ArrayList<ItemStock> = arrayListOf()
                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(cmdList)
                } else {
                    val filterKeyword = constraint.toString()
                        .lowercase(Locale.getDefault()).trim()
                    cmdList.filter { cmd ->
                        cmd.description.toString().lowercase().contains(filterKeyword) ||
                                cmd.code?.toString()
                                    ?.contains(filterKeyword) == true
                    }.forEach { cmd ->
                        filteredList.add(cmd)
                    }
                }
                return FilterResults().apply { values = filteredList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val filteredCmdList = if (results?.values == null) {
                    arrayListOf()
                } else {
                    results.values as ArrayList<ItemStock>
                }
                differ.submitList(filteredCmdList)
                notifyDataSetChanged()
            }
        }
    }
}
