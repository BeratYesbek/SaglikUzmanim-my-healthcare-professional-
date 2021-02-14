package com.example.salikuzmanim.Activity.ActivityUser;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.salikuzmanim.DataBaseManager.FireBaseUserDal;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetUserDataListener;
import com.example.salikuzmanim.Interfaces.IEntity;
import com.example.salikuzmanim.Concrete.User;
import com.example.salikuzmanim.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;

public class SignUpReceivingServiceActivity extends  AppCompatActivity   {


    private static Context context;
    private static ProgressBar progressBar;
    EditText firstNameUser;
    EditText lastNameUser;
    EditText tcNumber;
    EditText emailUser;
    EditText passwordUser;
    EditText passwordUserAgain;

    Boolean checkBox;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_receiving_service);

        firstNameUser = findViewById(R.id.editTextFirstName2);
        lastNameUser = findViewById(R.id.editTextLastName2);
        tcNumber = findViewById(R.id.editTextTSocialNumber2);
        emailUser = findViewById(R.id.editTextEmailSignUp2);
        passwordUser = findViewById(R.id.editTextPasswordSignUp2);
        passwordUserAgain = findViewById(R.id.editTextPasswordSignUpAgain2);
        progressBar = findViewById(R.id.progressbar_user_sign);
        Sprite fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
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

                FireBaseUserDal fireBaseUserDal = new FireBaseUserDal();
                fireBaseUserDal.createUserAccount(new User(_firstName, _lastName, _email, _password, "user", _tcNumber), new IGetUserDataListener() {
                    @Override
                    public void onSuccess(IEntity entity) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"Kayıt işleminiz başarı ile gerçekleştirildi.",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailed(Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"Üzgün kayıt işlemi sırasında bir hata meydana geldi.",Toast.LENGTH_LONG).show();
                    }
                });

                /*
                userDal = new UserDal();
                userDal.insertUserData(new User(_firstName, _lastName, _email, _password,"user",_tcNumber));
*/

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

    public static void access(Boolean result ){
        if(result == true){
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(context,"Kayıt işlemi başarı ile tamamlandı.",Toast.LENGTH_LONG).show();
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(context,"Kayıt işlemi sırasında bir hata meydana geldi.",Toast.LENGTH_LONG).show();
        }
    }


}
