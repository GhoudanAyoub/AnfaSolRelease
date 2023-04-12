package chari.groupewib.com.ui.command.purchase

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import chari.groupewib.com.app_models.Item
import chari.groupewib.com.common_ui.product.ItemViewListener
import chari.groupewib.com.networking.request.PurchaseOrderHeaderResult
import chari.groupewib.com.networking.request.UpdatePurchaseHeaderRequest
import chari.groupewib.com.ui.clients.suppliers.SupplierListBottomDialog
import chari.groupewib.com.ui.command.bottomDialogs.UpdateItemDialog
import chari.groupewib.com.ui.command.items.ItemListDialogFragment
import chari.groupewib.com.ui.command.items.ItemsAdapter
import chari.groupewib.com.ui.command.sales.CommandViewModel
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.anfaSolution.com.MainActivity
import ghoudan.anfaSolution.com.R
import ghoudan.anfaSolution.com.common_ui.appbar.AnfaAppBar
import ghoudan.anfaSolution.com.common_ui.dialog.CustomDialog
import ghoudan.anfaSolution.com.databinding.FragmentCreatePurchaseOrderBinding
import ghoudan.anfaSolution.com.networking.exception.HttpConflictException
import ghoudan.anfaSolution.com.networking.state.EpApiState
import ghoudan.anfaSolution.com.ui.clients.ClientListViewModel
import ghoudan.anfaSolution.com.utils.AppUtils
import ghoudan.anfaSolution.com.utils.LocaleHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import timber.log.Timber

@AndroidEntryPoint
class CreatePurchaseOrder : Fragment(), ItemViewListener {

    private lateinit var customErrorDialogView: View
    private val args: CreatePurchaseOrderArgs by navArgs()
    private var purchaseHeader: PurchaseOrderHeaderResult? = null
    private var demandeDate: String = ""
    private var requestedRecievedDate: String = ""
    private var ExpectedRecievedDate: String = ""
    private var promisReceivedDate: String = ""
    lateinit var binding: FragmentCreatePurchaseOrderBinding
    private val fragmentViewModel by viewModels<CommandViewModel>()
    private val viewModel: ClientListViewModel by activityViewModels()
    private val itemsAdapter: ItemsAdapter by lazy {
        ItemsAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatePurchaseOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.itemsContent.apply {
            isVisible = args.cmd != null
        }
        val currentDeliveryDate = Calendar.getInstance(Locale.FRENCH)
        val currentDay =
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.FRENCH
            ).format(currentDeliveryDate.time)
        if (currentDay.isNotEmpty()) {
            //show current date as default
            binding.demandeDate.setText(currentDay)

        }
        binding.demandeDate.setOnClickListener {
            openDatePickerDialog {
                binding.demandeDate.setText(it)
                demandeDate = it
            }
        }
        binding.requestedRecievedDate.setOnClickListener {
            openDatePickerDialog {
                binding.requestedRecievedDate.setText(it)
                requestedRecievedDate = it
            }
        }
        binding.ExpectedRecievedDate.setOnClickListener {
            openDatePickerDialog {
                binding.ExpectedRecievedDate.setText(it)
                ExpectedRecievedDate = it
            }
        }
        binding.promisReceivedDate.setOnClickListener {
            openDatePickerDialog {
                binding.promisReceivedDate.setText(it)
                promisReceivedDate = it
            }
        }

        if (args.cmd != null) {
            args.cmd?.let { purchaseOrder ->
                purchaseOrder.counter?.let {
                    (requireActivity() as? MainActivity)?.setupViewToolbar(
                        AnfaAppBar.AppBarViewConstraints(
                            showUnderLine = true,
                            showSearchBtnPlaceHolder = false,
                            showProfileBtn = false,
                            showCallSupportBtn = false,
                            showLogo = false,
                            showAppBarTitle = true,
                            appBarTitle = it
                        ),
                        searchNavDirection = null,
                        profileNavDirection = null
                    )
                }
                binding.nameClient.setText(purchaseOrder.supplierName)
                binding.nClient.setText(purchaseOrder.customerId.toString())
                binding.expSupplier.setText(purchaseOrder.expSupplier)
                binding.confirmOrderBtn.isEnabled = true
                purchaseOrder.doc_type?.let { doc_Type ->
                    purchaseOrder.counter?.let { No ->
                        fragmentViewModel.getSalesCommandsLines(
                            doc_Type,
                            No
                        )
                    }
                }
                purchaseOrder.doc_type?.let { doc_Type ->
                    purchaseOrder.counter?.let { No ->
                        fragmentViewModel.getPurchaseCommandsHeader(
                            doc_Type,
                            No
                        )
                    }
                }
            }
        }
        binding.addOrderLigne.setOnClickListener { openItemList() }
        binding.deleteHeader.apply {
            isVisible = args.cmd != null
            setOnClickListener {
                context?.let { _context ->
                    val customErrorDialogView = CustomDialog.inflateCancelableCustomDialogView(
                        _context,
                        getString(R.string.disconnect_aldert_title),
                        getString(R.string.deletePurchaseHeader_alert_msg)
                    )
                    CustomDialog.showCustomCancelableAlertDialog(
                        _context,
                        customErrorDialogView
                    ) { deleteDocuments() }
                }
            }
        }
        binding.confirmOrderBtn.setOnClickListener {
            confirmOrder()
        }
        binding.bottomBtnContainer.setOnClickListener {
            confirmOrder()
        }
        binding.searchClient.apply {
            isVisible = args.cmd == null
            setOnClickListener {
                openClientList()
            }
        }
        subscribe()
    }

    private fun deleteDocuments() {
        args.cmd?.let {
            it.etag?.let { etag ->
                it.doc_type?.let { doc_Type ->
                    it.counter?.let { No ->
                        fragmentViewModel.deletePurchaseCommandsHeader(
                            etag,
                            doc_Type, No
                        )
                    }
                }
            }
        }
    }

    private fun subscribe() {
        fragmentViewModel.itemLineOrder.observe(viewLifecycleOwner) { result ->
            when (result) {
                is EpApiState.Success -> {
                    if (result.data != null) {
                        (requireActivity() as? MainActivity)?.hideLoader()
                        //insert data line one by one
                        result.data?.map {
                            fragmentViewModel.addOrderLine(it)
                        }
                        itemsAdapter.setList(fragmentViewModel.items)
                        itemsAdapter.differ.submitList(fragmentViewModel.items)
                    }
                }
                is EpApiState.Error -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    Timber.e("8888${result.error}")
                    when (result.error) {
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
                                getString(R.string.generic_error)
                            )
                            CustomDialog.showCustomAlertDialog(
                                requireContext(),
                                customErrorDialogView
                            )
                        }

                    }
                }
                is EpApiState.Loading -> {
                    (requireActivity() as? MainActivity)?.showLoader()
                    Timber.d("loading")
                }
                is EpApiState.Failure -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    Timber.d("loading")
                }
            }
        }

        fragmentViewModel.getPurchaseHeaderOrder.observe(viewLifecycleOwner) { result ->
            when (result) {
                is EpApiState.Success -> {
                    demandeDate = result.data?.orderDate.toString()
                    requestedRecievedDate = result.data?.requestedRecievedDate.toString()
                    ExpectedRecievedDate = result.data?.ExpectedRecievedDate.toString()
                    promisReceivedDate = result.data?.promisReceivedDate.toString()
                    binding.demandeDate.text = demandeDate
                    binding.expSupplier.setText(result.data?.expSupplier.toString())
                    binding.phoneClient.setText(result.data?.Phone_no.toString())
                    binding.requestedRecievedDate.text =
                        if (requestedRecievedDate == "NULL") "" else requestedRecievedDate
                    binding.promisReceivedDate.text =
                        if (promisReceivedDate == "NULL") "" else promisReceivedDate
                    binding.ExpectedRecievedDate.text =
                        if (ExpectedRecievedDate == "NULL") "" else ExpectedRecievedDate
                }
                is EpApiState.Error -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    when (result.error) {
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
                                result.error?.message ?: getString(R.string.generic_error)
                            )
                            CustomDialog.showCustomAlertDialog(
                                requireContext(),
                                customErrorDialogView
                            )
                        }

                    }
                }
                is EpApiState.Loading -> {
                    (requireActivity() as? MainActivity)?.showLoader()
                    Timber.d("loading")
                }
                is EpApiState.Failure -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    Timber.d("loading")
                }
            }
        }
        fragmentViewModel.addPurchaseHeaderOrder.observe(viewLifecycleOwner) { result ->
            when (result) {
                is EpApiState.Success -> {
                    if (result.data != null) {
                        (requireActivity() as? MainActivity)?.hideLoader()
                        purchaseHeader = result.data

                        purchaseHeader?.No?.let {
                            (requireActivity() as? MainActivity)?.setupViewToolbar(
                                AnfaAppBar.AppBarViewConstraints(
                                    showUnderLine = true,
                                    showSearchBtnPlaceHolder = false,
                                    showProfileBtn = false,
                                    showCallSupportBtn = false,
                                    showLogo = false,
                                    showAppBarTitle = true,
                                    appBarTitle = it
                                ),
                                searchNavDirection = null,
                                profileNavDirection = null
                            )
                        }
                        binding.expSupplier.setText(purchaseHeader?.expSupplier)
                        binding.factureSupplier.setText(purchaseHeader?.facture_no)
                        binding.requestedRecievedDate.text = purchaseHeader?.requestedRecievedDate
                        binding.ExpectedRecievedDate.text = purchaseHeader?.ExpectedRecievedDate
                        binding.promisReceivedDate.text = purchaseHeader?.promisReceivedDate
                        binding.confirmOrderBtn.isEnabled = true
                        binding.itemsContent.visibility = View.VISIBLE
                    }
                }
                is EpApiState.Error -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    when (result.error) {
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
                                result.error?.message ?: getString(R.string.generic_error)
                            )
                            CustomDialog.showCustomAlertDialog(
                                requireContext(),
                                customErrorDialogView
                            )
                        }

                    }
                }
                is EpApiState.Loading -> {
                    (requireActivity() as? MainActivity)?.showLoader()
                    Timber.d("loading")
                }
                is EpApiState.Failure -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    customErrorDialogView = CustomDialog.inflateCustomDialogView(
                        requireContext(),
                        getString(R.string.dialog_error_title),
                        getString(R.string.generic_error)
                    )
                    CustomDialog.showCustomAlertDialog(
                        requireContext(),
                        customErrorDialogView
                    )
                }
            }
        }
        fragmentViewModel.updatePurchaseHeaderOrder.observe(viewLifecycleOwner) { result ->
            when (result) {
                is EpApiState.Success -> {
                    if (result.data != null) {
                        (requireActivity() as? MainActivity)?.hideLoader()
                        requireActivity().onBackPressed()
                    }
                }
                is EpApiState.Error -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    when (result.error) {
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
                                result.error?.message ?: getString(R.string.generic_error)
                            )
                            CustomDialog.showCustomAlertDialog(
                                requireContext(),
                                customErrorDialogView
                            )
                        }

                    }
                }
                is EpApiState.Loading -> {
                    (requireActivity() as? MainActivity)?.showLoader()
                    Timber.d("loading")
                }
                is EpApiState.Failure -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    Timber.d("loading")
                }
            }
        }
        fragmentViewModel.deleteDocumentOrder.observe(viewLifecycleOwner) { result ->
            when (result) {
                is EpApiState.Success -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    requireActivity().onBackPressed()
                }
                is EpApiState.Error -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    when (result.error) {
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
                            (requireActivity() as? MainActivity)?.hideLoader()
                            requireActivity().onBackPressed()
                        }

                    }
                }
                is EpApiState.Loading -> {
                    (requireActivity() as? MainActivity)?.showLoader()
                    Timber.d("loading")
                }
                is EpApiState.Failure -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    requireActivity().onBackPressed()
                    Timber.d("loading")
                }
            }
        }

        //*********Order Line observes
        fragmentViewModel.addOrder.observe(viewLifecycleOwner) { result ->
            when (result) {
                is EpApiState.Success -> {
                    if (result.data != null) {
                        (requireActivity() as? MainActivity)?.hideLoader()
                        //insert data line one by one
                        itemsAdapter.setList(fragmentViewModel.items)
                        itemsAdapter.differ.submitList(fragmentViewModel.items)
                    }
                }
                is EpApiState.Error -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    when (result.error) {
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
                                result.error?.message ?: getString(R.string.generic_error)
                            )
                            CustomDialog.showCustomAlertDialog(
                                requireContext(),
                                customErrorDialogView
                            )
                        }

                    }
                }
                is EpApiState.Loading -> {
                    (requireActivity() as? MainActivity)?.showLoader()
                    Timber.d("loading")
                }
                is EpApiState.Failure -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    Timber.d("loading")
                }
            }
        }
        fragmentViewModel.updateLineOrder.observe(viewLifecycleOwner) { result ->
            when (result) {
                is EpApiState.Success -> {
                    if (result.data != null) {
                        (requireActivity() as? MainActivity)?.hideLoader()
                        //insert data line one by one
                        itemsAdapter.setList(fragmentViewModel.items)
                        itemsAdapter.differ.submitList(fragmentViewModel.items)
                    }
                }
                is EpApiState.Error -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    when (result.error) {
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
                                getString(R.string.generic_error)
                            )
                            CustomDialog.showCustomAlertDialog(
                                requireContext(),
                                customErrorDialogView
                            )
                        }

                    }
                }
                is EpApiState.Loading -> {
                    (requireActivity() as? MainActivity)?.showLoader()
                    Timber.d("loading")
                }
                is EpApiState.Failure -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    Timber.d("loading")
                }
            }
        }
        fragmentViewModel.deleteLineOrder.observe(viewLifecycleOwner) { result ->
            when (result) {
                is EpApiState.Success -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    //insert data line one by one
                    itemsAdapter.setList(fragmentViewModel.items)
                    itemsAdapter.differ.submitList(fragmentViewModel.items)
                }
                is EpApiState.Error -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    //insert data line one by one
                    itemsAdapter.setList(fragmentViewModel.items)
                    itemsAdapter.differ.submitList(fragmentViewModel.items)
                    when (result.error) {
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
                            (requireActivity() as? MainActivity)?.hideLoader()
                            customErrorDialogView = CustomDialog.inflateCustomDialogView(
                                requireContext(),
                                getString(R.string.dialog_error_title),
                                result.error?.message ?: getString(R.string.conflict_name_error)
                            )
                            CustomDialog.showCustomAlertDialog(
                                requireContext(),
                                customErrorDialogView
                            )
                        }


                    }
                }
                is EpApiState.Loading -> {
                    (requireActivity() as? MainActivity)?.showLoader()
                    Timber.d("loading")
                }
                is EpApiState.Failure -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    //insert data line one by one
                    itemsAdapter.setList(fragmentViewModel.items)
                    itemsAdapter.differ.submitList(fragmentViewModel.items)
                    Timber.d("loading")
                }
            }
        }
    }

    private fun confirmOrder() {
        val currentDeliveryDate = Calendar.getInstance(Locale.FRENCH)
        val currentDay =
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.FRENCH
            ).format(currentDeliveryDate.time)
        val request = UpdatePurchaseHeaderRequest(
            supplierId = binding.nClient.text.toString(),
            supplierName = binding.nameClient.text.toString(),
            Phone_no = " ",
            Vendor_Invoice_No = binding.expSupplier.text.toString().ifEmpty { " " },
            facture_no = binding.factureSupplier.text.toString().ifEmpty { " " },
            orderDate = demandeDate.ifEmpty { currentDay },
            requestedRecievedDate = requestedRecievedDate.ifEmpty { currentDay },
            ExpectedRecievedDate = ExpectedRecievedDate.ifEmpty { currentDay },
            promisReceivedDate = promisReceivedDate.ifEmpty { currentDay },
        )

        if (args.cmd != null)
            args.cmd?.doc_type?.let { type ->
                args.cmd?.counter?.let { no ->
                    fragmentViewModel.updatePurchaseCommandsHeader(type, no, request)
                }
            }
        else
            purchaseHeader?.Type?.let { type ->
                purchaseHeader?.No?.let { no ->
                    fragmentViewModel.updatePurchaseCommandsHeader(
                        type,
                        no, request
                    )
                }
            }
    }

    private fun openItemList() {
        val item =
            ItemListDialogFragment { selectedItem ->
                val addProductDialog = UpdateItemDialog(
                    requireContext(), selectedItem
                ) { weight, units, parcel ->
                    selectedItem.itemWeight = weight
                    selectedItem.itemUnits = units
                    selectedItem.parcel = parcel

                    openItemEditor(selectedItem)
                    AppUtils.hideKeyboard(requireActivity())
                }
                addProductDialog.show()
                addProductDialog.window?.clearFlags(
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                )
            }
        item.show(
            requireActivity().supportFragmentManager,
            ItemListDialogFragment::class.simpleName
        )
    }

    private fun openItemEditor(it: Item) {
        fragmentViewModel.addOrderLine(it)

        if (purchaseHeader == null) {
            args.cmd?.let { cmd ->
                fragmentViewModel.addPurchaseCommandsLine(
                    PurchaseOrderHeaderResult(
                        cmd.counter.toString(),
                        cmd.doc_type,
                        supplierId = cmd.customerId
                    ), it
                )
            }
        } else
            purchaseHeader?.let { header ->
                fragmentViewModel.addPurchaseCommandsLine(
                    header, it
                )
            }
    }

    private fun openClientList() {
        val clients =
            SupplierListBottomDialog {
                binding.nameClient.setText(it.name)
//                binding.cityClient.setText(it.City)
                binding.phoneClient.setText(it.Phone_No)
                binding.nClient.setText(it.code.toString())
                (requireActivity() as? MainActivity)?.showLoader()
                fragmentViewModel.addPurchaseCommandsHeader(binding.nClient.text.toString())
            }
        clients.show(
            requireActivity().supportFragmentManager,
            SupplierListBottomDialog::class.simpleName
        )
    }

    private fun openDatePickerDialog(callBack: (String) -> Unit) {
        val calendar = Calendar.getInstance(Locale.FRENCH)
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val pickerDialog = DatePickerDialog.newInstance(
            { _, year, monthOfYear, dayOfMonth ->
                val realMonth = monthOfYear + 1
                callBack.invoke(
                    LocaleHelper.replaceArabicNumbers(
                        resources.getString(
                            R.string.delivery_date_placeholder,
                            year, realMonth, dayOfMonth
                        )
                    )
                )
            }, currentYear, currentMonth, currentDay
        ).apply {
            minDate = calendar
            locale = Locale.FRENCH
        }

        pickerDialog.show(requireActivity().supportFragmentManager, "Dialog")
    }

    private fun setupRecyclerView() {
        binding.itemsRecycler.adapter = itemsAdapter
        binding.itemsRecycler.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    override fun onItemClicked(item: Item) {

        val addProductDialog = UpdateItemDialog(
            requireContext(), item
        ) { weight, units, parcel ->
            item.itemWeight = weight
            item.itemUnits = units
            item.parcel = parcel
            fragmentViewModel.updateItemLine(item)
            itemsAdapter.setList(fragmentViewModel.items)
            itemsAdapter.differ.submitList(fragmentViewModel.items)
            if (args.cmd != null) {
                args.cmd?.doc_type?.let {
                    args.cmd?.counter?.let { Document_No ->
                        item.code?.let { code ->
                            item.Line_No?.let { Line_No ->
                                item.etag?.let { etag ->
                                    fragmentViewModel.updatePurchaseCommandsLine(
                                        it,
                                        Document_No, code, Line_No, weight, units, parcel
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                purchaseHeader?.Type?.let {
                    purchaseHeader?.No?.let { Document_No ->
                        item.code.let { code ->
                            item.Line_No?.let { Line_No ->
                                item.etag?.let { etag ->
                                    fragmentViewModel.updatePurchaseCommandsLine(
                                        it,
                                        Document_No, code, Line_No, weight, units, parcel
                                    )
                                }
                            }
                        }
                    }
                }
            }
            AppUtils.hideKeyboard(requireActivity())
        }
        addProductDialog.show()
        addProductDialog.window?.clearFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onItemDeleted(item: Item) {

        context?.let { _context ->
            val customErrorDialogView = CustomDialog.inflateCancelableCustomDialogView(
                _context,
                getString(R.string.disconnect_aldert_title),
                getString(R.string.deleteLine_alert_msg)
            )
            CustomDialog.showCustomCancelableAlertDialog(
                _context,
                customErrorDialogView
            ) {
                if (args.cmd != null) {
                    args.cmd?.doc_type?.let {
                        args.cmd?.counter?.let { Document_No ->
                            item.code.let { Line_No ->
                                item.etag?.let { etag ->
                                    fragmentViewModel.deletePurchaseCommandsLine(
                                        etag,
                                        it,
                                        Document_No, Line_No
                                    )
                                }
                            }
                        }
                    }
                } else {
                    purchaseHeader?.Type?.let {
                        purchaseHeader?.No?.let { Document_No ->
                            item.code.let { Line_No ->
                                item.etag?.let { etag ->
                                    fragmentViewModel.deletePurchaseCommandsLine(
                                        etag,
                                        it,
                                        Document_No, Line_No
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
