package chari.groupewib.com.common_ui.stock

import chari.groupewib.com.app_models.Item
import chari.groupewib.com.networking.entity.StockSaisieEntity

interface StockViewListener {
    fun onItemClicked(item: StockSaisieEntity)
}
