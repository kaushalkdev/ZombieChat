package com.example.zombiechat.account.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.zombiechat.MainActivity;
import com.example.zombiechat.R;
import com.example.zombiechat.account.viewModel.AuthVM;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AuthScreen extends AppCompatActivity {

    private SignInButton msignin;
    private static final String TAG = "Signin";
    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressDialog mdialog;
    private AuthVM authVM;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        authVM = new ViewModelProvider(this).get(AuthVM.class);


        //progress Dialog
        mdialog = new ProgressDialog(this);


        //google sign in
        msignin = findViewById(R.id.signinBtn);


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        msignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authVM.signIn();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if ( authVM.getCurrentUser() != null) {
            Intent mainIntent = new Intent(AuthScreen.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        mdialog.setTitle("Signing In");
        mdialog.setMessage("Please wait while we log you in to your account... ");
        mdialog.setCancelable(false);
        mdialog.show();


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    // Sign in success, update UI with the signed-in user's information
                    boolean newuser = task.getResult().getAdditionalUserInfo().isNewUser();
                    FirebaseUser user = mAuth.getCurrentUser();
                    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(AuthScreen.this);
                    SharedPreferences sharedPreferences = getSharedPreferences(account.getId(), MODE_PRIVATE);
                    String gender = sharedPreferences.getString("gender", "male");
                    if (newuser) {
                        mdialog.dismiss();

                        HashMap<String, String> usermap = new HashMap<>();
                        usermap.put("userid", mAuth.getCurrentUser().getUid());
                        usermap.put("name", user.getDisplayName());
                        usermap.put("image", user.getPhotoUrl().toString());
                        usermap.put("status", "Hey there i am using Zombie chat");
                        usermap.put("sex", gender);


                        // create a new account for first time user
                        db.collection("users").document(mAuth.getCurrentUser().getUid())

                                .set(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(AuthScreen.this, "Setup your Account...", Toast.LENGTH_LONG).show();
                                        Intent mainIntent = new Intent(AuthScreen.this, MainActivity.class);
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                });


                    } else {
                        mdialog.dismiss();

                        Intent mainIntent = new Intent(AuthScreen.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }


                } else {

                    mdialog.hide();
                    // If sign in fails, display a message to the user.

                }

                // ...
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AuthScreen.this, "Authentication Failed : " + e.getMessage().trim(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
