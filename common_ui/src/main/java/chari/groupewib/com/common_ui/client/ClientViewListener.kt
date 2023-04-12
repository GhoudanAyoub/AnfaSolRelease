package ghoudan.anfaSolution.com.common_ui.client

import ghoudan.anfaSolution.com.app_models.Customer
import ghoudan.anfaSolution.com.app_models.CustomerAnfa
import ghoudan.anfaSolution.com.app_models.SupplierAnfa

interface ClientViewListener {
    fun onCustomerClicked(customer: CustomerAnfa)
    fun onSupplierClicked(customer: SupplierAnfa)
}
