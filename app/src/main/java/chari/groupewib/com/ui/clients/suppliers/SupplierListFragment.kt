package ghoudan.anfaSolution.com.ui.clients.suppliers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import chari.groupewib.com.ui.clients.suppliers.SuppliersAdapters
import ghoudan.anfaSolution.com.MainActivity
import ghoudan.anfaSolution.com.R
import ghoudan.anfaSolution.com.app_models.CustomerAnfa
import ghoudan.anfaSolution.com.common_ui.client.ClientViewListener
import ghoudan.anfaSolution.com.networking.state.EpApiState
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.anfaSolution.com.app_models.SupplierAnfa
import ghoudan.anfaSolution.com.common_ui.itemDecoration.CartVerticalItemDecoration
import ghoudan.anfaSolution.com.databinding.FragmentListSupplierBinding
import ghoudan.anfaSolution.com.utils.AnfaAppNavigator

@AndroidEntryPoint
class SupplierListFragment : Fragment(R.layout.fragment_list_supplier), ClientViewListener {

    private lateinit var binding: FragmentListSupplierBinding
    private val viewModel: SupplierListViewModel by viewModels()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? MainActivity)?.enableView(false)
        setupRecyclerView()
        viewModel.getSuppliers()
        binding.clientsSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                    commandAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                    commandAdapter.filter.filter(newText)
                return true
            }
        })

        binding.addContactFabTitle.setOnClickListener {
            createNewClient()
        }
        binding.addContactBtn.setOnClickListener {
            createNewClient()
        }
        binding.addContactContainer.setOnClickListener {
            createNewClient()
        }
        subcribe()
    }

    private fun createNewClient() {
        val action = SupplierListFragmentDirections.actionSupplierListFragmentToCreateSupplierFragment()
        AnfaAppNavigator.navigate(findNavController(), action)
    }

    private fun subcribe() {
        viewModel.supplierList.observe(viewLifecycleOwner) { customersState ->
            when (customersState) {
                is EpApiState.Loading -> {
                    binding.loaderProgress.visibility = View.VISIBLE
                }
                is EpApiState.Success -> {
                    binding.loaderProgress.visibility = View.GONE
                    customersState.data?.toMutableList()?.let { commandAdapter.setList(it) }
                    commandAdapter.differ.submitList(customersState.data)
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
        val action = SupplierListFragmentDirections.actionSupplierListFragmentToCreateSupplierFragment(customer)
        AnfaAppNavigator.navigate(findNavController(), action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as? MainActivity)?.enableView(true)
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
            if ( binding.clientRecyclerView.itemDecorationCount == 0) {
                addItemDecoration(itemDecoration)
            }
            addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy <= 0) {
                        expandAddContactBtn()
                    } else {
                        collapseAddContactBtn()
                    }
                }
            })
        }
    }

    fun expandAddContactBtn() {
        binding.addContactFabTitle.visibility = View.VISIBLE
    }

    fun collapseAddContactBtn() {
        binding.addContactFabTitle.visibility = View.GONE
    }
}
