package ghoudan.anfaSolution.com.common_ui.order

import ghoudan.anfaSolution.com.app_models.PurchaseOrder
import ghoudan.anfaSolution.com.app_models.CommandAchat

interface CommandListener {
    fun onCommandClicked(command: CommandAchat)
    fun onDeleteClicked(command: CommandAchat)
    fun onSalesCommandClicked(command: PurchaseOrder)
    fun onSalesDeleteClicked(command: PurchaseOrder)
    fun getClientName(id: String, callback: (String,String) -> Unit)
    fun getSupplierName(id: String, callback: (String,String) -> Unit)
}
