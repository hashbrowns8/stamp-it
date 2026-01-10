package it.stamp.data.authentication.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import it.stamp.data.authentication.GoogleIdentityVerifier
import it.stamp.data.authentication.IdentityVerifier
import it.stamp.model.authentication.IdentityProvider

@Module
@InstallIn(SingletonComponent::class)
abstract class IdentityVerifierModule {

    @Binds
    @IntoMap
    @IdentityProviderKey(IdentityProvider.GOOGLE)
    abstract fun bindGoogleVerifier(verifier: GoogleIdentityVerifier): IdentityVerifier
}