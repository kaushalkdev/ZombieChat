package com.example.zombiechat.src.account.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.zombiechat.src.home.view.screens.HomeActivity
import com.example.zombiechat.R
import com.example.zombiechat.account.viewModel.AuthVM
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class AuthScreen : AppCompatActivity() {
    private var msignin: SignInButton? = null
    private var mdialog: ProgressDialog? = null
    private val authVM: AuthVM by viewModel()
    private lateinit var credentialManager: CredentialManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        credentialManager = CredentialManager.create(this)

        //progress Dialog
        mdialog = ProgressDialog(this)


        //google sign in
        msignin = findViewById(R.id.signinBtn)

        msignin?.setOnClickListener(View.OnClickListener {
            if (authVM.currentUser.value == null) {
                signInWithGoogle()
            } else {
                Toast.makeText(this@AuthScreen, "Already Signed in", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun signInWithGoogle() {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.default_web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    context = this@AuthScreen,
                    request = request
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                Toast.makeText(this@AuthScreen, "Error: " + e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        val credential = result.credential

        if (credential is GoogleIdTokenCredential) {
            val idToken = credential.idToken
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            
            lifecycleScope.launch {
                try {
                    authVM.signInWith(firebaseCredential)
                } catch (e: Exception) {
                    Toast.makeText(this@AuthScreen, "Error: " + e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.

        authVM.currentUser.observe(this) {
            if (it != null) {
                val mainIntent = Intent(this@AuthScreen, HomeActivity::class.java)
                startActivity(mainIntent)
                finish()
            }
        }
        if (authVM.isLoggedIn() == true) {
            val mainIntent = Intent(this@AuthScreen, HomeActivity::class.java)
            startActivity(mainIntent)
            finish()
        }
    }


    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private const val TAG = "Signin"
    }
}
