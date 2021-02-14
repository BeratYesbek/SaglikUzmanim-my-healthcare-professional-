package com.example.salikuzmanim.Activity.ActivityExpert;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;


import com.example.salikuzmanim.DataBaseManager.FireBaseExpertDal;
import com.example.salikuzmanim.Fragment.SingleChoiceExpertFragment;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetExpertDataListener;
import com.example.salikuzmanim.Interfaces.IEntity;
import com.example.salikuzmanim.Interfaces.SingleChoiceLister;
import com.example.salikuzmanim.Concrete.Expert;
import com.example.salikuzmanim.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;

public class SignUpGiveServiceActivity extends AppCompatActivity implements SingleChoiceLister {
    EditText firstName;
    EditText tcNumber;
    EditText lastName;
    EditText Email;
    EditText password;
    EditText passwordAgain;

    String departman;


    Uri imageDiploma;
    Uri imageIdCard;
    Uri imageData;
    boolean control = true;
    Bitmap SelectData;
    private static Context context;
    boolean checked;
    private static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_give_service);

        firstName = findViewById(R.id.editTextFirstName3);
        lastName = findViewById(R.id.editTextLastName);
        Email = findViewById(R.id.editTextEmailSignUp1);
        password = findViewById(R.id.editTextPasswordSignUp1);
        passwordAgain = findViewById(R.id.editTextPasswordSignUpAgain1);
        tcNumber = findViewById(R.id.editTextTSocialNumber);

        progressBar = findViewById(R.id.progressbar_fo_sign_expert);
        Sprite fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.INVISIBLE);



        context = getApplicationContext();


    }

    public void btnSignUp(View view){

        String _firstName = firstName.getText().toString();
        String _lastName = lastName.getText().toString();
        String _Email = Email.getText().toString();
        String _password = password.getText().toString();
        String _tcNumber = tcNumber.getText().toString();
        String _passwordAgain = passwordAgain.getText().toString();

        if(_firstName.isEmpty()){
            Toast.makeText(this,"İsim boş geçilemez!!!",Toast.LENGTH_LONG).show();
            
        }
        else if(_lastName.isEmpty()){
            Toast.makeText(this,"Soyad boş geçilemez!!!",Toast.LENGTH_LONG).show();

        }
        else if(_Email.isEmpty()) {
            Toast.makeText(this,"Email boş geçilemez!!!",Toast.LENGTH_LONG).show();

        }
        else if(_password.isEmpty()){
            Toast.makeText(this,"şifre boş geçilemez!!!",Toast.LENGTH_LONG).show();

        }
        else if(_tcNumber.isEmpty() || _tcNumber.length() != 11 || _tcNumber.startsWith("0")){
            if(_tcNumber.length() != 11){
                Toast.makeText(this,"Türkiye Cumhuriyeti  kimlik numarası 11 haneli olmalıdır ",Toast.LENGTH_LONG).show();

            }
            if(_tcNumber.startsWith("0")){
                Toast.makeText(this,"Türkiye Cumhuriyeti kimlik numarası 0 ile başlayamaz! ",Toast.LENGTH_LONG).show();

            }
            if(_tcNumber.isEmpty()){
                Toast.makeText(this,"Türkiye Cumhuriyeti Kimlik Numarası boş geçilemez!!!",Toast.LENGTH_LONG).show();

            }
        }
        else if(_passwordAgain.isEmpty()){
            Toast.makeText(this,"şifre boş geçilemez!!!",Toast.LENGTH_LONG).show();

        }

        else if(imageDiploma == null){
            Toast.makeText(this,"Diploma fotorafı boş geçilemez!!!",Toast.LENGTH_LONG).show();
            System.out.println(imageDiploma);


        }

       else if(imageIdCard == null){
            Toast.makeText(this,"kimlik ön yüz fotoğrafı boş geçilemez!!!",Toast.LENGTH_LONG).show();
            System.out.println(imageIdCard);


        }
       else if(departman == null){
           Toast.makeText(this,"Uzman Seçimi boş geçilemez!",Toast.LENGTH_LONG).show();

        }
       else if(checked == false){
           Toast.makeText(this,"Anlaşmayı etmeniz gerekiyor.",Toast.LENGTH_LONG).show();
        }

        else{
            if(!_password.equals(_passwordAgain)){
                Toast.makeText(this,"Şifreler birbiri ile aynı değil!!!",Toast.LENGTH_LONG).show();
            }
            else{
                progressBar.setVisibility(View.VISIBLE);
                /*
                ExpertDal expertDal = new ExpertDal();
                expertDal.insertExpertData(new Expert(_firstName,_lastName,_Email, _password,"expert",_tcNumber,departman,imageDiploma,imageIdCard));
                */
                try{
                    FireBaseExpertDal fireBaseExpertDal = new FireBaseExpertDal();
                    fireBaseExpertDal.createExpertAccount(new Expert(_firstName, _lastName, _Email, _password, "expert", _tcNumber, departman, imageDiploma, imageIdCard), new IGetExpertDataListener() {
                        @Override
                        public void onSuccess(IEntity entity) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"Kullanıcı başarı ile oluşturuldu",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(Exception exception) {

                        }
                    });

                }catch (Exception e){
                    System.out.println(e.toString());
                }

            }
        }
}
    public static void accessActivity(Boolean result){
        if(result == true){
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(context,"Kayıt işlemi başarı ile tamamlandı.",Toast.LENGTH_LONG).show();
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(context,"Kayıt işlemi sırasında bir hata meydana geldi",Toast.LENGTH_LONG).show();
        }
    }

public void onCheckbox(View view){
        checked = ((CheckBox ) view).isChecked();
        switch (view.getId()){
            case R.id.checkBox:
                if(checked){

                }
        }
}
    public void  btnChoseFileIdCard(View view){
        control = false;
        SelectImage();

    }


    public void btnChoseFileDiploma(View view){
        control = true;
        SelectImage();

    }
    public void btnSelectExpert(View view){
        DialogFragment singleDialogFragment = new SingleChoiceExpertFragment();
        singleDialogFragment.setCancelable(false);
        singleDialogFragment.show(getSupportFragmentManager(),"single choice expert");

    }
    @Override
    public void onPossitiveButtonClicked(String[] list, int position,String whichList) {
        departman = list[position];
        System.out.println(departman);
    }

    @Override
    public void onNegativeButtonClicked() {

    }


    public void SelectImage(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        else{
            Intent intenToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intenToGallery,2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults.length > 0 &&  grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intentToGallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageData = data.getData();

            if(control == true){
                imageDiploma = imageData;
            }else{
                imageIdCard = imageData;
            }

            try {

                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), imageData);
                    SelectData = ImageDecoder.decodeBitmap(source);

                } else {
                    SelectData = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);

                }
            }catch(Exception e){

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



}
