package com.example.zombiechat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.zombiechat.account.view.AccountSetting;
import com.example.zombiechat.account.view.SigninActivity;
import com.example.zombiechat.friends.AllusersActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Cloud Firestore";
    private FirebaseAuth mAuth;
    private SectionPageradapter mSectionPagerAdapter;
    private Toolbar mToolbar;
    private ViewPager mviewPager;
    private TabLayout mTablayout;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       //setting tabs
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Zombie Chat");

        mTablayout = findViewById(R.id.main_tabs);
        mviewPager = findViewById(R.id.main_view_pager);

        mSectionPagerAdapter = new SectionPageradapter(getSupportFragmentManager());
        mviewPager.setAdapter(mSectionPagerAdapter);
        mTablayout.setupWithViewPager(mviewPager);



        //need for firebase signout
        mAuth = FirebaseAuth.getInstance();


        //firebase cloud store
        db = FirebaseFirestore.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //google client needed for google signout
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);




    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            sendToSignin();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);


        if (item.getItemId() == R.id.main_logout_btn) {

            mAuth.signOut();
            mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(MainActivity.this, " Logged Out", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            sendToSignin();

        }

        if (item.getItemId() == R.id.main_account_setting) {
             accountSettingsIntent();
        }

        if (item.getItemId() == R.id.all_users) {
             allUserIntent();
        }


        return true;
    }




    //All Used methods


    public void sendToSignin() {
        Intent SigninIntent = new Intent(MainActivity.this, SigninActivity.class);
        startActivity(SigninIntent);
        finish();
    }


    private void accountSettingsIntent() {

        Intent AccountSettingIntent = new Intent(MainActivity.this, AccountSetting.class);
        startActivity(AccountSettingIntent);

    }


    private void allUserIntent() {

        Intent AllusersActivityIntent = new Intent(MainActivity.this, AllusersActivity.class);
        startActivity(AllusersActivityIntent);
    }
}
