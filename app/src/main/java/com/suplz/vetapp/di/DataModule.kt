package com.suplz.vetapp.di

import android.content.Context
import androidx.room.Room
import com.suplz.vetapp.data.VetDao
import com.suplz.vetapp.data.VetDatabase
import com.suplz.vetapp.data.VetRepositoryImpl
import com.suplz.vetapp.domain.VetRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindVetRepository(
        impl: VetRepositoryImpl
    ): VetRepository

    companion object {
        @Singleton
        @Provides
        fun provideDatabase(
            @ApplicationContext context: Context
        ): VetDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = VetDatabase::class.java,
                name = "vet.db"
            )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
        }

        @Singleton
        @Provides
        fun provideVetDao(
            database: VetDatabase
        ): VetDao {
            return database.vetDao()
        }
    }
}