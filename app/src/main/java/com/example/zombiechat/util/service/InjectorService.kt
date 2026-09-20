package com.example.zombiechat.util.service

import android.content.Context
import com.example.zombiechat.account.viewModel.AuthVM
import com.example.zombiechat.src.account.data.repo.AuthRepo
import com.example.zombiechat.src.account.data.repo.AuthRepoImpl
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


object InjectorService {


    fun startInjector(context: Context) {
        startKoin {
            androidLogger()
            androidContext(context)
            modules(modules)
        }
    }

    private val modules = module {
        singleOf(::AuthService)
        singleOf(::AuthRepoImpl) { bind<AuthRepo>() }
        viewModelOf(::AuthVM)
    }
}