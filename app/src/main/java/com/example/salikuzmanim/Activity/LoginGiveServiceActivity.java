package com.example.salikuzmanim.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salikuzmanim.Activity.ActivityExpert.FragmentActivityForExpert;
import com.example.salikuzmanim.Activity.ActivityExpert.SignUpGiveServiceActivity;
import com.example.salikuzmanim.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class LoginGiveServiceActivity extends AppCompatActivity {
    EditText editText_login_email;
    EditText editText_login_password;


    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    String type;
    String department;
    ImageView imageView;
    ProgressBar progressBar;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_give_service);
        editText_login_email = findViewById(R.id.editTextEmailLoginExpert);
        editText_login_password = findViewById(R.id.editTextPasswordLoginExpert);
        imageView = findViewById(R.id.imageView_text_expert);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.image_animation);
        animation.reset();

        imageView.clearAnimation();
        imageView.startAnimation(animation);

        context = getApplicationContext();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

       progressBar = findViewById(R.id.progressBar_for_giveService_login);


        progressBar.setVisibility(View.INVISIBLE);




    }

    public void BtnLogin(View view) {

        final String email = editText_login_email.getText().toString();
        final String password = editText_login_password.getText().toString();


        if (!email.isEmpty()) {
            if (password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Şifre boş geçilemez!", Toast.LENGTH_LONG).show();
            } else {

                progressBar.setVisibility(View.VISIBLE);

                firebaseFirestore.collection("Expert_users")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Map<String, Object> data = document.getData();

                                         type = (String) data.get("type");
                                         department = (String) data.get("department");
                                    }
                                    System.out.println(type);
                                    if (type == null) {

                                        Toast.makeText(getApplicationContext(), "Email veya şifre hatalı yada Hizmet Alan girişinden hesap bulunamadı!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.INVISIBLE);

                                    } else {
                                        Login(email, password);
                                    }

                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }

                            }

                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Hizmet alan bölümünde hesabınız bulunamadı!", Toast.LENGTH_LONG).show();


                    }
                });

            }

        }
    }
    public void btnSignUpGiveService (View view){
        Intent intentToSignUpGiveService = new Intent(getApplicationContext(), SignUpGiveServiceActivity.class);
        startActivity(intentToSignUpGiveService);
    }

        public void Login (String email, String password){

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {


                    Toast.makeText(getApplicationContext(), "Giriş yapılıyor", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(getApplicationContext(), FragmentActivityForExpert.class);
                    intent.putExtra("department",department);
                    startActivity(intent);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Email veya şifre yanlış", Toast.LENGTH_LONG).show();
                }
            });
        }
    public String getDepartment() {
        System.out.println("ee " + department);
        return department;
    }

    }

