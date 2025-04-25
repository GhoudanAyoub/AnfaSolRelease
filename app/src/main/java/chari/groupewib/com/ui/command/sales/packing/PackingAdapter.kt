package chari.groupewib.com.ui.command.sales.packing

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import chari.groupewib.com.common_ui.packing.PackingViewListener
import chari.groupewib.com.networking.entity.PackingListEntity
import ghoudan.anfaSolution.com.common_ui.stock.PackingViewModel
import ghoudan.anfaSolution.com.databinding.ItemPackingViewBinding

class PackingAdapter(val packingViewListener: PackingViewListener) :
    RecyclerView.Adapter<PackingAdapter.StockAdapterPackingListEntityViewHolder>() {
    var stockSaisieList: MutableList<PackingListEntity> = arrayListOf()

    inner class StockAdapterPackingListEntityViewHolder(private val PackingListEntityBinding: ItemPackingViewBinding) :
        RecyclerView.ViewHolder(PackingListEntityBinding.root) {

        fun bind(stock: PackingListEntity) {
            PackingListEntityBinding.root.bind(
                PackingViewModel(
                    stock,
                    packingViewListener
                )
            )
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<PackingListEntity>() {
        override fun areItemsTheSame(
            oldPackingListEntity: PackingListEntity,
            newPackingListEntity: PackingListEntity,
        ): Boolean {
            return oldPackingListEntity.codeFour == newPackingListEntity.codeFour ||
            oldPackingListEntity.isChecked == newPackingListEntity.isChecked
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldPackingListEntity: PackingListEntity,
            newPackingListEntity: PackingListEntity,
        ): Boolean {
            return oldPackingListEntity == newPackingListEntity
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): StockAdapterPackingListEntityViewHolder {
        val layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        return StockAdapterPackingListEntityViewHolder(
            ItemPackingViewBinding.inflate(
                LayoutInflater.from(parent.context)
            ).apply {
                this.root.layoutParams = layoutParams
            }
        )
    }

    override fun onBindViewHolder(holder: StockAdapterPackingListEntityViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setList(cmdList: MutableList<PackingListEntity>) {
        this.stockSaisieList = cmdList
        notifyDataSetChanged()
    }
}
