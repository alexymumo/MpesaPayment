package com.alexmumo.xpressway.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.alexmumo.xpressway.R;
import com.alexmumo.xpressway.models.LogBook;
import com.alexmumo.xpressway.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class LogbookActivity extends AppCompatActivity {

    private Uri filepath;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ImageView logBookImage, licenceImage;
    private Button logbookButton, numberPlateButton;
    private static final int PICK_IMAGE_REQUEST = 224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logbook);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
        logbookButton = findViewById(R.id.logBookBtn);
        numberPlateButton = findViewById(R.id.drivingLicenceBtn);
        logBookImage = findViewById(R.id.iv_logbook);

        // upload image to firebase storage

        logbookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filepath != null) {
                    uploadLogbook(filepath);
                } else {
                    Toast.makeText(LogbookActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                }
            }
        });
        logBookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
                // upload image from gallery
            }
        });

        numberPlateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            filepath = data.getData();
            logBookImage.setImageURI(filepath);
        }
    }
    private void uploadLogbook(Uri uri) {
        StorageReference fileRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        LogBook logBook = new LogBook(uri.toString());
                        String uploadId = databaseReference.push().getKey();
                        databaseReference.child(uploadId).setValue(uri.toString());
                        Toast.makeText(LogbookActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LogbookActivity.this, "uploading failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getFileExtension(Uri muri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(muri));
    }
}