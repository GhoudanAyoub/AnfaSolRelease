package ghoudan.anfaSolution.com.ui.account

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.anfaSolution.com.BuildConfig
import ghoudan.anfaSolution.com.MainActivity
import ghoudan.anfaSolution.com.R
import ghoudan.anfaSolution.com.common_ui.appbar.AnfaAppBar
import ghoudan.anfaSolution.com.databinding.FragmentAccountBinding
import ghoudan.anfaSolution.com.networking.di.CurrentUserProvider
import ghoudan.anfaSolution.com.networking.di.CustomerProvider
import ghoudan.anfaSolution.com.utils.AnfaAppNavigator
import ghoudan.anfaSolution.com.utils.LocaleHelper
import ghoudan.anfaSolution.com.utils.PreferencesModule
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {
    private lateinit var binding: FragmentAccountBinding

    @Inject
    @Named(PreferencesModule.CURRENT_LANGUAGE_NAME)
    lateinit var currentLanguage: SharedPreferences

    @Inject
    lateinit var currentCustomerProvider: CustomerProvider
    @Inject
    lateinit var currentUserProvider: CurrentUserProvider

    @Inject
    lateinit var localeHelper: LocaleHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBar()

        binding.appV.text =
            getString(
                R.string.about_version,
                "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
            )

        //
        binding.AchatContainer.visibility = View.GONE
        binding.SalesContainer.visibility = View.GONE
        binding.shareBtn.visibility = View.GONE

        currentUserProvider.currentUser()?.let {
            binding.clientName.text = it.username
        }
        //sous menu on gris

        binding.closeMenu.setOnClickListener {
            val action = AccountFragmentDirections.actionAccountFragmentToLoginFragmentt()
            AnfaAppNavigator.navigate(findNavController(), action)
        }
        binding.achatBtn.setOnClickListener {
            //second click close

            binding.AchatContainer.visibility = View.VISIBLE
            binding.SalesContainer.visibility = View.GONE
            binding.shareBtn.visibility = View.GONE
        }
        binding.salesBtn.setOnClickListener {
            binding.AchatContainer.visibility = View.GONE
            binding.SalesContainer.visibility = View.VISIBLE
            binding.shareBtn.visibility = View.GONE
        }
        binding.stockBtn.setOnClickListener {
            binding.AchatContainer.visibility = View.GONE
            binding.SalesContainer.visibility = View.GONE
            binding.shareBtn.visibility = View.VISIBLE
        }
        binding.profileBtn.setOnClickListener {
            val action = AccountFragmentDirections.actionAccountFragmentToListClientFragment()
            AnfaAppNavigator.navigate(findNavController(), action)
        }

        binding.supportBtn.setOnClickListener {
            val action = AccountFragmentDirections.actionAccountFragmentToSupplierListFragment()
            AnfaAppNavigator.navigate(findNavController(), action)
        }

        binding.reclamationBtn.setOnClickListener {
            val action = AccountFragmentDirections.actionAccountFragmentToCommandFragment()
            AnfaAppNavigator.navigate(findNavController(), action)
        }

        binding.securityBtn.setOnClickListener {
            val action = AccountFragmentDirections.actionAccountFragmentToCommandDoneFragment()
            AnfaAppNavigator.navigate(findNavController(), action)
        }

        binding.cguBtn.setOnClickListener {

            val action = AccountFragmentDirections.actionAccountFragmentToItemFragment()
            AnfaAppNavigator.navigate(findNavController(), action)
        }

        binding.shareBtn.setOnClickListener {
            val action = AccountFragmentDirections.actionAccountFragmentToStockFragment()
            AnfaAppNavigator.navigate(findNavController(), action)
        }
    }

    private fun openReclamation() {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, "")
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.Complaints))
        emailIntent.putExtra(Intent.EXTRA_TEXT, "")
        startActivity(Intent.createChooser(emailIntent, getString(R.string.Complaints)))
    }

    private fun setupAppBar() {
        (requireActivity() as? MainActivity)?.setupViewToolbar(
            AnfaAppBar.AppBarViewConstraints(
                showProfileBtn = false,
                showLogo = false,
                showCallSupportBtn = false,
                showSearchBtnPlaceHolder = false
            ),
            searchNavDirection = null
        )
    }

    private fun changeImageDrawable(icBaselineKeyboardArrowLeft: Int) {
        binding.profileBtn.setCompoundDrawablesWithIntrinsicBounds(
            icBaselineKeyboardArrowLeft,
            0,
            R.drawable.ic_person_outlined,
            0
        )
        binding.supportBtn.setCompoundDrawablesWithIntrinsicBounds(
            icBaselineKeyboardArrowLeft,
            0,
            R.drawable.ic_headset_mic_support,
            0
        )
        binding.reclamationBtn.setCompoundDrawablesWithIntrinsicBounds(
            icBaselineKeyboardArrowLeft,
            0,
            R.drawable.ic_fact_check,
            0
        )
        binding.securityBtn.setCompoundDrawablesWithIntrinsicBounds(
            icBaselineKeyboardArrowLeft,
            0,
            R.drawable.ic_privacy_tip,
            0
        )
        binding.cguBtn.setCompoundDrawablesWithIntrinsicBounds(
            icBaselineKeyboardArrowLeft,
            0,
            R.drawable.ic_lock_person,
            0
        )
        binding.shareBtn.setCompoundDrawablesWithIntrinsicBounds(
            icBaselineKeyboardArrowLeft,
            0,
            R.drawable.ic_share,
            0
        )
    }

    private fun shareApp() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            getString(R.string.share_app_text)
        )
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }

    fun openWebPage(activity: Activity, url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        activity.startActivity(intent)
    }

    private fun openAccountInfo() {
        val action = AccountFragmentDirections.actionAccountFragmentToAccountInfosFragment()
        AnfaAppNavigator.navigate(findNavController(), action)
    }
}
