package com.example.zombiechat.src.account.view

import android.app.ProgressDialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.zombiechat.R
import com.example.zombiechat.src.account.data.repo.AccountRepo
import com.example.zombiechat.src.account.viewModel.UserViewModel
import com.squareup.picasso.Picasso


import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import java.util.Locale

class AccountSetting : AppCompatActivity() {
    private var muserimage: CircleImageView? = null
    private var musername: TextView? = null
    private var muserstatus: TextView? = null
    private var musersex: TextView? = null

    private var medituserimage: ImageView? = null
    private var meditusername: ImageView? = null
    private var medituserstatus: ImageView? = null
    private var meditusersex: ImageView? = null

    private var mToolbar: Toolbar? = null
    private var mProgress: ProgressDialog? = null


    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting)


        //toolbar
        mToolbar = findViewById(R.id.account_setting_appbar)
        setSupportActionBar(mToolbar)
        supportActionBar!!.title = "Account Settings"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        muserimage = findViewById(R.id.user_image)
        musername = findViewById(R.id.user_name)
        muserstatus = findViewById(R.id.user_status)
        musersex = findViewById(R.id.user_sex)

        //btn to edit name, image, status
        medituserimage = findViewById(R.id.edit_image_btn)
        meditusername = findViewById(R.id.edit_name_btn)
        medituserstatus = findViewById(R.id.edit_status_btn)
        meditusersex = findViewById(R.id.edit_sex_btn)

        //proress dialog
        mProgress = ProgressDialog(this)


        userViewModel = UserViewModel(AccountRepo())
        userViewModel.userLiveData.observe(this) {
            musername?.text = it?.name
            muserstatus?.text = it?.status
            musersex?.text = it?.gender
            Picasso.Builder(this).build().load(it?.image).error(R.drawable.default_user)
                .placeholder(R.drawable.default_user).into(muserimage)


        }

        lifecycleScope.launch { userViewModel.getUser() }




        medituserimage?.setOnClickListener(View.OnClickListener {
            val imageIntent = Intent()
            imageIntent.setType("image/*")
            imageIntent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(imageIntent, REQUEST_CODE)
        })

        meditusersex?.setOnClickListener(View.OnClickListener {
            val inflater = layoutInflater
            val dialoglayout = inflater.inflate(R.layout.alert_name_status_editor, null)
            val builder = AlertDialog.Builder(this@AccountSetting)
            builder.setView(dialoglayout)
            builder.setTitle("Edit Sex")
            builder.setPositiveButton("Ok") { dialog, which ->
                val sexEditText = dialoglayout.findViewById<EditText>(R.id.input_name_status_text)
                val sex = sexEditText.text.toString().lowercase(Locale.getDefault())


                // updating user gender
                if (sexEditText.text.toString() == "") {
                    Toast.makeText(this@AccountSetting, "empty text", Toast.LENGTH_SHORT).show()
                } else if (sex.matches("male".toRegex()) || sex.matches("female".toRegex())) {


                    userViewModel.updateGender(sexEditText.text.toString())

                } else {
                    Toast.makeText(this@AccountSetting, "invalid values", Toast.LENGTH_SHORT).show()
                }
            }.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
            builder.show()
        })
        meditusername?.setOnClickListener(View.OnClickListener {
            val inflater = layoutInflater
            val dialoglayout = inflater.inflate(R.layout.alert_name_status_editor, null)
            val builder = AlertDialog.Builder(this@AccountSetting)
            builder.setView(dialoglayout)
            builder.setTitle("Edit Name")
            builder.setPositiveButton("Ok") { dialog, which ->
                val nameEditText = dialoglayout.findViewById<EditText>(R.id.input_name_status_text)
                if (nameEditText.text.toString() == "") {
                    Toast.makeText(this@AccountSetting, "no name", Toast.LENGTH_SHORT).show()
                } else {
                    userViewModel.updateName(nameEditText.text.toString())
                }
            }.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
            builder.show()
        })


        medituserstatus?.setOnClickListener(View.OnClickListener {
            val inflater = layoutInflater
            val dialoglayout = inflater.inflate(R.layout.alert_name_status_editor, null)
            val builder = AlertDialog.Builder(this@AccountSetting)
            builder.setView(dialoglayout)
            builder.setTitle("Edit Status")
            builder.setPositiveButton("Ok") { dialog, which ->
                val statusEditText =
                    dialoglayout.findViewById<EditText>(R.id.input_name_status_text)
                if (statusEditText.text.toString() == "") {
                    Toast.makeText(this@AccountSetting, "no status", Toast.LENGTH_SHORT).show()
                } else {
                    userViewModel.updateStatus(statusEditText.text.toString())
                }
            }.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
            builder.show()
        })
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            val resultUri = data!!.data
            uploadImage(resultUri!!)
        }
    }

    private fun uploadImage(resultUri: Uri) {
        mProgress!!.setTitle("Uploading Image")
        mProgress!!.setMessage("Please wait while we upload the image.")
        mProgress!!.setCanceledOnTouchOutside(false)


        lifecycleScope.launch {
            try {
                mProgress!!.show();
                userViewModel.updateImage(resultUri.toString())
                mProgress!!.dismiss();
            } catch (e: Exception) {
                mProgress!!.dismiss();
                Toast.makeText(this@AccountSetting, "Error uploading image", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }


    companion object {
        private const val REQUEST_CODE = 12
        private const val TAG = "AccountSettings"
    }
}
