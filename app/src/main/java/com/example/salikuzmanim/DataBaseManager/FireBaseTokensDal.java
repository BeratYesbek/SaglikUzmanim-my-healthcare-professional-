package com.example.salikuzmanim.DataBaseManager;

import androidx.annotation.NonNull;

import com.example.salikuzmanim.Concrete.Token;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FireBaseTokensDal {


    private FirebaseFirestore firebaseFirestore;

    public void updateTokens(Token token, String collection) {

        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection(collection).document(token.getUserID());
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("token", token.getToken());

        documentReference.update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }


}
