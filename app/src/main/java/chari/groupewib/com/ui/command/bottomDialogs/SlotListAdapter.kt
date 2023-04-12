package ghoudan.anfaSolution.com.ui.command.bottomDialogs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ghoudan.anfaSolution.com.app_models.Slot
import ghoudan.anfaSolution.com.common_ui.databinding.ItemTextSimpleBinding

class SlotListAdapter(
    private val entries: List<Slot>,
    private val callback: (Slot, Int) -> Unit
) :
    RecyclerView.Adapter<SlotListAdapter.SlotItemViewHolder>() {


    inner class SlotItemViewHolder(private val item: ItemTextSimpleBinding) :
        RecyclerView.ViewHolder(item.root) {

        fun bind(slot: Slot, position: Int) {
            item.title.apply {
                text = slot.toString()
                setOnClickListener {
                    callback.invoke(slot, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotItemViewHolder {
        return SlotItemViewHolder(
            ItemTextSimpleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SlotItemViewHolder, position: Int) {
        holder.bind(entries[position], position)
    }

    override fun getItemCount(): Int {
        return entries.size
    }
}
