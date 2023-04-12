package ghoudan.anfaSolution.com.ui.clients.suppliers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import chari.groupewib.com.networking.request.CreateSupplierRequest
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.anfaSolution.com.MainActivity
import ghoudan.anfaSolution.com.R
import ghoudan.anfaSolution.com.app_models.SupplierAnfa
import ghoudan.anfaSolution.com.common_ui.dialog.CustomDialog
import ghoudan.anfaSolution.com.databinding.FragmentCreateSupplierBinding
import ghoudan.anfaSolution.com.networking.exception.HttpConflictException
import ghoudan.anfaSolution.com.networking.exception.HttpNotFoundException
import ghoudan.anfaSolution.com.networking.state.EpApiState
import ghoudan.anfaSolution.com.utils.AppUtils
import ghoudan.anfaSolution.com.utils.LocationHelper
import javax.inject.Inject


@AndroidEntryPoint
class CreateSupplierFragment : Fragment(R.layout.fragment_create_client) {

    private val args: CreateSupplierFragmentArgs by navArgs()
    private lateinit var binding: FragmentCreateSupplierBinding
    private val viewModel: SupplierListViewModel by viewModels()
    private lateinit var customErrorDialogView: View
    private var supplierAnfa: SupplierAnfa? = null

    @Inject
    lateinit var locationHelper: LocationHelper


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateSupplierBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.supplier != null) {
            val supplier = args.supplier
            viewModel.getSingleSuppliers(supplier?.code.toString())
            binding.nameClient.setText(supplier?.name)
            binding.EnseigneClient.setText(supplier?.code)
            binding.phoneClient.setText(supplier?.Phone_No)
            binding.cityClient.setText(supplier?.City)
            binding.cpClient.setText(supplier?.Post_Code)
            binding.secteurClient.setText(supplier?.region)
            binding.deleteClient.isVisible = true
        }
        binding.EnseigneClient.isVisible = args.supplier != null
        binding.deleteClient.apply {
            setOnClickListener {
                context?.let { _context ->
                    val customErrorDialogView = CustomDialog.inflateCancelableCustomDialogView(
                        _context,
                        getString(R.string.disconnect_aldert_title),
                        getString(R.string.deleteSupplier_alert_msg)
                    )
                    CustomDialog.showCustomCancelableAlertDialog(
                        _context,
                        customErrorDialogView
                    ) {
                        supplierAnfa?.let { it1 -> viewModel.deleteSupplier(it1) }
                    }
                }
            }
        }

        binding.confirmClientBtn.setOnClickListener {
            if (validateForm()) {
                val lastName = binding.nameClient.text.toString()
                val code = binding.EnseigneClient.text.toString()
                val phone = binding.phoneClient.text.toString()
                val city = binding.cityClient.text.toString()
                val cp = binding.cpClient.text.toString()
                val secteur = binding.secteurClient.text.toString()


                AppUtils.hideKeyboard(requireActivity())
                if (args.supplier == null) {
                    viewModel.addSupplier(
                        CreateSupplierRequest(
                            code,
                            lastName,
                            phone,
                            city,
                            cp,
                            secteur
                        )
                    )
                } else {
                    supplierAnfa?.let { it1 ->
                        viewModel.updateSupplier(
                            it1,
                            CreateSupplierRequest(
                                code,
                                lastName,
                                phone,
                                city,
                                cp,
                                secteur
                            )
                        )
                    }
                }
            }
        }

        subscribe()
    }


    private fun subscribe() {

        viewModel.Supplier.observe(viewLifecycleOwner) { registrationState ->
            when (registrationState) {
                is EpApiState.Loading -> {
                    (requireActivity() as? MainActivity)?.showLoader()
                }
                is EpApiState.Success -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    supplierAnfa = registrationState.data
                }
                is EpApiState.Error -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                }
                else -> {}
            }
        }
        viewModel.addSupplier.observe(viewLifecycleOwner) { registrationState ->
            when (registrationState) {
                is EpApiState.Loading -> {
                    (requireActivity() as? MainActivity)?.showLoader()
                }
                is EpApiState.Success -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    findNavController().navigateUp()
                }
                is EpApiState.Error -> {
                    when (registrationState.error) {
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
                               registrationState.error?.message?: getString(R.string.generic_error)
                            )
                            CustomDialog.showCustomAlertDialog(
                                requireContext(),
                                customErrorDialogView
                            )
                        }

                    }
                    (requireActivity() as? MainActivity)?.hideLoader()
                }
                else -> {}
            }
        }
        viewModel.deleteSupplier.observe(viewLifecycleOwner) { registrationState ->
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
                               registrationState.error?.message?: getString(R.string.generic_error)
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
        if (binding.phoneClient.text.isEmpty()) {
            binding.phoneClient.error = ""
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
