package com.example.salikuzmanim.Fragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.salikuzmanim.Activity.meetingActivity.OutgoingInvitationActivity;
import com.example.salikuzmanim.Notification.AppointmentSendNotification;
import com.example.salikuzmanim.Concrete.Appointment;
import com.example.salikuzmanim.Concrete.Expert;
import com.example.salikuzmanim.Concrete.Person;
import com.example.salikuzmanim.DataBaseManager.FireBaseAppointmentDal;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetAppointmentDataListener;
import com.example.salikuzmanim.Interfaces.GetDataListener.IVideoMeetingListener;
import com.example.salikuzmanim.R;
import com.example.salikuzmanim.Service.AlarmReceiver;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.iwgang.countdownview.CountdownView;


public class DisplayAppointmentDetailForUserFragment extends Fragment implements IVideoMeetingListener {


    private  int REMAINING_TIME = 0;
    private TextView _textView_display_appointment_detail_name;
    private TextView _textView_display_appointment_detail_department;
    private TextView _textView_display_appointment_detail_appointmentPrice;
    private TextView _textView_display_appointment_detail_sendToDate;
    private TextView _textView_display_appointment_detail_appointmentDate;
    private TextView _textView_display_appointment_detail_info;
    private TextView _textView_display_appointment_detail_time_info;

    private ImageView _imageView_display_appointment_detail_userProfile;
    private ImageView _imageView_display_appointment_detail_checkExpert;

    private Button _btn_display_appointment_detail_refuse;
    private Button _btn_display_appointment_detail_payment;
    private Button _btn_display_appointment_goToBack_for_user;

    private final Appointment _appointment;
    private final Expert _expert;

    private CountdownView countdownView;

    public DisplayAppointmentDetailForUserFragment(Appointment appointment, Expert expert) {
        this._appointment = appointment;
        this._expert = expert;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_display_appointment_detail_for_user, container, false);

        _textView_display_appointment_detail_name = view.findViewById(R.id.textView_display_appointment_detail_userName);
        _textView_display_appointment_detail_department = view.findViewById(R.id.textView_display_appointment_detail_user_department);
        _textView_display_appointment_detail_appointmentPrice = view.findViewById(R.id.textView_display_appointment_detail_price);
        _textView_display_appointment_detail_sendToDate = view.findViewById(R.id.textView_display_appointment_detail_sendToDate);
        _textView_display_appointment_detail_appointmentDate = view.findViewById(R.id.textView_display_appointment_detail_appointmentDate);
        _textView_display_appointment_detail_info = view.findViewById(R.id.textView_display_appointment_detail_info);
        _textView_display_appointment_detail_time_info = view.findViewById(R.id.textView_display_appointment_detail_time_info);

        _imageView_display_appointment_detail_userProfile = view.findViewById(R.id.imageView_appointment_detail_user_profile);
        _imageView_display_appointment_detail_checkExpert = view.findViewById(R.id.imageView_display_check_expert_to_user);


        _btn_display_appointment_detail_payment = view.findViewById(R.id.btn_display_appointment_detail_payment_user);
        _btn_display_appointment_detail_refuse = view.findViewById(R.id.btn_display_appointment_detail_refuse_user);
        _btn_display_appointment_goToBack_for_user = view.findViewById(R.id.btn_display_appointment_detail_goToBack_for_user);

        countdownView = view.findViewById(R.id.countdown_user_appointment);

        countdownView.setVisibility(View.INVISIBLE);
        _btn_display_appointment_detail_payment.setVisibility(View.INVISIBLE);

        _btn_display_appointment_goToBack_for_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transition transition = new Slide(Gravity.RIGHT);
                transition.setDuration(800);
                transition.addTarget(view);

                TransitionManager.beginDelayedTransition(container, transition);
                view.setVisibility(View.GONE);
            }
        });
        _btn_display_appointment_detail_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(REMAINING_TIME <=4 && _appointment.get_payment()==true){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setTitle("Uyarı");
                    alertDialog.setMessage("Randevu süresine 4 saat kala iptal işlemi yapılamaz.");
                    alertDialog.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.create().show();
                }else{
                    abortAppointment();
                }
            }
        });
        _btn_display_appointment_detail_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_appointment.get_payment() == true) {
                    initiateVideoMeeting(_expert,_appointment);
                } else {

                    payAppointmentPrice();
                }
            }
        });
        createNotificationChannel();


        setData();

        return view;
    }
    private void payAppointmentPrice(){

        //this part must change later
        FireBaseAppointmentDal fireBaseAppointmentDal = new FireBaseAppointmentDal();
        fireBaseAppointmentDal.updateAppointment(
                new Appointment(_appointment.get_documentID(), null, null, true, null),
                new IGetAppointmentDataListener() {
                    @Override
                    public void onSuccess(ArrayList entity) {
                        AppointmentSendNotification.sendAppointmentPayNotification(_expert.get_token(),_expert.get_ID());
                        Toast.makeText(getContext(), "Ödeme yapıldı.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailed(Exception exception) {
                        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void abortAppointment(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Uyarı");
        alertDialog.setMessage("Iptal etmek istediğinize emin misiniz ?");
        alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FireBaseAppointmentDal fireBaseAppointmentDal = new FireBaseAppointmentDal();

                fireBaseAppointmentDal.updateAppointment(
                        new Appointment(_appointment.get_documentID(), null, true, null, null),
                        new IGetAppointmentDataListener() {
                            @Override
                            public void onSuccess(ArrayList entity) {
                                AppointmentSendNotification.sendAppointmentAbortNotification(_expert.get_token(),_expert.get_ID(),"user");
                                Toast.makeText(getContext(), "Randevu iptal edildi.", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailed(Exception exception) {
                                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                );
            }
        }).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.create().show();
    }

    private void startAlarm(Calendar c, long remaining_time) {

        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        long time1 = System.currentTimeMillis();
        long time2 = c.getTimeInMillis();

        alarmManager.set(AlarmManager.RTC_WAKEUP, remaining_time, pendingIntent);


    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "remainderChannel";
            String description = "Channel for reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("notifyAlarm", description, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);


        }

    }


    private Long countDown(Date date) {
        try {

            Date now = new Date();
            long currentTime = now.getTime();
            long appointmentDate = date.getTime();
            long remaining_time = appointmentDate - currentTime;

            return remaining_time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void setData() {

        _textView_display_appointment_detail_name.setText((_expert.get_firstName() + " " + _expert.get_lastName()).toUpperCase());
        _textView_display_appointment_detail_department.setText(_expert.get_department());

        try {

            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String dateSendToDate = sfd.format(new Date(String.valueOf(_appointment.get_timestamp_sendTo_time().toDate())));
            String appointmentDate = sfd.format(new Date(String.valueOf(_appointment.get_timestamp_appointment_date().toDate())));

            _textView_display_appointment_detail_sendToDate.setText(dateSendToDate);
            _textView_display_appointment_detail_appointmentDate.setText(appointmentDate);
            _textView_display_appointment_detail_appointmentPrice.setText(_appointment.get_appointmentPrice().toString());
        } catch (Exception e) {

        }

        if (_expert.get_profileImage() != null) {
            Picasso.get().load(_expert.get_profileImage()).into(_imageView_display_appointment_detail_userProfile);
        }
        if (_expert.get_check_expert() == false) {
            _imageView_display_appointment_detail_checkExpert.setVisibility(View.INVISIBLE);
        }


        if (_appointment.get_abort() == false) {


            if (_appointment.get_situation() == true) {
                _textView_display_appointment_detail_info.setText("Uzman tarafından onaylandı ödeme yapamnız bekleniliyor...");
                _textView_display_appointment_detail_info.setTextColor(Color.parseColor("#04AD37"));
                _btn_display_appointment_detail_payment.setVisibility(View.VISIBLE);

                if (_appointment.get_payment() == true) {
                    _textView_display_appointment_detail_info.setText("Ödeme yapıldı. Lütfen randevu tarihini bekleyiniz... ");
                    _btn_display_appointment_detail_payment.setBackgroundResource(R.drawable.custom_button6);
                    _btn_display_appointment_detail_payment.setEnabled(false);
                    _btn_display_appointment_detail_payment.setText("");

                    Long remaining_time = countDown(_appointment.get_timestamp_appointment_date().toDate());
                    REMAINING_TIME = ((int) (remaining_time/1000)/3600);


                    if (remaining_time < 0) {

                        countdownView.setVisibility(View.INVISIBLE);
                        countdownView.stop();


                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(_appointment.get_timestamp_appointment_date().toDate());
                        calendar.add(Calendar.MINUTE, 15);

                        Date date1 = calendar.getTime();

                        Date now = new Date();
                        long currentTime = now.getTime();
                        long addingTime = date1.getTime();
                        remaining_time = addingTime - currentTime;

                        if (remaining_time < 0) {
                            countdownView.setVisibility(View.INVISIBLE);
                            countdownView.stop();
                            _textView_display_appointment_detail_info.setText("Ödeme yapıldı.");
                            _textView_display_appointment_detail_time_info.setText("Üzgünüz randevu zamanından 15 dakika geçmiştir. Artık randevu gerçekleştirme işlemi yapamazsınız..");
                            _btn_display_appointment_detail_payment.setVisibility(View.INVISIBLE);
                            _btn_display_appointment_detail_refuse.setVisibility(View.INVISIBLE);
                        } else {
                            _btn_display_appointment_detail_payment.setEnabled(true);
                            _textView_display_appointment_detail_info.setText("Ödeme yapıldı.");
                            _textView_display_appointment_detail_time_info.setText("Randevu Zamanı, 15 dakika içinde görüşme gerçekleştirmeniz gerekmektedir.");
                            countdownView.setVisibility(View.VISIBLE);
                            countdownView.start(remaining_time);
                            CountDownTimer countDownTimer = new CountDownTimer(remaining_time, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {


                                }

                                @Override
                                public void onFinish() {
                                    setData();
                                    countdownView.setVisibility(View.INVISIBLE);

                                }
                            }.start();

                        }


                    } else {
                        _textView_display_appointment_detail_time_info.setText("Randevu Zamanına kalan süre.");
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(_appointment.get_timestamp_appointment_date().toDate());
                        startAlarm(calendar, remaining_time);
                        countdownView.setVisibility(View.VISIBLE);
                        countdownView.start(remaining_time);
                        CountDownTimer countDownTimer = new CountDownTimer(remaining_time, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {


                            }

                            @Override
                            public void onFinish() {
                                setData();
                                countdownView.setVisibility(View.INVISIBLE);

                            }
                        }.start();
                    }

                }

            } else {
                _textView_display_appointment_detail_info.setText("Uzmanınız tarafından onay bekleniliyor...");
                _textView_display_appointment_detail_info.setTextColor(Color.BLUE);
                _btn_display_appointment_detail_payment.setVisibility(View.GONE);
            }
        } else {

            if (_appointment.get_whoCanceled() != null) {
                if (_appointment.get_whoCanceled().equals("expert") || _appointment.get_whoCanceled().equals("user")) {

                    if (_appointment.get_whoCanceled().equals("user")) {
                        _textView_display_appointment_detail_info.setText("randevu talebi sizin tarafınızdan iptal edildi.");
                        _textView_display_appointment_detail_info.setTextColor(Color.RED);
                        _btn_display_appointment_detail_refuse.setVisibility(View.GONE);
                        _btn_display_appointment_detail_payment.setVisibility(View.GONE);
                    } else {
                        _textView_display_appointment_detail_info.setText("randevu talebi uzman tarafınızdan iptal edildi.");
                        _textView_display_appointment_detail_info.setTextColor(Color.RED);
                        _btn_display_appointment_detail_refuse.setVisibility(View.GONE);
                        _btn_display_appointment_detail_payment.setVisibility(View.GONE);
                    }
                }
            } else {
                _textView_display_appointment_detail_info.setText("randevu talebi sistem tarafından iptal edildi.");
                _textView_display_appointment_detail_info.setTextColor(Color.RED);
                _btn_display_appointment_detail_refuse.setVisibility(View.GONE);
                _btn_display_appointment_detail_payment.setVisibility(View.GONE);
            }

        }


    }

    @Override
    public void initiateVideoMeeting(Person person,Appointment appointment) {

        if (_expert.get_token() == null || _expert.get_token().trim().isEmpty()) {
            Toast.makeText(getContext(), ((_expert.get_firstName() + " " + _expert.get_lastName()).toUpperCase() + " şu anda görüşme için hazır değil"), Toast.LENGTH_LONG).show();

        } else {
            try {
                Intent intentToOutGoingInvitation = new Intent(getContext(), OutgoingInvitationActivity.class);
                intentToOutGoingInvitation.putExtra("firstName", _expert.get_firstName());
                intentToOutGoingInvitation.putExtra("lastName", _expert.get_lastName());
                intentToOutGoingInvitation.putExtra("image_uri", _expert.get_profileImage().toString());
                intentToOutGoingInvitation.putExtra("token", _expert.get_token());
                intentToOutGoingInvitation.putExtra("userID", _expert.get_ID());
                intentToOutGoingInvitation.putExtra("email", _expert.get_email());
                intentToOutGoingInvitation.putExtra("appointmentDate",appointment.get_timestamp_appointment_date().toDate().toString());
                startActivity(intentToOutGoingInvitation);
            } catch (Exception e) {
                System.out.println(e.toString());
            }

        }
    }
}