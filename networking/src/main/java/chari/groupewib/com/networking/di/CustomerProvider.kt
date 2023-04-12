package ghoudan.anfaSolution.com.networking.di

import ghoudan.anfaSolution.com.app_models.Customer

interface CustomerProvider {
    fun selectedCustomer(): Customer?
}
