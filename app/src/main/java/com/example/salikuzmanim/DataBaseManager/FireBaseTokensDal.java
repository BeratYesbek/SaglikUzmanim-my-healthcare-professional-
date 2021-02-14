package com.example.salikuzmanim.DataBaseManager;

import androidx.annotation.NonNull;

import com.example.salikuzmanim.Interfaces.GetDataListener.IGetDataListener;
import com.example.salikuzmanim.Notification.Notification;
import com.example.salikuzmanim.Concrete.Token;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FireBaseTokensDal {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;



    public void insertTokens(Token token) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token.getToken());
        hashMap.put("userID", firebaseAuth.getCurrentUser().getUid());
        firebaseFirestore.collection("Tokens").document(token.getToken()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void getTokens(Token token,IGetDataListener getDataListener) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        try {
            firebaseFirestore.collection("Tokens").whereEqualTo("userID", token.getUserID()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    if (!querySnapshot.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            Map<String, Object> data = documentSnapshot.getData();
                            String token = (String) data.get("token");
                            String userID = (String) data.get("userID");
                            Notification notification = new Notification();
                            System.out.println(userID);
                            notification.setToken(token);
                            notification.setUserID(userID);
                            try {
                                getDataListener.onSuccess(notification);
                            } catch (Exception e) {
                                System.out.println("hata veri tabanı " + e.toString());
                            }

                        }

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println(e.toString());
                }
            });
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    public void getTokensCheck(Token token) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Tokens").whereEqualTo("token", token.getToken()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                if (!querySnapshot.isEmpty()) {
                    for(DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()){
                        Map<String,Object> data = documentSnapshot.getData();
                        String token = (String) data.get("token");
                        String userID = (String) data.get("userID");
                        Token token1 = new Token();
                        token1.setUserID(userID);
                        token1.setToken(token);
                        updateTokens(token1);
                    }
                } else {
                    insertTokens(token);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void updateTokens(Token token) {
        firebaseAuth = FirebaseAuth.getInstance();
        try {

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("token", token.getToken());
            hashMap.put("userID", firebaseAuth.getCurrentUser().getUid());


            firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = firebaseFirestore.collection("Tokens").document(token.getToken());
            documentReference.update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("token update firebase error: " + e);
                }
            });
        } catch (Exception e) {
                System.out.println("token update error : " + e);
        }

    }
}
