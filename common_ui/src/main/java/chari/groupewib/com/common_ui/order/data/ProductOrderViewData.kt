package ghoudan.anfaSolution.com.common_ui.order.data

import chari.groupewib.com.app_models.Item
import chari.groupewib.com.common_ui.product.ItemViewListener
import ghoudan.anfaSolution.com.app_models.OrderLine
import ghoudan.anfaSolution.com.common_ui.product.ProductListener

data class ProductOrderViewData(
    val orderLine: Any,
    val listener: ItemViewListener
)
