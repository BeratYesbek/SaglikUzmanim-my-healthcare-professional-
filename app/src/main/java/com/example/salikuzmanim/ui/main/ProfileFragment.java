package com.example.salikuzmanim.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.salikuzmanim.Activity.ActivityExpert.profileEditActivityForExpert;
import com.example.salikuzmanim.DataBaseManager.FireBaseExpertDal;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetExpertDataListener;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetUserDataListener;
import com.example.salikuzmanim.Interfaces.IEntity;
import com.example.salikuzmanim.Concrete.Expert;
import com.example.salikuzmanim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.File;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {
    private static final int PICK_VIDEO_REQUEST = 1;


    private Uri videoUri;
    private static Uri VideoUriFirebase;
    private VideoView videoView;
    private MediaController mediaController;

    private TextView textView_expert_about;
    private TextView textView_showmore_profile;
    private TextView textView_first_last_name_expert_profile;
    private TextView textView_email_expert_profile;
    private TextView textView_departmant_expert;
    private TextView dialog_about;
    private TextView textView_appointmentPrice;
    private ImageView imageViewProfile;
    private ImageView imageViewCheckCircle;

    private Button btn_edit_profile_expert;
    private Button btn_show_video_expert;

    private ProgressBar progressBar;
    private Dialog showDialog;
    private Context context;

    private String _email;
    private String _about;
    private Uri _uriProfile;
    private Float appointmentPrice;

    private Dialog showVideoDialog;


    private TextView textView_secondDialog;
    private ProgressBar progressBar_secondDialog;
    private TextView getTextView_secondDialog2;
    private Button buttonSend;


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_expert, container, false);

        textView_expert_about = view.findViewById(R.id.textView_expert_about);
        textView_showmore_profile = view.findViewById(R.id.textView_showmore_profile);
        textView_email_expert_profile = view.findViewById(R.id.textView_expert_email);
        textView_first_last_name_expert_profile = view.findViewById(R.id.textView_expert_firstAndLastName);
        textView_departmant_expert = view.findViewById(R.id.textView_expert_departmant);
        textView_appointmentPrice = view.findViewById(R.id.textView_appointment_price);
        btn_edit_profile_expert = view.findViewById(R.id.btn_expert_edit_profile);
        imageViewProfile = view.findViewById(R.id.image_expert_profile);
        imageViewCheckCircle = view.findViewById(R.id.imageView_check_circle_icon);
        btn_show_video_expert = view.findViewById(R.id.btn_show_video_expert);


        progressBar = view.findViewById(R.id.progressBar_expert_profile);
        imageViewCheckCircle.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        context = getContext();

        showDialog = new Dialog(context);
        showDialog.setContentView(R.layout.fragment_show_about_expert);
        showDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_about = showDialog.findViewById(R.id.textView_about_text_fragment);
        textView_showmore_profile.setPaintFlags(textView_showmore_profile.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mediaController = new MediaController(context);
        btn_show_video_expert.setEnabled(false);
        btn_edit_profile_expert.setEnabled(false);


        textView_showmore_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog.show();
            }
        });

        btn_show_video_expert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVideoDialog();
            }

        });


        btn_edit_profile_expert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentToEditProfile = new Intent(getContext(), profileEditActivityForExpert.class);
                intentToEditProfile.putExtra("email", _email);
                intentToEditProfile.putExtra("about", _about);
                intentToEditProfile.putExtra("appointmentPrice", appointmentPrice);
                if (_uriProfile != null) {
                    intentToEditProfile.putExtra("uri", _uriProfile.toString());
                }
                startActivity(intentToEditProfile);
            }
        });
        getData();

        return view;

    }

    public void getVideoDialog() {

        showVideoDialog = new Dialog(context);
        showVideoDialog.setContentView(R.layout.show_video_fragment_dialog);
        showVideoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        videoView = showVideoDialog.findViewById(R.id.videoView_for_expert);
        Button button_upload = showVideoDialog.findViewById(R.id.btn_upload_video);
        Button button_delete = showVideoDialog.findViewById(R.id.btn_delete_video);
        TextView textView_info_text = showVideoDialog.findViewById(R.id.videoView_info_text);
        ProgressBar progressBar_showVideo = showVideoDialog.findViewById(R.id.progressBar_showVideo);
        progressBar_showVideo.setVisibility(View.VISIBLE);

        if (VideoUriFirebase != null) {
            videoView.setVideoURI(VideoUriFirebase);
            videoView.start();
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            mediaController.setMediaPlayer(videoView);
            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    progressBar_showVideo.setVisibility(View.INVISIBLE);
                    textView_info_text.setVisibility(View.INVISIBLE);
                }
            });

        } else {
            progressBar_showVideo.setVisibility(View.INVISIBLE);
            textView_info_text.setVisibility(View.VISIBLE);
            textView_info_text.setText("Videonuz bulunmamaktadır lütfen kendinizi tanıttığınız bir videoyu sisteme yükleyiniz.");
        }


        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectVideo();
            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        showVideoDialog.show();


    }


    public void getSecendDialog(Boolean result) {
        Dialog secondDialog = new Dialog(context);
        secondDialog.setContentView(R.layout.dialog_fragment);
        secondDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        buttonSend = secondDialog.findViewById(R.id.btn_send_uri);
        progressBar_secondDialog = secondDialog.findViewById(R.id.progressBar_send_uri);
        textView_secondDialog = secondDialog.findViewById(R.id.textView_progressBar_text);
        getTextView_secondDialog2 = secondDialog.findViewById(R.id.textView_progressBar_info2);
        if (result == true) {

            buttonSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FireBaseExpertDal fireBaseExpertDal = new FireBaseExpertDal();
                    fireBaseExpertDal.uploadExpertVideo(new Expert(videoUri), new IGetUserDataListener() {
                        @Override
                        public void onSuccess(IEntity entity) {
                            Toast.makeText(getContext(), "Video başarı ile yüklendi", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailed(Exception e) {
                            Toast.makeText(getContext(), "Video yüklenirken bir hata meydana geldi.", Toast.LENGTH_LONG).show();

                        }
                    });
                }
            });

        } else {

            progressBar_secondDialog.setVisibility(View.INVISIBLE);
            textView_secondDialog.setVisibility(View.INVISIBLE);
            getTextView_secondDialog2.setTextSize(16);
            getTextView_secondDialog2.setText("Video uzunluğu bir dakikadan kısa olmalıdır");
            buttonSend.setText("KAPAT");
            buttonSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    secondDialog.dismiss();
                }
            });

        }

        secondDialog.show();


    }

    public void selectVideo() {

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videoUri = data.getData();
            File file = new File(videoUri.toString());
            try {

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(getContext(), videoUri);

                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long timeInmillisec = Long.parseLong(time);
                long duration = timeInmillisec / 1000;
                long hours = duration / 3600;
                long minutes = (duration - hours * 3600) / 60;
                long seconds = duration - (hours * 3600 + minutes * 60);
                if (duration < 105) {
                    showVideoDialog.cancel();
                    getSecendDialog(true);
                } else {
                    getSecendDialog(false);
                }


            } catch (Exception e) {
                System.out.println(e.toString());
            }


        }
    }


    public void getData() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String expertUid = firebaseAuth.getCurrentUser().getUid();
        FireBaseExpertDal fireBaseExpertDal = new FireBaseExpertDal();
        fireBaseExpertDal.getExpertData(expertUid,new IGetExpertDataListener() {
            @Override
            public void onSuccess(IEntity entity) {
                try {
                    Expert expert = (Expert) entity;
                    textView_first_last_name_expert_profile.setText(expert.get_firstName().toUpperCase() + " " + expert.get_lastName().toUpperCase());
                    textView_departmant_expert.setText(expert.get_department());
                    textView_email_expert_profile.setText(expert.get_email());

                    if (expert.get_expertVideo() != null) {
                        VideoUriFirebase = expert.get_expertVideo();
                        System.out.println(VideoUriFirebase.toString());
                    }

                    if (expert.get_about().equals(null)) {
                        textView_expert_about.setText("Henüz bir açıklama girmediniz.");

                    } else {
                        textView_expert_about.setText(expert.get_about());
                        dialog_about.setText(expert.get_about());
                    }
                    if (expert.get_appointmentPrice().equals(0)) {
                        textView_appointmentPrice.setText("Henüz bir randevu ücreti girmediniz.");
                    } else {
                        textView_appointmentPrice.setText(expert.get_appointmentPrice().toString() + "TL");
                        appointmentPrice = expert.get_appointmentPrice();
                    }
                    if (expert.get_check_expert().equals(true)) {
                        imageViewCheckCircle.setVisibility(View.VISIBLE);
                    } else {
                        imageViewCheckCircle.setVisibility(View.INVISIBLE);
                    }
                    if (expert.get_profileImage() != null) {
                        Picasso.get().load(expert.get_profileImage()).into(imageViewProfile);
                    }
                    _email = expert.get_email();
                    _about = expert.get_about();
                    _uriProfile = expert.get_profileImage();

                    btn_edit_profile_expert.setEnabled(true);
                    btn_show_video_expert.setEnabled(true);

                    progressBar.setVisibility(View.INVISIBLE);

                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }

            @Override
            public void onError(Exception exception) {

            }
        });

    }


}