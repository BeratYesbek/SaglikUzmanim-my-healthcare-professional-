package com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.saglikuzmanimm.saglikuzmanim.Concrete.User;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract.IUserDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetUserListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetQueryListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FireBaseUserDal implements IUserDal<User, IResult, IGetUserListener> {
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    @Override
    public void addData(User entity, IResult iResult) {
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
    public void updateData(User entity, IResult iResult) {
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
    public void delete(User entity, IResult iResult) {

    }

    @Override
    public void getData(User entity, IGetUserListener iGetListener) {
        ArrayList<User> userArrayList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("users").whereEqualTo("userUid", firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
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
                            userArrayList.add(new User(firstName, lastName, email, type, userUid, uriProfile));
                            iGetListener.onSuccess(userArrayList);
                        } else {
                            userArrayList.add(new User(firstName, lastName, email, type, userUid, uriProfile));
                            iGetListener.onSuccess(userArrayList);
                        }
                    }
                }else {
                    iGetListener.onFailed(null);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iGetListener.onFailed(e);
            }
        });
    }

    @Override
    public void updateUserProfile(User entity, IResult iResult) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        if (entity.get_profileImage() != null) {
            final String url = "image_user_profile/" + firebaseAuth.getCurrentUser().getUid() + "/" + "profileImage" + ".jpg";
            firebaseStorage.getReference().child(url).putFile(entity.get_profileImage()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    getUserProfileImage(entity, new IGetUserListener() {
                        @Override
                        public void onSuccess(ArrayList<User> userArrayList) {
                            User user = userArrayList.get(0);
                            updateData(user, new IResult() {
                                @Override
                                public void onSuccess() {
                                    iResult.onSuccess();
                                }

                                @Override
                                public void onFailed(Exception exception) {
                                    iResult.onFailed(exception);
                                }
                            });
                        }

                        @Override
                        public void onFailed(Exception exception) {
                            iResult.onFailed(exception);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    iResult.onFailed(exception);
                }
            });
        } else {

            updateData(entity, new IResult() {
                @Override
                public void onSuccess() {
                    iResult.onSuccess();
                }

                @Override
                public void onFailed(Exception exception) {
                    iResult.onFailed(exception);
                }
            });
        }
    }

    @Override
    public void getUserProfileImage(User entity, IGetUserListener iGetUserListener) {
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        final String url = "image_user_profile/" + firebaseAuth.getCurrentUser().getUid() + "/" + "profileImage" + ".jpg";
        firebaseStorage.getReference().child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                entity.set_profileImage(uri);
                ArrayList<User> arrayList = new ArrayList<>();
                arrayList.add(entity);
                iGetUserListener.onSuccess(arrayList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                iGetUserListener.onFailed(exception);
            }
        });
    }

    @Override
    public void createUserAccount(User entity, IResult iResult) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(entity.get_email(), entity.get_password()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(entity.get_firstName() + " " + entity.get_lastName()).build();
                user.updateProfile(profileUpdates);
                addData(entity, new IResult() {
                    @Override
                    public void onSuccess() {
                        iResult.onSuccess();
                    }

                    @Override
                    public void onFailed(Exception exception) {
                        iResult.onFailed(exception);
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                iResult.onFailed(exception);
            }
        });
    }

    @Override
    public <Q extends IGetQueryListener> void getAllUserQuery(Q iGetQueryListener) {

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        try {
            firebaseFirestore.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    if (!querySnapshot.isEmpty()) {
                        try {
                            iGetQueryListener.onSuccess(querySnapshot);
                            System.out.println("516666666");
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
}
