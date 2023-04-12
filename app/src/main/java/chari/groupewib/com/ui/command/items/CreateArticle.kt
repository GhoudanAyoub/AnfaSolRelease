package chari.groupewib.com.ui.command.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import chari.groupewib.com.app_models.SingleItemResponse
import chari.groupewib.com.networking.request.CreateItemRequest
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.anfaSolution.com.MainActivity
import ghoudan.anfaSolution.com.R
import ghoudan.anfaSolution.com.common_ui.dialog.CustomDialog
import ghoudan.anfaSolution.com.databinding.FragmentCreateArticleBinding
import ghoudan.anfaSolution.com.networking.state.EpApiState
import ghoudan.anfaSolution.com.utils.AppUtils
import ghoudan.anfaSolution.com.utils.LocationHelper
import javax.inject.Inject
import timber.log.Timber

@AndroidEntryPoint
class CreateArticle : Fragment(R.layout.fragment_create_article) {

    private var selectedType: String = ""
    private var selectedBaseUnit: String = ""
    private var selectedfamily: String = ""
    private val args: CreateArticleArgs by navArgs()
    private lateinit var binding: FragmentCreateArticleBinding
    private val viewModel: ItemViewModel by viewModels()
    private lateinit var customErrorDialogView: View
    private var item: SingleItemResponse? = null

    @Inject
    lateinit var locationHelper: LocationHelper

    val Type = arrayListOf("Inventory", "Service", "Non-Inventory")
    val baseUnit = arrayListOf(
        "AUCUN",
        "COLIS",
        "KG",
        "KGR",
        "L",
        "PAN",
        "PCS",
        "PIECE",
        "PIECES",
        "SACHET",
        "SERVICE",
        "TO",
        "UNITE"
    )

    val family = arrayListOf(
        "CONGELE",
        "COQUILLAGES",
        "CRUSTACES",
        "DIVERS",
        "ELEVAGE",
        "EXOTIQUES",
        "FILET FRAIS",
        "GROS POISSONS",
        "HUITRES",
        "MOLLUSQUE",
        "MOLLUSQUES",
        "SALAISON",
        "SAUMON",
        "POISSON ELEVAGE",
        "PRODUITS NOBLES"
    )


    private val familyAdapter by lazy {
        ArrayAdapter(
            requireContext(),
            R.layout.item_text_simple,
            family
        )
    }
    private val typeAdapter by lazy {
        ArrayAdapter(
            requireContext(),
            R.layout.item_text_simple,
            Type
        )
    }
    private val baseUnitAdapter by lazy {
        ArrayAdapter(
            requireContext(),
            R.layout.item_text_simple,
            baseUnit
        )
    }

    private var typeItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (Type.isNotEmpty()) {
                selectedType = Type[position]
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
    private var familyItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (family.isNotEmpty()) {
                selectedfamily = family[position]
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
    private var baseUnitSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (baseUnit.isNotEmpty()) {
                selectedBaseUnit = baseUnit[position]
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateArticleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.Type.adapter = typeAdapter
        binding.Type.onItemSelectedListener = typeItemSelectedListener
        binding.baseUnit.adapter = baseUnitAdapter
        binding.baseUnit.onItemSelectedListener = baseUnitSelectedListener
        binding.family.adapter = familyAdapter
        binding.family.onItemSelectedListener = familyItemSelectedListener

        if (args.article != null) {
            val article = args.article
            viewModel.getSingleItem(article?.code.toString())
            binding.description.setText(article?.description)
            binding.categArticle.setText(article?.Item_Category_Code)
            binding.Type.setSelection(typeAdapter.getPosition(article?.Type))
            binding.baseUnit.setSelection(baseUnitAdapter.getPosition(article?.unitCode))
            binding.family.setSelection(familyAdapter.getPosition(article?.Family))
            binding.blockedCheckBox.isChecked = article?.Blocked == true
        }
        binding.deleteArticle.apply {
//            isVisible = args.article != null
            setOnClickListener {
                item?.let { it1 -> viewModel.deleteItem(it1) }
            }
        }

        binding.confirmClientBtn.setOnClickListener {
            if (validateForm()) {
                val description = binding.description.text.toString()
                val categArticle = binding.categArticle.text.toString()
                AppUtils.hideKeyboard(requireActivity())
                if (args.article == null) {
                    viewModel.addItem(
                        CreateItemRequest(
                            description,
                            selectedBaseUnit,
                            categArticle,
                            selectedType,
                            selectedfamily,
                            binding.blockedCheckBox.isChecked
                        )
                    )
                } else {
                    item?.let { item ->
                        item.code?.let { code ->
                            viewModel.updateItem(
                                item,
                                CreateItemRequest(
                                    description,
                                    selectedBaseUnit,
                                    categArticle,
                                    selectedType,
                                    selectedfamily,
                                    binding.blockedCheckBox.isChecked,
                                    code
                                )
                            )
                        }
                    }
                }
            }
        }

        subscribe()
    }


    private fun subscribe() {
        viewModel.Item.observe(viewLifecycleOwner) { registrationState ->
            when (registrationState) {
                is EpApiState.Loading -> {
                    (requireActivity() as? MainActivity)?.showLoader()
                }
                is EpApiState.Success -> {
                    item = registrationState.data
                    (requireActivity() as? MainActivity)?.hideLoader()
                }
                is EpApiState.Error -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                }
                else -> {}
            }
        }
        viewModel.addItem.observe(viewLifecycleOwner) { registrationState ->
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
//        viewModel.deleteCustomer.observe(viewLifecycleOwner) { registrationState ->
//            when (registrationState) {
//                is EpApiState.Loading -> {
//                    (requireActivity() as? MainActivity)?.showLoader()
//                }
//                is EpApiState.Success -> {
//                    (requireActivity() as? MainActivity)?.hideLoader()
//                    findNavController().navigateUp()
//                }
//                is EpApiState.Error -> {
//                    (requireActivity() as? MainActivity)?.hideLoader()
//                    when (registrationState.error) {
//                        is HttpNotFoundException -> {
//                            findNavController().navigateUp()
//                        }
//                        is HttpConflictException -> {
//                            customErrorDialogView = CustomDialog.inflateCustomDialogView(
//                                requireContext(),
//                                getString(R.string.dialog_error_title),
//                                getString(R.string.conflict_name_error)
//                            )
//                            CustomDialog.showCustomAlertDialog(
//                                requireContext(),
//                                customErrorDialogView
//                            )
//                        }
//                        else -> {
//                            customErrorDialogView = CustomDialog.inflateCustomDialogView(
//                                requireContext(),
//                                getString(R.string.dialog_error_title),
//                                registrationState.error?.message
//                                    ?: getString(R.string.generic_error)
//                            )
//                            CustomDialog.showCustomAlertDialog(
//                                requireContext(),
//                                customErrorDialogView
//                            )
//                        }
//
//                    }
//                }
//                else -> {
//                    findNavController().navigateUp()
//                    (requireActivity() as? MainActivity)?.hideLoader()
//                }
//            }
//        }
    }

    private fun validateForm(): Boolean {
        var formValidation = true
        if (binding.description.text.isEmpty()) {
            binding.description.error = ""
            formValidation = false
        }
        if (binding.categArticle.text.isEmpty()) {
            binding.categArticle.error = ""
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
