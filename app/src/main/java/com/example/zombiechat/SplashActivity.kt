package com.example.zombiechat

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.zombiechat.account.view.AuthScreen
import com.example.zombiechat.service.AuthService
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

class SplashActivity : AppCompatActivity() {


    final private val authService: AuthService by inject(AuthService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        startKoin {
            androidLogger()
            androidContext(this@SplashActivity)
            modules(appModule)
        }


        if (authService.getCurrentUser() == null) {
            sendToSigning()
        }else{
            val mainIntent = Intent(this@SplashActivity, HomeActivity::class.java)
            startActivity(mainIntent)
            finish()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun sendToSigning() {
        val SigninIntent = Intent(this@SplashActivity, AuthScreen::class.java)
        startActivity(SigninIntent)
        finish()
    }
}


val appModule = module {
    singleOf(::AuthService)

}