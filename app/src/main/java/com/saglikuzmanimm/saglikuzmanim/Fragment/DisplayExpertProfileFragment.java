package com.saglikuzmanimm.saglikuzmanim.Fragment;

import android.app.Dialog;
import android.content.Context;
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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.saglikuzmanimm.saglikuzmanim.Adapter.OtherAdapters.AdapterDisplayComment;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.CommentManager;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.ExpertManager;
import com.saglikuzmanimm.saglikuzmanim.Calculator.Calculator;
import com.saglikuzmanimm.saglikuzmanim.Calculator.CalculatorExpertPoint;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Comment;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Expert;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.CommentDal;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.ExpertDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetCommentListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetExpertListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;
import com.saglikuzmanimm.saglikuzmanim.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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

    private RecyclerView recyclerView;

    private ArrayList<Comment> _arrayListComment;
    private AdapterDisplayComment _adapterDisplayComment;

    public DisplayExpertProfileFragment(String expertUid) {
        this._expertUid = expertUid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_expert_profile, container, false);

        System.out.println(_expertUid.toString());

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

        _arrayListComment = new ArrayList<>();


        recyclerView = view.findViewById(R.id.recyclerView_display_expert_fragment);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _adapterDisplayComment = new AdapterDisplayComment(_arrayListComment);
        recyclerView.setAdapter(_adapterDisplayComment);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutAnimation(recyclerView);


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
        getComment(_expertUid);

        return view;

    }

    private void layoutAnimation(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_view_animation);
        recyclerView.setLayoutAnimation(animationController);

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


        ExpertManager expertManager = new ExpertManager(new ExpertDal());
        expertManager.getExpertById(expertUid, new IGetExpertListener() {
            @Override
            public void onSuccess(ArrayList<Expert> expertArrayList) {
                Expert expert = (Expert) expertArrayList.get(0);

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
            public void onFailed(Exception exception) {

            }
        });
    }

    private void getComment(String expertUid) {

        Comment comment = new Comment();
        comment.set_receiverID(expertUid);

        CommentManager commentManager = new CommentManager(new CommentDal());
        commentManager.getData(comment, new IGetCommentListener() {
            @Override
            public void onSuccess(ArrayList<Comment> comments, List<Float> point) {

                _arrayListComment.addAll(comments);
                _adapterDisplayComment.notifyDataSetChanged();
                recyclerView.scheduleLayoutAnimation();
                updatePoint(point);
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
        ExpertManager expertManager = new ExpertManager(new ExpertDal());
        expertManager.updateData(expert, new IResult() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(Exception exception) {

            }
        });

    }

}