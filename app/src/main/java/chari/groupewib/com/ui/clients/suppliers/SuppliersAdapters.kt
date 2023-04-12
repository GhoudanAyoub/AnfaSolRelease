package chari.groupewib.com.ui.clients.suppliers


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ghoudan.anfaSolution.com.app_models.SupplierAnfa
import ghoudan.anfaSolution.com.common_ui.client.ClientViewListener
import ghoudan.anfaSolution.com.common_ui.client.SupplierView
import ghoudan.anfaSolution.com.databinding.ItemSupplierViewBinding
import java.util.Locale

class SuppliersAdapters(
    private val commandListener: ClientViewListener
) : RecyclerView.Adapter<SuppliersAdapters.SuppliersAdaptersItemViewHolder>(), Filterable {
    var cmdList: MutableList<SupplierAnfa> = arrayListOf()

    inner class SuppliersAdaptersItemViewHolder(private val itemBinding: ItemSupplierViewBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(command: SupplierAnfa) {
            itemBinding.root.bind(
                SupplierView.SupplierViewData(
                    customer = command,
                    clientViewListener = commandListener
                )
            )
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<SupplierAnfa>() {
        override fun areItemsTheSame(oldItem: SupplierAnfa, newItem: SupplierAnfa): Boolean {
            return oldItem.code == newItem.code
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: SupplierAnfa, newItem: SupplierAnfa): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SuppliersAdaptersItemViewHolder {
        val layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        return SuppliersAdaptersItemViewHolder(
            ItemSupplierViewBinding.inflate(
                LayoutInflater.from(parent.context)
            ).apply {
                this.root.layoutParams = layoutParams
            }
        )
    }

    override fun onBindViewHolder(holder: SuppliersAdaptersItemViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setList(cmdList: MutableList<SupplierAnfa>) {
        this.cmdList = cmdList
    }

    @Suppress("UNCHECKED_CAST")
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList: ArrayList<SupplierAnfa> = arrayListOf()
                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(cmdList)
                } else {
                    val filterKeyword = constraint.toString()
                        .lowercase(Locale.getDefault()).trim()
                    cmdList.filter { cmd ->
                        cmd.name.toString().lowercase().contains(filterKeyword) ||
                                cmd.Phone_No.toString().contains(filterKeyword) ||
                                cmd.code?.lowercase()
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
                    results.values as ArrayList<SupplierAnfa>
                }
                differ.submitList(filteredCmdList)
                notifyDataSetChanged()
            }
        }
    }
}
