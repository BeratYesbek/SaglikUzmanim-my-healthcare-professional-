package com.saglikuzmanimm.saglikuzmanim.Activity.ActivityUser;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.UserManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.User;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.UserDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;
import com.saglikuzmanimm.saglikuzmanim.R;

public class SignUpUserActivity extends  AppCompatActivity   {


    private static Context context;
    private static ProgressBar progressBar;
    private EditText firstNameUser;
    private EditText lastNameUser;
    private EditText tcNumber;
    private EditText emailUser;
    private  EditText passwordUser;
    private   EditText passwordUserAgain;

    Boolean checkBox;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user);

        firstNameUser = findViewById(R.id.editTextFirstName2);
        lastNameUser = findViewById(R.id.editTextLastName2);
        tcNumber = findViewById(R.id.editTextTSocialNumber2);
        emailUser = findViewById(R.id.editTextEmailSignUp2);
        passwordUser = findViewById(R.id.editTextPasswordSignUp2);
        passwordUserAgain = findViewById(R.id.editTextPasswordSignUpAgain2);
        progressBar = findViewById(R.id.progressbar_user_sign);

        progressBar.setVisibility(View.INVISIBLE);
        context = getApplicationContext();



    }
    public void btnSignUp(View view){
        String _firstName = firstNameUser.getText().toString();
        String _lastName = lastNameUser.getText().toString();
        String _tcNumber = tcNumber.getText().toString();
        String _email = emailUser.getText().toString();
        String _password = passwordUser.getText().toString();
        String _passwordAgain = passwordUserAgain.getText().toString();

        if(_firstName == null ){
            Toast.makeText(this,"Ad boş geçilemez.",Toast.LENGTH_LONG).show();
        }
        else if(_lastName == null){
            Toast.makeText(this,"Soyad boş geçilemez.",Toast.LENGTH_LONG).show();
        }
        else if(_tcNumber.startsWith("0") || _tcNumber.length() <11){

            if(_tcNumber.startsWith("0")){
                Toast.makeText(this,"Kimlik Numarası 0 ile başlayamaz.",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this,"Kimlik Numarası 11 haneden küçük olamaz.",Toast.LENGTH_LONG).show();
            }

        }
        else if(_email == null){
            Toast.makeText(this,"Email boş geçilemez.",Toast.LENGTH_LONG).show();

        }
        else if(_password == null){
            Toast.makeText(this,"Şifre boş geçilemez.",Toast.LENGTH_LONG).show();

        }
        else{
            if(!checkBox.equals(true)){
                Toast.makeText(this,"Anlaşmayı kabul etmeniz gerekiyor",Toast.LENGTH_LONG).show();
            }
            if(!_password.equals(_passwordAgain)){
                Toast.makeText(this,"Şifreler birbiri ile eşleşmiyor",Toast.LENGTH_LONG).show();
            }
            else {
                progressBar.setVisibility(View.VISIBLE);


                UserManager userManager = new UserManager(new UserDal());
                userManager.createUserAccount(new User(_firstName, _lastName, _email, _password, "user", _tcNumber), new IResult() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"Kayıt işleminiz başarı ile gerçekleştirildi.",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailed(Exception exception) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"Üzgün kayıt işlemi sırasında bir hata meydana geldi.",Toast.LENGTH_LONG).show();
                    }
                });

            }
         }
    }

    public void btnOnCheckBox(View view){
        checkBox = ((CheckBox ) view).isChecked();
        switch (view.getId()){
            case R.id.checkBox2:
                if(checkBox){
                    System.out.println(checkBox);
                }

        }
    }


}
