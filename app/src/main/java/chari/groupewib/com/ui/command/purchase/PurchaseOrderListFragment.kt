package chari.groupewib.com.ui.command.purchase

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import chari.groupewib.com.ui.command.sales.CommandListFragment
import chari.groupewib.com.ui.command.sales.CommandViewModel
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.anfaSolution.com.MainActivity
import ghoudan.anfaSolution.com.R
import ghoudan.anfaSolution.com.app_models.CommandAchat
import ghoudan.anfaSolution.com.app_models.PurchaseOrder
import ghoudan.anfaSolution.com.common_ui.itemDecoration.CartVerticalItemDecoration
import ghoudan.anfaSolution.com.common_ui.order.CommandListener
import ghoudan.anfaSolution.com.databinding.FragmentCommandDoneListBinding
import ghoudan.anfaSolution.com.networking.state.EpApiState
import ghoudan.anfaSolution.com.utils.AnfaAppNavigator

@AndroidEntryPoint
class PurchaseOrderListFragment : Fragment(), CommandListener {


    private var commands: List<PurchaseOrder>? = arrayListOf()
    private lateinit var fragmentBinding: FragmentCommandDoneListBinding
    private val fragmentViewModel by viewModels<CommandViewModel>()
    var cmdPosition: Int = 0

    companion object {
        @JvmStatic
        fun newInstance() = PurchaseOrderListFragment()
    }

    private val commandAdapter: CommandVenteAdapter by lazy {
        CommandVenteAdapter(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = FragmentCommandDoneListBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentBinding.llBtns.visibility = when (cmdPosition) {
            CommandListFragment.Page.TO_GO.ordinal -> {
                View.GONE
            }
            CommandListFragment.Page.TO_DELIVER.ordinal -> {
                View.VISIBLE
            }
            else -> {
                View.GONE
            }
        }
        (requireActivity() as? MainActivity)?.showLoader()

        fragmentBinding.cmdRecycler.apply {
            adapter = commandAdapter
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false
            )
            val itemDecoration = CartVerticalItemDecoration(
                resources.getDimensionPixelSize(R.dimen.spacing_xxs)
            )
            if (fragmentBinding.cmdRecycler.itemDecorationCount == 0) {
                addItemDecoration(itemDecoration)
            }
        }
        fragmentBinding.clientsSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                commandAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                commandAdapter.filter.filter(newText)
                return true
            }
        })
        subscribe()
    }

    override fun onResume() {
        super.onResume()
        fragmentViewModel.getPorchesCommands()
    }


    private fun watchTextOf(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                commandAdapter.filter.filter(charSequence.toString())
            }

            override fun afterTextChanged(editable: Editable) {
                commandAdapter.filter.filter(editable.toString())
            }
        }
    }

    private fun subscribe() {
        fragmentViewModel.achats.observe(viewLifecycleOwner) { result ->
            when (result) {
                is EpApiState.Success -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    result.data?.let { cmds ->
                        commands = cmds
                        commands?.sortedByDescending { it.counter }?.toMutableList()
                            ?.let { commandAdapter.setList(it) }
                        commands?.sortedByDescending { it.counter }?.toMutableList()
                            ?.let { commandAdapter.differ.submitList(it) }
                    }
                }
                is EpApiState.Error -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                }
                is EpApiState.Loading -> {
                    (requireActivity() as? MainActivity)?.showLoader()
                }
                else -> {}
            }
        }
    }

    override fun onCommandClicked(command: CommandAchat) {

    }


    override fun onDeleteClicked(command: CommandAchat) {
    }

    override fun onSalesCommandClicked(command: PurchaseOrder) {
        val action =
            PurchaseOrderFragmentDirections.actionCommandDoneFragmentToCreatePurchaseOrder(command)
        AnfaAppNavigator.navigate(findNavController(), action)
    }

    override fun onSalesDeleteClicked(command: PurchaseOrder) {
    }

    override fun getClientName(id: String, callback: (String, String) -> Unit) {
    }

    override fun getSupplierName(id: String, callback: (String, String) -> Unit) {
        fragmentViewModel.getSupplierByID(id)
        fragmentViewModel.supplier.observe(viewLifecycleOwner) { result ->
            when (result) {
                is EpApiState.Success -> {
                    callback.invoke(result.data?.name ?: "-", result.data?.Phone_No ?: "-")
                }
                is EpApiState.Error -> {
                }
                is EpApiState.Loading -> {
                }
                else -> {}
            }
        }
    }


}
