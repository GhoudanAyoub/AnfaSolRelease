package chari.groupewib.com.ui.command.items

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import chari.groupewib.com.app_models.Item
import chari.groupewib.com.common_ui.product.ItemViewListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.anfaSolution.com.MainActivity
import ghoudan.anfaSolution.com.R
import ghoudan.anfaSolution.com.databinding.FragmentItemListDialogListDialogBinding
import ghoudan.anfaSolution.com.networking.offline.Resource
import ghoudan.anfaSolution.com.ui.orders.detailProducts.OrderDetailProductsController
import ghoudan.anfaSolution.com.utils.AppUtils
import ghoudan.anfaSolution.com.utils.PreferencesModule.SELECTED_CUSTOMER_PREFERENCES_NAME
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ItemListDialogFragment(var callBack: (Item) -> Unit) :
    BottomSheetDialogFragment(),
    ItemViewListener {

    private lateinit var binding: FragmentItemListDialogListDialogBinding
    private val viewModel: ItemViewModel by activityViewModels()
    private val controller by lazy {
        OrderDetailProductsController(this)
    }

    @Named(SELECTED_CUSTOMER_PREFERENCES_NAME)
    @Inject
    lateinit var selectedCustomerPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
        viewModel.getAllItems()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.fetchItems(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.fetchItems(newText)
                return true
            }
        })

        viewModel.itemsList.observe(viewLifecycleOwner) { customersState ->
            when (customersState) {
                is Resource.Loading -> {
                    binding.loaderProgress.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.loaderProgress.visibility = View.GONE
                    controller.setData(customersState.data?.filter { it.Blocked==false })
                }
                is Resource.Error -> {
                    binding.loaderProgress.visibility = View.GONE
                }
                else -> {}
            }
        }
    }

    override fun onItemClicked(item: Item) {
        AppUtils.hideKeyboardWithView(requireContext())
        callBack.invoke(item)
        dismiss()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = controller.adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    override fun onItemDeleted(item: Item) {

    }
}
