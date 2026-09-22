package com.example.zombiechat.src.account.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.zombiechat.R
import com.example.zombiechat.account.viewModel.AuthVM
import com.example.zombiechat.src.home.view.screens.HomeActivity
import com.google.android.gms.common.SignInButton
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

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

        authVM.authError.observe(this) { errorMsg ->
            if (!errorMsg.isNullOrEmpty()) {
                mdialog?.dismiss()
                Toast.makeText(this@AuthScreen, errorMsg, Toast.LENGTH_LONG).show()
            }
        }
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
                Log.e(TAG, "GetCredentialException: ", e)
                Toast.makeText(this@AuthScreen, "Error: " + e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        val credential = result.credential

        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                mdialog?.setMessage("Signing in...")
                mdialog?.show()
                authVM.signInWith(firebaseCredential)
            } catch (e: GoogleIdTokenParsingException) {
                Log.e(TAG, "GoogleIdTokenParsingException: ", e)
                Toast.makeText(this@AuthScreen, "Error parsing Google token: " + e.message, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e(TAG, "Error handling sign in: ", e)
                Toast.makeText(this@AuthScreen, "Error: " + e.message, Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e(TAG, "Unexpected credential type: ${credential.type}")
            Toast.makeText(this@AuthScreen, "Unexpected credential type: ${credential.type}", Toast.LENGTH_SHORT).show()
        }
    }

    public override fun onStart() {
        super.onStart()

        authVM.currentUser.observe(this) {
            if (it != null) {
                mdialog?.dismiss()
                val mainIntent = Intent(this@AuthScreen, HomeActivity::class.java)
                startActivity(mainIntent)
                finish()
            }
        }
        if (authVM.isLoggedIn()) {
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
