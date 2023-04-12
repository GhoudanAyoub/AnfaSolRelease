package ghoudan.anfaSolution.com.ui.command.bottomDialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import ghoudan.anfaSolution.com.R
import ghoudan.anfaSolution.com.app_models.Slot
import ghoudan.anfaSolution.com.common_ui.itemDecoration.CartVerticalItemDecoration
import ghoudan.anfaSolution.com.databinding.FragmentSlotListBottomDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SlotListBottomDialog(var slots: List<Slot>, var callback: (Slot) -> Unit) :
    BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSlotListBottomDialogBinding

    private val slotListAdapter: SlotListAdapter by lazy {
        SlotListAdapter(slots) { selectedItem, position ->
            binding.recyclerView.smoothScrollToPosition(position)
            callback.invoke(selectedItem)
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSlotListBottomDialogBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = slotListAdapter
            if (itemDecorationCount == 0) {
                addItemDecoration(
                    CartVerticalItemDecoration(
                        resources.getDimensionPixelSize(R.dimen.spacing_xs)
                    )
                )
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val window = dialog.window
        window?.setBackgroundDrawableResource(R.drawable.bg_primary_transparent)
        return dialog
    }

}
