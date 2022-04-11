package su.usatu.navigator.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import su.usatu.navigator.data.db.HistoryDatabase
import su.usatu.navigator.data.db.PointsDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    @Provides
    @Singleton
    fun providePointDao(database: PointsDatabase) = database.pointDao()

    @Provides
    @Singleton
    fun providePointsDatabase(@ApplicationContext context: Context): PointsDatabase =
        Room.databaseBuilder(context, PointsDatabase::class.java, "points_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideHistoryDao(database: HistoryDatabase) = database.historyDao()

    @Provides
    @Singleton
    fun provideHistoryDatabase(@ApplicationContext context: Context): HistoryDatabase =
        Room.databaseBuilder(context, HistoryDatabase::class.java, "history_database")
            .fallbackToDestructiveMigration()
            .build()
}