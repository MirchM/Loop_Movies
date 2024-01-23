package com.mmisoft.loop_movies.module

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.mmisoft.loop_movies.repository.user.UserDataStoreRepositoryImpl
import com.mmisoft.loop_movies.repository.user.UserRepository
import com.mmisoft.loop_movies.ui.viewmodel.UserViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideUserDataStoreRepository(
        @ApplicationContext app: Context
    ): UserRepository = UserDataStoreRepositoryImpl(app)
}