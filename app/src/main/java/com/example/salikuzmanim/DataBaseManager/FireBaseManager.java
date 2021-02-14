package com.example.salikuzmanim.DataBaseManager;

public class FireBaseManager {
/*
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private Query.Direction OrderBy;
    private int counter = 0;

    @Override
    public void getUser() {

        firebaseFirestore.collection("users").whereEqualTo("Email", firebaseAuth.getCurrentUser().getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    Map<String, Object> data = document.getData();
                    String firstName = (String) data.get("FirstName");
                    String lastName = (String) data.get("LastName");
                    String email = (String) data.get("Email");


                    User user = new User();
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setEmail(email);
                    getUserProfile(user);

                }
            }
        });
    }

    public void getUserProfile(User user) {
        final String url = "image_user_profile/" + firebaseAuth.getCurrentUser().getUid() + "/" + "profileImage.jpg";
        firebaseStorage.getReference().child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String _email = user.getEmail();
                String _firstName = user.getFirstName();
                String _lastName = user.getLastName();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    @Override
    public void insertUser(final User user) {


        final HashMap<String, Object> users = new HashMap<>();
        users.put("FirstName", user.getFirstName());
        users.put("LastName", user.getLastName());
        users.put("TcNumber", user.getTcNumber());
        users.put("Email", user.getEmail());
        users.put("Password", user.getPassword());
        users.put("Agreement", "accepted");
        users.put("type", "user");
        users.put("userUid", firebaseAuth.getCurrentUser().getUid());

        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                addDeafultProfile(user);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });

    }

    @Override
    public void updateUser(User user) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("FirstName", user.getFirstName());
        hashMap.put("LastName", user.getLastName());

        DocumentReference washingtonRef = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid());
        washingtonRef
                .update(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        FirebaseUser _user = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(user.getFirstName() + " " + user.getLastName()).build();
                        _user.updateProfile(profileUpdates);
                        getAdUserDocId(user.getFirstName(), user.getLastName());
                        uploadPictureUser(new User(user.getFirstName(), user.getLastName(), user.getEmail(), user.getProfileImage()));

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                profileEditActivityForUser.access(false);
                Log.w(TAG, "Error updating document", e);
            }
        });


    }


    @Override
    public void uploadPictureUser(User user) {

        if (user.getProfileImage() != null) {
            final String url = "image_user_profile/" + firebaseAuth.getCurrentUser().getUid() + "/" + "profileImage" + ".jpg";
            firebaseStorage.getReference().child(url).putFile(user.getProfileImage()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    profileEditActivityForUser.access(true);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    profileEditActivityForUser.access(false);
                }
            });
        } else {
            profileEditActivityForUser.access(true);
        }

    }

    @Override
    public void updateEmailAndPasswordUser(User user) {

        FirebaseUser _user = FirebaseAuth.getInstance().getCurrentUser();
        _user.updateEmail(user.getEmail())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updateUser(new User(user.getFirstName(), user.getLastName(), user.getEmail(), user.getProfileImage()));


                        } else {
                            profileEditActivityForUser.access(false);
                        }
                    }
                });
    }


    @Override
    public void deleteUser(User user) {


    }

    @Override
    public void insertExpert(Expert expert) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("firstName", expert.getFirstName());
        hashMap.put("lastName", expert.getLastName());
        hashMap.put("tcNumber", expert.getTcNumber());
        hashMap.put("department", expert.getDepartmant());
        hashMap.put("email", expert.getEmail());
        hashMap.put("password", expert.getPassword());
        hashMap.put("point", 0);
        hashMap.put("Agreement", "accepted");
        hashMap.put("check_expert", false);
        hashMap.put("type", "expert");
        System.out.println(expert.getEmail());

        firebaseFirestore.collection("Expert_users").document(firebaseAuth.getCurrentUser().getUid()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                UploadDiplomaPicture(expert);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                SignUpGiveServiceActivity.accessActivity(false);

            }
        });

    }

    @Override
    public void getExpert() {
        Expert expert = new Expert();
        firebaseFirestore.collection("Expert_users").whereEqualTo("email", firebaseAuth.getCurrentUser().getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> data = document.getData();
                        String firstName = (String) data.get("firstName");
                        String lastName = (String) data.get("lastName");
                        String email = (String) data.get("email");
                        String about = (String) data.get("about");
                        String departmant = (String) data.get("department");
                        Object appointmentPrice = data.get("appointmentPrice");
                        float price;
                        if (appointmentPrice == null) {
                            price = 0;
                        } else {
                            price = Float.parseFloat(appointmentPrice.toString());
                        }

                        boolean check_circle = (boolean) data.get("check_expert");
                        expert.setFirstName(firstName);
                        expert.setLastName(lastName);
                        expert.setEmail(email);
                        expert.setAbout(about);
                        expert.setDepartmant(departmant);
                        expert.setCheck_circle(check_circle);
                        expert.setAppointmentPrice(price);
                        getExpertProfile(expert);
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    int t = 0;
    int getDocumentSizeForExpert = 0;
    ArrayList<String> DocumentIdForExpert = new ArrayList<>();

    @Override
    public void getExpertForUser(String choiceDepartment, String accordingToWhat, Object orderBy) {

        if (accordingToWhat == null || orderBy == null) {
            firebaseFirestore.collection("Expert_users").whereEqualTo("department", choiceDepartment)
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    getDocumentSizeForExpert = queryDocumentSnapshots.size();
                    if (!queryDocumentSnapshots.isEmpty()) {

                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {

                            Map<String, Object> data = document.getData();
                            String documentId = document.getId();
                            String expertUid = documentId;
                            String email = (String) data.get("email");
                            String firstName = (String) data.get("firstName");
                            String lastName = (String) data.get("lastName");
                            String departmant = (String) data.get("department");
                            String about = (String) data.get("about");
                            Object point = (Object) data.get("point");


                            Float f = Float.parseFloat(point.toString());

                            Boolean check_circle = (Boolean) data.get("check_expert");

                            DocumentIdForExpert.add(documentId);
                            t++;

                        //   ReyclerViewShowExpertForUserActivity.accessReyclerView(new Expert(firstName, lastName, email, departmant, about, f, check_circle, expertUid), true);
                            if (t == queryDocumentSnapshots.size()) {
                                t = 0;
                                getVideoExpertForUser();

                            }
                        }
                    } else {
                      //  ReyclerViewShowExpertForUserActivity.accessReyclerView(null, false);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println(e.toString());
                }
            });


        } else {
            firebaseFirestore.collection("Expert_users").whereEqualTo("department", choiceDepartment).orderBy(accordingToWhat, (Query.Direction) orderBy)
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    getDocumentSizeForExpert = queryDocumentSnapshots.size();
                    if (!queryDocumentSnapshots.isEmpty()) {

                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {

                            Map<String, Object> data = document.getData();
                            String documentId = document.getId();
                            String expertUid = documentId;
                            String email = (String) data.get("email");
                            String firstName = (String) data.get("firstName");
                            String lastName = (String) data.get("lastName");
                            String departmant = (String) data.get("department");
                            String about = (String) data.get("about");
                            Object point = (Object) data.get("point");
                            Float f = Float.parseFloat(point.toString());

                            Boolean check_circle = (Boolean) data.get("check_expert");

                            DocumentIdForExpert.add(documentId);
                            t++;
                         //   ReyclerViewShowExpertForUserActivity.accessReyclerView(new Expert(firstName, lastName, email, departmant, about, f, check_circle, expertUid), true);
                            if (t == queryDocumentSnapshots.size()) {
                                t = 0;
                                getVideoExpertForUser();

                            }
                        }
                    } else {
             //           ReyclerViewShowExpertForUserActivity.accessReyclerView(null, false);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println(e.toString());
                }
            });
        }


    }

    public void getVideoExpertForUser() {

        final String url = "image_expert_video/" + DocumentIdForExpert.get(t) + "/" + "expertVideo" + ".jpg";
        firebaseStorage.getReference().child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                t++;
               // ReyclerViewShowExpertForUserActivity.accessVideoUri(uri, true);
                if (t == getDocumentSizeForExpert) {
                    t = 0;
                    getExpertProfileForUser();
                } else {
                    getVideoExpertForUser();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                t++;
           //     ReyclerViewShowExpertForUserActivity.accessVideoUri(null, false);
                if (t == getDocumentSizeForExpert) {
                    t = 0;
                    getExpertProfileForUser();
                } else {
                    getVideoExpertForUser();
                }
            }
        });
    }


    public void getExpertProfileForUser() {

        final String url = "image_expert_profile/" + DocumentIdForExpert.get(t) + "/" + "profileImage" + ".jpg";
        System.out.println(DocumentIdForExpert.get(t));
        firebaseStorage.getReference().child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println("123");
                t++;
                if (t == getDocumentSizeForExpert) {
                    System.out.println(uri);
              //      ReyclerViewShowExpertForUserActivity.accessImage(uri, true);
                } else {
               //     ReyclerViewShowExpertForUserActivity.accessImage(uri, false);
                    getExpertProfileForUser();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.toString());
            }
        });
    }

    @Override
    public void getExpertProfile(Expert expert) {

        final String url = "image_expert_profile/" + firebaseAuth.getCurrentUser().getUid() + "/" + "profileImage" + ".jpg";
        firebaseStorage.getReference().child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                expert.setProfileImage(uri);
                ProfileFragment.accessFragment(expert, true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ProfileFragment.accessFragment(expert, false);
            }
        });
    }

    @Override
    public void updateExpert(Expert expert) {
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("about", expert.getAbout());
        hashMap.put("appointmentPrice", expert.getAppointmentPrice());


        DocumentReference washingtonRef = firebaseFirestore.collection("Expert_users").document(firebaseAuth.getCurrentUser().getUid());
        washingtonRef
                .update(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        UploadPictureExpert(expert);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                profileEditActivityForExpert.accessActivity(false);
                Log.w(TAG, "Error updating document", e);
            }
        });


    }

    @Override
    public void deleteExpert(Expert expert) {

    }

    @Override
    public void UploadPictureExpert(Expert expert) {
        if (expert.getProfileImage() != null) {
            final String url = "image_expert_profile/" + firebaseAuth.getCurrentUser().getUid() + "/" + "profileImage" + ".jpg";
            firebaseStorage.getReference().child(url).putFile(expert.getProfileImage()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    profileEditActivityForExpert.accessActivity(true);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } else {
            profileEditActivityForExpert.accessActivity(true);
        }

    }

    @Override
    public void UploadDiplomaPicture(Expert expert) {


        Uri[] uri = new Uri[2];
        uri[0] = expert.getDiplomaImage();
        uri[1] = expert.getIdCardImage();

        for (int i = 0; i < 2; i++) {
            UUID uuid = UUID.randomUUID();
            System.out.println(uri[i]);
            System.out.println(expert.getEmail());
            final String imageName = "images_expert_diploma/" + firebaseAuth.getCurrentUser().getUid() + "/" + uuid + ".jpg";
            int finalI = i;
            firebaseStorage.getReference().child(imageName).putFile(uri[i])
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (finalI == 1) {
                                addDeafultProfile(expert);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println(e.toString());
                    SignUpGiveServiceActivity.accessActivity(false);
                }
            });
        }


    }

    @Override
    public void uploadVideoExpert(Expert expert) {

        final String url = "image_expert_video/" + firebaseAuth.getCurrentUser().getUid() + "/" + "expertVideo" + ".jpg";
        firebaseStorage.getReference().child(url).putFile(expert.getExpertVideo()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                ProfileFragment.accessSecondFragment(progress);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    @Override
    public void getVideoExpert() {
        final String url = "image_expert_video/" + firebaseAuth.getCurrentUser().getUid() + "/" + "expertVideo" + ".jpg";
        firebaseStorage.getReference().child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ProfileFragment.AccessProfileFragmentGetVideoUri(uri, true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ProfileFragment.AccessProfileFragmentGetVideoUri(null, false);
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void insertAd(JobAdvertisement jobAdvertisement) {

        DateimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("title", jobAdvertisement.getTitle());
        hashMap.put("explanation", jobAdvertisement.getExplanation());
        hashMap.put("departmant_ad", jobAdvertisement.getDepartmant_ad());
        hashMap.put("location_ad", jobAdvertisement.getLocation_ad());
        hashMap.put("email", firebaseAuth.getCurrentUser().getEmail());
        hashMap.put("user_Uid", firebaseAuth.getCurrentUser().getUid());
        hashMap.put("date", FieldValue.serverTimestamp());
        hashMap.put("dateForText", dtf.format(now));
        hashMap.put("ad_url", jobAdvertisement.getAd_url());
        hashMap.put("name", firebaseAuth.getCurrentUser().getDisplayName());


        firebaseFirestore.collection("user_ad").add(hashMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                AdAddActivity.accessActivity(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                AdAddActivity.accessActivity(false);
            }
        });


    }

    @Override
    public void updateAd(JobAdvertisement jobAdvertisement) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("title", jobAdvertisement.getTitle());
        hashMap.put("explanation", jobAdvertisement.getExplanation());
        hashMap.put("location_ad", jobAdvertisement.getLocation_ad());
        hashMap.put("departmant_ad", jobAdvertisement.getDepartmant_ad());

        DocumentReference documentReference = firebaseFirestore.collection("user_ad").document(jobAdvertisement.getDocumentID());
        documentReference.update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AdUpdateActivity.accessActivity(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                AdUpdateActivity.accessActivity(false);
            }
        });


    }

    @Override
    public void deleteAd(JobAdvertisement jobAdvertisement) {

    }

    int a = 0;

    @Override
    public void getAdForUser() {

        JobAdvertisement _jobAdvertisement = new JobAdvertisement();
        String user_email = firebaseAuth.getCurrentUser().getEmail();
        firebaseFirestore.collection("user_ad").whereEqualTo("user_Uid", firebaseAuth.getCurrentUser().getUid())
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {

                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                Map<String, Object> data = document.getData();
                                String documentID = (String) document.getId();
                                String title = (String) data.get("title");
                                String explanation = (String) data.get("explanation");
                                String departmant_ad = (String) data.get("departmant_ad");
                                String location_ad = (String) data.get("location_ad");
                                String date = (String) data.get("dateForText");
                                String ad_url = (String) data.get("ad_url");


                                _jobAdvertisement.setDepartmant_ad(departmant_ad);
                                _jobAdvertisement.setExplanation(explanation);
                                _jobAdvertisement.setTitle(title);
                                _jobAdvertisement.setLocation_ad(location_ad);
                                _jobAdvertisement.setDate(date);
                                _jobAdvertisement.setDocumentID(documentID);
                                _jobAdvertisement.setAd_url(ad_url);
                                a++;

                                if (a == queryDocumentSnapshots.size()) {

                                    listFragment.access(_jobAdvertisement, true);
                                } else {
                                    listFragment.access(_jobAdvertisement, false);
                                }
                            }
                        } else {
                            listFragment.access2();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.toString());

            }
        });

    }


    ArrayList<String> user_uid = new ArrayList<>();
    int i = 0;
    int documentSize;

    public void getUserProfileForAd() {
        System.out.println(user_uid.get(i));
        final String url = "image_user_profile/" + user_uid.get(i) + "/" + "profileImage.jpg";
        firebaseStorage.getReference().child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                i++;
                if (i == documentSize) {
                    ReyclerViewShowAddForExpertActivity.accessImage(uri, true);
                } else {
                    ReyclerViewShowAddForExpertActivity.accessImage(uri, false);
                    getUserProfileForAd();
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    @Override
    public void getAdForExpert(JobAdvertisement jobAdvertisement) {
        /*
        System.out.println(jobAdvertisement.getDepartmant_ad());
        System.out.println(jobAdvertisement.getLocation_ad());

        firebaseFirestore.collection("user_ad")
                .whereEqualTo("departmant_ad", jobAdvertisement.getDepartmant_ad())
                .whereEqualTo("location_ad", jobAdvertisement.getLocation_ad())
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        documentSize = queryDocumentSnapshots.size();
                        if (!queryDocumentSnapshots.isEmpty()) {
                            int j = 0;
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {

                                Map<String, Object> data = document.getData();
                                String _title = (String) data.get("title");
                                String _explanation = (String) data.get("explanation");
                                String _location = (String) data.get("location_ad");
                                String _depatmant = (String) data.get("departmant_ad");
                                String _date = (String) data.get("dateForText");
                                String _name = (String) data.get("name");
                                String user_email = (String) data.get("email");
                                String _user_uid = (String) data.get("user_Uid");
                                String ad_url = (String) data.get("ad_url");
                                user_uid.add(_user_uid);
                                ReyclerViewShowAddForExpertActivity.accessData(new JobAdvertisement(_title, _explanation, _location, _depatmant, _date, _name, ad_url), true);
                                j++;

                                if (j == documentSize) {
                                    getUserProfileForAd();
                                }
                            }
                        } else {


                            ReyclerViewShowAddForExpertActivity.accessData(new JobAdvertisement(), false);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.toString());
                ReyclerViewShowAddForExpertActivity.accessData(null, false);

            }
        });

    }


    @Override
    public void createAccount(final Person person) {


        firebaseAuth.createUserWithEmailAndPassword(person.getEmail(), person.getPassword()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(person.getFirstName() + " " + person.getLastName()).build();
                user.updateProfile(profileUpdates);
                addDeafultProfile(person);


                if (person.getType() == "expert") {
                    insertExpert((Expert) person);
                } else {
                    insertUser((User) person);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (person.getType() == "user") {
                    SignUpReceivingServiceActivity.access(false);
                } else {
                    SignUpGiveServiceActivity.accessActivity(false);
                }

            }
        });


    }


    public void addDeafultProfile(Person person) {

        Uri uri = Uri.parse("android.resource://com.example.salikuzmanim/drawable/profile");
        final String url = "image_" + person.getType() + "_profile/" + firebaseAuth.getCurrentUser().getUid() + "/" + "profileImage" + ".jpg";
        System.out.println("sadsadsadsadsadsadda111111");
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        firebaseStorage.getReference().child(url).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (person.getType() == "user") {

                    SignUpReceivingServiceActivity.access(true);
                } else {
                    SignUpGiveServiceActivity.accessActivity(true);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (person.getType() == "user") {
                    SignUpReceivingServiceActivity.access(false);
                } else {
                    SignUpGiveServiceActivity.accessActivity(false);
                }
            }
        });


    }


    int b = 0;
    ArrayList<String> documentId = new ArrayList<>();

    public void getAdUserDocId(String firstName, String lastName) {

        firebaseFirestore.collection("user_ad").whereEqualTo("user_Uid", firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> data = document.getData();
                        documentId.add(document.getId());
                        System.out.println(documentId.get(b));
                        b++;

                        if (b == queryDocumentSnapshots.size()) {
                            getAdUpdateName(firstName, lastName);
                        }
                    }
                }
            }
        });

    }

    public void getAdUpdateName(String firstName, String lastName) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", firstName + " " + lastName);
        for (int j = 0; j < documentId.size(); j++) {
            DocumentReference documentReference = firebaseFirestore.collection("user_ad").document(documentId.get(j));
            documentReference.update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void sendAppointment(Appointment appointment) {


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        final String uuid = UUID.randomUUID().toString().replace("-", "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("whosendUid", firebaseAuth.getCurrentUser().getUid());
        hashMap.put("toWhomUid", appointment.getToWhomUid());
        hashMap.put("date", appointment.getDate());
        hashMap.put("EXACT TIME", appointment.getEXACT_TIME());
        hashMap.put("time_to_send", appointment.getSend_to_time());
        hashMap.put("time_to_sendForOrder", FieldValue.serverTimestamp());
        hashMap.put("situation", false);
        hashMap.put("abort", false);
        hashMap.put("payment", false);
        hashMap.put("appointmentUUID", uuid);


        firebaseFirestore.collection("appointments").add(hashMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                AdapterShowEpertForUser.alertInfo(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    int index_appointment = 0;
    int calculater_size = 0;
    ArrayList<String> _whosendUid = new ArrayList<>();
    ArrayList<String> _toWhomUid = new ArrayList<>();

    @Override
    public void getAppointmentForExpert() {

        firebaseFirestore.collection("appointments").whereEqualTo("toWhomUid", firebaseAuth.getCurrentUser().getUid()).orderBy("time_to_sendForOrder", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                calculater_size = queryDocumentSnapshots.size();
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> data = document.getData();
                        String documentID = document.getId();
                        String appointmentUUID = (String) data.get("appointmentUUID");
                        String date = (String) data.get("date");
                        String time_to_send = (String) data.get("time_to_send");
                        String toWhomUid = (String) data.get("toWhomUid");
                        String whosendUid = (String) data.get("whosendUid");
                        Boolean abort = (Boolean) data.get("abort");
                        Boolean payment = (Boolean) data.get("payment");
                        Boolean situation = (Boolean) data.get("situation");
                        com.google.firebase.Timestamp EXACT_TIME = (com.google.firebase.Timestamp) data.get("EXACT TIME");
                        String whoAbort = (String) data.get("whoAbort");


                        _whosendUid.add(whosendUid);


                        AppointmentsFragment.AccessFragmet(new Appointment(whosendUid, toWhomUid, date, EXACT_TIME, situation, abort, payment, time_to_send, documentID, appointmentUUID, whoAbort), true);
                        index_appointment++;
                        if (index_appointment == calculater_size) {
                            index_appointment = 0;
                            getUserName();
                        }


                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.toString());
            }
        });
    }

    @Override
    public void getAppointmentForUser() {
        firebaseFirestore.collection("appointments").whereEqualTo("whosendUid", firebaseAuth.getCurrentUser().getUid()).orderBy("time_to_sendForOrder", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                calculater_size = queryDocumentSnapshots.size();
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> data = document.getData();
                        String documentID = document.getId();
                        String appointmentUUID = (String) data.get("appointmentUUID");
                        String date = (String) data.get("date");
                        String time_to_send = (String) data.get("time_to_send");
                        String toWhomUid = (String) data.get("toWhomUid");
                        String whosendUid = (String) data.get("whosendUid");
                        Boolean abort = (Boolean) data.get("abort");
                        Boolean payment = (Boolean) data.get("payment");
                        Boolean situation = (Boolean) data.get("situation");
                        Timestamp EXACT_TIME = (Timestamp) data.get("EXACT TIME");
                        String whoAbort = (String) data.get("whoAbort");


                        _toWhomUid.add(toWhomUid);


                        appointment_for_user.AccessFragment(new Appointment(whosendUid, toWhomUid, date, EXACT_TIME, situation, abort, payment, time_to_send, documentID, appointmentUUID, whoAbort), true);
                        index_appointment++;
                        if (index_appointment == calculater_size) {

                            index_appointment = 0;
                            getExpertName();
                        }

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.toString());
            }
        });
    }

    @Override
    public void deleteAppointment(Appointment appointment) {

    }

    @Override
    public void updateAppointment(Appointment appointment) {
        String documentID = appointment.getDocumentID();

        HashMap<String, Object> hashMap = new HashMap();

        hashMap.put("abort", appointment.getAbort());
        hashMap.put("payment", appointment.getPayment());
        hashMap.put("situation", appointment.getSituation());
        hashMap.put("whoAbort", appointment.getWhoAbort());

        firebaseFirestore.collection("appointments").document(documentID).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void sendMessage(Message message) {

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

    int indexGetMessage = 0;


    @Override
    public void getMessage(Message message) {

        firebaseFirestore.collection("Chats").orderBy("timestamp", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                if (!querySnapshot.isEmpty()) {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Map<String, Object> data = document.getData();
                        String senderID = (String) data.get("senderID");
                        String receiverID = (String) data.get("receiverID");
                        String time = (String) data.get("time");
                        String messages = (String) data.get("message");
                        String messageID = (String) data.get("messageID");
                        boolean isSeen = (boolean) data.get("isSeen");
                        indexGetMessage++;
                        if (receiverID.equals(message.getReciverID()) && senderID.equals(message.getSenderID()) ||
                                receiverID.equals(message.getSenderID()) && senderID.equals(message.getReciverID())) {

                        //    MessageActivity.accessMessageActivity(new Message(messages, messageID, receiverID, senderID, time, isSeen), false);

                        }
                        if (querySnapshot.size() == indexGetMessage) {
                           // MessageActivity.accessMessageActivity(null, true);
                        }
                    }
                } else {

                }
            }
        }).

                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e.toString());
                    }
                });


    }

    private ArrayList<String> userList;

    @Override
    public void getMessageForList(String whoSend) {
        userList = new ArrayList<>();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore.collection("Chats").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                if (!querySnapshot.isEmpty()) {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Map<String, Object> data = document.getData();
                        String senderID = (String) data.get("senderID");
                        String receiverID = (String) data.get("receiverID");
                        String time = (String) data.get("time");
                        String messages = (String) data.get("message");
                        String messageID = (String) data.get("messageID");


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
                            System.out.println("ilk");
                        }


                    }
                    getGetUserDetailForMessage(whoSend);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void getGetUserDetailForMessage(String whoSend) {
        if (whoSend.equals("user")) {
            firebaseFirestore.collection("Expert_users").whereEqualTo("expertUid", userList.get(counter)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    if (!querySnapshot.isEmpty()) {

                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {

                            Map<String, Object> data = document.getData();
                            String firstName = (String) data.get("firstName");
                            String lastName = (String) data.get("lastName");
                            String userID = (String) data.get("expertUid");
                            counter++;
                            Expert expert = new Expert();
                            expert.setFirstName(firstName);
                            expert.setLastName(lastName);
                            expert.setID(userID);
                           // messageFragmentForUser.accessListMessageFragment(expert);


                            if (counter != userList.size()) {
                                getGetUserDetailForMessage("user");
                            } else {
                                getExpertProfilImage();
                            }
                        }

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } else {
            firebaseFirestore.collection("users").whereEqualTo("userUid", userList.get(counter)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    if (!querySnapshot.isEmpty()) {

                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {

                            Map<String, Object> data = document.getData();
                            String firstName = (String) data.get("FirstName");
                            String lastName = (String) data.get("LastName");
                            String userID = (String) data.get("userUid");
                            counter++;
                            User user = new User();
                            user.setFirstName(firstName);
                            user.setLastName(lastName);
                            user.setID(userID);

                   //         messageActivityForExpert.accessListMessageActivity(user);
                            if (counter != userList.size()) {
                                getGetUserDetailForMessage("expert");
                            } else {
                                getUserProfilImage();
                            }
                        }

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }

    }

    int index_for_image = 0;

    public void getExpertProfilImage() {
        System.out.println(index_for_image);
        final String url = "image_expert_profile/" + userList.get(index_for_image) + "/" + "profileImage" + ".jpg";
        firebaseStorage.getReference().child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    index_for_image++;
                    if (userList.size() != index_for_image) {

                       // messageFragmentForUser.accessListMessageFragment2(uri, false);
                        getExpertProfilImage();

                    } else {
                      //  messageFragmentForUser.accessListMessageFragment2(uri, true);
                    }

                } catch (Exception e) {
                    System.out.println(e.toString());

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void getUserProfilImage() {

        final String url = "image_user_profile/" + userList.get(index_for_image) + "/" + "profileImage" + ".jpg";
        firebaseStorage.getReference().child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                index_for_image++;
                try {
                    if (userList.size() != index_for_image) {
                    //    messageActivityForExpert.accessListMessageActivity2(uri, false);
                        getUserProfilImage();

                    } else {
                   //     messageActivityForExpert.accessListMessageActivity2(uri, true);
                    }

                } catch (Exception e) {
                    System.out.println(e.toString());

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void seenMessages(final String userID) {
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


    public void getExpertName() {

        firebaseFirestore.collection("Expert_users").whereEqualTo("expertUid", _toWhomUid.get(index_appointment)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    String firstName = (String) document.get("firstName");
                    String lastName = (String) document.get("lastName");
                    String name = firstName + " " + lastName;
                    System.out.println(calculater_size);
                    index_appointment++;

                    if (index_appointment == calculater_size) {
                        appointment_for_user.AccessFragment2(name, true);

                    } else {
                        getExpertName();
                        appointment_for_user.AccessFragment2(name, false);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.toString());
            }
        });
    }

    public void getUserName() {
        firebaseFirestore.collection("users").whereEqualTo("userUid", _whosendUid.get(index_appointment)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    String firstName = (String) document.get("FirstName");
                    String lastName = (String) document.get("LastName");
                    String name = firstName + " " + lastName;
                    index_appointment++;
                    if (index_appointment == calculater_size) {
                        AppointmentsFragment.AccessFragment2(name, true);

                    } else {
                        AppointmentsFragment.AccessFragment2(name, false);
                        getUserName();
                    }
                }
            }
        });

    }
}
*/
}