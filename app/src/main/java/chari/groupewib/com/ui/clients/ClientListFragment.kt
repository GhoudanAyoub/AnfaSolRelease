package ghoudan.anfaSolution.com.ui.clients

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
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.anfaSolution.com.MainActivity
import ghoudan.anfaSolution.com.R
import ghoudan.anfaSolution.com.app_models.CustomerAnfa
import ghoudan.anfaSolution.com.app_models.SupplierAnfa
import ghoudan.anfaSolution.com.common_ui.client.ClientViewListener
import ghoudan.anfaSolution.com.databinding.FragmentListClientBinding
import ghoudan.anfaSolution.com.networking.state.EpApiState
import ghoudan.anfaSolution.com.utils.AnfaAppNavigator

@AndroidEntryPoint
class ClientListFragment : Fragment(R.layout.fragment_list_client), ClientViewListener {

    private lateinit var binding: FragmentListClientBinding
    private val viewModel: ClientListViewModel by viewModels()
    private val controller by lazy {
        ClientListController(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListClientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? MainActivity)?.enableView(false)
        setupRecyclerView()
        viewModel.getClients()
        binding.clientsSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.fetchClients(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.fetchClients(newText)
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
                    controller.setData(customersState.data)
                }
                is EpApiState.Error -> {
                    binding.loaderProgress.visibility = View.GONE
                }
                else -> {}
            }
        }

        binding.addContactFabTitle.setOnClickListener {
            createNewClient()
        }
        binding.addContactBtn.setOnClickListener {
            createNewClient()
        }
        binding.addContactContainer.setOnClickListener {
            createNewClient()
        }
    }

    fun createNewClient(){
        val action = ClientListFragmentDirections.actionListClientFragmentToCreateClientFragment()
        AnfaAppNavigator.navigate(findNavController(), action)
    }

    override fun onCustomerClicked(customer: CustomerAnfa) {
        val action = ClientListFragmentDirections.actionListClientFragmentToCreateClientFragment(customer)
        AnfaAppNavigator.navigate(findNavController(), action)
    }

    override fun onSupplierClicked(customer: SupplierAnfa) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as? MainActivity)?.enableView(true)
    }

    private fun setupRecyclerView() {
        binding.clientRecyclerView.apply {
            adapter = controller.adapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
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
