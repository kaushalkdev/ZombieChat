package com.example.zombiechat

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.zombiechat.src.account.view.AuthScreen
import com.example.zombiechat.src.home.view.screens.HomeActivity
import com.example.zombiechat.util.service.AuthService
import com.example.zombiechat.util.service.InjectorService
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {


    private val authService: AuthService by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        InjectorService.startInjector(this)


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


