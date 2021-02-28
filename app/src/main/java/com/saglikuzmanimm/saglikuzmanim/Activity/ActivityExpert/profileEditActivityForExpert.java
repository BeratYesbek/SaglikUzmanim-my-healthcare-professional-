package com.saglikuzmanimm.saglikuzmanim.Activity.ActivityExpert;

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

import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.ExpertManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Expert;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase.FireBaseExpertDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;
import com.saglikuzmanimm.saglikuzmanim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class profileEditActivityForExpert extends AppCompatActivity {


    private ImageView ImageView_profile;
    private Bitmap SelectData;
    private Uri imageData=null;




    private EditText editText_edit_appointmentPrice_information;
    private EditText editText_about_expert;
    private String _about;
    private String _uriProfile;
    private Float appointmentPrice;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit_for_expert);

        progressBar = findViewById(R.id.progressBar_edit_expert_profile);
        editText_about_expert = findViewById(R.id.editText_update_about_expert);
        editText_edit_appointmentPrice_information = findViewById(R.id.editText_add_appointment_price);
        ImageView_profile = findViewById(R.id.image_expert_profile_for_add);


        progressBar.setVisibility(View.INVISIBLE);


        Intent intent = getIntent();



        _about = intent.getStringExtra("about");
        _uriProfile = intent.getStringExtra("uri");


        appointmentPrice = intent.getFloatExtra("appointmentPrice", 0);

        if (_uriProfile != null) {
            Uri myUri = Uri.parse(_uriProfile);
            Picasso.get().load(myUri).into(ImageView_profile);
        }


        editText_about_expert.setText(_about);
        editText_edit_appointmentPrice_information.setText(appointmentPrice.toString());


    }

    public void btn_update_expert_profile(View view) {
        progressBar.setVisibility(View.VISIBLE);
        float price = Float.parseFloat(editText_edit_appointmentPrice_information.getText().toString());
        String about = editText_about_expert.getText().toString();


            Expert expert = new Expert();
            expert.set_profileImage(imageData);
            expert.set_appointmentPrice(price);
            expert.set_about(about);
            expert.set_ID(FirebaseAuth.getInstance().getCurrentUser().getUid());

            ExpertManager expertManager = new ExpertManager(new FireBaseExpertDal());
            expertManager.updateExpertProfileImage(expert, new IResult() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.INVISIBLE);

                    Toast.makeText(getApplicationContext(), "Güncellendi", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailed(Exception exception) {

                }
            });


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
