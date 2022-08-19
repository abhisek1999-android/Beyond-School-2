package com.maths.beyond_school_280720220930;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.databinding.ActivityKidsInfoBinding;
import com.maths.beyond_school_280720220930.dialogs.HintDialog;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class KidsInfoActivity extends AppCompatActivity {

    FirebaseFirestore kidsDb = FirebaseFirestore.getInstance();
    Uri imageURI;
    private int GALLERY_REQUEST_CODE = 200;
    private StorageReference mStorageReference;
    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    private ActivityKidsInfoBinding binding;
    private String type = "next";
    String[] array;
    ArrayAdapter adapter;
   private CustomProgressDialogue customProgressDialogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityKidsInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mStorageReference=FirebaseStorage.getInstance().getReference();

        customProgressDialogue=new CustomProgressDialogue(KidsInfoActivity.this);

        binding.toolBar.titleText.setText("Kid's Info");


        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }





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

        binding.getImages.setOnClickListener(v -> {
            selectImage();
        });

        binding.updateButton.setOnClickListener(v -> {
            uploadImage();
            customProgressDialogue.show();
        });

        binding.deleteProfile.setOnClickListener(v->{

            displayHintDialog();
        });


        binding.toolBar.imageViewBack.setOnClickListener(v->{onBackPressed();});

        setUpTextLayoutGrade();


        try {
            type = getIntent().getStringExtra("type");
        } catch (Exception e) {
        }





        if (type.equals("update")) {

            binding.deleteProfile.setVisibility(View.VISIBLE);
            String grade = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_grade)).split(" ")[1].trim();
            //    binding.textInputLayoutGrade.getEditText().setText(array[1]);
            binding.kidsNameTextView.setText(PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_name)));
            binding.kidsAgeTextView.setText(PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_dob)));
            UtilityFunctions.loadImage(PrefConfig.readIdInPref(getApplicationContext(),getResources().getString(R.string.kids_profile_url)), binding.kidsProfileImage);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                binding.kidsGrade.setText(binding.kidsGrade.getAdapter().getItem(Integer.parseInt(grade) - 1).toString(), false);
            }
            binding.toolBar.titleText.setText("Kid's Profile");
            binding.nextButton.setVisibility(View.GONE);
            binding.updateButton.setVisibility(View.VISIBLE);
        }
        else{
            binding.toolBar.imageViewBack.setVisibility(View.GONE);
        }





    }

    private void displayHintDialog() {

        HintDialog hintDialog = new HintDialog(KidsInfoActivity.this);
        hintDialog.setCancelable(false);
        hintDialog.setAlertTitle("ALERT !");
        hintDialog.setAlertDesciption("Are you sure you want to delete this profile?");

        hintDialog.actionButton();
        hintDialog.setOnActionListener(viewId->{

            switch (viewId.getId()){

                case R.id.closeButton:
                    hintDialog.dismiss();
                    break;
                case R.id.buttonAction:

                    hintDialog.dismiss();
                    deleteProfile();
                    break;
            }
        });

        hintDialog.show();

    }

    private void deleteProfile() {


        customProgressDialogue.show();

        kidsDb.collection("users").document(mCurrentUser.getUid()).collection("kids").
                document(PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_id)))
                .update("status","deleted").addOnSuccessListener(unused -> {

                    UtilityFunctions.saveDataLocally(getApplicationContext(),"","","","","");
                    startActivity(new Intent(getApplicationContext(),SplashScreen.class));
                    finish();
                    customProgressDialogue.dismiss();
                }).addOnFailureListener(e->{

                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    customProgressDialogue.dismiss();
                });

    }

    private void setUpTextLayoutGrade() {
        array = getResources().getStringArray(R.array.grades);
        adapter = new ArrayAdapter(this, R.layout.list_item, array);
        AutoCompleteTextView editText = Objects.requireNonNull((AutoCompleteTextView) binding.textInputLayoutGrade.getEditText());
        editText.setAdapter(adapter);

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
            } catch (Exception e) {

                Log.i("ImageException",e.getMessage());
            }

        }
    }


    private void uploadImage() {

        String uuid = UUID.randomUUID().toString();
        if (!binding.kidsNameTextView.getText().toString().equals("") && !binding.kidsAgeTextView.getText().toString().equals("")) {

            if (imageURI != null && mAuth != null) {
                StorageReference storageReference = mStorageReference.child("kids_profile_image/" + mCurrentUser.getUid() + "/pic_" + String.valueOf(System.currentTimeMillis()) + uuid + ".jpg");

                try {

                    storageReference.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    final String downloadUrl = task.getResult().toString();

                                    if (task.isSuccessful()) {
                                        if (type.equals("next"))
                                            saveKidsData(downloadUrl);
                                        else
                                            updateKidsData(downloadUrl);
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
                if (mAuth != null) {

                    if (type.equals("next"))
                        saveKidsData("default");
                    else
                        updateKidsData(PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_profile_url)));
                }

            }
        } else {
            Toast.makeText(this, "Name and Age is required!", Toast.LENGTH_LONG).show();
        }

    }

    public void goButtonClicked(View view) {
        uploadImage();
        customProgressDialogue.show();
    }


    private void updateKidsData(String imageUrl) {

        if (mAuth != null) {


            Log.i("data", binding.kidsNameTextView.getText().toString() + "," + binding.kidsAgeTextView.getText().toString() + "," +
                    Objects.requireNonNull(binding.textInputLayoutGrade.getEditText()).getText().toString() + "," + imageUrl);

            kidsDb.collection("users").document(mCurrentUser.getUid()).collection("kids").
                    document(PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_id))).update(
                            "name", binding.kidsNameTextView.getText().toString(),
                            "age", binding.kidsAgeTextView.getText().toString(),
                            "grade", Objects.requireNonNull(binding.textInputLayoutGrade.getEditText()).getText().toString(),
                            "profile_url", imageUrl
                    ).addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                        customProgressDialogue.dismiss();
                        UtilityFunctions.saveDataLocally(getApplicationContext(), Objects.requireNonNull(binding.textInputLayoutGrade.getEditText()).getText().toString(), binding.kidsNameTextView.getText().toString(),
                                binding.kidsAgeTextView.getText().toString(), imageUrl, PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_id)));
                    }).addOnFailureListener(e -> {
                        customProgressDialogue.dismiss();
                        Toast.makeText(this, "Failed to update" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }

    }

    private void saveKidsData(String imageUrl) {
        if (mAuth != null) {

            String uuid = kidsDb.collection("users").document(mCurrentUser.getUid()).collection("kids").document().getId();
            Map<String, Object> kidsData = new HashMap<>();
            kidsData.put("name", binding.kidsNameTextView.getText().toString());
            kidsData.put("kids_id", uuid);
            kidsData.put("profile_url", imageUrl);
            kidsData.put("parent_id", mCurrentUser.getUid());
            kidsData.put("age", binding.kidsAgeTextView.getText().toString());
            kidsData.put("status","active");
            kidsData.put("grade", Objects.requireNonNull(binding.textInputLayoutGrade.getEditText()).getText().toString());
            // Add a new document with a generated ID
            kidsDb.collection("users").document(mCurrentUser.getUid()).collection("kids").document(uuid)
                    .set(kidsData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Log.d(TAG, "DocumentSnapshot successfully written!");


                            UtilityFunctions.saveDataLocally(getApplicationContext(), Objects.requireNonNull(binding.textInputLayoutGrade.getEditText()).getText().toString(), binding.kidsNameTextView.getText().toString(),
                                    binding.kidsAgeTextView.getText().toString(), imageUrl, uuid);
                            customProgressDialogue.dismiss();
                            Intent intent = new Intent(getApplicationContext(), Select_Sub_Activity.class);
                            intent.putExtra("name", binding.kidsNameTextView.getText().toString());
                            intent.putExtra("image", imageUrl);
                            intent.putExtra("age", binding.kidsAgeTextView.getText().toString());
                            intent.putExtra("grade", Objects.requireNonNull(binding.textInputLayoutGrade.getEditText()).getText().toString());
                            startActivity(intent);
                            //    Toast.makeText(getApplicationContext(),"added",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //  Log.w(TAG, "Error writing document", e);
                            Toast.makeText(KidsInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            customProgressDialogue.dismiss();
                        }
                    });
        }

    }


}