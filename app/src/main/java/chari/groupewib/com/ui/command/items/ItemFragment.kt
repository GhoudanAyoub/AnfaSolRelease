package chari.groupewib.com.ui.command.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import chari.groupewib.com.app_models.Item
import chari.groupewib.com.common_ui.product.ItemViewListener
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.anfaSolution.com.databinding.FragmentItemBinding
import ghoudan.anfaSolution.com.networking.offline.Resource
import ghoudan.anfaSolution.com.utils.AnfaAppNavigator

@AndroidEntryPoint
class ItemFragment : Fragment(), ItemViewListener {

    private lateinit var binding: FragmentItemBinding
    private val viewModel: ItemViewModel by activityViewModels()

    private val itemsAdapter: ItemsAdapter by lazy {
        ItemsAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.getAllItems()

        binding.addContactFabTitle.setOnClickListener {
            openNewOrder()
        }
        binding.addContactBtn.setOnClickListener {
            openNewOrder()
        }
        binding.addContactContainer.setOnClickListener {
            openNewOrder()
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                    itemsAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                    itemsAdapter.filter.filter(newText)
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
                    customersState.data?.toMutableList()?.let { items ->
                        items.map { it.hideUnits = true }
                        itemsAdapter.setList(items)
                        itemsAdapter.differ.submitList(items)
                    }
                }
                is Resource.Error -> {
                    binding.loaderProgress.visibility = View.GONE
                }
                else -> {}
            }
        }
    }

    private fun openNewOrder() {
        val action = ItemFragmentDirections.actionItemFragmentToCreateArticle()
        AnfaAppNavigator.navigate(findNavController(), action)
    }

    override fun onItemClicked(item: Item) {
        val action = ItemFragmentDirections.actionItemFragmentToCreateArticle(item)
        AnfaAppNavigator.navigate(findNavController(), action)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = itemsAdapter
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
    override fun onItemDeleted(item: Item) {

    }
}
