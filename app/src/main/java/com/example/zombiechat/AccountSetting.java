package com.example.zombiechat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSetting extends AppCompatActivity {

    private static final int REQUEST_CODE = 12;
    private static final String TAG = "AccountSettings";
    private CircleImageView muserimage;
    private TextView musername, muserstatus, musersex;

    private ImageView medituserimage, meditusername, medituserstatus, meditusersex;

    private Toolbar mToolbar;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        //toolbar
        mToolbar = findViewById(R.id.account_setting_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        muserimage = findViewById(R.id.user_image);
        musername = findViewById(R.id.user_name);
        muserstatus = findViewById(R.id.user_status);
        musersex = findViewById(R.id.user_sex);

        //btn to edit name, image, status
        medituserimage = findViewById(R.id.edit_image_btn);
        meditusername = findViewById(R.id.edit_name_btn);
        medituserstatus = findViewById(R.id.edit_status_btn);
        meditusersex = findViewById(R.id.edit_sex_btn);

        //proress dialog
        mProgress = new ProgressDialog(this);

        //cloud firestore
        db = FirebaseFirestore.getInstance();

        //firebase auth
        mAuth = FirebaseAuth.getInstance();

        //firebase reference
        mStorageRef = FirebaseStorage.getInstance().getReference();

        medituserimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageIntent = new Intent();
                imageIntent.setType("image/*");
                imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(imageIntent, REQUEST_CODE);

            }
        });

        meditusersex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = getLayoutInflater();
                final View dialoglayout = inflater.inflate(R.layout.alert_name_status_editor, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(AccountSetting.this);
                builder.setView(dialoglayout);
                builder.setTitle("Edit Sex");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText  sexEditText = dialoglayout.findViewById(R.id.input_name_status_text);

                        String sex = sexEditText.getText().toString().toLowerCase();

                        if (sexEditText.getText().toString().equals("")) {
                            Toast.makeText(AccountSetting.this, "empty text", Toast.LENGTH_SHORT).show();

                        }else if(sex.matches("male")|| sex.matches("female"))
                            {
                                DocumentReference documentRef = db.collection("users").document(mAuth.getCurrentUser().getUid());


                                documentRef
                                        .update("sex", sexEditText.getText().toString().toLowerCase())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error updating document", e);
                                            }
                                        });

                            }


                        else {

                            Toast.makeText(AccountSetting.this, "invalid values", Toast.LENGTH_SHORT).show();

                        }

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        meditusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = getLayoutInflater();
                final View dialoglayout = inflater.inflate(R.layout.alert_name_status_editor, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(AccountSetting.this);
                builder.setView(dialoglayout);
                builder.setTitle("Edit Name");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText nameEditText = dialoglayout.findViewById(R.id.input_name_status_text);

                        if (nameEditText.getText().toString().equals("")) {
                            Toast.makeText(AccountSetting.this, "no name", Toast.LENGTH_SHORT).show();

                        } else {
                            DocumentReference documentRef = db.collection("users").document(mAuth.getCurrentUser().getUid());


                            documentRef
                                    .update("name", nameEditText.getText().toString())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error updating document", e);
                                        }
                                    });
                        }

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });


        medituserstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                final View dialoglayout = inflater.inflate(R.layout.alert_name_status_editor, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(AccountSetting.this);
                builder.setView(dialoglayout);
                builder.setTitle("Edit Status");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText statusEditText = dialoglayout.findViewById(R.id.input_name_status_text);

                        if (statusEditText.getText().toString().equals("")) {
                            Toast.makeText(AccountSetting.this, "no status", Toast.LENGTH_SHORT).show();

                        } else {

                            //updating status
                            DocumentReference documentRef = db.collection("users").document(mAuth.getCurrentUser().getUid());


                            documentRef
                                    .update("status", statusEditText.getText().toString())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error updating document", e);
                                        }
                                    });
                        }

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


        //method 1
        DocumentReference docRef = db.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(AccountSetting.this, "error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (documentSnapshot.exists()) {
                    musername.setText(documentSnapshot.get("name").toString());
                    muserstatus.setText(documentSnapshot.get("status").toString());
                    musersex.setText(documentSnapshot.get("sex").toString());
                    Picasso.with(AccountSetting.this)
                            .load(documentSnapshot.get("image").toString())
                            .error(R.drawable.default_user)
                            .placeholder(R.drawable.default_user)
                            .into(muserimage);

                }
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            // start cropping activity for pre-acquired image saved on the device
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 500)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                uploadImage(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void uploadImage(Uri resultUri) {
        mProgress.setTitle("Uploading Image");
        mProgress.setMessage("Please wait while we upload the image.");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
        final String uid = mAuth.getCurrentUser().getUid();
        final StorageReference filePath = mStorageRef.child("profile_pictures").child(uid + ".jpg");


        //add file on Firebase and got Download Link
        filePath.putFile(resultUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    mProgress.dismiss();
                    Uri downUri = task.getResult();
                    updateDatabase(downUri);
                }
            }
        });


    }

    private void updateDatabase(final Uri downUri) {


        Map<String, Object> newmap = new HashMap<>();
        newmap.put("image", downUri.toString());
        DocumentReference documentRef = db.collection("users").document(mAuth.getCurrentUser().getUid());


        documentRef
                .update(newmap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Picasso.with(AccountSetting.this)
                                .load(downUri)
                                .error(R.drawable.default_user)
                                .placeholder(R.drawable.default_user)
                                .into(muserimage);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });


    }
}
