package chari.groupewib.com.ui.clients.suppliers

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.anfaSolution.com.MainActivity
import ghoudan.anfaSolution.com.R
import ghoudan.anfaSolution.com.app_models.CustomerAnfa
import ghoudan.anfaSolution.com.app_models.SupplierAnfa
import ghoudan.anfaSolution.com.common_ui.client.ClientViewListener
import ghoudan.anfaSolution.com.common_ui.itemDecoration.CartVerticalItemDecoration
import ghoudan.anfaSolution.com.databinding.FragmentListSupplierBinding
import ghoudan.anfaSolution.com.networking.state.EpApiState
import ghoudan.anfaSolution.com.ui.clients.suppliers.SupplierListViewModel
import ghoudan.anfaSolution.com.utils.AppUtils


@AndroidEntryPoint
class SupplierListBottomDialog(var callBack: (SupplierAnfa) -> Unit) :
    BottomSheetDialogFragment(),
    ClientViewListener {

    private lateinit var binding: FragmentListSupplierBinding
    private val viewModel: SupplierListViewModel by activityViewModels()
    private val commandAdapter: SuppliersAdapters by lazy {
        SuppliersAdapters(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListSupplierBinding.inflate(inflater, container, false)
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
        viewModel.getSuppliers()

        binding.clientsSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty() && query.length > 2) {
                    commandAdapter.filter.filter(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty() && newText.length >= 2) {
                    commandAdapter.filter.filter(newText)
                }
                return true
            }
        })

        viewModel.supplierList.observe(viewLifecycleOwner) { customersState ->
            when (customersState) {
                is EpApiState.Loading -> {
                    binding.loaderProgress.visibility = View.VISIBLE
                }
                is EpApiState.Success -> {
                    binding.loaderProgress.visibility = View.GONE
                    customersState.data?.toMutableList()?.let {
                        commandAdapter.setList(it.filter { it.vpg?.isNotEmpty() == true }
                            .toMutableList())
                    }
                    commandAdapter.differ.submitList(customersState.data?.filter { it.vpg?.isNotEmpty() == true }
                        ?.toMutableList())
                }
                is EpApiState.Error -> {
                    binding.loaderProgress.visibility = View.GONE
                }
                else -> {}
            }
        }
    }

    override fun onCustomerClicked(customer: CustomerAnfa) {
    }

    override fun onSupplierClicked(customer: SupplierAnfa) {
        AppUtils.hideKeyboardWithView(requireContext())
        callBack.invoke(customer)
        dismiss()
    }

    private fun setupRecyclerView() {
        binding.clientRecyclerView.apply {
            adapter = commandAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            val itemDecoration = CartVerticalItemDecoration(
                resources.getDimensionPixelSize(R.dimen.spacing_xxs)
            )
            if (binding.clientRecyclerView.itemDecorationCount == 0) {
                addItemDecoration(itemDecoration)
            }
        }
    }

}
