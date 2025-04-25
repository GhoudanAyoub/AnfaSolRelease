package chari.groupewib.com.ui.command.bottomDialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import chari.groupewib.com.common_ui.packing.PackingViewListener
import chari.groupewib.com.networking.entity.PackingListEntity
import chari.groupewib.com.ui.command.sales.packing.PackingAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ghoudan.anfaSolution.com.MainActivity
import ghoudan.anfaSolution.com.R
import ghoudan.anfaSolution.com.databinding.FragmentItemListDialogListDialogBinding

class PackingListBottomDialog(
    var stockSaisieList: List<PackingListEntity>,
    var callback: () -> Unit,
) : BottomSheetDialogFragment(), PackingViewListener {

    private lateinit var binding: FragmentItemListDialogListDialogBinding

    var poids: Double = 0.0
    var colis: Int = 0

    private val controller by lazy {
        PackingAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentItemListDialogListDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setCancelable(true)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        val window = dialog.window
        window?.setBackgroundDrawableResource(R.drawable.bg_primary_transparent)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? MainActivity)?.enableView(false)
        val modalBottomSheetBehavior = (this.dialog as BottomSheetDialog).behavior
        modalBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        modalBottomSheetBehavior.isHideable = true
        modalBottomSheetBehavior.skipCollapsed = true
        setupRecyclerView()
        binding.coliesInfoCntr.visibility = View.VISIBLE
        binding.searchView.visibility = View.GONE
        getPackingList()
        updateColisInfo()
        binding.save.setOnClickListener {
            callback.invoke()
            dismiss()
        }
    }

    private fun getPackingList() {
        controller.differ.submitList(stockSaisieList)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = controller
        binding.recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    override fun onItemClicked(stock: PackingListEntity,weight: Double, ischecked: Boolean) {
        if (ischecked) {
            this.poids += weight
            this.colis++
        } else {
            this.poids -= weight
            if (this.colis > 0) this.colis--
        }
        updateList(stock)
        updateColisInfo()
    }

    private fun updateList(stock: PackingListEntity) {
        stockSaisieList.find { it.codeFour == stock.codeFour }?.isChecked = stock.isChecked
        controller.differ.submitList(stockSaisieList)
    }

    private fun updateColisInfo() {
        binding.totalColis.text = resources.getString(R.string.total_colis, colis.toString())
        binding.totalPoids.text = resources.getString(R.string.total_poids, poids)
        binding.coliesInfoCntr.visibility = View.VISIBLE
        binding.save.visibility = View.VISIBLE
        binding.save.isEnabled = colis > 0
    }
}
