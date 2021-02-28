package com.saglikuzmanimm.saglikuzmanim.Fragment.AppointmentDetailFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

import com.saglikuzmanimm.saglikuzmanim.Activity.meetingActivity.OutgoingInvitationActivity;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.AppointmentManager;
import com.saglikuzmanimm.saglikuzmanim.Calculator.AppointmentTimeCalculator;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Appointment;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Expert;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Notification;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Person;
import com.saglikuzmanimm.saglikuzmanim.Constants.Messages;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase.FireBaseAppointmentDal;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase.FireBaseNotificationDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IVideoMeetingListener;
import com.saglikuzmanimm.saglikuzmanim.JitsiMeet.AfterJitsiMeetingConference;
import com.saglikuzmanimm.saglikuzmanim.Notification.AppointmentSendNotification;
import com.saglikuzmanimm.saglikuzmanim.R;
import com.saglikuzmanimm.saglikuzmanim.Time.TimeCalculator;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.iwgang.countdownview.CountdownView;


public class DisplayAppointmentDetailForUserFragment extends Fragment implements IVideoMeetingListener {


    private int REMAINING_TIME = 0;
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

    private Boolean check_onBackPressed_in_jitsiMeet = false;

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
        _imageView_display_appointment_detail_checkExpert = view.findViewById(R.id.imageView_display_appointment_detail_check_expert);


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
                if (REMAINING_TIME <= 4 && _appointment.get_payment() == true) {
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
                    abortAppointment();
                }
            }
        });
        _btn_display_appointment_detail_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_appointment.get_payment() == true) {
                    initiateVideoMeeting(_expert, _appointment);
                } else {

                    payAppointmentPrice();
                }
            }
        });



        setData();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (check_onBackPressed_in_jitsiMeet == true) {
            String appointmentID = _appointment.get_appointmentID();
            String documentID = _appointment.get_documentID();
            String expertUid = _expert.get_ID();
            String expertName = (_expert.get_firstName() + " " + _expert.get_lastName()).toUpperCase();
            Uri expertProfileImage = _expert.get_profileImage();

            AfterJitsiMeetingConference afterJitsiMeetingConference = new AfterJitsiMeetingConference(documentID, appointmentID, expertUid, expertName, expertProfileImage);
            afterJitsiMeetingConference.displayDialogComplete(getContext());

        }
        check_onBackPressed_in_jitsiMeet = true;
    }

    private void payAppointmentPrice() {

        AppointmentManager appointmentManager = new AppointmentManager(new FireBaseAppointmentDal());
        appointmentManager.updateData(new Appointment(_appointment.get_documentID(), null, null, true, null), new IResult() {
            @Override
            public void onSuccess() {
                AppointmentSendNotification.sendAppointmentPayNotification(_expert.get_token(), _expert.get_ID());
                addNotification(Messages.APPOINTMENT_MESSAGES_PAY_BY_USER, Messages.APPOINTMENT_MESSAGE_TITLE);
                Toast.makeText(getContext(), "Ödeme yapıldı.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailed(Exception exception) {
                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    private void abortAppointment() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Uyarı");
        alertDialog.setMessage("Iptal etmek istediğinize emin misiniz ?");
        alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                AppointmentManager appointmentManager = new AppointmentManager(new FireBaseAppointmentDal());
                appointmentManager.updateData(new Appointment(_appointment.get_documentID(), "user", true, null, null), new IResult() {
                    @Override
                    public void onSuccess() {
                        AppointmentSendNotification.sendAppointmentAbortNotification(_expert.get_token(), _expert.get_ID(), "user");
                        addNotification(Messages.APPOINTMENT_MESSAGES_ABORT_BY_USER, Messages.APPOINTMENT_MESSAGE_TITLE);
                        Toast.makeText(getContext(), "Randevu iptal edildi.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailed(Exception exception) {
                        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
            }
        }).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.create().show();
    }




    public void setData() {

        _textView_display_appointment_detail_name.setText((_expert.get_firstName() + " " + _expert.get_lastName()).toUpperCase());
        _textView_display_appointment_detail_department.setText(_expert.get_department());

        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateSendToDate = sfd.format(new Date(String.valueOf(_appointment.get_timestamp_sendTo_time().toDate())));
        String appointmentDate = sfd.format(new Date(String.valueOf(_appointment.get_timestamp_appointment_date().toDate())));

        _textView_display_appointment_detail_sendToDate.setText(dateSendToDate);
        _textView_display_appointment_detail_appointmentDate.setText(appointmentDate);
        _textView_display_appointment_detail_appointmentPrice.setText(_appointment.get_appointmentPrice().toString() + " TL ");

        if (_expert.get_profileImage() != null) {
            Picasso.get().load(_expert.get_profileImage()).into(_imageView_display_appointment_detail_userProfile);
        } else {
            _imageView_display_appointment_detail_userProfile.setImageResource(R.drawable.ic_profile);
        }
        if (_expert.get_check_expert() == false) {
            _imageView_display_appointment_detail_checkExpert.setVisibility(View.INVISIBLE);
        }
        if (_appointment.get_abort() == false) {

            if (_appointment.get_situation() == true) {
                _textView_display_appointment_detail_info.setText("Uzman tarafından onaylandı lütfen ödeme yapınız...");
                _btn_display_appointment_detail_payment.setVisibility(View.VISIBLE);

                if (_appointment.get_payment() == true) {
                    _textView_display_appointment_detail_info.setText("Ödeme yapıldı. Lütfen randevu tarihini bekleyiniz... ");
                    _btn_display_appointment_detail_payment.setBackgroundResource(R.drawable.custom_button6);
                    _btn_display_appointment_detail_payment.setEnabled(false);
                    _btn_display_appointment_detail_payment.setText("");

                    TimeCalculator timeCalculator = new AppointmentTimeCalculator();
                    Long remaining_time = timeCalculator.calculateRemainingTime(_appointment.get_timestamp_appointment_date().toDate());
                    REMAINING_TIME = ((int) (remaining_time / 1000) / 3600);

                    if (remaining_time < 0) {

                        AppointmentTimeCalculator appointmentTimeCalculator = new AppointmentTimeCalculator();
                        remaining_time = appointmentTimeCalculator.remainingFifteenMinutesCalculator(_appointment.get_timestamp_appointment_date().toDate());

                        if (remaining_time < 0) {


                            countdownView.setVisibility(View.INVISIBLE);
                            countdownView.stop();
                            _textView_display_appointment_detail_info.setText("Ödeme yapıldı.");
                            _textView_display_appointment_detail_time_info.setText("Üzgünüz randevu zamanından 15 dakika geçmiştir. Artık randevu gerçekleştirme işlemi yapamazsınız..");
                            _textView_display_appointment_detail_info.setTextColor(Color.RED);
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
                                    getActivity().finish();
                                    countdownView.setVisibility(View.INVISIBLE);

                                }
                            }.start();

                        }
                    } else {
                        _textView_display_appointment_detail_time_info.setText("Randevu Zamanına kalan süre.");
                        _btn_display_appointment_detail_payment.setVisibility(View.INVISIBLE);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(_appointment.get_timestamp_appointment_date().toDate());

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
                                getActivity().finish();


                            }
                        }.start();
                    }


                } else {
                    TimeCalculator timeCalculator = new AppointmentTimeCalculator();
                    Long remaining_time = timeCalculator.calculateRemainingTime(_appointment.get_timestamp_appointment_date().toDate());
                    REMAINING_TIME = ((int) (remaining_time / 1000) / 3600);


                    if (remaining_time < 0) {
                        _textView_display_appointment_detail_time_info.setText("Randevu tarihi geçmiştir.");
                        _textView_display_appointment_detail_info.setText("Artık ödeme yapamazsınız.");
                        _textView_display_appointment_detail_time_info.setTextColor(Color.RED);
                        _btn_display_appointment_detail_payment.setVisibility(View.GONE);
                        _btn_display_appointment_detail_refuse.setVisibility(View.GONE);
                    }
                }


            } else {
                _textView_display_appointment_detail_info.setText("Uzman tarafından onaylanması bekleniliyor...");
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
        if (_appointment.get_completed() != null) {
            if (_appointment.get_completed() == true) {
                _textView_display_appointment_detail_info.setText("Görüşme başarı ile gerçekleşmiştir");
                _textView_display_appointment_detail_info.setTextColor(Color.GREEN);
            }
        }

        TimeCalculator timeCalculator = new AppointmentTimeCalculator();
        Long remainingTime = timeCalculator.calculateRemainingTime(_appointment.get_timestamp_appointment_date().toDate());
        REMAINING_TIME = ((int) (remainingTime / 1000) / 3600);
        if (remainingTime < 0) {
            AppointmentTimeCalculator appointmentTimeCalculator = new AppointmentTimeCalculator();
            remainingTime = appointmentTimeCalculator.remainingFifteenMinutesCalculator(_appointment.get_timestamp_appointment_date().toDate());

            if (remainingTime < 0) {
                _textView_display_appointment_detail_time_info.setText("Randevu tarihi geçmiştir");
                _textView_display_appointment_detail_time_info.setTextColor(Color.RED);
                _btn_display_appointment_detail_refuse.setVisibility(View.GONE);
                _btn_display_appointment_detail_payment.setVisibility(View.GONE);
            }
        }


    }

    @Override
    public void initiateVideoMeeting(Person person, Appointment appointment) {

        if (_expert.get_token() == null || _expert.get_token().trim().isEmpty()) {
            Toast.makeText(getContext(), ((_expert.get_firstName() + " " + _expert.get_lastName()).toUpperCase() + " şu anda görüşme için hazır değil"), Toast.LENGTH_LONG).show();

        } else {
            try {

                Intent intentToOutGoingInvitation = new Intent(getContext(), OutgoingInvitationActivity.class);
                intentToOutGoingInvitation.putExtra("firstName", _expert.get_firstName());
                intentToOutGoingInvitation.putExtra("lastName", _expert.get_lastName());
                if (_expert.get_profileImage() != null) {
                    intentToOutGoingInvitation.putExtra("image_uri", _expert.get_profileImage().toString());
                }
                intentToOutGoingInvitation.putExtra("token", _expert.get_token());
                intentToOutGoingInvitation.putExtra("userID", _expert.get_ID());
                intentToOutGoingInvitation.putExtra("email", _expert.get_email());
                intentToOutGoingInvitation.putExtra("appointmentDate", appointment.get_timestamp_appointment_date().toDate().toString());
                startActivity(intentToOutGoingInvitation);
            } catch (Exception e) {
                System.out.println(e.toString());
            }

        }
    }

    private void addNotification(String messageBody, String messageTitle) {

        String senderID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        com.saglikuzmanimm.saglikuzmanim.Business.Concrete.NotificationManager notificationManager
                = new com.saglikuzmanimm.saglikuzmanim.Business.Concrete.NotificationManager(new FireBaseNotificationDal());

        notificationManager.addData(new Notification(_expert.get_ID(), senderID, messageBody, messageTitle, false), new IResult() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(Exception exception) {

            }
        });
    }
}