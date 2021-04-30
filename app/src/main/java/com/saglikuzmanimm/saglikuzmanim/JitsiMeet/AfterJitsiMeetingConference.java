package com.saglikuzmanimm.saglikuzmanim.JitsiMeet;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.AppointmentManager;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.CommentManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Appointment;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Comment;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.AppointmentDal;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.CommentDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;
import com.saglikuzmanimm.saglikuzmanim.R;
import com.squareup.picasso.Picasso;

public class AfterJitsiMeetingConference {
    //  after jitsi meeting here make a survey
    private Dialog dialogRating;
    private Dialog dialogComment;
    private Dialog dialogComplete;

    private String _documentID;
    private String _appointmentID;
    private String _expertUid;
    private String _expertName;
    private Uri _expertProfile;

    private Float _rating;
    private String _comment;
    private Boolean complete_check;


    public AfterJitsiMeetingConference(String documentID, String appointmentID, String expertUid, String expertName, Uri expertProfile) {
        this._documentID = documentID;
        this._appointmentID = appointmentID;
        this._expertUid = expertUid;
        this._expertName = expertName;
        this._expertProfile = expertProfile;
    }

    public void displayDialogComplete(Context context) {
        dialogComplete = new Dialog(context);
        dialogComplete.setContentView(R.layout.fragment_complete_meeting);
        dialogComplete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Switch s = dialogComplete.findViewById(R.id.switch_complete_fragment);
        Button btn_next = dialogComplete.findViewById(R.id.btn_fragment_complete_next);


        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                complete_check = isChecked;

            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogComplete.dismiss();
                updateAppointment();
                if (complete_check == true) {
                    displayDialogRating(context);
                }
            }
        });
        dialogComplete.show();

    }

    public void displayDialogRating(Context context) {
        dialogRating = new Dialog(context);
        dialogRating.setContentView(R.layout.fragment_ratingbar);
        dialogRating.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RatingBar ratingBar = dialogRating.findViewById(R.id.ratingBar_fragment);
        Button btn_next = dialogRating.findViewById(R.id.btn_ratingBar_next);
        ImageView imageView = dialogRating.findViewById(R.id.imageView_fragment_ratingBar);
        TextView textView_name = dialogRating.findViewById(R.id.textView_fragment_ratingBar_expert_name);

        if (_expertProfile != null) {
            Picasso.get().load(_expertProfile).into(imageView);
        }
        textView_name.setText(_expertName);


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                _rating = rating;
                System.out.println(_rating.toString());
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRating.dismiss();
                displayDialogComment(context);
            }
        });

        dialogRating.show();
    }

    public void displayDialogComment(Context context) {
        dialogComment = new Dialog(context);
        dialogComment.setContentView(R.layout.fragment_comment);
        dialogComment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText editText_comment = dialogComment.findViewById(R.id.editText_fragment_comment);
        Button button_next = dialogComment.findViewById(R.id.btn_next_fragment_comment);

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _comment = editText_comment.getText().toString();
                sendPointAndComment();
                dialogComment.dismiss();
            }
        });

        dialogComment.show();

    }


    private void updateAppointment() {
        AppointmentManager appointmentManager = new AppointmentManager(new AppointmentDal());

        Appointment appointment = new Appointment();
        appointment.set_documentID(_documentID);
        appointment.set_completed(complete_check);

        appointmentManager.updateData(appointment, new IResult() {
            @Override
            public void onSuccess() {
                System.out.println("success");
            }

            @Override
            public void onFailed(Exception exception) {

            }
        });
    }

    private void sendPointAndComment() {
        CommentManager commentManager = new CommentManager(new CommentDal());

        Comment comment = new Comment();
        comment.set_comment(_comment);
        comment.set_point(_rating);
        comment.set_receiverID(_expertUid);

        commentManager.addData(comment, new IResult() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(Exception exception) {

            }
        });

    }
}


