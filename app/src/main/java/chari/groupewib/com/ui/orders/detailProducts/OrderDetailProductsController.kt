package ghoudan.anfaSolution.com.ui.orders.detailProducts

import chari.groupewib.com.app_models.Item
import chari.groupewib.com.common_ui.product.ItemViewListener
import chari.groupewib.com.ui.command.items.ItemListDialogFragment
import ghoudan.anfaSolution.com.common_ui.order.data.ProductOrderViewData
import ghoudan.anfaSolution.com.common_ui.product.productOrderView
import com.airbnb.epoxy.TypedEpoxyController

class OrderDetailProductsController(val listener: ItemViewListener) : TypedEpoxyController<List<Item>>() {

    override fun buildModels(data: List<Item>?) {
        data.let { order ->
            order?.forEach { orderLine ->
                productOrderView {
                    id(orderLine.code).bind(
                        ProductOrderViewData(
                            orderLine = orderLine,
                            listener = this@OrderDetailProductsController.listener
                        )
                    )
                }
            }
        }
    }
}
