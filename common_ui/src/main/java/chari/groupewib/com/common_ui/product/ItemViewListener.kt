package chari.groupewib.com.common_ui.product

import chari.groupewib.com.app_models.Item

interface ItemViewListener {

    fun onItemClicked(item: Item)
    fun onItemDeleted(item: Item)
}
