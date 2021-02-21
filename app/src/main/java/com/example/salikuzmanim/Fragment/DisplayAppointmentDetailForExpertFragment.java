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

import com.example.salikuzmanim.Notification.AppointmentSendNotification;
import com.example.salikuzmanim.Concrete.Appointment;
import com.example.salikuzmanim.Concrete.User;
import com.example.salikuzmanim.DataBaseManager.FireBaseAppointmentDal;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetAppointmentDataListener;
import com.example.salikuzmanim.R;
import com.example.salikuzmanim.Service.AlarmReceiver;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.iwgang.countdownview.CountdownView;


public class DisplayAppointmentDetailForExpertFragment extends Fragment {

    private TextView _textView_display_appointment_detail_name;
    private TextView _textView_display_appointment_detail_appointmentPrice;
    private TextView _textView_display_appointment_detail_sendToDate;
    private TextView _textView_display_appointment_detail_appointmentDate;
    private TextView _textView_display_appointment_detail_info;
    private TextView _textView_display_appointment_detail_time_info;

    private ImageView _imageView_display_appointment_detail_expertProfile;


    private Button _btn_display_appointment_detail_refuse;
    private Button _btn_display_appointment_detail_accept;
    private Button _btn_display_appointment_goToBack_for_user;

    private Appointment _appointment;
    private User _user;

    private int REMAINING_TIME;

    private CountdownView countdownView;

    public DisplayAppointmentDetailForExpertFragment(Appointment appointment, User user) {
        this._appointment = appointment;
        this._user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_appointment_detail_for_expert, container, false);

        _textView_display_appointment_detail_name = view.findViewById(R.id.textView_display_appointment_detail_expertName);

        _textView_display_appointment_detail_appointmentPrice = view.findViewById(R.id.textView_display_appointment_detail_price_for_expert);
        _textView_display_appointment_detail_sendToDate = view.findViewById(R.id.textView_display_appointment_detail_sendToDate_for_expert);
        _textView_display_appointment_detail_appointmentDate = view.findViewById(R.id.textView_display_appointment_detail_appointmentDate_for_expert);
        _textView_display_appointment_detail_info = view.findViewById(R.id.textView_display_appointment_detail_time_info_for_expert);
        _textView_display_appointment_detail_time_info = view.findViewById(R.id.textView_display_appointment_detail_time_info_for_expert);

        _imageView_display_appointment_detail_expertProfile = view.findViewById(R.id.imageView_appointment_detail_expert_profile);


        _btn_display_appointment_detail_accept = view.findViewById(R.id.btn_display_appointment_detail_payment_expert);
        _btn_display_appointment_detail_refuse = view.findViewById(R.id.btn_display_appointment_detail_refuse_expert);
        _btn_display_appointment_goToBack_for_user = view.findViewById(R.id.btn_display_appointment_detail_goToBack_for_expert);

        countdownView = view.findViewById(R.id.countdown_expert_appointment);

        countdownView.setVisibility(View.INVISIBLE);
        _btn_display_appointment_detail_accept.setVisibility(View.INVISIBLE);

        System.out.println(_user.get_token());

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
                if (REMAINING_TIME <=4 && _appointment.get_payment()==true) {
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
                } else {
                    refuseAppointment();

                }
            }
        });
        _btn_display_appointment_detail_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptAppointment();

            }
        });


        createNotificationChannel();

        setData();


        return view;
    }

    private void refuseAppointment() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Uyarı");
        alertDialog.setMessage("Reddetmek istediğinize emin misiniz ?");
        alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FireBaseAppointmentDal fireBaseAppointmentDal = new FireBaseAppointmentDal();

                fireBaseAppointmentDal.updateAppointment(
                        new Appointment(_appointment.get_documentID(), null, true, null, null),
                        new IGetAppointmentDataListener() {
                            @Override
                            public void onSuccess(ArrayList entity) {
                                if(_appointment.get_situation() != true){
                                    AppointmentSendNotification.sendAppointmentRejectNotification(_user.get_token(),_user.get_ID());
                                }else{
                                    AppointmentSendNotification.sendAppointmentAbortNotification(_user.get_token(),_user.get_ID(),"expert");
                                }
                                Toast.makeText(getContext(), "Randevu red edildi.", Toast.LENGTH_LONG).show();
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

    private void acceptAppointment() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Uyarı");
        alertDialog.setMessage("Onay vermek istediğinize emin misiniz ?");
        alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FireBaseAppointmentDal fireBaseAppointmentDal = new FireBaseAppointmentDal();

                fireBaseAppointmentDal.updateAppointment(
                        new Appointment(_appointment.get_documentID(), null, null, null, true),
                        new IGetAppointmentDataListener() {
                            @Override
                            public void onSuccess(ArrayList entity) {
                                AppointmentSendNotification.sendAppointmentApprovedNotification(_user.get_token(),_user.get_ID());
                                Toast.makeText(getContext(), "Randevu Onaylandı", Toast.LENGTH_LONG).show();

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


        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);


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

        _textView_display_appointment_detail_name.setText((_user.get_firstName() + " " + _user.get_lastName()).toUpperCase());


        try {

            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String dateSendToDate = sfd.format(new Date(String.valueOf(_appointment.get_timestamp_sendTo_time().toDate())));
            String appointmentDate = sfd.format(new Date(String.valueOf(_appointment.get_timestamp_appointment_date().toDate())));

            _textView_display_appointment_detail_sendToDate.setText(dateSendToDate);
            _textView_display_appointment_detail_appointmentDate.setText(appointmentDate);

            _textView_display_appointment_detail_appointmentPrice.setText(_appointment.get_appointmentPrice().toString());

        } catch (Exception e) {

        }

        if (_user.get_profileImage() != null) {
            Picasso.get().load(_user.get_profileImage()).into(_imageView_display_appointment_detail_expertProfile);
        }

        if (_appointment.get_abort() == false) {
            _textView_display_appointment_detail_info.setText("Randevu talebine onay veya red vermeniz gerekmektedir.");
            _btn_display_appointment_detail_accept.setVisibility(View.VISIBLE);
            if (_appointment.get_situation() == true) {
                _btn_display_appointment_detail_accept.setVisibility(View.GONE);
                _btn_display_appointment_detail_refuse.setText("Iptal Et");
                if (_appointment.get_payment() == false) {
                    _textView_display_appointment_detail_info.setText("Hastanın ödeme yapması bekleniliyor...");

                } else {

                    _textView_display_appointment_detail_info.setText("Hasta tarafından ödeme gerçekleştirildi lütfen randevu tarihini bekleyiniz.");
                    _textView_display_appointment_detail_info.setTextColor(Color.parseColor("#04AD37"));

                    Long remaining_time = countDown(_appointment.get_timestamp_appointment_date().toDate());
                    REMAINING_TIME = ((int) (remaining_time * 1000 * 60 * 60));


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
                            _textView_display_appointment_detail_time_info.setText("Randevu süresi dolmuştur. Artık kullanıcı tarafından bir arama gerçekleştirilemez.");
                            _btn_display_appointment_detail_refuse.setVisibility(View.INVISIBLE);
                        } else {

                            _textView_display_appointment_detail_time_info.setText("Randevu Zamanı, 15 dakika içinde kullanıcının arama yapması beklenmektedir.");
                            countdownView.setVisibility(View.VISIBLE);
                            countdownView.start(remaining_time);
                            CountDownTimer countDownTimer = new CountDownTimer(remaining_time, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {


                                }

                                @Override
                                public void onFinish() {
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
                        ;
                    }


                }

            }

        } else {
            if (_appointment.get_whoCanceled().equals("expert") || _appointment.get_whoCanceled().equals("user")) {

                if (_appointment.get_whoCanceled().equals("expert")) {
                    _textView_display_appointment_detail_info.setText("BU RANDEVU TALEBİ SİZİN TARAFINIZDAN İPTAL EDİLDİ.");
                    _textView_display_appointment_detail_info.setTextColor(Color.RED);
                } else {
                    _textView_display_appointment_detail_info.setText("BU RANDEVU TALEBİ HASTA TARAFINDAN İPTAL EDİLDİ.");
                    _textView_display_appointment_detail_info.setTextColor(Color.RED);
                }
            }

            _btn_display_appointment_detail_accept.setVisibility(View.GONE);
            _btn_display_appointment_detail_refuse.setVisibility(View.GONE);
        }

    }
}