package chari.groupewib.com.common_ui.packing

import chari.groupewib.com.networking.entity.PackingListEntity
import chari.groupewib.com.networking.entity.StockSaisieEntity

interface PackingViewListener {
    fun onItemClicked(stock: PackingListEntity, weight: Double, ischecked: Boolean)
}
