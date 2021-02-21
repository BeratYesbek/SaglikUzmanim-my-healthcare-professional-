package com.example.salikuzmanim.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ParseException;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salikuzmanim.Activity.MessageActivity;
import com.example.salikuzmanim.Notification.AppointmentSendNotification;
import com.example.salikuzmanim.Concrete.Appointment;
import com.example.salikuzmanim.Concrete.Expert;
import com.example.salikuzmanim.DataBaseManager.FireBaseAppointmentDal;
import com.example.salikuzmanim.Time.DatePickerManager;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetAppointmentDataListener;
import com.example.salikuzmanim.R;
import com.example.salikuzmanim.ui.DisplayExpertProfileFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class AdapterShowExpertToUser extends RecyclerView.Adapter<AdapterShowExpertToUser.ExpertShowListHolder> {


    private static TextView textViewForInfoAlert;
    private static ProgressBar progressBarForInfoAlert;
    private static ImageView imageViewForInfoAlert;
    private static Button btn_alert_info;


    private static Context _context;
    private static Dialog showApplyDialog;
    private static Dialog showInfoDialog;
    private static String departmentText;
    private static String name_text;
    private static int i = 0;
    private FragmentManager _fragmentManager;

    private static ArrayList<Expert> _expertArrayList;

    public AdapterShowExpertToUser(ArrayList<Expert> expertArrayList, Context context,FragmentManager fragmentManager) {
        this._expertArrayList = expertArrayList;
        this._context = context;
        this._fragmentManager = fragmentManager;

    }


    @NonNull
    @Override
    public ExpertShowListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        _context = parent.getContext();

        showApplyDialog = new Dialog(_context);
        showApplyDialog.setContentView(R.layout.apply_fragment);
        showApplyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LayoutInflater layoutInflater = LayoutInflater.from(_context);
        View view = layoutInflater.inflate(R.layout.fragmet_show_experts_for_user, parent, false);
        return new ExpertShowListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpertShowListHolder holder, int position) {
        try{
            Expert expert = _expertArrayList.get(position);


            holder.textView_name.setText(expert.get_firstName().toUpperCase() + " " + expert.get_lastName().toUpperCase());
            holder.textView_department.setText(expert.get_department());
            holder.textView_point.setText(expert.get_point().toString());
            if(expert.get_about() != null){
                holder.textView_about.setText(expert.get_about());

            }else{
                holder.textView_about.setText("Uzman henüz bir açıklama girmemiş.");
            }
            holder.textView_appointment_price.setText(expert.get_appointmentPrice().toString() + " TL");
            if (expert.get_check_expert() == true) {
                holder.check_okey.setVisibility(View.VISIBLE);
            } else {
                holder.check_okey.setVisibility(View.INVISIBLE);
            }
            if (expert.get_profileImage() != null) {
                Picasso.get().load(expert.get_profileImage()).into(holder.imageView_profile);

            } else {
                holder.imageView_profile.setImageResource(R.drawable.profile);
            }

            holder.btnDemandAppointment.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View view) {
                    i = position;
                    DatePickerManager datePickerManager = new DatePickerManager();
                    datePickerManager.showDatePickerDialog(_context);
                    name_text = expert.get_firstName() + " " + expert.get_lastName();
                    departmentText = expert.get_department();

                }
            });

            holder.textView_goTo_expertProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        DisplayExpertProfileFragment displayExpertProfileFragment = new DisplayExpertProfileFragment(expert.get_expertUid());
                        FragmentManager fragmentManager = _fragmentManager;
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.enter_to_right, R.anim.exit_to_right);
                        fragmentTransaction.add(R.id.fragment_container, displayExpertProfileFragment).commit();
                        fragmentManager.popBackStack();

                    }catch (Exception e){
                        System.out.println(e.toString());
                    }
                }


            });


            holder.textView_see_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getShowVideoDialog(position);
                }
            });

            holder.btnSendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = expert.get_firstName() + " " + expert.get_lastName();
                    Intent intentToMessageActivity = new Intent(_context, MessageActivity.class);
                    intentToMessageActivity.putExtra("receiverID", expert.get_expertUid());
                    if (expert.get_profileImage() != null) {
                        intentToMessageActivity.putExtra("receiverImage", expert.get_profileImage().toString());
                    }
                    intentToMessageActivity.putExtra("receiverName", name);
                    intentToMessageActivity.putExtra("receiverToken",expert.get_token());
                    _context.startActivity(intentToMessageActivity);
                }
            });
        }catch (Exception e){
                System.out.println(e.toString());
        }



    }

    public void getShowVideoDialog(int index) {
        Expert expert = _expertArrayList.get(index);

        Dialog showVideoDialog = new Dialog(_context);
        showVideoDialog.setContentView(R.layout.show_video_fragment_dialog_for_user);
        showVideoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        VideoView videoView = showVideoDialog.findViewById(R.id.videoView_for_user);
        TextView textView = showVideoDialog.findViewById(R.id.textView_videoView_info_for_user);
        Button btn_dismiss_videoView = showVideoDialog.findViewById(R.id.btn_showVide_for_user_dismiss);
        ProgressBar progressBar = showVideoDialog.findViewById(R.id.progressBar_showVideo_for_user);
        if (expert.get_expertVideo() != null) {
            videoView.setVideoURI(expert.get_expertVideo());
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

    public static void getDate(String date, String time) {


        SpannableString ss1 = new SpannableString("Tarih: " + date);
        SpannableString ss2 = new SpannableString("Saat: " + time);
        ForegroundColorSpan colorBlue = new ForegroundColorSpan(Color.BLUE);

        ss2.setSpan(colorBlue, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss1.setSpan(colorBlue, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textView_name = showApplyDialog.findViewById(R.id.alert_expert_name);
        TextView textView_department = showApplyDialog.findViewById(R.id.alert_departmant);
        TextView textView_date = showApplyDialog.findViewById(R.id.alert_date);
        TextView textView_time = showApplyDialog.findViewById(R.id.alert_time);
        Button btn_alert_send = showApplyDialog.findViewById(R.id.btn_alert_send_appointment);
        Button btn_alert_cancel = showApplyDialog.findViewById(R.id.btn_alert_cancel);

        textView_name.setText(name_text);
        textView_date.setText(ss1);
        textView_time.setText(ss2);
        textView_department.setText(departmentText);

        showApplyDialog.show();


        btn_alert_send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                try {
                    Expert expert = _expertArrayList.get(i);
                    System.out.println("token085" + expert.get_token());
                    String senderID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String receiverID = expert.get_expertUid();
                    String date1 = date + " " + time + ":00";
                    Date _date = (Date) new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(date1);
                    Timestamp timestamp = new Timestamp(_date);
                    System.out.println("timestamp: " + timestamp.toString());

                    UUID uuid = UUID.randomUUID();
                    String appointmentID = uuid.toString();
                    FireBaseAppointmentDal fireBaseAppointmentDal = new FireBaseAppointmentDal();
                    showApplyDialog.dismiss();
                    showAlertInfo();
                    fireBaseAppointmentDal.sendAppointment(new Appointment(senderID, receiverID, null, appointmentID, null, false, false, false, timestamp, null,expert.get_appointmentPrice()),
                            new IGetAppointmentDataListener() {
                                @Override
                                public void onSuccess(ArrayList entity) {
                                    System.out.println("token 1: " + expert.get_token());
                                    AppointmentSendNotification.sendAppointmentRequestNotification(expert.get_token(),expert.get_ID());
                                    alertInfo(true);

                                }

                                @Override
                                public void onFailed(Exception e) {
                                    alertInfo(false);
                                }
                            });


                } catch (ParseException | java.text.ParseException e) {
                    System.out.println(e.toString());
                }

            }
        });
        btn_alert_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showApplyDialog.dismiss();
            }
        });

    }

    public static void showAlertInfo() {
        try {
            showInfoDialog = new Dialog(_context);
            showInfoDialog.setContentView(R.layout.appointment_info_fragment);
            showInfoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            textViewForInfoAlert = showInfoDialog.findViewById(R.id.alert_info_textView);
            progressBarForInfoAlert = showInfoDialog.findViewById(R.id.progressBar_info_alert);
            imageViewForInfoAlert = showInfoDialog.findViewById(R.id.ImageView_alert_info);
            btn_alert_info = showInfoDialog.findViewById(R.id.btn_info_alert_ok);
            textViewForInfoAlert.setVisibility(View.INVISIBLE);
            imageViewForInfoAlert.setVisibility(View.INVISIBLE);
            btn_alert_info.setVisibility(View.INVISIBLE);


            showInfoDialog.show();
        } catch (Exception e) {

        }


    }

    public static void alertInfo(Boolean result) {
        if (result == true) {
            progressBarForInfoAlert.setVisibility(View.INVISIBLE);
            imageViewForInfoAlert.setVisibility(View.VISIBLE);
            textViewForInfoAlert.setVisibility(View.VISIBLE);
            btn_alert_info.setVisibility(View.VISIBLE);
            btn_alert_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showInfoDialog.dismiss();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return _expertArrayList.size();
    }




    public static class ExpertShowListHolder extends RecyclerView.ViewHolder {
        TextView textView_name;
        TextView textView_department;
        TextView textView_point;
        TextView textView_about;
        TextView textView_see_video;
        TextView textView_appointment_price;
        TextView textView_goTo_expertProfile;
        ImageView imageView_profile;
        ImageView check_okey;
        Button btnSendMessage;
        Button btnDemandAppointment;


        public ExpertShowListHolder(@NonNull View itemView) {
            super(itemView);

            textView_name = itemView.findViewById(R.id.textView_expert_name5);
            textView_about = itemView.findViewById(R.id.textView_about_text2);
            textView_department = itemView.findViewById(R.id.textView_show_expert_departmant);
            textView_point = itemView.findViewById(R.id.point_for_expert);
            textView_see_video = itemView.findViewById(R.id.textView_show_experts_see_video);
            textView_appointment_price = itemView.findViewById(R.id.textView_expert_show_appointment_price);
            textView_goTo_expertProfile = itemView.findViewById(R.id.textView_show_experts_see_profile);

            imageView_profile = itemView.findViewById(R.id.image_expert_profile_for_show);

            check_okey = itemView.findViewById(R.id.okey);

            btnSendMessage = itemView.findViewById(R.id.btn_send_message);
            btnDemandAppointment = itemView.findViewById(R.id.btn_appointment_demand);

        }
    }
}
