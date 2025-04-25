package ghoudan.anfaSolution.com.ui.login

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.anfaSolution.com.MainActivity
import ghoudan.anfaSolution.com.R
import ghoudan.anfaSolution.com.common_ui.appbar.AnfaAppBar
import ghoudan.anfaSolution.com.common_ui.inputFields.ChariInputField
import ghoudan.anfaSolution.com.common_ui.inputFields.AnfaTextField
import ghoudan.anfaSolution.com.databinding.FragmentLoginBinding
import ghoudan.anfaSolution.com.networking.offline.Resource
import ghoudan.anfaSolution.com.utils.AnfaAppNavigator
import ghoudan.anfaSolution.com.utils.Constants
import ghoudan.anfaSolution.com.utils.LocaleHelper
import ghoudan.anfaSolution.com.utils.PreferencesModule
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import javax.inject.Inject
import javax.inject.Named
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginFragmentViewModel>()
    private lateinit var customErrorDialogView: View
    private var phoneInput = ""

    @Inject
    @Named(PreferencesModule.CURRENT_LANGUAGE_NAME)
    lateinit var currentLanguage: SharedPreferences

    @Inject
    @Named(PreferencesModule.CURRENT_USER_PREFERENCES_NAME)
    lateinit var currentUserPreferences: SharedPreferences

    @Inject
    lateinit var localeHelper: LocaleHelper

    private val phoneNumberUtil: PhoneNumberUtil by lazy {
        PhoneNumberUtil.createInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? MainActivity)?.setupViewToolbar(
            AnfaAppBar.AppBarViewConstraints(
                showUnderLine = true,
                showSearchBtnPlaceHolder = false,
                showProfileBtn = false,
                showCallSupportBtn = false,
                showLogo = false
            ),
            searchNavDirection = null,
            profileNavDirection = null
        )

        subscribe()

        binding.phoneVal.apply {
            setHint(R.string.type_your_first_name)
            setLeftIcon(R.drawable.ic_person)
            setMaxLines(1)
            setInputType(EditorInfo.TYPE_CLASS_TEXT)
            post {
                askFocus()
                setState(ChariInputField.State.HOVER)
            }
            setImOption(EditorInfo.IME_ACTION_NEXT)
            addOnTextChangedListener(watchTextOf(this))
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    binding.phoneVal.setState(ChariInputField.State.FULL)
                    binding.passowrd.apply {
                        askFocus()
                        setState(ChariInputField.State.HOVER)
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }

        binding.passowrd.apply {
            setHint(R.string.type_your_pwd)
            setLeftIcon(R.drawable.ic_lock_person)
            setMaxLines(1)
            setImOption(EditorInfo.IME_ACTION_DONE)
            addOnTextChangedListener(watchTextOf(this))
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    setState(ChariInputField.State.FULL)
                    binding.passowrd.setState(ChariInputField.State.FULL)
                    (requireActivity() as? MainActivity)?.hideSoftKeyboard()
                    binding.btnConnect.isEnabled = true
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }
        binding.btnConnect.setOnClickListener {
            if (binding.phoneVal.getText().isEmpty()) {
                binding.phoneVal.setState(ChariInputField.State.ERROR)
                binding.btnConnect.isEnabled = false
            } else if (binding.passowrd.getText().isEmpty()) {
                binding.passowrd.setState(ChariInputField.State.ERROR)
                binding.btnConnect.isEnabled = false
            } else {
                viewModel.getUserInfo()
            }
        }

    }

    private fun subscribe() {
        viewModel.userInfo.observe(viewLifecycleOwner) { userCredentialsState ->
            (requireActivity() as? MainActivity)?.hideSoftKeyboard()
            when (userCredentialsState) {
                is Resource.Loading -> {
                    (requireActivity() as? MainActivity)?.showLoader()
                }
                is Resource.Success -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    val user = userCredentialsState.data?.filter {
                        it.username.equals(binding.phoneVal.getText()) && it.password.equals(binding.passowrd.getText())
                    }
                    if (!user.isNullOrEmpty()) {
                        val currentUserJson = Json.encodeToString(user.first())
                        currentUserPreferences.edit()
                            .putString(Constants.CURRENT_USER_KEY, currentUserJson).apply()
                        AnfaAppNavigator.navigate(findNavController(), R.id.AccountFragment)
                    }else{
                        binding.phoneVal.setState(ChariInputField.State.ERROR)
                        binding.passowrd.setState(ChariInputField.State.ERROR)
                        binding.btnConnect.isEnabled = false
                    }
                }
                is Resource.Error -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                }
                else -> {
                }
            }
        }
    }

    private fun watchTextOf(inputField: ChariInputField): TextWatcher {
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
                when (inputField.id) {
                    R.id.phone_val -> {
                        binding.phoneVal.apply {
                            setState(ChariInputField.State.HOVER)
                            setRightIcon(
                                R.drawable.ic_close,
                                AnfaTextField.RightIconAction.CLEAR_TEXT
                            )
                        }
                    }
                    R.id.passowrd -> {
                        binding.passowrd.apply {
                            setState(ChariInputField.State.HOVER)
                            setRightIcon(
                                R.drawable.ic_close,
                                AnfaTextField.RightIconAction.CLEAR_TEXT
                            )
                        }
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {
                when (inputField.id) {
                    R.id.phone_val -> {
                        if (editable.isEmpty()) binding.phoneVal.hideRightIcon()
                        binding.phoneVal.setState(ChariInputField.State.HOVER)
                    }
                    R.id.passowrd -> {
                        if (editable.isEmpty()) binding.passowrd.hideRightIcon()
                        binding.btnConnect.isEnabled = editable.length >= 4
                        binding.passowrd.setState(ChariInputField.State.HOVER)
                    }
                }
            }
        }
    }

}
