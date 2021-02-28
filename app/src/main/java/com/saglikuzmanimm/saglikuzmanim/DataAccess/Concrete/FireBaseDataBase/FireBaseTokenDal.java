package com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase;

import android.content.Context;

import androidx.annotation.NonNull;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Token;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract.ITokenDal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class FireBaseTokenDal implements ITokenDal {

    private FirebaseFirestore firebaseFirestore;


    @Override
    public void getToken(Context context,String collection) {
        firebaseFirestore = FirebaseFirestore.getInstance();

            String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseApp.initializeApp(context);
            FirebaseMessaging.getInstance().setAutoInitEnabled(true);
            FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
            firebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String token) {

                    updateToken(new Token(token, ID), collection);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println(e.toString());
                }
            });


    }

    @Override
    public void updateToken(Token token, String collection) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection(collection).document(token.getUserID());
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("token", token.getToken());

        documentReference.update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("updated Token");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
