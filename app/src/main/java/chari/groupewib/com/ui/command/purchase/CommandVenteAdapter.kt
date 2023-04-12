package chari.groupewib.com.ui.command.purchase

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ghoudan.anfaSolution.com.app_models.PurchaseOrder
import ghoudan.anfaSolution.com.common_ui.order.CommandSalesView
import ghoudan.anfaSolution.com.common_ui.order.CommandListener
import ghoudan.anfaSolution.com.databinding.ItemSalesOrderViewBinding
import java.util.Locale

class CommandVenteAdapter(
    private val commandListener: CommandListener
) : RecyclerView.Adapter<CommandVenteAdapter.CommandVenteAdapterItemViewHolder>(), Filterable {
    var cmdList: MutableList<PurchaseOrder> = arrayListOf()

    inner class CommandVenteAdapterItemViewHolder(private val itemBinding: ItemSalesOrderViewBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(command: PurchaseOrder) {
            itemBinding.root.bind(
                CommandSalesView.CommandSalesViewData(
                    command = command,
                    listener = commandListener
                )
            )
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<PurchaseOrder>() {
        override fun areItemsTheSame(oldItem: PurchaseOrder, newItem: PurchaseOrder): Boolean {
            return oldItem.counter == newItem.counter
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: PurchaseOrder, newItem: PurchaseOrder): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommandVenteAdapterItemViewHolder {
        val layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        return CommandVenteAdapterItemViewHolder(
            ItemSalesOrderViewBinding.inflate(
                LayoutInflater.from(parent.context)
            ).apply {
                this.root.layoutParams = layoutParams
            }
        )
    }

    override fun onBindViewHolder(holder: CommandVenteAdapterItemViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setList(cmdList: MutableList<PurchaseOrder>) {
        this.cmdList = cmdList
    }

    @Suppress("UNCHECKED_CAST")
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList: ArrayList<PurchaseOrder> = arrayListOf()
                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(cmdList)
                } else {
                    val filterKeyword = constraint.toString()
                        .lowercase(Locale.getDefault()).trim()
                    cmdList.filter { cmd ->
                        cmd.Pay_to_Name.toString().lowercase().contains(filterKeyword) ||
                        cmd.Pay_to_Contact.toString().lowercase().contains(filterKeyword) ||
                        cmd.Pay_to_Vendor_No.toString().lowercase().contains(filterKeyword) ||
                        cmd.counter.toString().lowercase().contains(filterKeyword) ||
                                cmd.customerId.toString().contains(filterKeyword) ||
                                cmd.counter?.lowercase()
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
                    results.values as ArrayList<PurchaseOrder>
                }
                differ.submitList(filteredCmdList)
                notifyDataSetChanged()
            }
        }
    }
}
