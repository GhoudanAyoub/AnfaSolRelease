package ghoudan.anfaSolution.com.common_ui.order

import ghoudan.anfaSolution.com.app_models.Order

interface OrderListener {
    fun onOrderClicked(order: Order)
}
