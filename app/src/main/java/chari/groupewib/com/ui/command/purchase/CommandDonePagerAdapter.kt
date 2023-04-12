package chari.groupewib.com.ui.command.purchase

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CommandDonePagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 1
    }

    override fun createFragment(position: Int): Fragment {
        return PurchaseOrderListFragment.newInstance().apply {
            cmdPosition = position
        }
    }

}
