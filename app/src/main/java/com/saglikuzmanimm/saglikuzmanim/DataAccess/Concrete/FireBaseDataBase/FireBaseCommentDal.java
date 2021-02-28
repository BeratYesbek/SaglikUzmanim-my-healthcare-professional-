package com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase;

import androidx.annotation.NonNull;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Comment;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract.ICommentDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetCommentListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireBaseCommentDal implements ICommentDal {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    @Override
    public void addData(Comment entity, IResult iResult) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("comment", entity.get_comment());
        hashMap.put("timestamp", FieldValue.serverTimestamp());
        hashMap.put("senderID", firebaseAuth.getCurrentUser().getUid());
        hashMap.put("receiverID", entity.get_receiverID());
        hashMap.put("point", entity.get_point());

        firebaseFirestore.collection("Comments").document().set(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        iResult.onSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void updateData(Comment entity, IResult iResult) {

    }

    @Override
    public void delete(Comment entity, IResult iResult) {

    }

    @Override
    public void getData(Comment entity, IGetCommentListener iGetListener) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        ArrayList<Comment> commentArrayList = new ArrayList<>();
        List<Float> list = new ArrayList<>();

        firebaseFirestore.collection("Comments").whereEqualTo("receiverID", entity.get_receiverID())
                .orderBy("timestamp", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                if (!querySnapshot.isEmpty()) {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Map<String, Object> data = document.getData();

                        String comment = (String) data.get("comment");
                        Timestamp timestamp = (Timestamp) data.get("timestamp");
                        Object point = (Object) data.get("point");
                        Float floatPoint = null;
                        if(point != null) {
                            floatPoint = Float.parseFloat(point.toString());
                            list.add(floatPoint);
                            System.out.println(floatPoint);
                        }
                        Comment _comment = new Comment();
                        _comment.set_comment(comment);
                        _comment.set_timestamp(timestamp);
                        _comment.set_point(floatPoint);

                        commentArrayList.add(_comment);
                    }

                    iGetListener.onSuccess(commentArrayList,list);
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
