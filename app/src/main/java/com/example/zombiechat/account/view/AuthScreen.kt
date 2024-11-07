package com.example.zombiechat.account.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zombiechat.MainActivity
import com.example.zombiechat.R
import com.example.zombiechat.account.data.models.UserModel
import com.example.zombiechat.account.data.repo.AuthRepoImpl
import com.example.zombiechat.account.viewModel.AuthVM
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AdditionalUserInfo
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.util.Objects

class AuthScreen : AppCompatActivity() {
    private var msignin: SignInButton? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var mdialog: ProgressDialog? = null
    private var authVM: AuthVM? = null
    private var auth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        authVM = AuthVM(AuthRepoImpl())
        auth = FirebaseAuth.getInstance()


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
            if (authVM?.currentUser == null) {
                signIn()
            } else {
                Toast.makeText(this@AuthScreen, "Already Signed in", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        if (authVM?.currentUser != null) {
            val mainIntent = Intent(this@AuthScreen, MainActivity::class.java)
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
                val account = task.getResult(
                    ApiException::class.java
                )
                val token = account.idToken
                val credential = GoogleAuthProvider.getCredential(token, null)


                // Sign in with firebase auth and navigate to screen
                auth!!.signInWithCredential(credential)
                    .addOnSuccessListener { authResult: AuthResult ->

                        // Adding new user to database
                        if (Objects.requireNonNull<AdditionalUserInfo?>(authResult.additionalUserInfo).isNewUser) {
                            val user = checkNotNull(authResult.user)
                            val image = Objects.requireNonNull(user.photoUrl).toString()
                            val name = Objects.requireNonNull(user.displayName)
                            val userId = Objects.requireNonNull(user.uid)
                            val gender = "male"
                            val status = "Hey there i am using Zombie chat"


                            authVM?.createAccount(UserModel(image, name, gender, status, userId))
                        }

                        // Navigate to main screen
                        val mainIntent = Intent(this@AuthScreen, MainActivity::class.java)
                        startActivity(mainIntent)
                        finish()
                    }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Error: " + e.message, Toast.LENGTH_SHORT).show()
                // ...
            }
        }
    } //    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
    //
    //        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
    //
    //        mdialog.setTitle("Signing In");
    //        mdialog.setMessage("Please wait while we log you in to your account... ");
    //        mdialog.setCancelable(false);
    //        mdialog.show();
    //
    //
    //        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
    //        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
    //            @Override
    //            public void onComplete(@NonNull Task<AuthResult> task) {
    //                if (task.isSuccessful()) {
    //
    //                    // Sign in success, update UI with the signed-in user's information
    //                    boolean newuser = task.getResult().getAdditionalUserInfo().isNewUser();
    //                    FirebaseUser user = mAuth.getCurrentUser();
    //                    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(AuthScreen.this);
    //                    SharedPreferences sharedPreferences = getSharedPreferences(account.getId(), MODE_PRIVATE);
    //                    String gender = sharedPreferences.getString("gender", "male");
    //                    if (newuser) {
    //                        mdialog.dismiss();
    //
    //                        HashMap<String, String> usermap = new HashMap<>();
    //                        usermap.put("userid", mAuth.getCurrentUser().getUid());
    //                        usermap.put("name", user.getDisplayName());
    //                        usermap.put("image", user.getPhotoUrl().toString());
    //                        usermap.put("status", "Hey there i am using Zombie chat");
    //                        usermap.put("sex", gender);
    //
    //
    //                        // create a new account for first time user
    //                        db.collection("users").document(mAuth.getCurrentUser().getUid())
    //
    //                                .set(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
    //                                    @Override
    //                                    public void onComplete(@NonNull Task<Void> task) {
    //                                        Toast.makeText(AuthScreen.this, "Setup your Account...", Toast.LENGTH_LONG).show();
    //                                        Intent mainIntent = new Intent(AuthScreen.this, MainActivity.class);
    //                                        startActivity(mainIntent);
    //                                        finish();
    //                                    }
    //                                });
    //
    //
    //                    } else {
    //                        mdialog.dismiss();
    //
    //                        Intent mainIntent = new Intent(AuthScreen.this, MainActivity.class);
    //                        startActivity(mainIntent);
    //                        finish();
    //                    }
    //
    //
    //                } else {
    //
    //                    mdialog.dismiss();
    //                    // If sign in fails, display a message to the user.
    //
    //                }
    //
    //                // ...
    //            }
    //        }).addOnFailureListener(new OnFailureListener() {
    //            @Override
    //            public void onFailure(@NonNull Exception e) {
    //                Toast.makeText(AuthScreen.this, "Authentication Failed : " + e.getMessage().trim(), Toast.LENGTH_SHORT).show();
    //            }
    //        });
    //    }


    companion object {
        private const val TAG = "Signin"
        private const val RC_SIGN_IN = 123
    }
}
