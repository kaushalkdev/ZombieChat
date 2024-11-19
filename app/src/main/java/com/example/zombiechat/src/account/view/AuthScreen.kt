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
import com.example.zombiechat.src.account.data.repo.AuthRepoImpl
import com.example.zombiechat.account.viewModel.AuthVM
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

class AuthScreen : AppCompatActivity() {
    private var msignin: SignInButton? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var mdialog: ProgressDialog? = null
    private var authVM: AuthVM? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        authVM = AuthVM(AuthRepoImpl())


        //progress Dialog
        mdialog = ProgressDialog(this)


        //google sign in
        msignin = findViewById(R.id.signinBtn)


        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        msignin?.setOnClickListener(View.OnClickListener {
            if (authVM?.currentUser?.value == null) {

                mGoogleSignInClient!!.signOut().addOnSuccessListener {
                    val signInIntent = mGoogleSignInClient!!.signInIntent
                    startActivityForResult(signInIntent, RC_SIGN_IN)
                }


            } else {
                Toast.makeText(this@AuthScreen, "Already Signed in", Toast.LENGTH_SHORT).show()
            }
        })
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.

        authVM?.currentUser?.observe(this) {
            if (it != null) {
                val mainIntent = Intent(this@AuthScreen, HomeActivity::class.java)
                startActivity(mainIntent)
                finish()
            }
        }
        if (authVM?.isLoggedIn() == true) {
            val mainIntent = Intent(this@AuthScreen, HomeActivity::class.java)
            startActivity(mainIntent)
            finish()
        }
    }


    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                val token = account.idToken
                val credential = GoogleAuthProvider.getCredential(token, null)

                lifecycleScope.launch {
                    try {
                        authVM?.signInWith(credential)

                    } catch (e: Exception) {

                        Toast.makeText(this@AuthScreen, "Error: " + e.message, Toast.LENGTH_SHORT)
                            .show()

                    }
                }


            } catch (e: Exception) {
                Toast.makeText(this, "Error: " + task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val TAG = "Signin"
        private const val RC_SIGN_IN = 123
    }
}
