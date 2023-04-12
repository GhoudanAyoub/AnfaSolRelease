package chari.groupewib.com.ui.command.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import chari.groupewib.com.app_models.Item
import chari.groupewib.com.common_ui.product.ItemViewListener
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.anfaSolution.com.databinding.FragmentStockBinding
import ghoudan.anfaSolution.com.networking.state.EpApiState

@AndroidEntryPoint
class StockFragment : Fragment(), ItemViewListener {

    private lateinit var binding: FragmentStockBinding
    private val viewModel: ItemViewModel by activityViewModels()

    private val itemsAdapter: StockAdapter by lazy {
        StockAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStockBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.getStock()

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

        viewModel.stockList.observe(viewLifecycleOwner) { customersState ->
            when (customersState) {
                is EpApiState.Loading -> {
                    binding.loaderProgress.visibility = View.VISIBLE
                }
                is EpApiState.Success -> {
                    binding.loaderProgress.visibility = View.GONE
                    customersState.data?.toMutableList()?.let { items ->
                        itemsAdapter.setList(items)
                        itemsAdapter.differ.submitList(items)
                    }
                }
                is EpApiState.Error -> {
                    binding.loaderProgress.visibility = View.GONE
                }
                else -> {}
            }
        }
    }

    override fun onItemClicked(item: Item) {
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = itemsAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    override fun onItemDeleted(item: Item) {

    }
}
