package su.usatu.navigator.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import su.usatu.navigator.data.repository.HistoryRepository
import su.usatu.navigator.data.repository.HistoryRepositoryImpl
import su.usatu.navigator.data.repository.PointsRepository
import su.usatu.navigator.data.repository.PointsRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindPointsRepository(pointsRepositoryImpl: PointsRepositoryImpl): PointsRepository

    @Singleton
    @Binds
    fun bindHistoryRepository(historyRepositoryImpl: HistoryRepositoryImpl): HistoryRepository
}