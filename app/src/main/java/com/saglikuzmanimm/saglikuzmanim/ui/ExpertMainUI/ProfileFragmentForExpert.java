package com.saglikuzmanimm.saglikuzmanim.ui.ExpertMainUI;

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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saglikuzmanimm.saglikuzmanim.Activity.ActivityExpert.profileEditActivityForExpert;
import com.saglikuzmanimm.saglikuzmanim.Adapter.OtherAdapters.AdapterDisplayComment;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.CommentManager;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.ExpertManager;
import com.saglikuzmanimm.saglikuzmanim.Calculator.Calculator;
import com.saglikuzmanimm.saglikuzmanim.Calculator.CalculatorExpertPoint;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Comment;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Expert;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase.FireBaseCommentDal;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase.FireBaseExpertDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetCommentListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetExpertListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;
import com.saglikuzmanimm.saglikuzmanim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class ProfileFragmentForExpert extends Fragment {
    private static final int PICK_VIDEO_REQUEST = 1;


    private static Uri videoUri;
    private static Uri VideoUriFirebase;
    private static VideoView videoView;
    private static MediaController mediaController;

    private static TextView textView_expert_about;
    private static TextView textView_showmore_profile;
    private static TextView textView_first_last_name_expert_profile;
    private static TextView textView_email_expert_profile;
    private static TextView textView_department_expert;
    private static TextView dialog_about;
    private static TextView textView_score;
    private static TextView textView_appointmentPrice;
    private static ImageView imageViewProfile;
    private static ImageView imageViewCheckCircle;

    private static Button btn_edit_profile_expert;
    private static Button btn_show_video_expert;

    private static ProgressBar progressBar;
    private static Dialog showDialog;
    private static Context context;

    private static String _email;
    private static String _about;
    private static Uri _uriProfile;
    private static Float appointmentPrice;

    private static Dialog showVideoDialog;


    private static TextView textView_secondDialog;
    private static ProgressBar progressBar_secondDialog;
    private static TextView getTextView_secondDialog2;
    private static Button buttonSend;

    private RecyclerView recyclerView;
    private AdapterDisplayComment adapterDisplayComment;

    private ArrayList<Comment> _arrayListComment;


    public static ProfileFragmentForExpert newInstance() {
        return new ProfileFragmentForExpert();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_expert, container, false);

        textView_expert_about = view.findViewById(R.id.textView_expert_about);
        textView_showmore_profile = view.findViewById(R.id.textView_showmore_profile);
        textView_email_expert_profile = view.findViewById(R.id.textView_expert_email);
        textView_first_last_name_expert_profile = view.findViewById(R.id.textView_expert_firstAndLastName);
        textView_department_expert = view.findViewById(R.id.textView_expert_departmant);
        textView_appointmentPrice = view.findViewById(R.id.textView_appointment_price);
        btn_edit_profile_expert = view.findViewById(R.id.btn_expert_edit_profile);
        imageViewProfile = view.findViewById(R.id.image_expert_profile);
        imageViewCheckCircle = view.findViewById(R.id.imageView_check_circle_icon);
        btn_show_video_expert = view.findViewById(R.id.btn_show_video_expert);
        textView_score = view.findViewById(R.id.textView_point_expert_for_profile);


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

        _arrayListComment = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView_profile_expert);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterDisplayComment = new AdapterDisplayComment(_arrayListComment);
        recyclerView.setAdapter(adapterDisplayComment);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutAnimation(recyclerView);


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
                intentToEditProfile.putExtra("about", _about);
                intentToEditProfile.putExtra("appointmentPrice", appointmentPrice);
                if (_uriProfile != null) {
                    intentToEditProfile.putExtra("uri", _uriProfile.toString());
                }
                startActivity(intentToEditProfile);
            }
        });

        getComment();
        getData();

        return view;

    }

    private void layoutAnimation(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_view_animation);
        recyclerView.setLayoutAnimation(animationController);

    }

    private void getVideoDialog() {

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

    public static void setVideoProgressBar( double progress) {
        textView_secondDialog.setText("% " + progress);
        int value = (int) progress;
        progressBar_secondDialog.setProgress(value);
    }


    public void getSecendDialog(Boolean result) {
        Dialog secondDialog = new Dialog(context);
        secondDialog.setContentView(R.layout.dialog_fragment);
        secondDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        buttonSend = secondDialog.findViewById(R.id.btn_send_uri);
        progressBar_secondDialog = secondDialog.findViewById(R.id.progressBar_upload_video);
        textView_secondDialog = secondDialog.findViewById(R.id.textView_progressBar_text);
        getTextView_secondDialog2 = secondDialog.findViewById(R.id.textView_progressBar_info2);
        if (result == true) {

            buttonSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ExpertManager expertManager = new ExpertManager(new FireBaseExpertDal());
                    expertManager.uploadExpertVideo(new Expert(videoUri), new IResult() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getContext(), "Video başarı ile yüklendi", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onFailed(Exception exception) {
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


        ExpertManager expertManager = new ExpertManager(new FireBaseExpertDal());

        expertManager.getExpertById(expertUid, new IGetExpertListener() {
            @Override
            public void onSuccess(ArrayList<Expert> expertArrayList) {

                Expert expert = (Expert) expertArrayList.get(0);

                textView_first_last_name_expert_profile.setText(expert.get_firstName().toUpperCase() + " " + expert.get_lastName().toUpperCase());
                textView_department_expert.setText(expert.get_department());
                textView_email_expert_profile.setText(expert.get_email());

                if (expert.get_expertVideo() != null) {
                    VideoUriFirebase = expert.get_expertVideo();
                    System.out.println(VideoUriFirebase.toString());
                }


                if (expert.get_about() == null) {
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
                if (expert.get_point() > 0) {
                    textView_score.setText(expert.get_point().toString());
                } else {
                    textView_score.setText("Yeni uzman");
                }
                _email = expert.get_email();
                _about = expert.get_about();
                _uriProfile = expert.get_profileImage();

                btn_edit_profile_expert.setEnabled(true);
                btn_show_video_expert.setEnabled(true);

                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailed(Exception exception) {

            }
        });
    }

    private void getComment() {

        Comment comment = new Comment();
        comment.set_receiverID(FirebaseAuth.getInstance().getCurrentUser().getUid());

        CommentManager commentManager = new CommentManager(new FireBaseCommentDal());
        commentManager.getData(comment, new IGetCommentListener() {
            @Override
            public void onSuccess(ArrayList<Comment> comments, List<Float> point) {
                updatePoint(point);
                _arrayListComment.addAll(comments);
                adapterDisplayComment.notifyDataSetChanged();
                recyclerView.scheduleLayoutAnimation();
            }

            @Override
            public void onFailed(Exception exception) {

            }
        });
    }

    private void updatePoint(List<Float> list) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Calculator calculator = new CalculatorExpertPoint();
        Float average = calculator.calculatorPoint(list);
        Expert expert = new Expert();
        expert.set_ID(firebaseAuth.getCurrentUser().getUid());
        expert.set_point(average);
        ExpertManager expertManager = new ExpertManager(new FireBaseExpertDal());
        expertManager.updateData(expert, new IResult() {
            @Override
            public void onSuccess() {
                System.out.println("updated");
            }

            @Override
            public void onFailed(Exception exception) {

            }
        });

    }


}