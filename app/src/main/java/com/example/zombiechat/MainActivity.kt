package com.example.zombiechat

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.example.zombiechat.account.view.AccountSetting
import com.example.zombiechat.account.view.AuthScreen
import com.example.zombiechat.friends.view.allUsers.AllusersActivity
import com.example.zombiechat.service.AuthService
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.tabs.TabLayout

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

class MainActivity : AppCompatActivity() {
    private var mSectionPagerAdapter: SectionPageradapter? = null
    private var mToolbar: Toolbar? = null
    private var viewPager: ViewPager? = null
    private var tablayout: TabLayout? = null
    private val mGoogleSignInClient: GoogleSignInClient? = null

    private val authService: AuthService by inject(AuthService::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setting tabs
        mToolbar = findViewById(R.id.main_page_toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar!!.title = "Zombie Chat"

        tablayout = findViewById(R.id.main_tabs)
        viewPager = findViewById(R.id.main_view_pager)

        mSectionPagerAdapter = SectionPageradapter(supportFragmentManager)
        viewPager?.setAdapter(mSectionPagerAdapter)
        tablayout?.setupWithViewPager(viewPager)


    }

    override fun onStart() {
        super.onStart()


        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(appModule)
        }

        lifecycleScope.launch {
            if (authService.getCurrentUser() == null) {
                sendToSigning();
            }
        }


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.main_menu, menu)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)


        if (item.itemId == R.id.main_logout_btn) {
            //            mAuth.signOut();

            mGoogleSignInClient!!.signOut().addOnCompleteListener {
                Toast.makeText(
                    this@MainActivity, " Logged Out", Toast.LENGTH_SHORT
                ).show()
            }.addOnFailureListener { e ->
                Toast.makeText(
                    this@MainActivity, "Error: " + e.message, Toast.LENGTH_SHORT
                ).show()
            }

            sendToSigning()
        }

        if (item.itemId == R.id.main_account_setting) {
            accountSettingsIntent()
        }

        if (item.itemId == R.id.all_users) {
            allUserIntent()
        }


        return true
    }


    //All Used methods
    fun sendToSigning() {
        val SigninIntent = Intent(this@MainActivity, AuthScreen::class.java)
        startActivity(SigninIntent)
        finish()
    }


    private fun accountSettingsIntent() {
        val AccountSettingIntent = Intent(this@MainActivity, AccountSetting::class.java)
        startActivity(AccountSettingIntent)
    }


    private fun allUserIntent() {
        val AllusersActivityIntent = Intent(this@MainActivity, AllusersActivity::class.java)
        startActivity(AllusersActivityIntent)
    }

    companion object {
        private const val TAG = "Cloud Firestore"
    }
}


val appModule = module {
    singleOf(::AuthService)

}
