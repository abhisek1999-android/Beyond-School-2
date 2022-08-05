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


    CircleImageView profileImageView;
    EditText kidsName;

    TextView kidsAge;
    ImageButton getImages;
    FirebaseFirestore kidsDb = FirebaseFirestore.getInstance();
    Uri imageURI;
    private int GALLERY_REQUEST_CODE = 200;
    private StorageReference mStorageReference;
    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    TextView titleText;
    ImageView back;
    TextInputLayout textInputLayoutGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kids_info);

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        titleText = findViewById(R.id.titleText);
        titleText.setText("Kids Info");

        back = findViewById(R.id.imageView4);
        back.setVisibility(View.GONE);

        profileImageView = findViewById(R.id.kidsProfileImage);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        kidsName = findViewById(R.id.kidsNameTextView);
        kidsAge = findViewById(R.id.kidsAgeTextView);
        getImages = findViewById(R.id.getImages);
        textInputLayoutGrade = findViewById(R.id.textInputLayoutGrade);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();


        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        // now define the properties of the
        // materialDateBuilder that is title text as SELECT A DATE
        materialDateBuilder.setTitleText("SELECT A DATE");

        // now create the instance of the material date
        // picker
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        // handle select date button which opens the
        // material design date picker
        kidsAge.setOnClickListener(
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
            kidsAge.setText(formattedDate);


        });

        getImages.setOnClickListener(v -> {
            selectImage();
        });

        //  showSetSpeedDialDialog(0);
        setUpTextLayoutGrade();
    }

    private void setUpTextLayoutGrade() {
        String[] array = getResources().getStringArray(R.array.grades);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, array);
        AutoCompleteTextView editText = Objects.requireNonNull((AutoCompleteTextView) textInputLayoutGrade.getEditText());
        editText.setAdapter(adapter);
    }


    private void showSetSpeedDialDialog(int i) {


        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        // now define the properties of the
        // materialDateBuilder that is title text as SELECT A DATE
        materialDateBuilder.setTitleText("SELECT A DATE");

        // now create the instance of the material date
        // picker
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        final AlertDialog.Builder alert = new AlertDialog.Builder(KidsInfoActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.result_display, null);


        TextView title = mView.findViewById(R.id.openDialog);


        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        try {
            alertDialog.show();
        } catch (Exception e) {

        }


        title.setOnClickListener(v -> {
            materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(Long.parseLong(selection.toString()));
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = format.format(calendar.getTime());
            //   kidsAge.setText(formattedDate);
            title.setText(formattedDate);


        });

    }


    private void selectImage() {

        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);

//        final CharSequence[] o = {"Take Photo", "Choose from Gallery",
//                "Cancel"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(KidsInfoActivity.this);
//        builder.setTitle("Add Photo!");
//        builder.setItems(o, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (o[item].equals("Take Photo")) {
//
//
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    // Declare mUri as globel varibale in class
//                    imageURI = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "pic_"+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
//                    startActivityForResult(intent, 12);
//                }
//                else if (o[item].equals("Choose from Gallery")) {
//                    Intent intent = new Intent(
//                            Intent.ACTION_PICK,
//                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);startActivityForResult(intent, 22);
//
//                }
//                else if (o[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//         if (resultCode == RESULT_OK) {
//            if (requestCode == 12) {
//
//                if (imageURI!=null){
//
//                    //  Bitmap bmp= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageURI));
//                    //  contact_bitmap=bmp;
//                    profileImageView.setImageURI(imageURI);
//                }
//
//            }

        if (requestCode == GALLERY_REQUEST_CODE) {

            Uri selectedImage = data.getData();
            imageURI = selectedImage;
            // Bitmap bmp=BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
            // contact_bitmap=bmp;
            // userImage.setImageBitmap(bmp);
            profileImageView.setImageURI(selectedImage);
        }


    }

    public void goButtonClicked(View view) {

        String uuid= UUID.randomUUID().toString();
        if (!kidsName.getText().toString().equals("") && !kidsAge.getText().toString().equals("")) {

            if (imageURI != null && mAuth != null) {
                StorageReference storageReference = mStorageReference.child("kids_profile_image/" + mCurrentUser.getUid() + "/pic_" + String.valueOf(System.currentTimeMillis())+uuid + ".jpg");

                try {

                    storageReference.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    final String downloadUrl = task.getResult().toString();

                                    if (task.isSuccessful()) {

                                        saveKidsData(downloadUrl);
                                        Log.i("Image Uploaded", downloadUrl);

                                    } else {
                                        //show exception
                                        Log.i("Error Uploading Image", "Error");
                                    }
                                }

                            });
                        }
                    });


                } catch (Exception e) {


                }
            } else {
                saveKidsData("default");
            }
        } else {
            Toast.makeText(this, "Name and Age is required!", Toast.LENGTH_LONG).show();
        }

    }

    private void saveKidsData(String imageUrl) {
        if (mAuth != null) {

            String uuid = kidsDb.collection("users").document(mCurrentUser.getUid()).collection("kids").document().getId();
            Map<String, Object> kidsData = new HashMap<>();
            kidsData.put("name", kidsName.getText().toString());
            kidsData.put("kids_id", uuid);
            kidsData.put("profile_url", imageUrl);
            kidsData.put("parent_id", mCurrentUser.getUid());
            kidsData.put("age", kidsAge.getText().toString());
            kidsData.put("grade", Objects.requireNonNull(textInputLayoutGrade.getEditText()).getText().toString());
            // Add a new document with a generated ID
            kidsDb.collection("users").document(mCurrentUser.getUid()).collection("kids").document(uuid)
                    .set(kidsData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Log.d(TAG, "DocumentSnapshot successfully written!");
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            //    Toast.makeText(getApplicationContext(),"added",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //  Log.w(TAG, "Error writing document", e);
                            Toast.makeText(KidsInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
}