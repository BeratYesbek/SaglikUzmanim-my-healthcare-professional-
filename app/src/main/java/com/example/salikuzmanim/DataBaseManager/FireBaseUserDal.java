package com.example.salikuzmanim.DataBaseManager;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.salikuzmanim.Concrete.User;
import com.example.salikuzmanim.Interfaces.FireBaseInsterfaces.IFireBaseUserDal;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetExpertDataListener;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetQueryListener;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetUserDataListener;
import com.example.salikuzmanim.Interfaces.IEntity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class FireBaseUserDal implements IFireBaseUserDal<User> {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;

    @Override
    public void insertUser(User entity, IGetUserDataListener iGetUserDataListener) {

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("firstName", entity.get_firstName());
        hashMap.put("lastName", entity.get_lastName());
        hashMap.put("TcNumber", entity.getTcNumber());
        hashMap.put("email", entity.get_email());
        hashMap.put("Password", entity.get_password());
        hashMap.put("Agreement", "accepted");
        hashMap.put("type", "user");
        hashMap.put("userUid", firebaseAuth.getCurrentUser().getUid());

        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                iGetUserDataListener.onSuccess(entity);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iGetUserDataListener.onFailed(e);

            }
        });


    }

    @Override
    public void getUserData(IGetUserDataListener iGetUserDataListener) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("users").whereEqualTo("userUid", firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    Map<String, Object> data = document.getData();
                    String firstName = (String) data.get("firstName");
                    String lastName = (String) data.get("lastName");
                    String email = (String) data.get("email");
                    String type = (String) data.get("type");
                    String userUid = (String) data.get("userUid");
                    String profileImage = (String) data.get("profileImage");
                    Uri uriProfile = null;
                    if (profileImage != null) {
                        uriProfile = Uri.parse(profileImage);
                        iGetUserDataListener.onSuccess(new User(firstName, lastName, email, type, userUid, uriProfile));
                    } else {
                        iGetUserDataListener.onSuccess(new User(firstName, lastName, email, type, userUid, uriProfile));
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iGetUserDataListener.onFailed(e);
            }
        });
    }

    @Override
    public void updateUser(User entity, IGetUserDataListener iGetUserDataListener) {


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("firstName", entity.get_firstName());
        hashMap.put("lastName", entity.get_lastName());

        if (entity.get_profileImage() != null) {
            hashMap.put("profileImage", entity.get_profileImage().toString());
        }
        DocumentReference documentReference = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid());
        documentReference.update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("3");
                iGetUserDataListener.onSuccess(entity);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.toString());
                iGetUserDataListener.onFailed(e);
            }
        });
    }

    @Override
    public void updateUserProfile(User entity, IGetUserDataListener iGetUserDataListener) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        System.out.println(entity.get_profileImage());
        if (entity.get_profileImage() != null) {
            final String url = "image_user_profile/" + firebaseAuth.getCurrentUser().getUid() + "/" + "profileImage" + ".jpg";
            firebaseStorage.getReference().child(url).putFile(entity.get_profileImage()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    getUserProfileImage(entity, new IGetExpertDataListener() {
                        @Override
                        public void onSuccess(IEntity entity) {
                            updateUser((User) entity, new IGetUserDataListener() {
                                @Override
                                public void onSuccess(IEntity entity) {
                                    iGetUserDataListener.onSuccess(entity);
                                }

                                @Override
                                public void onFailed(Exception e) {
                                    System.out.println(e.toString());
                                    iGetUserDataListener.onFailed(e);
                                }
                            });
                        }

                        @Override
                        public void onError(Exception exception) {
                            System.out.println(exception.toString());
                            iGetUserDataListener.onFailed(exception);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } else {


            updateUser(entity, new IGetUserDataListener() {
                @Override
                public void onSuccess(IEntity entity) {
                    iGetUserDataListener.onSuccess(entity);
                }

                @Override
                public void onFailed(Exception e) {
                    System.out.println(e.toString());
                    iGetUserDataListener.onFailed(e);
                }
            });
        }


    }


    @Override
    public void createUserAccount(User entity, IGetUserDataListener iGetUserDataListener) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(entity.get_email(), entity.get_password()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(entity.get_firstName() + " " + entity.get_lastName()).build();
                user.updateProfile(profileUpdates);
                insertUser(entity, new IGetUserDataListener() {
                    @Override
                    public void onSuccess(IEntity entity) {

                    }

                    @Override
                    public void onFailed(Exception e) {
                        iGetUserDataListener.onFailed(e);
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iGetUserDataListener.onFailed(e);
            }
        });
    }


    @Override
    public void getAllUser(IGetQueryListener iGetQueryListener) {

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        try {
            firebaseFirestore.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    if (!querySnapshot.isEmpty()) {
                        try {
                            iGetQueryListener.onSuccess(querySnapshot);
                        } catch (Exception e) {
                            iGetQueryListener.onFailed(e);
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    iGetQueryListener.onFailed(e);
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public void getUserProfileImage(User entity, IGetExpertDataListener iGetExpertDataListener) {
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        final String url = "image_user_profile/" + firebaseAuth.getCurrentUser().getUid() + "/" + "profileImage" + ".jpg";
        firebaseStorage.getReference().child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                entity.set_profileImage(uri);
                System.out.println("2");
                iGetExpertDataListener.onSuccess(entity);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.toString());
                iGetExpertDataListener.onError(e);
            }
        });
    }


}
