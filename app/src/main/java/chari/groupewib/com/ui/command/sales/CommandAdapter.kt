package chari.groupewib.com.ui.command.sales

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ghoudan.anfaSolution.com.app_models.CommandAchat
import ghoudan.anfaSolution.com.common_ui.databinding.ItemOrderViewBinding
import ghoudan.anfaSolution.com.common_ui.order.CommandListener
import ghoudan.anfaSolution.com.common_ui.order.CommandView
import java.util.Locale

class CommandAdapter(
    private val commandListener: CommandListener
) : RecyclerView.Adapter<CommandAdapter.CommandAdapterItemViewHolder>(), Filterable {
    var cmdList: MutableList<CommandAchat> = arrayListOf()

    inner class CommandAdapterItemViewHolder(private val itemBinding: ItemOrderViewBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(command: CommandAchat) {
            itemBinding.root.bind(
                CommandView.CommandViewData(
                    command = command,
                    listener = commandListener
                )
            )
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<CommandAchat>() {
        override fun areItemsTheSame(oldItem: CommandAchat, newItem: CommandAchat): Boolean {
            return oldItem.counter == newItem.counter
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: CommandAchat, newItem: CommandAchat): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommandAdapterItemViewHolder {
        val layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        return CommandAdapterItemViewHolder(
            ItemOrderViewBinding.inflate(
                LayoutInflater.from(parent.context)
            ).apply {
                this.root.layoutParams = layoutParams
            }
        )
    }

    override fun onBindViewHolder(holder: CommandAdapterItemViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setList(cmdList: MutableList<CommandAchat>) {
        this.cmdList = cmdList
    }

    @Suppress("UNCHECKED_CAST")
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList: ArrayList<CommandAchat> = arrayListOf()
                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(cmdList)
                } else {
                    val filterKeyword = constraint.toString()
                        .lowercase(Locale.getDefault()).trim()
                    cmdList.filter { cmd ->
                        cmd.No.toString().lowercase().contains(filterKeyword) ||
                        cmd.customerName.toString().lowercase().contains(filterKeyword) ||
                        cmd.counter.toString().lowercase().contains(filterKeyword) ||
                                cmd.customerId.toString().contains(filterKeyword)
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
                    results.values as ArrayList<CommandAchat>
                }
                differ.submitList(filteredCmdList)
                notifyDataSetChanged()
            }
        }
    }
}
