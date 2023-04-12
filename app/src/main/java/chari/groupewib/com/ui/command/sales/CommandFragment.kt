package chari.groupewib.com.ui.command.sales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ghoudan.anfaSolution.com.MainActivity
import ghoudan.anfaSolution.com.common_ui.appbar.AnfaAppBar
import ghoudan.anfaSolution.com.common_ui.databinding.ItemFamilyTabBinding
import ghoudan.anfaSolution.com.databinding.FragmentCommandBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.anfaSolution.com.utils.AnfaAppNavigator

@AndroidEntryPoint
class CommandFragment : Fragment() {

    private lateinit var fragmentBinding: FragmentCommandBinding
    private val commandViewModel by activityViewModels<CommandViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = FragmentCommandBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBar()
        setupViewPager()
        fragmentBinding.addContactFabTitle.setOnClickListener {
            openNewOrder()
        }
        fragmentBinding.addContactBtn.setOnClickListener {
            openNewOrder()
        }
        fragmentBinding.addContactContainer.setOnClickListener {
            openNewOrder()
        }
    }

    private fun openNewOrder() {
        val action = CommandFragmentDirections.actionCommandFragmentToCreateSalesOrder()
        AnfaAppNavigator.navigate(findNavController(), action)
    }

    private fun setupViewPager() {
        val brandStatusesTabs = fragmentBinding.fragmentTabs
        val brandViewPager = fragmentBinding.fragmentPager
        //create new list with two strings
        val tabTitles = listOf("Commandes")
        val pagerAdapter = CommandPagerAdapter(
            this
        )
        fragmentBinding.fragmentPager.adapter = pagerAdapter
        TabLayoutMediator(
            brandStatusesTabs,
            brandViewPager
        ) { tab, position ->
            val tabCustomView =
                ItemFamilyTabBinding.inflate(LayoutInflater.from(requireContext()))
            val code = tabTitles[position]

            tabCustomView.tabTxt.apply {
                setCompoundDrawables(null, null, null, null)
                text = code
            }
            tab.customView = tabCustomView.root
        }.attach()
    }

    private fun setupAppBar() {
        (requireActivity() as? MainActivity)?.setupViewToolbar(
            AnfaAppBar.AppBarViewConstraints(
                showUnderLine = true,
                showSearchBtnPlaceHolder = false,
                showProfileBtn = false,
                showCallSupportBtn = false,
                showLogo = false
            ),
            searchNavDirection = null,
            profileNavDirection = CommandFragmentDirections.actionCommandFragmentToAccountFragment()
        )
    }

    fun expandAddContactBtn() {
        fragmentBinding.addContactFabTitle.visibility = View.VISIBLE
    }

    fun collapseAddContactBtn() {
        fragmentBinding.addContactFabTitle.visibility = View.GONE
    }

}
