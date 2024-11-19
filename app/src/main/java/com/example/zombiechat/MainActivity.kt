package com.example.zombiechat

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.zombiechat.account.view.AuthScreen
import com.example.zombiechat.src.home.view.screens.HomeActivity
import com.example.zombiechat.util.service.AuthService
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

class MainActivity : AppCompatActivity() {


    private val authService: AuthService by inject(AuthService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(appModule)
        }


        if (authService.getCurrentUser() == null) {
            sendToSigning()
        } else {
            val mainIntent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(mainIntent)
            finish()
        }

    }


    private fun sendToSigning() {
        val SigninIntent = Intent(this@MainActivity, AuthScreen::class.java)
        startActivity(SigninIntent)
        finish()
    }
}


val appModule = module {
    singleOf(::AuthService)

}