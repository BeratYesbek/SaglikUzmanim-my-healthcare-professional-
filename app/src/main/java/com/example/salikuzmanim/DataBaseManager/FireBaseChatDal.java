package com.example.salikuzmanim.DataBaseManager;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.salikuzmanim.Interfaces.FireBaseInsterfaces.IFireBaseChatDal;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetDataListener;
import com.example.salikuzmanim.Concrete.Message;
import com.example.salikuzmanim.Concrete.MessageArrayList;
import com.example.salikuzmanim.Concrete.Person;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetQueryListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class FireBaseChatDal implements IFireBaseChatDal {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    @Override
    public void insertMessage(Message message) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("messageID", message.getMessageID());
        hashMap.put("receiverID", message.getReciverID());
        hashMap.put("senderID", message.getSenderID());
        hashMap.put("message", message.getMessage());
        hashMap.put("time", message.getMessageTime());
        hashMap.put("timestamp", FieldValue.serverTimestamp());
        hashMap.put("isSeen", message.isSeen());
        firebaseFirestore.collection("Chats").document().set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("eklendi");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void getMessage(Message message, IGetDataListener getDataListener) {
        ArrayList<Message> messageArrayList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Chats").orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                messageArrayList.clear();
                for (DocumentSnapshot document : value.getDocuments()) {
                    Map<String, Object> data = document.getData();

                    String senderID = (String) data.get("senderID");
                    String receiverID = (String) data.get("receiverID");
                    String time = (String) data.get("time");
                    String messages = (String) data.get("message");
                    String messageID = (String) data.get("messageID");
                    boolean isSeen = (boolean) data.get("isSeen");


                    if (receiverID.equals(message.getReciverID()) && senderID.equals(message.getSenderID()) ||
                            receiverID.equals(message.getSenderID()) && senderID.equals(message.getReciverID())) {
                        Message message1 = new Message();
                        message1.setMessage(messages);
                        message1.setSenderID(senderID);
                        message1.setReciverID(receiverID);
                        message1.setMessageTime(time);
                        message1.setSeen(isSeen);
                        messageArrayList.add(message1);
                    }
                }
                getDataListener.onSuccess(messageArrayList);

            }
        });
    }

    @Override
    public void seenMessages(String userID) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Chats").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {

                if (!querySnapshot.isEmpty()) {
                    try {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            Map<String, Object> data = document.getData();
                            String receiverID = (String) data.get("receiverID");
                            String senderID = (String) data.get("senderID");
                            if (receiverID.equals(firebaseAuth.getCurrentUser().getUid()) && senderID.equals(userID)) {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("isSeen", true);
                                document.getReference().update(hashMap);

                            }
                        }

                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private ArrayList<String> userList;

    @Override
    public void getMessageForList(String whoSend, IGetDataListener getDataListener) {
        userList = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        firebaseFirestore.collection("Chats").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                int isSeenTotal = 0;
                ArrayList<HashMap> hashMapArrayList = new ArrayList<>();
                ArrayList<Person> personArrayList = new ArrayList<>();
                if (!value.isEmpty()) {
                    for (DocumentSnapshot document : value.getDocuments()) {
                        Map<String, Object> data = document.getData();
                        String senderID = (String) data.get("senderID");
                        String receiverID = (String) data.get("receiverID");
                        String time = (String) data.get("time");
                        String messages = (String) data.get("message");
                        String messageID = (String) data.get("messageID");
                        boolean isSeen = (boolean) data.get("isSeen");

                        if (isSeen == false && receiverID.equals(firebaseAuth.getCurrentUser().getUid())) {

                            isSeenTotal++;
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("WhoisntSeen", senderID);
                            hashMap.put("totalIsntSeen", isSeenTotal);
                            hashMapArrayList.add(hashMap);
                            System.out.println(senderID);
                        }
                        try {
                            if (senderID.equals(firebaseUser.getUid().toString())) {
                                int counter = 0;
                                if (userList.size() != 0) {
                                    for (String id : userList) {
                                        if (!id.equals(receiverID.toString())) {
                                            counter++;
                                        }
                                        if (counter == userList.size()) {
                                            userList.add(receiverID);

                                        }
                                    }
                                } else {

                                    userList.add(receiverID);

                                }
                            }
                            if (receiverID.equals(firebaseUser.getUid().toString())) {
                                int counter = 0;
                                if (userList.size() != 0) {
                                    for (String id : userList) {
                                        if (!id.equals(senderID.toString())) {
                                            counter++;
                                        }
                                        if (counter == userList.size()) {

                                            userList.add(senderID);

                                        }
                                    }
                                } else {

                                    userList.add(senderID);
                                }


                            }
                        } catch (Exception e) {
                            System.out.println(e.toString());

                        }

                    }
                    if (whoSend.equals("expert")) {
                        try {


                            FireBaseExpertDal fireBaseExpertDal = new FireBaseExpertDal();
                            fireBaseExpertDal.getAllExpert(new IGetDataListener() {
                                @Override
                                public void onSuccess(Object object) {
                                    QuerySnapshot queryDocumentSnapshots = (QuerySnapshot) object;

                                    for (int i = 0; i < userList.size(); i++) {
                                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                            Map<String, Object> data = document.getData();
                                            String firstName = (String) data.get("firstName");
                                            String lastName = (String) data.get("lastName");
                                            String email = (String) data.get("email");
                                            String about = (String) data.get("about");
                                            String expertUid = (String) data.get("expertUid");
                                            String department = (String) data.get("department");
                                            String imageProfile = (String) data.get("profileImage");
                                            Uri uriImage = Uri.parse(imageProfile);

                                            Person expert = new Person();
                                            expert.set_firstName(firstName);
                                            expert.set_lastName(lastName);
                                            expert.set_email(email);
                                            expert.set_profileImage(uriImage);
                                            expert.set_ID(expertUid);

                                            if (userList.get(i).equals(expertUid)) {
                                                personArrayList.add(expert);

                                            }


                                        }
                                    }

                                    getDataListener.onSuccess(new MessageArrayList(personArrayList, hashMapArrayList));
                                }

                                @Override
                                public void onFailed(Object object) {

                                }
                            });


                        } catch (Exception e) {
                            System.out.println("hata 1 " + e.toString());
                        }

                    } else {
                        try {


                            FireBaseUserDal fireBaseUserDal = new FireBaseUserDal();
                            fireBaseUserDal.getAllUser(new IGetQueryListener() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocument) {
                                    for (int i = 0; i < userList.size(); i++) {
                                        for (DocumentSnapshot document : queryDocument.getDocuments()) {
                                            Map<String, Object> data = document.getData();
                                            String firstName = (String) data.get("FirstName");
                                            String lastName = (String) data.get("LastName");
                                            String email = (String) data.get("email");
                                            String userID = (String) data.get("userUid");
                                            String profileImage = (String) data.get("profileImage");
                                            Uri uriProfile = Uri.parse(profileImage);
                                            Person user = new Person();
                                            user.set_firstName(firstName);
                                            user.set_lastName(lastName);
                                            user.set_ID(userID);
                                            user.set_profileImage(uriProfile);
                                            if (userList.get(i).equals(userID)) {
                                                personArrayList.add(user);

                                            }
                                        }
                                    }
                                    getDataListener.onSuccess(new MessageArrayList(personArrayList, hashMapArrayList));
                                }

                                @Override
                                public void onFailed(Exception e) {

                                }
                            });

/*
                                    for (int i = 0; i < userList.size(); i++) {
                                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                            Map<String, Object> data = document.getData();
                                            String firstName = (String) data.get("FirstName");
                                            String lastName = (String) data.get("LastName");
                                            String email = (String) data.get("email");
                                            String userID = (String) data.get("userUid");
                                            String profileImage = (String) data.get("profileImage");
                                            Uri uriProfile = Uri.parse(profileImage);
                                            Person user = new Person();
                                            user.set_firstName(firstName);
                                            user.set_lastName(lastName);
                                            user.set_ID(userID);
                                            user.set_profileImage(uriProfile);
                                            if (userList.get(i).equals(userID)) {
                                                personArrayList.add(user);

                                            }
                                        }
                                    }
                                    getDataListener.onSuccess(new MessageArrayList(personArrayList, hashMapArrayList));


                                }
*/


                        } catch (Exception e) {
                            System.out.println("hata 1 " + e.toString());
                        }
                    }


                }
            }
        });

    }

}
