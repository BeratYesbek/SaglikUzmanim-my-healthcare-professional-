package com.example.salikuzmanim.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.example.salikuzmanim.Concrete.Expert;
import com.example.salikuzmanim.DataBaseManager.FireBaseExpertDal;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetExpertDataListener;
import com.example.salikuzmanim.Interfaces.IEntity;
import com.example.salikuzmanim.R;
import com.squareup.picasso.Picasso;

public class DisplayExpertProfileFragment extends Fragment {

    private TextView _textView_display_name_expert_to_user;
    private TextView _textView_display_department_to_user;
    private TextView _textView_display_appointmentPrice_to_user;
    private TextView _textView_display_about_to_user;
    private TextView _textView_display_point_expert_to_user;
    private TextView _textView_display_showMore_about;
    private ImageView _imageView_display_expert_profile_to;
    private ImageView _imageView_display_expert_check_to_user;
    private Button _button_show_video;
    private Button _button_close_fragment;

    private ProgressBar _progressBar_display;

    private Dialog showDialog;
    private TextView dialog_about;


    private Uri VideoUri = null;

    private String _expertUid;

    public DisplayExpertProfileFragment(String expertUid) {
        this._expertUid = expertUid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_expert_profile, container, false);

        try {
            Intent intent = getActivity().getIntent();
            System.out.println(intent.getStringExtra("expertUid"));
        } catch (Exception e) {
            System.out.println("hata burada " + e.toString());
        }


        _textView_display_name_expert_to_user = view.findViewById(R.id.textView_display_expert_firstAndLastName_to_user);
        _textView_display_about_to_user = view.findViewById(R.id.textView_display_expert_about_to_user);
        _textView_display_appointmentPrice_to_user = view.findViewById(R.id.textView_display_appointment_price_to_user);
        _textView_display_point_expert_to_user = view.findViewById(R.id.textView_display_point_expert_to_user);
        _textView_display_department_to_user = view.findViewById(R.id.textView_display_expert_department_to_user);
        _textView_display_showMore_about = view.findViewById(R.id.textView_display_showMore_profile_to_user);

        _imageView_display_expert_profile_to = view.findViewById(R.id.imageView_display_expert_profile_to_user);
        _imageView_display_expert_check_to_user = view.findViewById(R.id.imageView_display_check_expert_to_user);

        _button_show_video = view.findViewById(R.id.btn_display_show_video_expert_to_user);
        _button_close_fragment = view.findViewById(R.id.btn_display_expert_profile_go_to_back);
        _progressBar_display = view.findViewById(R.id.progressBar_display_expert_profile_to_user);


        showDialog = new Dialog(getContext());
        showDialog.setContentView(R.layout.fragment_show_about_expert);
        showDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_about = showDialog.findViewById(R.id.textView_about_text_fragment);
        _textView_display_showMore_about.setPaintFlags(_textView_display_showMore_about.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        _textView_display_showMore_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog.show();
            }
        });
        _button_show_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVideoDialog();
            }
        });
        _button_close_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transition transition = new Slide(Gravity.RIGHT);
                transition.setDuration(800);
                transition.addTarget(view);

                TransitionManager.beginDelayedTransition(container, transition);
                view.setVisibility(View.GONE);
            }
        });
        getExpertData(_expertUid);

        return view;

    }


    public void getVideoDialog() {
        Dialog showVideoDialog = new Dialog(getContext());
        showVideoDialog.setContentView(R.layout.show_video_fragment_dialog_for_user);
        showVideoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        VideoView videoView = showVideoDialog.findViewById(R.id.videoView_for_user);
        TextView textView = showVideoDialog.findViewById(R.id.textView_videoView_info_for_user);
        Button btn_dismiss_videoView = showVideoDialog.findViewById(R.id.btn_showVide_for_user_dismiss);
        ProgressBar progressBar = showVideoDialog.findViewById(R.id.progressBar_showVideo_for_user);
        if (VideoUri != null) {
            videoView.setVideoURI(VideoUri);
            videoView.start();

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    progressBar.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            textView.setText("Bu uzmana ait bir video bulunamadı!");

        }
        btn_dismiss_videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.pause();
                showVideoDialog.dismiss();
            }
        });


        showVideoDialog.show();

    }

    void getExpertData(String expertUid) {
        FireBaseExpertDal fireBaseExpertDal = new FireBaseExpertDal();
        fireBaseExpertDal.getExpertData(expertUid, new IGetExpertDataListener() {
            @Override
            public void onSuccess(IEntity entity) {
                Expert expert = (Expert) entity;

                _textView_display_name_expert_to_user.setText((expert.get_firstName() + " " + expert.get_lastName()).toUpperCase());
                _textView_display_department_to_user.setText(expert.get_department());
                _textView_display_appointmentPrice_to_user.setText(expert.get_appointmentPrice().toString());

                _textView_display_point_expert_to_user.setText(expert.get_point().toString());
                dialog_about.setText(expert.get_about());
                if (expert.get_about() != null) {
                    _textView_display_about_to_user.setText(expert.get_about());
                } else {
                    _textView_display_about_to_user.setText("Uzman tarafından henüz açıklama girilmedi.");
                    dialog_about.setText("Uzman tarafından henüz açıklama girilmedi.");
                }
                if (expert.get_profileImage() != null) {
                    Picasso.get().load(expert.get_profileImage()).into(_imageView_display_expert_profile_to);
                }
                if (expert.get_check_expert() != false) {
                    _imageView_display_expert_check_to_user.setVisibility(View.INVISIBLE);
                }
                VideoUri = expert.get_expertVideo();
                _progressBar_display.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onError(Exception exception) {

            }
        });

    }
}