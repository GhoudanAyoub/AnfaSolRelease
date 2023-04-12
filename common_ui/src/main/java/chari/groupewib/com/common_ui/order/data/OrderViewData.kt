package ghoudan.anfaSolution.com.common_ui.order.data

import ghoudan.anfaSolution.com.app_models.Order
import ghoudan.anfaSolution.com.common_ui.order.OrderListener

data class OrderViewData(
    val order: Order,
    val listener: OrderListener
)
