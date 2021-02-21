package com.example.salikuzmanim.DataBaseManager;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.salikuzmanim.Concrete.Expert;
import com.example.salikuzmanim.Concrete.Order;
import com.example.salikuzmanim.Interfaces.FireBaseInsterfaces.IFireBaseExpertDal;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetDataListener;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetExpertDataListener;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetListDataListener;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetUserDataListener;
import com.example.salikuzmanim.Interfaces.IEntity;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class FireBaseExpertDal implements IFireBaseExpertDal<Expert> {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;


    @Override
    public void insertExpert(Expert entity, IGetExpertDataListener iGetExpertDataListener) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("firstName", entity.get_firstName());
        hashMap.put("lastName", entity.get_lastName());
        hashMap.put("tcNumber", entity.getTcNumber());
        hashMap.put("department", entity.get_department());
        hashMap.put("email", entity.get_email());
        hashMap.put("password", entity.get_password());
        hashMap.put("point", 0);
        hashMap.put("Agreement", "accepted");
        hashMap.put("check_expert", false);
        hashMap.put("type", "expert");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Expert_users").document(firebaseAuth.getCurrentUser().getUid()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("create account 1");
                iGetExpertDataListener.onSuccess(entity);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iGetExpertDataListener.onError(e);
            }
        });
    }

    @Override
    public void updateExpert(Expert entity, IGetExpertDataListener iGetExpertDataListener) {
        try {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseFirestore = FirebaseFirestore.getInstance();
            HashMap<String, Object> hashMap = new HashMap<>();
            if (entity.get_about() != null) {
                hashMap.put("about", entity.get_about());
            }
            if (entity.get_appointmentPrice() != null) {
                hashMap.put("appointmentPrice", entity.get_appointmentPrice());
            }
            if (entity.get_profileImage() != null) {
                hashMap.put("profileImage", entity.get_profileImage().toString());
            }
            if (entity.get_expertVideo() != null) {
                hashMap.put("expertVideoUri", entity.get_expertVideo().toString());
            }


            DocumentReference washingtonRef = firebaseFirestore.collection("Expert_users").document(firebaseAuth.getCurrentUser().getUid());
            washingtonRef
                    .update(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                            iGetExpertDataListener.onSuccess(entity);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("update " + e.toString());
                    iGetExpertDataListener.onError(e);

                    Log.w(TAG, "Error updating document", e);

                }
            });
        } catch (Exception e) {

        }

    }

    @Override
    public void getExpertData(String expertUid,IGetExpertDataListener iGetExpertDataListener) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        try {
            firebaseFirestore.collection("Expert_users").whereEqualTo("expertUid", expertUid).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (!value.isEmpty()) {
                        for (DocumentSnapshot document : value.getDocuments()) {

                            Map<String, Object> data = document.getData();

                            String firstName = (String) data.get("firstName");
                            String lastName = (String) data.get("lastName");
                            String email = (String) data.get("email");
                            String about = (String) data.get("about");
                            String department = (String) data.get("department");
                            String profileImage = (String) data.get("profileImage");
                            String expertUid = (String) data.get("expertUid");
                            String type = (String) data.get("type");
                            String expertVideo = (String) data.get("expertVideoUri");
                            String token = (String) data.get("token");
                            Boolean check_expert = (Boolean) data.get("check_expert");
                            Object point = (Object) data.get("point");
                            Object appointmentPrice = data.get("appointmentPrice");

                            Uri uriImage = null;
                            Uri uriVideo = null;
                            Float pointFloat = Float.parseFloat(point.toString());
                            Float appointmentPriceFloat = Float.parseFloat(appointmentPrice.toString());
                            try {
                                if (expertVideo != null) {
                                    uriVideo = Uri.parse(expertVideo);
                                }
                                if (profileImage != null) {
                                    uriImage = Uri.parse(profileImage);
                                }
                            } catch (Exception e) {
                                System.out.println(e.toString());
                            } finally {
                                iGetExpertDataListener.onSuccess(new Expert(firstName, lastName, email, department, type, about, appointmentPriceFloat, pointFloat, check_expert, expertUid, uriImage, uriVideo));
                            }
                        }
                    } else {
                        iGetExpertDataListener.onError(null);
                    }
                }
            });

        } catch (Exception e) {
            iGetExpertDataListener.onError(e);
        }
    }


    @Override
    public void insertCertificateImage(Expert entity, IGetExpertDataListener iGetExpertDataListener) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        Uri[] uri = new Uri[2];
        uri[0] = entity.get_diplomaImage();
        uri[1] = entity.get_idCardImage();

        for (int i = 0; i < 2; i++) {
            UUID uuid = UUID.randomUUID();

            final String imageName = "images_expert_diploma/" + firebaseAuth.getCurrentUser().getUid() + "/" + uuid + ".jpg";
            int finalI = i;
            firebaseStorage.getReference().child(imageName).putFile(uri[i])
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (finalI == 1) {
                                insertExpert(entity, new IGetExpertDataListener() {
                                    @Override
                                    public void onSuccess(IEntity entity) {
                                        System.out.println("create account 2");
                                        iGetExpertDataListener.onSuccess(entity);
                                    }

                                    @Override
                                    public void onError(Exception exception) {
                                        iGetExpertDataListener.onError(exception);
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                }
            });
        }

    }


    @Override
    public void createExpertAccount(Expert entity, IGetExpertDataListener iGetExpertDataListener) {
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(entity.get_email(), entity.get_password()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(entity.get_firstName() + " " + entity.get_lastName()).build();
                user.updateProfile(profileUpdates);
                insertCertificateImage(entity, new IGetExpertDataListener() {
                    @Override
                    public void onSuccess(IEntity entity) {
                        iGetExpertDataListener.onSuccess(entity);
                    }

                    @Override
                    public void onError(Exception exception) {
                        iGetExpertDataListener.onError(exception);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void updateExpertProfileImage(Expert entity, IGetExpertDataListener iGetExpertDataListener) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        try {
            if (entity.get_profileImage() != null) {
                final String url = "image_expert_profile/" + firebaseAuth.getCurrentUser().getUid() + "/" + "profileImage" + ".jpg";
                firebaseStorage.getReference().child(url).putFile(entity.get_profileImage()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        getExpertProfileImage(entity, new IGetExpertDataListener() {
                            @Override
                            public void onSuccess(IEntity entity) {
                                updateExpert((Expert) entity, new IGetExpertDataListener() {
                                    @Override
                                    public void onSuccess(IEntity entity) {
                                        iGetExpertDataListener.onSuccess(entity);
                                    }

                                    @Override
                                    public void onError(Exception exception) {
                                        iGetExpertDataListener.onError(exception);
                                    }
                                });
                            }

                            @Override
                            public void onError(Exception exception) {

                            }
                        });
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {

                    }
                });
            } else {
                updateExpert(entity, new IGetExpertDataListener() {
                    @Override
                    public void onSuccess(IEntity entity) {
                        iGetExpertDataListener.onSuccess(entity);
                    }

                    @Override
                    public void onError(Exception exception) {
                        iGetExpertDataListener.onError(exception);
                    }
                });
            }

        } catch (Exception e) {

        }

    }

    @Override
    public void getExpertProfileImage(Expert entity, IGetExpertDataListener iGetExpertDataListener) {
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        final String url = "image_expert_profile/" + firebaseAuth.getCurrentUser().getUid() + "/" + "profileImage" + ".jpg";
        firebaseStorage.getReference().child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                entity.set_profileImage(uri);
                iGetExpertDataListener.onSuccess(entity);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void uploadExpertVideo(Expert entity, IGetUserDataListener iGetUserDataListener) {
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        final String url = "image_expert_video/" + firebaseAuth.getCurrentUser().getUid() + "/" + "expertVideo" + ".jpg";
        firebaseStorage.getReference().child(url).putFile(entity.get_expertVideo()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getExpertVideo(entity, new IGetUserDataListener() {
                    @Override
                    public void onSuccess(IEntity entity) {
                        iGetUserDataListener.onSuccess(entity);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        iGetUserDataListener.onFailed(e);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                //ProfileFragment.accessSecondFragment(progress);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iGetUserDataListener.onFailed(e);
            }
        });
    }

    @Override
    public void getExpertVideo(Expert entity, IGetUserDataListener iGetUserDataListener) {

        firebaseStorage = FirebaseStorage.getInstance();

        final String url = "image_expert_video/" + firebaseAuth.getCurrentUser().getUid() + "/" + "expertVideo" + ".jpg";
        firebaseStorage.getReference().child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                entity.set_expertVideo(uri);
                updateExpert(entity, new IGetExpertDataListener() {
                    @Override
                    public void onSuccess(IEntity entity) {
                        iGetUserDataListener.onSuccess(entity);
                    }

                    @Override
                    public void onError(Exception exception) {
                        iGetUserDataListener.onFailed(exception);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public <E extends Order> void getAllExpert(E entity, IGetListDataListener iGetListDataListener) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        ArrayList<Expert> arrayListExpert = new ArrayList<>();

        if (entity.getAccording_to_what() == null || entity.getOrderBy() == null) {
            firebaseFirestore.collection("Expert_users").whereEqualTo("department", entity.getDepartment())
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    if (!queryDocumentSnapshots.isEmpty()) {

                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            Map<String, Object> data = document.getData();
                            String documentId = document.getId();
                            String expertUid = (String) data.get("expertUid");
                            String email = (String) data.get("email");
                            String firstName = (String) data.get("firstName");
                            String lastName = (String) data.get("lastName");
                            String department = (String) data.get("department");
                            String about = (String) data.get("about");
                            String profileImage = (String) data.get("profileImage");
                            String expertVideoUri = (String) data.get("expertVideoUri");
                            Object appointment_price = data.get("appointmentPrice");
                            Object point = (Object) data.get("point");
                            Boolean check_expert = (Boolean) data.get("check_expert");
                            String token = (String) data.get("token");
                            System.out.println("token01 " + token );
                            Uri uri_profileImage = null;
                            Uri uri_expertVideo = null;
                            try {
                                Float float_point = Float.parseFloat(point.toString());
                                Float float_appointment_price = Float.parseFloat(appointment_price.toString());
                                if (profileImage != null) {
                                    uri_profileImage = Uri.parse(profileImage);
                                }
                                if (expertVideoUri != null) {
                                    uri_expertVideo = Uri.parse(expertVideoUri);
                                }
                                arrayListExpert.add(new Expert(firstName, lastName, email, department, "expert", token,about, float_appointment_price, float_point, check_expert, expertUid, uri_profileImage, uri_expertVideo));


                            } catch (Exception e) {
                                iGetListDataListener.onFailed(e);
                                break;
                            }

                        }
                        iGetListDataListener.onSuccess(arrayListExpert);
                    } else {
                        iGetListDataListener.onFailed(null);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    iGetListDataListener.onFailed(null);
                    System.out.println(e.toString());
                }
            });


        } else {
            firebaseFirestore.collection("Expert_users").whereEqualTo("department", entity.getDepartment()).orderBy(entity.getAccording_to_what(), (Query.Direction) entity.getOrderBy())
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                    if (!queryDocumentSnapshots.isEmpty()) {

                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {

                            Map<String, Object> data = document.getData();
                            String documentId = document.getId();
                            String expertUid = documentId;
                            String email = (String) data.get("email");
                            String firstName = (String) data.get("firstName");
                            String lastName = (String) data.get("lastName");
                            String department = (String) data.get("department");
                            String about = (String) data.get("about");
                            String profileImage = (String) data.get("profileImage");
                            String expertVideoUri = (String) data.get("expertVideoUri");
                            String token = (String) data.get("token");
                            System.out.println("token02 " + token );
                            Object appointment_price = data.get("appointmentPrice");
                            Object point = (Object) data.get("point");
                            Boolean check_expert = (Boolean) data.get("check_expert");
                            Uri uri_profileImage = null;
                            Uri uri_expertVideo = null;
                            try {
                                Float float_point = Float.parseFloat(point.toString());
                                Float float_appointment_price = Float.parseFloat(appointment_price.toString());
                                if (profileImage != null) {
                                    uri_profileImage = Uri.parse(profileImage);
                                }
                                if (expertVideoUri != null) {
                                    uri_expertVideo = Uri.parse(expertVideoUri);
                                }
                                arrayListExpert.add(new Expert(firstName, lastName, email, department, "expert",token, about, float_appointment_price, float_point, check_expert, expertUid, uri_profileImage, uri_expertVideo));


                            } catch (Exception e) {
                                iGetListDataListener.onFailed(e);
                                break;

                            }

                        }
                        iGetListDataListener.onSuccess(arrayListExpert);
                    } else {
                        iGetListDataListener.onFailed(null);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    iGetListDataListener.onFailed(e);
                    System.out.println(e.toString());
                }
            });
        }
    }

    @Override
    public void getExpertList(IGetDataListener iGetDataListener) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        try {
            firebaseFirestore.collection("Expert_users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    if (!querySnapshot.isEmpty()) {
                        try {
                            iGetDataListener.onSuccess(querySnapshot);
                        } catch (Exception e) {
                        }
                    } else {
                        iGetDataListener.onFailed(null);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    iGetDataListener.onFailed(e);
                }
            });
        } catch (Exception e) {
            iGetDataListener.onFailed(e);
        }
    }


    public void getAllExpert(IGetDataListener iGetDataListener) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        try {
            firebaseFirestore.collection("Expert_users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    if (!querySnapshot.isEmpty()) {
                        try {
                            iGetDataListener.onSuccess(querySnapshot);
                        } catch (Exception e) {

                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } catch (Exception e) {
            System.out.println("hata 2: " + e.toString());
        }
    }


}
