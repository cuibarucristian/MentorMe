package com.example.cristian.mentorme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristian.mentorme.JavaClasses.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateProfile extends AppCompatActivity
{

    private static final int GALLERY_PIC = 1;

    //ROLES
   private Button mPickYourRole;
   private TextView myRole;

   //Strings
   private String role = "";
   private String imageUri = "";

   //EditTexts
    private EditText mFirstName;
    private EditText mSecondName;
    private EditText mFieldOfStudy;
    private EditText mSemester;
    private EditText mBio;

    //Other
    private CircleImageView mCircleImageView;

    //Button
    private Button mDone;
    private ImageView changeImage;

   //Firebase
    private DatabaseReference mUsers;
    private FirebaseUser mCurrentUser;
    private StorageReference mImageStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        //button
        mDone = (Button) findViewById(R.id.create_profile_save);

        //firebase
        mUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mImageStorage = FirebaseStorage.getInstance().getReference();

        //change role
        myRole = (TextView) findViewById(R.id.create_profile_role);
        mPickYourRole = (Button) findViewById(R.id.create_profile_select_role);
        mPickYourRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getRoleRaw();
            }
        });

        //View init
        mCircleImageView = (CircleImageView) findViewById(R.id.create_profile_image);
        mFirstName = (EditText) findViewById(R.id.create_profile_first_name);
        mSecondName = (EditText) findViewById(R.id.create_profile_second_name);
        mFieldOfStudy = (EditText) findViewById(R.id.create_profile_spec);
        mSemester = (EditText) findViewById(R.id.create_profile_semester);
        mBio = (EditText) findViewById(R.id.create_profile_bio);


        changeImage = (ImageView)findViewById(R.id.create_profile_image);
        changeImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                changePicture();

            }
        });



    }


    private void changePicture()
    {


        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"),GALLERY_PIC);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PIC && resultCode == RESULT_OK)
        {
            Uri imageURI = data.getData();
            CropImage.activity(imageURI).setAspectRatio(1,1).start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            //after it crops the image its store is it in this result var
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                setImageUriString(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }

    private void setImageUriString(Uri resultUri)
    {

        final StorageReference filePath = mImageStorage.child("profile_images").child(mCurrentUser.getUid() + ".jpeg");
        filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(getApplicationContext(), "Image saved into database.", Toast.LENGTH_SHORT).show();

                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        /*mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                        mUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid()).child("Image");
                        String image_url = uri.toString();
                        mUsers.setValue(image_url);*/

                        String image_url = uri.toString();
                        setImageUri(image_url);
                        changePictureCreateProfile(image_url);


                    }
                });
            }
        });

    }

    private void changePictureCreateProfile(String image_url)
    {
        Picasso.with(this).load(getImageUri()).into(mCircleImageView);
    }

    private void getRoleRaw()
    {

        final String[] colors = {"Mentor", "Mentee", "I don't know yet"};
        final String temp = "";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a role:");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                myRole.setText(colors[which]);
                setRole(colors[which]);
            }
        });
        builder.show();

    }
    private void setRole(String role)
    {
        this.role = role;
    }
    private String getRoleString()
    {
        return role;
    }

    public String getImageUri()
    {
        return imageUri;
    }

    public void setImageUri(String imageUri)
    {
        this.imageUri = imageUri;
    }


    @Override
    protected void onStart()
    {
        super.onStart();

        mDone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

                String biography = mBio.getText().toString();
                String first_name = mFirstName.getText().toString();
                String image = getImageUri();
                String second_Name = mSecondName.getText().toString();
                int semester = Integer.parseInt(mSemester.getText().toString());
                String specialization = mFieldOfStudy.getText().toString();
                String status = getRoleString();
                User user =  new User(biography, first_name, image, second_Name, semester, specialization, status);

                mUsers.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(), "Your information is updated.", Toast.LENGTH_SHORT).show();
                            goToMainActivity();
                        }
                    }
                });
            }
        });

    }

    private void goToMainActivity()
    {
        Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
        goToMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToMainActivity);
        finish();
    }

}


