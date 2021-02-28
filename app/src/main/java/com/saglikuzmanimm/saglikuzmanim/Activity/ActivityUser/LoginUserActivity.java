package com.saglikuzmanimm.saglikuzmanim.Activity.ActivityUser;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.saglikuzmanimm.saglikuzmanim.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class LoginUserActivity extends AppCompatActivity {
    private EditText email_user;
    private EditText password_user;
    private ImageView imageView;
    private ProgressBar progressBar;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private Map<String, Object> data;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    Context context;
    String type;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        context = getApplicationContext();
        progressBar = findViewById(R.id.progressBarLoginReceiving);
        email_user = findViewById(R.id.editTextEmailLoginUser);
        password_user = findViewById(R.id.editTextPasswordLoginUser);
        imageView = findViewById(R.id.imageView_text_user);
        Animation a = AnimationUtils.loadAnimation(this, R.anim.image_animation);
        a.reset();
        imageView.clearAnimation();
        imageView.startAnimation(a);


        progressBar.setVisibility(View.INVISIBLE);


    }

    public void btnLogin2(View view) {
        final String _email = email_user.getText().toString();
        final String password = password_user.getText().toString();
        progressBar.setVisibility(View.VISIBLE);


        if (!_email.isEmpty()) {
            if (!password.isEmpty()) {

                firebaseFirestore.collection("users")
                        .whereEqualTo("email", _email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        data = document.getData();
                                        String email = (String) data.get("email");
                                        String firstName = (String) data.get("firstName");
                                        String lastName = (String) data.get("lastName");
                                        type = (String) data.get("type");
                                    }
                                    System.out.println(type);
                                    if (type == null) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getApplicationContext(), "Email veya şifre hatalı yada Hizmet Alan girişinden hesap bulunamadı!", Toast.LENGTH_LONG).show();

                                    } else {
                                        getUserAccount(_email, password);
                                    }

                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }

                            }

                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Hizmet alan bölümünde hesabınız bulunamadı!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        System.out.println(e.toString());
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Şifre boş geçilemez", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Email boş geçilemez", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
        }


    }

    public void btnSignUpReceiving(View view) {
        Intent intentToSignUpReceiving = new Intent(getApplicationContext(), SignUpUserActivity.class);
        startActivity(intentToSignUpReceiving);
    }

    public void getUserAccount(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intentToNavigationActivity = new Intent(getApplicationContext(), NavigationUserActivity.class);
                startActivity(intentToNavigationActivity);
                finish();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Email veya şifre hatalı!", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

}