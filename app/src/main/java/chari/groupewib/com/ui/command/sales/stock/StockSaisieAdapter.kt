package chari.groupewib.com.ui.command.sales.stock

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import chari.groupewib.com.common_ui.stock.StockViewListener
import chari.groupewib.com.networking.entity.StockSaisieEntity
import ghoudan.anfaSolution.com.common_ui.stock.StockSaisieViewState
import ghoudan.anfaSolution.com.databinding.ItemStockSaisieViewBinding
import java.util.Locale

class StockSaisieAdapter(
    private val stockViewListener: StockViewListener,
) : RecyclerView.Adapter<StockSaisieAdapter.StockAdapterStockSaisieEntityViewHolder>(), Filterable {
    var stockSaisieList: MutableList<StockSaisieEntity> = arrayListOf()

    inner class StockAdapterStockSaisieEntityViewHolder(private val stockSaisieEntityBinding: ItemStockSaisieViewBinding) :
        RecyclerView.ViewHolder(stockSaisieEntityBinding.root) {

        fun bind(stock: StockSaisieEntity) {
            stockSaisieEntityBinding.root.bind(
                StockSaisieViewState(
                    stock,
                    stockViewListener
                )
            )
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<StockSaisieEntity>() {
        override fun areItemsTheSame(
            oldStockSaisieEntity: StockSaisieEntity,
            newStockSaisieEntity: StockSaisieEntity,
        ): Boolean {
            return oldStockSaisieEntity.no == newStockSaisieEntity.no
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldStockSaisieEntity: StockSaisieEntity,
            newStockSaisieEntity: StockSaisieEntity,
        ): Boolean {
            return oldStockSaisieEntity == newStockSaisieEntity
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): StockAdapterStockSaisieEntityViewHolder {
        val layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        return StockAdapterStockSaisieEntityViewHolder(
            ItemStockSaisieViewBinding.inflate(
                LayoutInflater.from(parent.context)
            ).apply {
                this.root.layoutParams = layoutParams
            }
        )
    }

    override fun onBindViewHolder(holder: StockAdapterStockSaisieEntityViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setList(cmdList: MutableList<StockSaisieEntity>) {
        this.stockSaisieList = cmdList
        notifyDataSetChanged()
    }

    @Suppress("UNCHECKED_CAST")
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList: ArrayList<StockSaisieEntity> = arrayListOf()
                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(stockSaisieList)
                } else {
                    val filterKeyword = constraint.toString()
                        .lowercase(Locale.getDefault()).trim()
                    stockSaisieList.filter { cmd ->
                        cmd.description.toString().lowercase().contains(filterKeyword) ||
                                cmd.no?.toString()
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
                    results.values as ArrayList<StockSaisieEntity>
                }
                differ.submitList(filteredCmdList)
                notifyDataSetChanged()
            }
        }
    }
}
