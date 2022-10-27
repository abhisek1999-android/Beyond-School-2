package com.maths.beyond_school_280720220930;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.databinding.ActivityKidsInfoBinding;
import com.maths.beyond_school_280720220930.databinding.ActivityNameAgeBinding;
import com.maths.beyond_school_280720220930.extras.CustomProgressDialogue;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;

public class NameAgeActivity extends AppCompatActivity {
    private ActivityNameAgeBinding binding;
    private FirebaseFirestore kidsDb = FirebaseFirestore.getInstance();
    private Uri imageURI;
    private int GALLERY_REQUEST_CODE = 200;
    private StorageReference mStorageReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String type = "next";
    private String[] array;
    private ArrayAdapter adapter;
    private CustomProgressDialogue customProgressDialogue;
    private String gradeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNameAgeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        gradeIntent=getIntent().getStringExtra("grade");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mStorageReference= FirebaseStorage.getInstance().getReference();

        customProgressDialogue=new CustomProgressDialogue(NameAgeActivity.this);

        binding.toolBar.titleText.setText("Kid's Info");


        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        setDate();

        binding.getImages.setOnClickListener(v -> {
            selectImage();
        });

        binding.kidsProfileImage.setOnClickListener(v->{
            selectImage();
        });

        binding.next.setOnClickListener(v->{
            uploadImage();
            customProgressDialogue.show();
        });

        binding.skip.setOnClickListener(v->{
            saveKidsData("default");

        });


    }


    private void selectImage() {

        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GALLERY_REQUEST_CODE) {

            try {
                Uri selectedImage = data.getData();
                imageURI = selectedImage;
                binding.kidsProfileImage.setImageURI(selectedImage);
                uploadImage();
            } catch (Exception e) {

                Log.i("ImageException",e.getMessage());
            }

        }
    }


    private void uploadImage() {

        customProgressDialogue.show();
        String uuid = UUID.randomUUID().toString();
        if (!binding.kidsNameTextView.getText().toString().equals("") && !binding.kidsAgeTextView.getText().toString().equals("")) {
                StorageReference storageReference = mStorageReference.child("kids_profile_image/" + mCurrentUser.getUid() + "/pic_" + String.valueOf(System.currentTimeMillis()) + uuid + ".jpg");

                try {

                    storageReference.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @SuppressLint("SuspiciousIndentation")
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    final String downloadUrl = task.getResult().toString();

                                    if (task.isSuccessful()) {
                                        if (type.equals("next")){
                                            saveKidsData(downloadUrl);
                                            UtilityFunctions.loadImage(downloadUrl,binding.kidsProfileImage);
                                        }

                                        else
                                        Log.i("Image Uploaded", downloadUrl);

                                    } else {
                                        //show exception
                                        Log.i("Error Uploading Image", "Error");
                                        customProgressDialogue.dismiss();
                                    }
                                }

                            });
                        }
                    });


                } catch (Exception e) {


                }

        } else {
            customProgressDialogue.dismiss();
            Toast.makeText(this, "Name and DOB is required!", Toast.LENGTH_LONG).show();
        }

    }

    private void setDate() {
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        // now define the properties of the
        // materialDateBuilder that is title text as SELECT A DATE
        materialDateBuilder.setTitleText("SELECT A DATE");

        // now create the instance of the material date
        // picker
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        // handle select date button which opens the
        // material design date picker
        binding.kidsAgeTextView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // getSupportFragmentManager() to
                        // interact with the fragments
                        // associated with the material design
                        // date picker tag is to get any error
                        // in logcat
                        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });

        // now handle the positive button click from the
        // material design date picker
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(Long.parseLong(selection.toString()));
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = format.format(calendar.getTime());
            binding.kidsAgeTextView.setText(formattedDate);


        });

    }


    private void saveKidsData(String imageUrl) {
        if (mAuth != null) {

            String uuid = kidsDb.collection("users").document(mCurrentUser.getUid()).collection("kids").document().getId();
            Map<String, Object> kidsData = new HashMap<>();
            kidsData.put("name", !binding.kidsNameTextView.getText().toString().equals("")?binding.kidsNameTextView.getText().toString():"Kids Name");
            kidsData.put("kids_id", uuid);
            kidsData.put("profile_url", imageUrl);
            kidsData.put("parent_id", mCurrentUser.getUid());
            kidsData.put("age", !binding.kidsAgeTextView.getText().toString().equals("")?binding.kidsAgeTextView.getText().toString():"01/08/2017");
            kidsData.put("status","active");
            kidsData.put("grade", gradeIntent);
            // Add a new document with a generated ID
            kidsDb.collection("users").document(mCurrentUser.getUid()).collection("kids").document(uuid)
                    .set(kidsData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Log.d(TAG, "DocumentSnapshot successfully written!");


                            UtilityFunctions.saveDataLocally(getApplicationContext(), gradeIntent, binding.kidsNameTextView.getText().toString(),
                                    binding.kidsAgeTextView.getText().toString(), imageUrl, uuid);

                            Intent intent = new Intent(getApplicationContext(), TabbedHomePage.class);
                            intent.putExtra("name", binding.kidsNameTextView.getText().toString());
                            intent.putExtra("image", imageUrl);
                            intent.putExtra("age", binding.kidsAgeTextView.getText().toString());
                            intent.putExtra("grade", gradeIntent);
                            startActivity(intent);
                            customProgressDialogue.dismiss();
                            //    Toast.makeText(getApplicationContext(),"added",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //  Log.w(TAG, "Error writing document", e);
                            Toast.makeText(NameAgeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            customProgressDialogue.dismiss();
                        }
                    });
        }

    }
}