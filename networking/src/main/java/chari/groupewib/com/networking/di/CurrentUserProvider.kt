package ghoudan.anfaSolution.com.networking.di

import ghoudan.anfaSolution.com.app_models.User

interface CurrentUserProvider {
    fun currentUser(): User?
}
