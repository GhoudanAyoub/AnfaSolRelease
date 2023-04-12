package  ghoudan.anfaSolution.com.ui.clients

import ghoudan.anfaSolution.com.app_models.Customer
import ghoudan.anfaSolution.com.app_models.CustomerAnfa
import ghoudan.anfaSolution.com.common_ui.client.ClientView
import ghoudan.anfaSolution.com.common_ui.client.ClientViewListener
import ghoudan.anfaSolution.com.common_ui.client.clientView
import com.airbnb.epoxy.TypedEpoxyController

class ClientListController(private val listener: ClientViewListener) :
        TypedEpoxyController<List<CustomerAnfa>>() {

    override fun buildModels(customers: List<CustomerAnfa>?) {
        customers?.let {
            customers.forEach { customer ->
                clientView {
                    id(customer.code)
                    bind(ClientView.ClientViewData(customer, this@ClientListController.listener))
                }
            }
        }
    }
}
