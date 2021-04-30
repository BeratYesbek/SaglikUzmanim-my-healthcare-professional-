package com.saglikuzmanimm.saglikuzmanim.Core.Concrete.FireBaseDataBase;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Collection;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Expert;
import com.saglikuzmanimm.saglikuzmanim.Core.Abstract.IFirebaseExpertDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetExpertListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetQueryListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;
import com.saglikuzmanimm.saglikuzmanim.ui.ExpertMainUI.ProfileFragmentForExpert;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class FirebaseExpertDal implements IFirebaseExpertDal<Expert, IResult, IGetExpertListener> {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;

    @Override
    public void addData(Expert entity, IResult iResult) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("firstName", entity.get_firstName());
        hashMap.put("lastName", entity.get_lastName());
        hashMap.put("tcNumber", entity.getTcNumber());
        hashMap.put("department", entity.get_department());
        hashMap.put("email", entity.get_email());
        hashMap.put("password", entity.get_password());
        hashMap.put("point", 0);
        hashMap.put("appointmentPrice", 100);
        hashMap.put("Agreement", "accepted");
        hashMap.put("check_expert", false);
        hashMap.put("type", "expert");
        hashMap.put("expertUid", firebaseAuth.getCurrentUser().getUid());


        firebaseFirestore.collection("Expert_users").document(firebaseAuth.getCurrentUser().getUid()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void updateData(Expert entity, IResult iResult) {

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
            if (entity.get_point() != null) {
                hashMap.put("point", entity.get_point());
                System.out.println(entity.get_point());
            }


            DocumentReference washingtonRef = firebaseFirestore.collection("Expert_users").document(entity.get_ID());
            washingtonRef
                    .update(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
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
    public void delete(Expert entity, IResult iResult) {

    }

    @Override
    public void getData(Expert entity, IGetExpertListener iGetListener) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore.collection("Expert_users").whereEqualTo("expertUid", entity.get_ID()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                if (!querySnapshot.isEmpty()) {
                    ArrayList<Expert> expertArrayList = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {

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
                        Float appointmentPriceFloat = null;
                        Float pointFloat = Float.parseFloat(point.toString());

                        if (appointmentPrice != null) {
                            appointmentPriceFloat = Float.parseFloat(appointmentPrice.toString());
                        }
                        if (expertVideo != null) {
                            uriVideo = Uri.parse(expertVideo);
                        }
                        if (profileImage != null) {
                            uriImage = Uri.parse(profileImage);
                        }
                        expertArrayList.add(new Expert(firstName, lastName, email, department, type, about, appointmentPriceFloat, pointFloat, check_expert, expertUid, uriImage, uriVideo));
                        iGetListener.onSuccess(expertArrayList);

                    }
                } else {
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
    public void getExpertById(String expertUid, IGetExpertListener iGetListener) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore.collection("Expert_users").whereEqualTo("expertUid", expertUid).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()) {
                    ArrayList<Expert> expertArrayList = new ArrayList<>();
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
                        Float appointmentPriceFloat = null;
                        Float pointFloat = Float.parseFloat(point.toString());

                        if (appointmentPrice != null) {
                            appointmentPriceFloat = Float.parseFloat(appointmentPrice.toString());
                        }
                        if (expertVideo != null) {
                            uriVideo = Uri.parse(expertVideo);
                        }
                        if (profileImage != null) {
                            uriImage = Uri.parse(profileImage);
                        }
                        expertArrayList.add(new Expert(firstName, lastName, email, department, type, about, appointmentPriceFloat, pointFloat, check_expert, expertUid, uriImage, uriVideo));
                        iGetListener.onSuccess(expertArrayList);

                    }
                } else {
                    iGetListener.onFailed(null);
                }
            }
        });

    }


    @Override
    public void insertCertificateImage(Expert entity, IResult iResult) {
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
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                }
            });
        }

    }

    @Override
    public void createExpertAccount(Expert entity, IResult iResult) {
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(entity.get_email(), entity.get_password()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(entity.get_firstName() + " " + entity.get_lastName()).build();
                user.updateProfile(profileUpdates);
                insertCertificateImage(entity, new IResult() {
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
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void updateExpertProfileImage(Expert entity, IResult iResult) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        if (entity.get_profileImage() != null) {
            final String url = "image_expert_profile/" + firebaseAuth.getCurrentUser().getUid() + "/" + "profileImage" + ".jpg";
            firebaseStorage.getReference().child(url).putFile(entity.get_profileImage()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    getExpertProfileImage(entity, new IResult() {
                        @Override
                        public void onSuccess() {
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

                        @Override
                        public void onFailed(Exception exception) {
                            iResult.onFailed(exception);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    iResult.onFailed(e);

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
    public void getExpertProfileImage(Expert entity, IResult iResult) {
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        final String url = "image_expert_profile/" + firebaseAuth.getCurrentUser().getUid() + "/" + "profileImage" + ".jpg";
        firebaseStorage.getReference().child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                entity.set_profileImage(uri);
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
    public void uploadExpertVideo(Expert entity, IResult iResult) {
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        final String url = "image_expert_video/" + firebaseAuth.getCurrentUser().getUid() + "/" + "expertVideo" + ".jpg";
        firebaseStorage.getReference().child(url).putFile(entity.get_expertVideo()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                entity.set_ID(FirebaseAuth.getInstance().getCurrentUser().getUid());
                getExpertVideo(entity, new IResult() {
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
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                ProfileFragmentForExpert.setVideoProgressBar(progress);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                iResult.onFailed(exception);
            }
        });
    }

    @Override
    public void getExpertVideo(Expert entity, IResult iResult) {
        firebaseStorage = FirebaseStorage.getInstance();

        final String url = "image_expert_video/" + firebaseAuth.getCurrentUser().getUid() + "/" + "expertVideo" + ".jpg";
        firebaseStorage.getReference().child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                entity.set_expertVideo(uri);

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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                iResult.onFailed(exception);
            }
        });
    }

    @Override
    public void getExpertQuery(IGetQueryListener iGetQueryListener) {

        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Expert_users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                if (!querySnapshot.isEmpty()) {

                    iGetQueryListener.onSuccess(querySnapshot);
                    System.out.println("516777777");

                } else {
                    iGetQueryListener.onFailed(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iGetQueryListener.onFailed(e);
            }
        });

    }

    @Override
    public <C extends Collection> void getAllExpert(C entity, IGetExpertListener iGetListener) {
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

                            Uri uri_profileImage = null;
                            Uri uri_expertVideo = null;
                            Float float_appointment_price = null;
                            if (appointment_price != null) {
                                float_appointment_price = Float.parseFloat(appointment_price.toString());
                            }
                            Float float_point = Float.parseFloat(point.toString());

                            if (profileImage != null) {
                                uri_profileImage = Uri.parse(profileImage);
                            }
                            if (expertVideoUri != null) {
                                uri_expertVideo = Uri.parse(expertVideoUri);
                            }
                            arrayListExpert.add(new Expert(firstName, lastName, email, department, "expert", token, about, float_appointment_price, float_point, check_expert, expertUid, uri_profileImage, uri_expertVideo));


                        }
                        iGetListener.onSuccess(arrayListExpert);
                    } else {
                        iGetListener.onFailed(null);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    System.out.println(exception.toString());
                    iGetListener.onFailed(exception);
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
                                arrayListExpert.add(new Expert(firstName, lastName, email, department, "expert", token, about, float_appointment_price, float_point, check_expert, expertUid, uri_profileImage, uri_expertVideo));


                            } catch (Exception exception) {
                                break;
                            }
                        }
                        iGetListener.onSuccess(arrayListExpert);
                    } else {
                        iGetListener.onFailed(null);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    iGetListener.onFailed(exception);
                }
            });
        }
    }


}
