package com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Chat;
import com.saglikuzmanimm.saglikuzmanim.Concrete.MessageArrayList;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Person;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract.IChatDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetChatListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetMessageArrayListListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetQueryListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;
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

public class FireBaseChatDal implements IChatDal<Chat, IResult, IGetChatListener> {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private ArrayList<String> userIdList;

    @Override
    public void addData(Chat entity, IResult iResult) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("messageID", entity.getMessageID());
        hashMap.put("receiverID", entity.getReciverID());
        hashMap.put("senderID", entity.getSenderID());
        hashMap.put("message", entity.getMessage());
        hashMap.put("time", entity.getMessageTime());
        hashMap.put("timestamp", FieldValue.serverTimestamp());
        hashMap.put("isSeen", entity.isSeen());
        firebaseFirestore.collection("Chats").document().set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void updateData(Chat entity, IResult iResult) {
        //that's method not necessary
    }

    @Override
    public void delete(Chat entity, IResult iResult) {
        //that's method not necessary
        // bu methoda ihtiyaç yok
    }

    @Override
    public void getData(Chat entity, IGetChatListener iGetListener) {
        ArrayList<Chat> chatArrayList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Chats").orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                chatArrayList.clear();

                if(!value.isEmpty()){
                    for (DocumentSnapshot document : value.getDocuments()) {
                        Map<String, Object> data = document.getData();

                        String senderID = (String) data.get("senderID");
                        String receiverID = (String) data.get("receiverID");
                        String time = (String) data.get("time");
                        String messages = (String) data.get("message");
                        String messageID = (String) data.get("messageID");
                        boolean isSeen = (boolean) data.get("isSeen");


                        if (receiverID.equals(entity.getReciverID()) && senderID.equals(entity.getSenderID()) ||
                                receiverID.equals(entity.getSenderID()) && senderID.equals(entity.getReciverID())) {
                            Chat chat1 = new Chat();
                            chat1.setMessage(messages);
                            chat1.setSenderID(senderID);
                            chat1.setReciverID(receiverID);
                            chat1.setMessageTime(time);
                            chat1.setSeen(isSeen);
                            chatArrayList.add(chat1);
                        }
                    }
                    iGetListener.onSuccess(chatArrayList);
                }else {
                    iGetListener.onFailed(null);
                }
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

                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void getMessageForList(String whoSend, IGetMessageArrayListListener iGetListener) {

        userIdList = new ArrayList<>();
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
                    personArrayList.clear();
                    hashMapArrayList.clear();
                    userIdList.clear();
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

                        }

                            if (senderID.equals(firebaseUser.getUid().toString())) {
                                int counter = 0;
                                if (userIdList.size() != 0) {
                                    for (String id : userIdList) {
                                        if (!id.equals(receiverID.toString())) {
                                            counter++;
                                        }
                                        if (counter == userIdList.size()) {
                                            userIdList.add(receiverID);

                                        }
                                    }
                                } else {

                                    userIdList.add(receiverID);

                                }
                            }
                            if (receiverID.equals(firebaseUser.getUid().toString())) {
                                int counter = 0;
                                if (userIdList.size() != 0) {
                                    for (String id : userIdList) {
                                        if (!id.equals(senderID.toString())) {
                                            counter++;
                                        }
                                        if (counter == userIdList.size()) {

                                            userIdList.add(senderID);

                                        }
                                    }
                                } else {

                                    userIdList.add(senderID);
                                }


                            }

                    }
                    if (whoSend.equals("expert")) {


                            FireBaseExpertDal fireBaseExpertDal = new FireBaseExpertDal();
                            fireBaseExpertDal.getExpertQuery(new IGetQueryListener() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocument) {
                                    for (int i = 0; i < userIdList.size(); i++) {
                                        for (DocumentSnapshot document : queryDocument.getDocuments()) {
                                            Map<String, Object> data = document.getData();
                                            String firstName = (String) data.get("firstName");
                                            String lastName = (String) data.get("lastName");
                                            String email = (String) data.get("email");
                                            String about = (String) data.get("about");
                                            String expertUid = (String) data.get("expertUid");
                                            String department = (String) data.get("department");
                                            String imageProfile = (String) data.get("profileImage");
                                            String token = (String) data.get("token");
                                            Uri uriImage = null;
                                            if (imageProfile != null) {
                                                uriImage = Uri.parse(imageProfile);
                                            }


                                            Person expert = new Person();
                                            expert.set_firstName(firstName);
                                            expert.set_lastName(lastName);
                                            expert.set_email(email);
                                            expert.set_profileImage(uriImage);
                                            expert.set_ID(expertUid);
                                            expert.set_token(token);

                                            if (userIdList.get(i).equals(expertUid)) {
                                                personArrayList.add(expert);

                                            }

                                        }

                                    }
                                    iGetListener.onSuccess(new MessageArrayList(personArrayList, hashMapArrayList));

                                }


                                @Override
                                public void onFailed(Exception e) {

                                }
                            });


                    } else {

                            FireBaseUserDal fireBaseUserDal = new FireBaseUserDal();

                            fireBaseUserDal.getAllUserQuery(new IGetQueryListener() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocument) {
                                    for (int i = 0; i < userIdList.size(); i++) {
                                        for (DocumentSnapshot document : queryDocument.getDocuments()) {
                                            Map<String, Object> data = document.getData();
                                            String firstName = (String) data.get("firstName");
                                            String lastName = (String) data.get("lastName");
                                            String email = (String) data.get("email");
                                            String userID = (String) data.get("userUid");
                                            String profileImage = (String) data.get("profileImage");
                                            String token = (String) data.get("token");
                                            Uri uriProfile = null;
                                            if (profileImage != null) {
                                                uriProfile = Uri.parse(profileImage);
                                            }

                                            Person user = new Person();
                                            user.set_firstName(firstName);
                                            user.set_lastName(lastName);
                                            user.set_ID(userID);
                                            user.set_profileImage(uriProfile);
                                            user.set_token(token);

                                            if (userIdList.get(i).equals(userID)) {

                                                personArrayList.add(user);

                                            }
                                        }
                                    }
                                    iGetListener.onSuccess(new MessageArrayList(personArrayList, hashMapArrayList));
                                }

                                @Override
                                public void onFailed(Exception e) {
                                    iGetListener.onFailed(e);
                                }
                            });
                    }



                }else{
                    iGetListener.onFailed(null);
                }
            }
        });
    }

}
