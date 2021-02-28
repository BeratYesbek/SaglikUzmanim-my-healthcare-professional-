package com.saglikuzmanimm.saglikuzmanim.Activity.ActivityUser;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.UserManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.User;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase.FireBaseUserDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;
import com.saglikuzmanimm.saglikuzmanim.R;
import com.squareup.picasso.Picasso;

public class ProfileEditActivityForUser extends AppCompatActivity {


    private EditText editTextUpdateFirstName;
    private EditText editTextUpdateLastName;

    private ProgressBar progressBar_edit_profile;


    private ImageView ImageView_profile;
    private Bitmap SelectData;

    private Uri imageData = null;
    private Uri myUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit_for_user);


        ImageView_profile = findViewById(R.id.imageView_edit_user);
        editTextUpdateFirstName = findViewById(R.id.editText_FirstName_edit);
        editTextUpdateLastName = findViewById(R.id.editText_lastName_edit);
        progressBar_edit_profile = findViewById(R.id.progressBar_edit_profile);
        progressBar_edit_profile.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        try {
            String firstName = intent.getStringExtra("firstName");
            String lastName = intent.getStringExtra("lastName");
            String uri = intent.getStringExtra(("uri"));

            if (uri != null) {
                myUri = Uri.parse(uri);
                Picasso.get().load(myUri).into(ImageView_profile);
            }

            editTextUpdateFirstName.setText(firstName);
            editTextUpdateLastName.setText(lastName);
        } catch (Exception exception) {
            System.out.println(exception.toString());
        }

    }

    public void btn_update_profile(View view) {

        String _firstName = editTextUpdateFirstName.getText().toString();
        String _lastName = editTextUpdateLastName.getText().toString();


        if (_firstName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Adınızı boş bırakılamaz", Toast.LENGTH_LONG).show();
        } else if (_lastName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Soyadınız boş bırakılamaz", Toast.LENGTH_LONG).show();
        } else {
            progressBar_edit_profile.setVisibility(View.VISIBLE);


            UserManager userManager = new UserManager(new FireBaseUserDal());
            userManager.updateUserProfile(new User(_firstName, _lastName, imageData), new IResult() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "Başarı ile güncellendi", Toast.LENGTH_LONG).show();
                    progressBar_edit_profile.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailed(Exception exception) {
                    Toast.makeText(getApplicationContext(), "Güncelleme sırasında bir hata meydana geldi.", Toast.LENGTH_LONG).show();
                    progressBar_edit_profile.setVisibility(View.INVISIBLE);
                }
            });

        }


    }


    public void SelectImage(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            Intent intenToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intenToGallery, 2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery, 2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageData = data.getData();

            try {

                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), imageData);
                    SelectData = ImageDecoder.decodeBitmap(source);
                    ImageView_profile.setImageBitmap(SelectData);

                } else {
                    SelectData = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData);
                    ImageView_profile.setImageBitmap(SelectData);

                }
            } catch (Exception e) {

            }
        }

        super.onActivityResult(requestCode, resultCode, data);


    }


}