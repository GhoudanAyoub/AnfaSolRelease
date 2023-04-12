package ghoudan.anfaSolution.com.common_ui.appbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import ghoudan.anfaSolution.com.common_ui.databinding.AppbarViewBinding
import ghoudan.anfaSolution.com.common_ui.hideKeyboard
import ghoudan.anfaSolution.com.common_ui.isVisible
import ghoudan.anfaSolution.com.common_ui.showKeyboard

class AnfaAppBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), OnClickListener {

    private val binding = AppbarViewBinding.inflate(LayoutInflater.from(context), this)
    private var callback: ((AppBarAction) -> Unit)? = null
    private var searchCallback: ((String) -> Unit)? = null

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.searchBtnPlaceholder.id -> callback?.invoke(AppBarAction.SHOW_SEARCH)
            binding.profileBtn.id -> callback?.invoke(AppBarAction.SHOW_PROFILE)
            binding.callCenterBtn.id -> callback?.invoke(AppBarAction.SHOW_SUPPORT_POPUP)
            binding.appLogo.id -> callback?.invoke(AppBarAction.SHOW_DONE)
        }
    }

    fun getToolbar(): Toolbar {
        return binding.appToolbar
    }

    fun setupAppBar(
        constraints: AppBarViewConstraints,
        callback: (AppBarAction) -> Unit = {}
    ): AnfaAppBar {
        satisfyConstraints(constraints)
        this.callback = callback

        binding.toolbarSearchInput.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (binding.toolbarSearchInput.text?.isNotEmpty() == true) {
                        binding.toolbarSearchInput.hideKeyboard(this@AnfaAppBar.context)
                        searchCallback?.invoke(binding.toolbarSearchInput.text.toString())
                    }
                }
                return@setOnEditorActionListener false
            }
        }
        return this
    }

    private fun askFocus() {
        binding.toolbarSearchInput.apply {
            this.requestFocus()
            this.showKeyboard(context)
        }
    }

    private fun removeFocus() {
        binding.toolbarSearchInput.apply {
            this.clearFocus()
            this.hideKeyboard(context)
        }
    }

    private fun satisfyConstraints(
        constraints: AppBarViewConstraints,
    ) {
        binding.navBarSeparator.isVisible(constraints.showUnderLine)
        binding.profileBtn.isVisible(constraints.showProfileBtn)
        binding.callCenterBtn.isVisible(constraints.showCallSupportBtn)
        binding.searchBtnPlaceholder.isVisible(constraints.showSearchBtnPlaceHolder)
        binding.appLogo.isVisible(constraints.showLogo)
        showAppBarTitle(constraints.appBarTitle, constraints.showAppBarTitle)
        if (constraints.isInSearchMode) {
            forceSearchMode()
        } else {
            binding.toolbarSearchInput.apply {
                isVisible(false)
                text?.clear()
                removeFocus()
            }
        }
        binding.profileBtn.setOnClickListener(this)
        binding.searchBtnPlaceholder.setOnClickListener(this)
        binding.callCenterBtn.setOnClickListener(this)
    }

    fun setQueryListener(onUserSubmitQuery: (String) -> Unit) {
        searchCallback = onUserSubmitQuery
    }

    private fun forceSearchMode() {
        binding.toolbarSearchInput.apply {
            isVisible(true)
            askFocus()
        }
        binding.searchBtnPlaceholder.isVisible(false)
        binding.profileBtn.isVisible(false)
        binding.callCenterBtn.isVisible(false)
        binding.appLogo.isVisible(false)
    }

    fun showAppBarTitle(title: String, isVisible: Boolean) {
        binding.appToolbarTitle.apply {
            text = title
            isVisible(isVisible)
        }
    }

    enum class AppBarAction {
        SHOW_PROFILE,
        SHOW_SUPPORT_POPUP,
        SHOW_SEARCH,
        SHOW_DONE
    }

    data class AppBarViewConstraints(
        var showUnderLine: Boolean = false,
        var showProfileBtn: Boolean = true,
        var showCallSupportBtn: Boolean = true,
        var showLogo: Boolean = true,
        var showSearchBtnPlaceHolder: Boolean = false,
        var isInSearchMode: Boolean = false,
        var showAppBarTitle: Boolean = false,
        var appBarTitle: String = ""
    )
}
