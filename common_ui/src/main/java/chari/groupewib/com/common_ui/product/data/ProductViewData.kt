package ghoudan.anfaSolution.com.common_ui.product.data

import ghoudan.anfaSolution.com.common_ui.product.ProductListener
import ghoudan.anfaSolution.com.common_ui.product.ProductViewHelper

data class ProductViewData(
//    val article: Article,
    val listener: ProductListener,
    val productViewHelper: ProductViewHelper? = null
)
