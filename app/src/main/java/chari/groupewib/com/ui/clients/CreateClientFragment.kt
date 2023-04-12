package ghoudan.anfaSolution.com.ui.clients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import chari.groupewib.com.networking.request.CreateClientRequest
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.anfaSolution.com.MainActivity
import ghoudan.anfaSolution.com.R
import ghoudan.anfaSolution.com.app_models.CustomerAnfa
import ghoudan.anfaSolution.com.common_ui.dialog.CustomDialog
import ghoudan.anfaSolution.com.databinding.FragmentCreateClientBinding
import ghoudan.anfaSolution.com.networking.exception.HttpConflictException
import ghoudan.anfaSolution.com.networking.exception.HttpNotFoundException
import ghoudan.anfaSolution.com.networking.state.EpApiState
import ghoudan.anfaSolution.com.utils.AppUtils
import ghoudan.anfaSolution.com.utils.LocationHelper
import javax.inject.Inject
import timber.log.Timber


@AndroidEntryPoint
class CreateClientFragment : Fragment(R.layout.fragment_create_client) {

    private var selectedBlockedType: String = ""
    private val args: CreateClientFragmentArgs by navArgs()
    private lateinit var binding: FragmentCreateClientBinding
    private val viewModel: ClientListViewModel by viewModels()
    private lateinit var customErrorDialogView: View
    private var customerAnfa: CustomerAnfa? = null

    @Inject
    lateinit var locationHelper: LocationHelper

    val blockedType = arrayListOf("", "Ship", "Invoice", "All")

    private val blockedAdapter by lazy {
        ArrayAdapter(
            requireContext(),
            R.layout.item_text_simple,
            blockedType
        )
    }

    private var blockedItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (blockedType.isNotEmpty()) {
                selectedBlockedType = blockedType[position]
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateClientBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.blockedType.adapter = blockedAdapter
        binding.blockedType.onItemSelectedListener = blockedItemSelectedListener
        if (args.customer != null) {
            val customer = args.customer
            viewModel.getSingleClients(customer?.code.toString())
            binding.nameClient.setText(customer?.name)
            binding.EnseigneClient.setText(customer?.description)
            binding.phoneClient.setText(customer?.Phone_No)
            binding.cityClient.setText(customer?.City)
            binding.cpClient.setText(customer?.Post_Code)
            binding.secteurClient.setText(customer?.Sector)
            binding.AddressClient.setText(customer?.Address)
            selectedBlockedType = customer?.Blocked.toString()
            val position = blockedAdapter.getPosition(selectedBlockedType)
            binding.blockedType.setSelection(position)
        }
        binding.EnseigneClient.isVisible = args.customer != null
        binding.deleteClient.apply {
            setOnClickListener {
                context?.let { _context ->
                    val customErrorDialogView = CustomDialog.inflateCancelableCustomDialogView(
                        _context,
                        getString(R.string.disconnect_aldert_title),
                        getString(R.string.deleteClient_alert_msg)
                    )
                    CustomDialog.showCustomCancelableAlertDialog(
                        _context,
                        customErrorDialogView
                    ) {
                        isVisible = args.customer != null
                        customerAnfa?.let { it1 -> viewModel.deleteClient(it1) }
                    }
                }
            }
        }

        binding.confirmClientBtn.setOnClickListener {
            if (validateForm()) {
                val lastName = binding.nameClient.text.toString()
                val firstName = binding.EnseigneClient.text.toString()
                val phone = binding.phoneClient.text.toString()
                val city = binding.cityClient.text.toString()
                val cp = binding.cpClient.text.toString()
                val secteur = binding.secteurClient.text.toString()
                val address = binding.AddressClient.text.toString()

                AppUtils.hideKeyboard(requireActivity())
                if (args.customer == null) {
                    viewModel.addClient(
                        CreateClientRequest(
                            lastName,
                            firstName,
                            phone,
                            city,
                            cp,
                            address,
                            secteur,
                            selectedBlockedType
                        )
                    )
                } else {
                    customerAnfa?.let { it1 ->
                        viewModel.updateClient(
                            it1,
                            CreateClientRequest(
                                lastName,
                                firstName,
                                phone,
                                city,
                                cp,
                                address,
                                secteur,
                                selectedBlockedType
                            )
                        )
                    }
                }
            }
        }

        subscribe()
    }


    private fun subscribe() {
        viewModel.Customer.observe(viewLifecycleOwner) { registrationState ->
            when (registrationState) {
                is EpApiState.Loading -> {
                    (requireActivity() as? MainActivity)?.showLoader()
                }
                is EpApiState.Success -> {
                    customerAnfa = registrationState.data
                    (requireActivity() as? MainActivity)?.hideLoader()
                }
                is EpApiState.Error -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                }
                else -> {}
            }
        }
        viewModel.addCustomer.observe(viewLifecycleOwner) { registrationState ->
            when (registrationState) {
                is EpApiState.Loading -> {
                    (requireActivity() as? MainActivity)?.showLoader()
                }
                is EpApiState.Success -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    findNavController().navigateUp()
                }
                is EpApiState.Error -> {
                    Timber.e("8855" + registrationState.error)
                    customErrorDialogView = CustomDialog.inflateCustomDialogView(
                        requireContext(),
                        getString(R.string.dialog_error_title),
                        registrationState.error?.message ?: getString(R.string.generic_error)
                    )
                    CustomDialog.showCustomAlertDialog(
                        requireContext(),
                        customErrorDialogView
                    )
                    (requireActivity() as? MainActivity)?.hideLoader()
                }
                else -> {}
            }
        }
        viewModel.deleteCustomer.observe(viewLifecycleOwner) { registrationState ->
            when (registrationState) {
                is EpApiState.Loading -> {
                    (requireActivity() as? MainActivity)?.showLoader()
                }
                is EpApiState.Success -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    findNavController().navigateUp()
                }
                is EpApiState.Error -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    when (registrationState.error) {
                        is HttpNotFoundException -> {
                            findNavController().navigateUp()
                        }
                        is HttpConflictException -> {
                            customErrorDialogView = CustomDialog.inflateCustomDialogView(
                                requireContext(),
                                getString(R.string.dialog_error_title),
                                getString(R.string.conflict_name_error)
                            )
                            CustomDialog.showCustomAlertDialog(
                                requireContext(),
                                customErrorDialogView
                            )
                        }
                        else -> {
                            customErrorDialogView = CustomDialog.inflateCustomDialogView(
                                requireContext(),
                                getString(R.string.dialog_error_title),
                                registrationState.error?.message
                                    ?: getString(R.string.generic_error)
                            )
                            CustomDialog.showCustomAlertDialog(
                                requireContext(),
                                customErrorDialogView
                            )
                        }

                    }
                }
                else -> {
                    findNavController().navigateUp()
                    (requireActivity() as? MainActivity)?.hideLoader()
                }
            }
        }
    }

    private fun validateForm(): Boolean {
        var formValidation = true
        if (binding.nameClient.text.isEmpty()) {
            binding.nameClient.error = ""
            formValidation = false
        }
        if (binding.cityClient.text.isEmpty()) {
            binding.cityClient.error = ""
            showErrorFormToast()
            formValidation = false
        }
        if (binding.cpClient.text.isEmpty()) {
            binding.cpClient.error = ""
            showErrorFormToast()
            formValidation = false
        }
        if (binding.secteurClient.text.isEmpty()) {
            binding.secteurClient.error = ""
            showErrorFormToast()
            formValidation = false
        }

        return formValidation
    }

    private fun showErrorFormToast() {
        Toast.makeText(
            requireContext(),
            resources.getString(R.string.required_fields),
            Toast.LENGTH_SHORT
        ).show()
    }
}
