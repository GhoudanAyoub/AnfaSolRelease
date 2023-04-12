package ghoudan.anfaSolution.com.ui.account

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import ghoudan.anfaSolution.com.MainActivity
import ghoudan.anfaSolution.com.R
import ghoudan.anfaSolution.com.app_models.Customer
import ghoudan.anfaSolution.com.common_ui.appbar.AnfaAppBar
import ghoudan.anfaSolution.com.common_ui.dialog.CustomDialog
import ghoudan.anfaSolution.com.databinding.FragmentAccountInfosBinding
import ghoudan.anfaSolution.com.networking.di.CustomerProvider
import ghoudan.anfaSolution.com.utils.PreferencesModule
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class AccountInfoFragment : Fragment(R.layout.fragment_account_infos) {

    private lateinit var binding: FragmentAccountInfosBinding
    private val viewModel: AccountInfoFragmentViewModel by viewModels()

    @Inject
    @Named(PreferencesModule.CURRENT_CREDENTIALS_PREFERENCES_NAME)
    lateinit var userDataPreferences: SharedPreferences

    @Inject
    @Named(PreferencesModule.CURRENT_USER_PREFERENCES_NAME)
    lateinit var currentUserPreferences: SharedPreferences

    @Inject
    @Named(PreferencesModule.SELECTED_CUSTOMER_PREFERENCES_NAME)
    lateinit var selectedCustomerPreferences: SharedPreferences

    @Inject
    lateinit var customerProvider: CustomerProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountInfosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInfo()
        setupAppBar()
        binding.disconnectBtn.setOnClickListener {

            context?.let { _context ->
                val customErrorDialogView = CustomDialog.inflateCancelableCustomDialogView(
                    _context,
                    getString(R.string.disconnect_aldert_title),
                    getString(R.string.disconnect_alert_msg)
                )
                CustomDialog.showCustomCancelableAlertDialog(
                    _context,
                    customErrorDialogView
                ) {
                    disconnect()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as? MainActivity)?.handleNoContent(false)
    }

    private fun getInfo() {
        val customerId = customerProvider.selectedCustomer()?.customerId
        customerId?.let { _customerId ->
            viewModel.getCustomer(_customerId).observe(viewLifecycleOwner) { customer ->
                customer?.let { _customer ->
                    bindCustomer(customer)
                }
            }
        }
    }

    private fun bindCustomer(customer: Customer) {
        binding.clientName.text =
            StringBuilder().append(customer.user?.username)
        binding.phoneClient.text = customer.phoneNumber
        binding.typeClient.text = getString(R.string.user_type)



    }

    private fun disconnect() {
        lifecycleScope.launchWhenCreated {
            userDataPreferences.edit().clear().apply()
            currentUserPreferences.edit().clear().apply()
            selectedCustomerPreferences.edit().clear().apply()
        }
    }

    private fun setupAppBar() {
        (requireActivity() as? MainActivity)?.setupViewToolbar(
            AnfaAppBar.AppBarViewConstraints(
                showProfileBtn = false,
                showLogo = false,
                showCallSupportBtn = true,
                showSearchBtnPlaceHolder = true
            ),
            searchNavDirection = null
        )
    }
}
