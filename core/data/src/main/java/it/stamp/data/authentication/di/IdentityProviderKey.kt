package it.stamp.data.authentication.di

import dagger.MapKey
import it.stamp.model.authentication.IdentityProvider

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class IdentityProviderKey(val value: IdentityProvider)