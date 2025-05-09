package chari.groupewib.com.ui.command.sales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import chari.groupewib.com.app_models.Item
import chari.groupewib.com.common_ui.product.ItemViewListener
import chari.groupewib.com.networking.entity.PackingListEntity
import chari.groupewib.com.networking.entity.StockSaisieEntity
import chari.groupewib.com.networking.request.SalesOrderHeaderResult
import chari.groupewib.com.networking.request.UpdateSalesHeaderRequest
import chari.groupewib.com.ui.clients.ClientListBottomDialog
import chari.groupewib.com.ui.command.bottomDialogs.PackingListBottomDialog
import chari.groupewib.com.ui.command.bottomDialogs.StockSaisieListBottomDialog
import chari.groupewib.com.ui.command.bottomDialogs.UpdateItemDialog
import chari.groupewib.com.ui.command.items.ItemListDialogFragment
import chari.groupewib.com.ui.command.items.ItemsAdapter
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.anfaSolution.com.MainActivity
import ghoudan.anfaSolution.com.R
import ghoudan.anfaSolution.com.common_ui.appbar.AnfaAppBar
import ghoudan.anfaSolution.com.common_ui.dialog.CustomDialog
import ghoudan.anfaSolution.com.databinding.FragmentCreateSalesOrderBinding
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
class CreateSalesOrder : Fragment(), ItemViewListener {

    private lateinit var customErrorDialogView: View
    private val args: CreateSalesOrderArgs by navArgs()
    private var saleHeader: SalesOrderHeaderResult? = null
    private var shipDate: String = ""
    private var postClientDate: String = ""
    private var dueClientDate: String = ""
    lateinit var binding: FragmentCreateSalesOrderBinding
    private val fragmentViewModel by viewModels<CommandViewModel>()
    private val viewModel: ClientListViewModel by activityViewModels()
    private val itemsAdapter: ItemsAdapter by lazy {
        ItemsAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCreateSalesOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.totalExVatText.text = "0"
        binding.totalIncVatText.text = "0"
        binding.totalVatText.text = "0"
        args.cmd?.let {
            it.No?.let {
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
            binding.nameClient.setText(it.customerName)
            binding.nClient.setText(it.customerId.toString())
            binding.salerID.setText(it.salerCode)
            binding.shipDate.setText(it.desiredDeliveryDate)
            binding.dueClient.setText(it.Due_Date)
            binding.postClient.setText(it.Posting_Date)
            it.doc_Type?.let { doc_Type ->
                it.No?.let { No ->
                    fragmentViewModel.getSalesCommandsLines(
                        doc_Type,
                        No.toString(),
                        true
                    )
                    fragmentViewModel.getSalesCommandsHeader(
                        doc_Type,
                        No.toString()
                    )
                }
            }
            binding.confirmOrderBtn.isEnabled = true
        }

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
            binding.shipDate.setText(currentDay)
            binding.postClient.setText(currentDay)

        }
        binding.shipDate.setOnClickListener {
            openDatePickerDialog {
                binding.shipDate.setText(it)
                shipDate = it
            }
        }
        binding.postClient.setOnClickListener {
            openDatePickerDialog {
                binding.postClient.setText(it)
                postClientDate = it
            }
        }
        binding.dueClient.setOnClickListener {
            openDatePickerDialog {
                binding.dueClient.setText(it)
                dueClientDate = it
            }
        }
        binding.addOrderLigne.setOnClickListener { openStockSaisieList() }
        binding.deleteHeader.apply {
            isVisible = args.cmd != null
            setOnClickListener {
                context?.let { _context ->
                    val customErrorDialogView = CustomDialog.inflateCancelableCustomDialogView(
                        _context,
                        getString(R.string.disconnect_aldert_title),
                        getString(R.string.deleteSalesHeader_alert_msg)
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

    private fun openStockSaisieList() {
        fragmentViewModel.getStockSaisieList().observe(viewLifecycleOwner) {
            when (it) {
                is EpApiState.Success -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    it.data?.let { stockList ->
                        AppUtils.hideKeyboard(requireActivity())
                        showStockeSaisieList(stockList)
                    }
                }

                is EpApiState.Loading -> {
                    (requireActivity() as? MainActivity)?.showLoader()
                }

                else -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                }
            }
        }
    }

    private fun showStockeSaisieList(data: List<StockSaisieEntity>) {
        args.cmd?.let {
            val item =
                StockSaisieListBottomDialog(it, data) {
                    getPackingListEntity(it)
                }
            item.show(
                requireActivity().supportFragmentManager,
                StockSaisieListBottomDialog::class.simpleName
            )
        }
    }

    private fun getPackingListEntity(noArticle: String) {
        fragmentViewModel.getPackingListEntity(noArticle)
            .observe(viewLifecycleOwner) { packingListState ->
                when (packingListState) {
                    is EpApiState.Loading -> {
                        (requireActivity() as? MainActivity)?.showLoader()
                    }

                    is EpApiState.Success -> {
                        (requireActivity() as? MainActivity)?.hideLoader()
                        packingListState.data?.let {
                            AppUtils.hideKeyboard(requireActivity())
                            openPackingListBottomDialog(it)
                        }
                    }

                    is EpApiState.Error -> {
                        (requireActivity() as? MainActivity)?.hideLoader()
                    }

                    else -> {}
                }
            }
    }

    private fun getPackingListByColisNumber(NumColis: String,article_num: String, isSelected: Boolean? = false) {
        fragmentViewModel.getPackingListByColisNumber(NumColis,article_num)
            .observe(viewLifecycleOwner) { packingListState ->
                when (packingListState) {
                    is EpApiState.Loading -> {
                        (requireActivity() as? MainActivity)?.showLoader()
                    }

                    is EpApiState.Success -> {
                        (requireActivity() as? MainActivity)?.hideLoader()
                        packingListState.data?.let {
                            AppUtils.hideKeyboard(requireActivity())
                            openPackingListBottomDialog(it, isSelected)
                        }
                    }

                    is EpApiState.Error -> {
                        (requireActivity() as? MainActivity)?.hideLoader()
                    }

                    else -> {}
                }
            }
    }

    private fun openPackingListBottomDialog(
        packingListEntities: List<PackingListEntity>,
        isSelected: Boolean? = false,
    ) {
        if (packingListEntities.isNotEmpty()) {
            val item =
                PackingListBottomDialog(packingListEntities, isSelected) {
                    AppUtils.hideKeyboard(requireActivity())
                }
            item.show(
                requireActivity().supportFragmentManager,
                PackingListBottomDialog::class.simpleName
            )
        }else{
            customErrorDialogView = CustomDialog.inflateCustomDialogView(
                requireContext(),
                getString(R.string.dialog_error_title),
                getString(R.string.no_packing_list)
            )
            CustomDialog.showCustomAlertDialog(
                requireContext(),
                customErrorDialogView
            )
        }
    }

    private fun deleteDocuments() {
        args.cmd?.let {
            it.etag?.let { etag ->
                it.doc_Type?.let { doc_Type ->
                    it.No?.let { No ->
                        fragmentViewModel.deleteSalesCommandsHeader(
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
                        if (args.cmd != null) {
                            result.data?.map {
                                fragmentViewModel.addOrderLine(it)
                            }
                            itemsAdapter.setList(fragmentViewModel.items)
                            itemsAdapter.differ.submitList(fragmentViewModel.items)
                        } else {
                            val header = SalesOrderHeaderResult(
                                saleHeader?.No.toString(),
                                saleHeader?.Type,
                                saleHeader?.customerId
                            )
                            result.data?.let { items ->
                                fragmentViewModel.addSalesCommandsLineList(
                                    header,
                                    items
                                )
                            }
                        }
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

        //Sales Order Header
        fragmentViewModel.getHeaderOrder.observe(viewLifecycleOwner) { result ->
            when (result) {
                is EpApiState.Success -> {
                    postClientDate = result.data?.Posting_Date.toString()
                    dueClientDate = result.data?.Due_Date.toString()
                    shipDate = result.data?.Order_Date.toString()
                    binding.dueClient.text = result.data?.Due_Date
                    binding.postClient.text = result.data?.Posting_Date
                    binding.phoneClient.setText(result.data?.Sell_to_Phone_No)
                    binding.shipDate.text = result.data?.Order_Date
                    binding.cityClient.setText(result.data?.city)
                    binding.salerID.setText(result.data?.Salesperson_Code)
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
        fragmentViewModel.addHeaderOrder.observe(viewLifecycleOwner) { result ->
            when (result) {
                is EpApiState.Success -> {
                    if (result.data != null) {
                        saleHeader = result.data
                        binding.modeSaisie.text = saleHeader?.modeSaisie
                        binding.adrsP.setText(saleHeader?.addressPostal)
                        binding.confirmOrderBtn.isEnabled = true
                        binding.itemsContent.visibility = View.VISIBLE


                        saleHeader?.No?.let {
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
                        saleHeader?.Type?.let { doc_Type ->
                            saleHeader?.No?.let { No ->
                                fragmentViewModel.getSalesCommandsLines(
                                    doc_Type,
                                    No,
                                    true
                                )
                            }
                        }
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
        fragmentViewModel.updateHeaderOrder.observe(viewLifecycleOwner) { result ->
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

        //Sales Order Lines
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
                            customErrorDialogView = CustomDialog.inflateCustomDialogView(
                                requireContext(),
                                getString(R.string.dialog_error_title),
                                result.error?.message ?: getString(R.string.conflict_name_error)
                            )
                            CustomDialog.showCustomAlertDialog(
                                requireContext(),
                                customErrorDialogView
                            )
                            (requireActivity() as? MainActivity)?.hideLoader()
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
        val request = UpdateSalesHeaderRequest(
            customerId = binding.nClient.text.toString(),
            addressPostal = if (binding.adrsP.text.isNotEmpty()) binding.adrsP.text.toString() else " ",
            city = if (binding.cityClient.text.isNotEmpty()) binding.cityClient.text.toString() else " ",
            Due_Date = dueClientDate.ifEmpty { currentDay },
            Posting_Date = postClientDate.ifEmpty { currentDay },
            Sell_to_Phone_No = if (binding.phoneClient.text.isNotEmpty()) binding.phoneClient.text.toString() else " ",
            Salesperson_Code = binding.salerID.text.toString(),
            Order_Date = shipDate.ifEmpty { currentDay }
        )
        if (args.cmd != null)
            args.cmd?.doc_Type?.let { type ->
                args.cmd?.No?.let { no ->
                    fragmentViewModel.updateSalesCommandsHeader(type, no, request)
                }
            }
        else
            saleHeader?.Type?.let { type ->
                saleHeader?.No?.let { no ->
                    fragmentViewModel.updateSalesCommandsHeader(
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
        if (saleHeader == null) {
            args.cmd?.let { cmd ->
                val salesHeader = SalesOrderHeaderResult(
                    No = cmd.No.toString(),
                    Type = cmd.doc_Type,
                    customerId = cmd.customerId
                )
                fragmentViewModel.addSalesCommandsLine(
                    salesHeader, it
                )
            }
        } else
            saleHeader?.let { header ->
                fragmentViewModel.addSalesCommandsLine(
                    header, it
                )
            }
    }

    private fun openClientList() {
        val clients =
            ClientListBottomDialog {
                binding.nameClient.setText(it.name)
                binding.cityClient.setText(it.City)
                binding.phoneClient.setText(it.Phone_No)
                binding.nClient.setText(it.code.toString())
                (requireActivity() as? MainActivity)?.showLoader()
                fragmentViewModel.addSalesCommandsHeader(binding.nClient.text.toString())
            }
        clients.show(
            requireActivity().supportFragmentManager,
            ClientListBottomDialog::class.simpleName
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
        getPackingListByColisNumber(item.Line_No.toString(),item.code, true)
    }

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
                    args.cmd?.doc_Type?.let {
                        args.cmd?.No?.let { Document_No ->
                            item.code?.let { Line_No ->
                                item.etag?.let { etag ->
                                    fragmentViewModel.deleteSalesCommandsLine(
                                        etag,
                                        it,
                                        Document_No, item.code
                                    )
                                }
                            }
                        }
                    }
                } else {
                    saleHeader?.Type?.let {
                        saleHeader?.No?.let { Document_No ->
                            item.Line_No?.let { Line_No ->
                                item.etag?.let { etag ->
                                    fragmentViewModel.deleteSalesCommandsLine(
                                        etag,
                                        it,
                                        Document_No, item.code
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
