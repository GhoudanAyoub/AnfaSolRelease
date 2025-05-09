package chari.groupewib.com.ui.command.bottomDialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import chari.groupewib.com.common_ui.stock.StockViewListener
import chari.groupewib.com.networking.entity.PackingListEntity
import chari.groupewib.com.networking.entity.StockSaisieEntity
import chari.groupewib.com.ui.command.sales.CommandViewModel
import chari.groupewib.com.ui.command.sales.stock.StockSaisieAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.anfaSolution.com.MainActivity
import ghoudan.anfaSolution.com.R
import ghoudan.anfaSolution.com.app_models.CommandAchat
import ghoudan.anfaSolution.com.databinding.FragmentItemListDialogListDialogBinding
import ghoudan.anfaSolution.com.networking.state.EpApiState
import ghoudan.anfaSolution.com.utils.AppUtils

@AndroidEntryPoint
class StockSaisieListBottomDialog(
    var cmd: CommandAchat,
    var stockSaisieList: List<StockSaisieEntity>,
    var callback: (String) -> Unit,
) :
    BottomSheetDialogFragment(),
    StockViewListener {

    private lateinit var binding: FragmentItemListDialogListDialogBinding


    private val controller by lazy {
        StockSaisieAdapter(this)
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
        modalBottomSheetBehavior.isHideable = true
        modalBottomSheetBehavior.skipCollapsed = true
        setupRecyclerView()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                getStockList(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                getStockList(newText)
                return true
            }
        })
        AppUtils.hideKeyboardWithView(requireContext(), binding.searchView)
        getStockList()
    }

    private fun getStockList(query: String? = "") {
        if (query?.isEmpty() == true) {
            controller.differ.submitList(stockSaisieList)
        } else {
            val filteredList = stockSaisieList.filter { it.no?.contains(query ?: "", true) == true }
            controller.differ.submitList(filteredList)
        }
    }

    override fun onItemClicked(item: StockSaisieEntity) {
        AppUtils.hideKeyboard(requireActivity())
        callback.invoke(item.no)
        dismiss()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = controller
        binding.recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }
}
