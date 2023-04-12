package  ghoudan.anfaSolution.com.ui.clients.suppliers

import ghoudan.anfaSolution.com.common_ui.client.ClientViewListener
import com.airbnb.epoxy.TypedEpoxyController
import ghoudan.anfaSolution.com.app_models.SupplierAnfa
import ghoudan.anfaSolution.com.common_ui.client.SupplierView
import ghoudan.anfaSolution.com.databinding.ActivityMainBinding.bind

class SupplierListController(private val listener: ClientViewListener) :
        TypedEpoxyController<List<SupplierAnfa>>() {

    override fun buildModels(customers: List<SupplierAnfa>?) {
        customers?.let {
            customers.forEach { customer ->
//                SupplierView {
//                    id(customer.id)
//                    bind(SupplierView.SupplierViewData(customer, this@SupplierListController.listener))
//                }
            }
        }
    }
}
