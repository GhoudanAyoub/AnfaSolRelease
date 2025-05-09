package chari.groupewib.com.ui.command.bottomDialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import androidx.recyclerview.widget.LinearLayoutManager
import chari.groupewib.com.common_ui.packing.PackingViewListener
import chari.groupewib.com.networking.entity.PackingListEntity
import chari.groupewib.com.networking.request.BuildPackingListUrl
import chari.groupewib.com.ui.command.sales.CommandViewModel
import chari.groupewib.com.ui.command.sales.UpdateColisStatus
import chari.groupewib.com.ui.command.sales.packing.PackingAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ghoudan.anfaSolution.com.MainActivity
import ghoudan.anfaSolution.com.R
import ghoudan.anfaSolution.com.databinding.FragmentItemListDialogListDialogBinding
import ghoudan.anfaSolution.com.networking.state.EpApiState
import ghoudan.anfaSolution.com.utils.AppUtils

class PackingListBottomDialog(
    var stockSaisieList: List<PackingListEntity>,
    var isSelected : Boolean? = false,
    var callback: () -> Unit,
) : BottomSheetDialogFragment(), PackingViewListener {

    private lateinit var binding: FragmentItemListDialogListDialogBinding
    private val fragmentViewModel by activityViewModels<CommandViewModel>()

    var modalBottomSheetBehavior: BottomSheetBehavior<FrameLayout>? = null

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
        modalBottomSheetBehavior = (this.dialog as BottomSheetDialog).behavior
        modalBottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED

        modalBottomSheetBehavior?.isHideable = true
        modalBottomSheetBehavior?.skipCollapsed = true
        setupRecyclerView()
        binding.coliesInfoCntr.visibility = View.VISIBLE
        binding.searchView.visibility = View.GONE

        if (isSelected == true) {
            stockSaisieList.map {
                this.poids += it.restePoids.toDouble()
            }
            this.colis = stockSaisieList.size
            binding.save.visibility = View.GONE
        } else {
            binding.save.visibility = View.VISIBLE
        }
        checkIfListIsEmpty()
        getPackingList()
        updateColisInfo()

        binding.save.setOnClickListener {
            binding.loaderProgress.visibility = View.VISIBLE
            binding.save.visibility = View.GONE
            modalBottomSheetBehavior?.isHideable = false
            modalBottomSheetBehavior?.skipCollapsed = false
            updateArticleColis()
        }
    }

    private fun updateArticleColis() {
        fragmentViewModel.updateArticleColis(stockSaisieList){
            when(it){
                UpdateColisStatus.DELIVERED -> {
                   binding.loaderProgress.visibility = View.GONE
                   binding.save.visibility = View.VISIBLE
                    modalBottomSheetBehavior?.isHideable = true
                    modalBottomSheetBehavior?.skipCollapsed = true
                    dismiss()
                }
                UpdateColisStatus.LOADING -> {
                    binding.loaderProgress.visibility = View.VISIBLE
                    binding.save.visibility = View.GONE
                    modalBottomSheetBehavior?.isHideable = false
                    modalBottomSheetBehavior?.skipCollapsed = false
                    (requireActivity() as? MainActivity)?.showLoader()
                }
                UpdateColisStatus.CANCELLED -> {
                    binding.loaderProgress.visibility = View.GONE
                    binding.savle.visibility = View.VISIBLE
                    modalBottomSheetBehavior?.isHideable = true
                    modalBottomSheetBehavior?.skipCollapsed = true
                    (requireActivity() as? MainActivity)?.hideLoader()
                    dismiss()
                }
            }
        }
    }

    private fun checkIfListIsEmpty() {
        if (stockSaisieList.isEmpty()) {
            binding.coliesInfoCntr.visibility = View.GONE
            binding.save.visibility = View.GONE
        } else {
            binding.coliesInfoCntr.visibility = View.VISIBLE
            binding.save.visibility = View.VISIBLE
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
        updateColisInfo()
        updateList(stock)
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (requireActivity() as? MainActivity)?.hideLoader()
    }
}
