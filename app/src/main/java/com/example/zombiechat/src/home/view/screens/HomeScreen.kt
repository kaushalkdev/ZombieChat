package com.example.zombiechat.src.home.view.screens

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.zombiechat.R
import com.example.zombiechat.account.view.AccountSetting
import com.example.zombiechat.account.view.AuthScreen
import com.example.zombiechat.friends.view.allUsers.AllusersActivity
import com.example.zombiechat.src.home.view.adapters.HomePagerAdapter
import com.example.zombiechat.util.service.AuthService
import com.google.android.material.tabs.TabLayout
import org.koin.java.KoinJavaComponent.inject

class HomeActivity : AppCompatActivity() {
    private var mSectionPagerAdapter: HomePagerAdapter? = null
    private var mToolbar: Toolbar? = null
    private var viewPager: ViewPager? = null
    private var tablayout: TabLayout? = null
    private val authService: AuthService by inject(AuthService::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //setting tabs
        mToolbar = findViewById(R.id.main_page_toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar!!.title = "Zombie Chat"

        tablayout = findViewById(R.id.main_tabs)
        viewPager = findViewById(R.id.main_view_pager)

        mSectionPagerAdapter = HomePagerAdapter(supportFragmentManager)
        viewPager?.setAdapter(mSectionPagerAdapter)
        tablayout?.setupWithViewPager(viewPager)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when (item.itemId) {
            R.id.main_logout_btn -> {
                authService.signOut()
                sendToSigning()
            }

            R.id.main_account_setting -> {
                accountSettingsIntent()
            }

            R.id.all_users -> {
                allUserIntent()
            }
        }
        return true
    }


    //All Used methods
    private fun sendToSigning() {
        val SigninIntent = Intent(this@HomeActivity, AuthScreen::class.java)
        startActivity(SigninIntent)
        finish()
    }

    private fun accountSettingsIntent() {
        val AccountSettingIntent = Intent(this@HomeActivity, AccountSetting::class.java)
        startActivity(AccountSettingIntent)
    }


    private fun allUserIntent() {
        val AllusersActivityIntent = Intent(this@HomeActivity, AllusersActivity::class.java)
        startActivity(AllusersActivityIntent)
    }


}



