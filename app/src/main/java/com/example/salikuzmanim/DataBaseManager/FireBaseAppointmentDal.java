package com.example.salikuzmanim.DataBaseManager;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.salikuzmanim.Interfaces.FireBaseInsterfaces.IFireBaseAppointmentDal;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetAppointmentDataListener;
import com.example.salikuzmanim.Concrete.Appointment;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FireBaseAppointmentDal implements IFireBaseAppointmentDal<Appointment> {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void sendAppointment(Appointment entity, IGetAppointmentDataListener iGetAppointmentDataListener) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        HashMap<String, Object> hashMap = new HashMap<>();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        hashMap.put("timestamp_appointment_date", entity.get_timestamp_appointment_date());
        hashMap.put("timestamp_sendTo_date", FieldValue.serverTimestamp());
        hashMap.put("senderID", entity.get_senderID());
        hashMap.put("receiverID", entity.get_receiverID());
        hashMap.put("appointmentID", entity.get_appointmentID());
        hashMap.put("situation", entity.get_situation());
        hashMap.put("abort", entity.get_abort());
        hashMap.put("payment", entity.get_payment());
        hashMap.put("appointmentPrice",entity.get_appointmentPrice());

        firebaseFirestore.collection("Appointments").document().set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                iGetAppointmentDataListener.onSuccess(null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iGetAppointmentDataListener.onFailed(e);
            }
        });
    }

    @Override
    public void updateAppointment(Appointment entity, IGetAppointmentDataListener iGetAppointmentDataListener) {

        firebaseFirestore = FirebaseFirestore.getInstance();

        HashMap<String, Object> hashMap = new HashMap<>();

        if(entity.get_abort() != null){
            hashMap.put("abort", entity.get_abort());
        }
        if(entity.get_payment() != null){
            hashMap.put("payment", entity.get_payment());
        }
        if(entity.get_situation() != null){
            hashMap.put("situation", entity.get_situation());
        }
        if (entity.get_whoCanceled() != null){
            hashMap.put("whoCancelled",entity.get_whoCanceled());
        }

        DocumentReference documentReference = firebaseFirestore.collection("Appointments").document(entity.get_documentID());

        documentReference.update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                     iGetAppointmentDataListener.onSuccess(null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                iGetAppointmentDataListener.onFailed(exception);
            }
        });
    }

    @Override
    public void getAppointment(IGetAppointmentDataListener iGetAppointmentDataListener) {
        try {
            ArrayList<Appointment> appointmentArrayList = new ArrayList<>();
            firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            String ID = firebaseAuth.getCurrentUser().getUid();

            firebaseFirestore.collection("Appointments").orderBy("timestamp_sendTo_date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value.size() != 0) {
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
                                System.out.println("biz ekliyoz gardaş");
                                appointmentArrayList.add(new Appointment(senderID, receiverID, documentID, appointmentID, whoCanceled, situation, abort, payment, timestamp_appointment_date, timestamp_sendTo_date,appointmentPrice));
                            }

                        }
                        if (appointmentArrayList.size() != 0) {
                            iGetAppointmentDataListener.onSuccess(appointmentArrayList);
                        }

                    }

                }
            });
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }
}
