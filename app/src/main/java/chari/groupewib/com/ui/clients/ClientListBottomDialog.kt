package chari.groupewib.com.ui.clients

import android.app.Dialog
import android.content.SharedPreferences
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
import ghoudan.anfaSolution.com.databinding.FragmentListClientBinding
import ghoudan.anfaSolution.com.networking.state.EpApiState
import ghoudan.anfaSolution.com.ui.clients.ClientListController
import ghoudan.anfaSolution.com.ui.clients.ClientListViewModel
import ghoudan.anfaSolution.com.utils.AppUtils
import ghoudan.anfaSolution.com.utils.PreferencesModule.SELECTED_CUSTOMER_PREFERENCES_NAME
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ClientListBottomDialog(var callBack: (CustomerAnfa) -> Unit) :
    BottomSheetDialogFragment(),
    ClientViewListener {

    private lateinit var binding: FragmentListClientBinding
    private val viewModel: ClientListViewModel by activityViewModels()
    private val controller by lazy {
        ClientListController(this)
    }

    @Named(SELECTED_CUSTOMER_PREFERENCES_NAME)
    @Inject
    lateinit var selectedCustomerPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListClientBinding.inflate(inflater, container, false)
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
        viewModel.getClients()

        binding.clientsSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty() && query.length > 2) {
                    viewModel.fetchClients(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty() && newText.length >= 2) {
                    viewModel.fetchClients(newText)
                }
                return true
            }
        })

        viewModel.customersList.observe(viewLifecycleOwner) { customersState ->
            when (customersState) {
                is EpApiState.Loading -> {
                    binding.loaderProgress.visibility = View.VISIBLE
                }
                is EpApiState.Success -> {
                    binding.loaderProgress.visibility = View.GONE
                    controller.setData(customersState.data?.sortedBy { it.Blocked?.isEmpty() })
                }
                is EpApiState.Error -> {
                    binding.loaderProgress.visibility = View.GONE
                }
                else -> {}
            }
        }
    }

    override fun onCustomerClicked(customer: CustomerAnfa) {
        AppUtils.hideKeyboardWithView(requireContext())
        if (customer.Blocked?.contains("All") == false) {
            callBack.invoke(customer)
            dismiss()
        }
    }

    override fun onSupplierClicked(customer: SupplierAnfa) {
    }

    private fun setupRecyclerView() {
        binding.clientRecyclerView.adapter = controller.adapter
        binding.clientRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }

}
