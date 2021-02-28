package com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase;

import androidx.annotation.NonNull;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Notification;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract.INotificationDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetNotificationListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FireBaseNotificationDal implements INotificationDal<Notification, IResult, IGetNotificationListener>  {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    public void addData(Notification entity, IResult iResult) {

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("receiverID", entity.get_receiverID());
        hashMap.put("senderID", entity.get_senderID());
        hashMap.put("messageBody", entity.get_messageBody());
        hashMap.put("messageTitle", entity.get_messageTitle());
        hashMap.put("isSeen", entity.get_isSeen());
        hashMap.put("timestamp", FieldValue.serverTimestamp());

        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("AppointmentNotifications").document().set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                iResult.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iResult.onFailed(e);
                System.out.println(e);
            }
        });
    }

    @Override
    public void updateData(Notification entity, IResult iResult) {
        firebaseFirestore = FirebaseFirestore.getInstance();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("isSeen", true);


        DocumentReference documentReference = firebaseFirestore.collection("AppointmentNotifications").document(entity.get_documentID());

        documentReference.update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                iResult.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                iResult.onFailed(exception);
            }
        });
    }

    @Override
    public void delete(Notification entity, IResult iResult) {

    }

    @Override
    public void getData(Notification entity, IGetNotificationListener iGetNotificationListener) {
        ArrayList<Notification> notificationArrayList = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        String userUid = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore.collection("AppointmentNotifications").whereEqualTo("receiverID", userUid)
                .orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                if (!querySnapshot.isEmpty()) {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Map<String, Object> data = document.getData();

                        String documentID = document.getId();

                        String receiverID = (String) data.get("receiverID");
                        String senderID = (String) data.get("senderID");
                        String messageBody = (String) data.get("messageBody");
                        String messageTitle = (String) data.get("messageTitle");

                        Boolean isSeen = (Boolean) data.get("isSeen");

                        Timestamp timestamp = (Timestamp) data.get("timestamp");

                        notificationArrayList.add(new Notification(receiverID, senderID, messageBody, messageTitle, isSeen, documentID, timestamp));

                    }
                    iGetNotificationListener.onSuccess(notificationArrayList);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

}
