package su.usatu.navigator.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import su.usatu.navigator.data.repository.PreferenceRepository

@Module
@InstallIn(SingletonComponent::class)
class PreferenceModule {
    @Provides
    @Reusable
    fun providePreferenceRepository(@ApplicationContext context: Context): PreferenceRepository =
        PreferenceRepository(
            context.getSharedPreferences(
                "usatu_nav_preferences",
                Context.MODE_PRIVATE
            )
        )
}