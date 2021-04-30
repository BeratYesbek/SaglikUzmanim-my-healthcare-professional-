package com.saglikuzmanimm.saglikuzmanim.Core.Concrete.FireBaseDataBase;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Appointment;
import com.saglikuzmanimm.saglikuzmanim.Core.Abstract.IFirebaseAppointmentDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetAppointmentListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseAppointmentDal implements IFirebaseAppointmentDal<Appointment, IResult, IGetAppointmentListener> {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void addData(Appointment entity, IResult iResult) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        HashMap<String, Object> hashMap = new HashMap<>();


        hashMap.put("timestamp_appointment_date", entity.get_timestamp_appointment_date());
        hashMap.put("timestamp_sendTo_date", FieldValue.serverTimestamp());
        hashMap.put("senderID", entity.get_senderID());
        hashMap.put("receiverID", entity.get_receiverID());
        hashMap.put("appointmentID", entity.get_appointmentID());
        hashMap.put("situation", entity.get_situation());
        hashMap.put("abort", entity.get_abort());
        hashMap.put("payment", entity.get_payment());
        hashMap.put("appointmentPrice", entity.get_appointmentPrice());

        firebaseFirestore.collection("Appointments").document().set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void updateData(Appointment entity, IResult iResult) {
        firebaseFirestore = FirebaseFirestore.getInstance();

        HashMap<String, Object> hashMap = new HashMap<>();

        if (entity.get_abort() != null) {
            hashMap.put("abort", entity.get_abort());
        }
        if (entity.get_payment() != null) {
            hashMap.put("payment", entity.get_payment());
        }
        if (entity.get_situation() != null) {
            hashMap.put("situation", entity.get_situation());
        }
        if (entity.get_whoCanceled() != null) {
            hashMap.put("whoCancelled", entity.get_whoCanceled());
        }
        if(entity.get_completed() != null) {
            hashMap.put("completed",entity.get_completed());
        }

        DocumentReference documentReference = firebaseFirestore.collection("Appointments").document(entity.get_documentID());

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
    public void delete(Appointment entity, IResult iResult) {

    }

    @Override
    public void getData(Appointment entity, IGetAppointmentListener iGetListener) {
        try {
            ArrayList<Appointment> appointmentArrayList = new ArrayList<>();
            firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            String ID = firebaseAuth.getCurrentUser().getUid();

            firebaseFirestore.collection("Appointments").orderBy("timestamp_sendTo_date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (!value.isEmpty()) {
                        appointmentArrayList.clear();
                        for (DocumentSnapshot document : value.getDocuments()) {
                            Map<String, Object> data = document.getData();
                            String documentID = document.getId();
                            Boolean abort = (Boolean) data.get("abort");
                            Boolean situation = (Boolean) data.get("situation");
                            Boolean payment = (Boolean) data.get("payment");

                            String appointmentID = (String) data.get("appointmentID");
                            String receiverID = (String) data.get("receiverID");
                            String senderID = (String) data.get("senderID");
                            String whoCanceled = (String) data.get("whoCancelled");


                            Object object = data.get("appointmentPrice");
                            Float appointmentPrice = Float.parseFloat(object.toString());

                            Timestamp timestamp_appointment_date = (Timestamp) data.get("timestamp_appointment_date");
                            Timestamp timestamp_sendTo_date = (Timestamp) data.get("timestamp_sendTo_date");
                            if (receiverID.equals(ID) || senderID.equals(ID)) {
                                appointmentArrayList.add(new Appointment(senderID, receiverID, documentID
                                        , appointmentID, whoCanceled, situation, abort, payment
                                        , timestamp_appointment_date, timestamp_sendTo_date, appointmentPrice
                                ));
                            }

                        }
                        iGetListener.onSuccess(appointmentArrayList);
                        appointmentArrayList.clear();

                    }else{
                        iGetListener.onFailed(null);
                    }

                }
            });
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

}
