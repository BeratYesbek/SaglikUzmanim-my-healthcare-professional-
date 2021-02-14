package com.example.salikuzmanim.DataBaseManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.salikuzmanim.Concrete.JobAdvertisement;
import com.example.salikuzmanim.Interfaces.FireBaseInsterfaces.IFireBaseJobAdvertisementDal;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetJobAdvertisementDataListener;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetListDataListener;
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


public class FireBaseJobAdvertisementDal implements IFireBaseJobAdvertisementDal<JobAdvertisement> {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    public void addJobAdvertisement(JobAdvertisement entity, IGetJobAdvertisementDataListener iGetJobAdvertisementDataListener) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uploaderID", firebaseAuth.getCurrentUser().getUid());
        hashMap.put("job_advertisement_ID", entity.get_job_advertisement_ID());
        hashMap.put("job_advertisement_title", entity.get_job_advertisement_title());
        hashMap.put("job_advertisement_explanation", entity.get_job_advertisement_explanation());
        hashMap.put("job_advertisement_department", entity.get_job_advertisement_department());
        hashMap.put("job_advertisement_location", entity.get_job_advertisement_location());
        hashMap.put("timestamp", FieldValue.serverTimestamp());

        firebaseFirestore.collection("JobAdvertisements").document().set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                iGetJobAdvertisementDataListener.onSuccess(entity);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iGetJobAdvertisementDataListener.onFailed(e);
            }
        });
    }

    @Override
    public void updateJobAdvertisement(JobAdvertisement entity, IGetJobAdvertisementDataListener iGetJobAdvertisementDataListener) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("job_advertisement_title", entity.get_job_advertisement_title());
        hashMap.put("job_advertisement_explanation", entity.get_job_advertisement_explanation());
        hashMap.put("job_advertisement_department", entity.get_job_advertisement_department());
        hashMap.put("job_advertisement_location", entity.get_job_advertisement_location());
        System.out.println(entity.get_documentID());
        DocumentReference documentReference = firebaseFirestore.collection("JobAdvertisements").document(entity.get_documentID());
        documentReference.update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                iGetJobAdvertisementDataListener.onSuccess(entity);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.toString());
                iGetJobAdvertisementDataListener.onFailed(e);
            }
        });


    }

    @Override
    public void getJobAdvertisement(IGetListDataListener iGetListDataListener) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        String uploaderID = firebaseAuth.getCurrentUser().getUid();
        try {
            firebaseFirestore.collection("JobAdvertisements").whereEqualTo("uploaderID", uploaderID).orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    ArrayList<JobAdvertisement> advertisementArrayList = new ArrayList<>();
                    if (value.getDocuments().size() != 0) {
                        System.out.println("eeeee");
                        for (DocumentSnapshot document : value.getDocuments()) {
                            Map<String, Object> data = document.getData();
                            String documentID = document.getId();
                            String uploaderID = (String) data.get("uploaderID");
                            String job_advertisement_ID = (String) data.get("job_advertisement_ID");
                            String job_advertisement_title = (String) data.get("job_advertisement_title");
                            String job_advertisement_explanation = (String) data.get("job_advertisement_explanation");
                            String job_advertisement_department = (String) data.get("job_advertisement_department");
                            String job_advertisement_location = (String) data.get("job_advertisement_location");
                            try {
                                Timestamp timestamp = (Timestamp) data.get("timestamp");
                                advertisementArrayList.add(new JobAdvertisement(job_advertisement_title, job_advertisement_explanation, job_advertisement_department, job_advertisement_location, job_advertisement_ID, uploaderID,documentID, timestamp));


                            } catch (Exception e) {
                                System.out.println(e.toString());
                            }
                        }
                        iGetListDataListener.onSuccess(advertisementArrayList);

                    } else {
                        iGetListDataListener.onFailed(null);
                    }
                }
            });
            iGetListDataListener.onFailed(null);
        } catch (Exception e) {
            e.toString();
            iGetListDataListener.onFailed(e);
        }
    }

    @Override
    public void deleteJobAdvertisement(JobAdvertisement entity, IGetJobAdvertisementDataListener iGetJobAdvertisementDataListener) {

    }

    @Override
    public void getAllJobAdvertisement(String location,String department, IGetListDataListener iGetListDataListener) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        ArrayList<JobAdvertisement> advertisementArrayList = new ArrayList<>();
        Query query;
        try{
            if(location != null){
                query = firebaseFirestore.collection("JobAdvertisements").whereEqualTo("job_advertisement_location",location).whereEqualTo("job_advertisement_department",department).orderBy("timestamp", Query.Direction.DESCENDING);

            }else{
                query = firebaseFirestore.collection("JobAdvertisements").whereEqualTo("job_advertisement_department",department).orderBy("timestamp", Query.Direction.DESCENDING);
            }
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    if(!querySnapshot.isEmpty()){
                        for(DocumentSnapshot document : querySnapshot.getDocuments()){
                            Map<String,Object> data = document.getData();
                            String documentID = document.getId();
                            String job_advertisement_ID = (String) data.get("job_advertisement_ID");
                            String job_advertisement_department = (String) data.get("job_advertisement_department");
                            String job_advertisement_explanation = (String) data.get("job_advertisement_explanation");
                            String job_advertisement_location = (String) data.get("job_advertisement_location");
                            String job_advertisement_title = (String) data.get("job_advertisement_title");
                            String uploaderID = (String) data.get("uploaderID");
                            Timestamp timestamp = (Timestamp) data.get("timestamp");
                            advertisementArrayList.add(new JobAdvertisement(job_advertisement_title,job_advertisement_explanation,job_advertisement_department,job_advertisement_location,job_advertisement_ID,uploaderID,documentID,timestamp));

                        }
                        iGetListDataListener.onSuccess(advertisementArrayList);
                    }else{
                        iGetListDataListener.onFailed(null);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println(e.toString());
                    iGetListDataListener.onFailed(e);
                }
            });
        }catch (Exception e){
            System.out.println(e.toString());
        }

    }
}
