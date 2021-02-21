package com.example.salikuzmanim.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salikuzmanim.Concrete.Appointment;
import com.example.salikuzmanim.Concrete.Expert;
import com.example.salikuzmanim.Fragment.DisplayAppointmentDetailForUserFragment;
import com.example.salikuzmanim.Interfaces.GetDataListener.IVideoMeetingListener;
import com.example.salikuzmanim.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterAppointmentForUser extends RecyclerView.Adapter<AdapterAppointmentForUser.AdapterAppointment> {


    private ArrayList<Appointment> _appointmentArrayList;
    private ArrayList<Expert> _expertArrayList;
    private IVideoMeetingListener _iVideoMeetingListener;
    private FragmentManager _fragmentManager;

    private Context context;

    public AdapterAppointmentForUser(ArrayList<Appointment> appointmentArrayList, ArrayList<Expert> expertArrayList, IVideoMeetingListener iVideoMeetingListener, FragmentManager fragmentManager) {
        this._appointmentArrayList = appointmentArrayList;
        this._expertArrayList = expertArrayList;
        this._iVideoMeetingListener = iVideoMeetingListener;
        this._fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public AdapterAppointment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_adapter_appointment_for_user, parent, false);
        return new AdapterAppointmentForUser.AdapterAppointment(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAppointment holder, int position) {
        Appointment appointment = _appointmentArrayList.get(position);
        Expert expert = _expertArrayList.get(position);
        try {


            holder.textView_name.setText((expert.get_firstName() + " " + expert.get_lastName()).toUpperCase());
            holder.textView_appointment_department.setText(expert.get_department());
            if (expert.get_profileImage() != null) {
                Picasso.get().load(expert.get_profileImage()).into(holder.imageView_profile);
            }
            if (expert.get_check_expert() != true) {
                holder.imageView_check_expert.setVisibility(View.INVISIBLE);
            }

            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String date_appointment = sfd.format(new Date(String.valueOf(appointment.get_timestamp_appointment_date().toDate())));
            String date_sendAppointment = sfd.format(new Date(String.valueOf(appointment.get_timestamp_sendTo_time().toDate())));

          Long time =   countDown(appointment.get_timestamp_appointment_date().toDate());

        //    holder.countdownView.start(time);
            holder.textView_date_appointment.setText(date_appointment);
            holder.textView_date_send_appointment.setText(date_sendAppointment);
            if (appointment.get_abort() == false) {


                if (appointment.get_situation() == true) {
                    holder.textView_info.setText("Uzman tarafından onaylandı ödeme yapamnız bekleniliyor...");
                    holder.textView_info.setTextColor(Color.parseColor("#04AD37"));
             //       holder.btn_payment.setVisibility(View.VISIBLE);

                    if (appointment.get_payment() == true) {
                        holder.textView_info.setText("Ödeme yapıldı. Lütfen randevu tarihini bekleyiniz... ");
                    //    holder.btn_payment.setBackgroundResource(R.drawable.custom_button6);
                     //   holder.btn_payment.setText("");

                     /*holder.btn_payment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //temp code

                            }
                        });*/

                    } else {

                    }

                } else {
                    holder.textView_info.setText("Uzmanınız tarafından onay bekleniliyor...");
                    holder.textView_info.setTextColor(Color.BLUE);
                  //  holder.btn_payment.setVisibility(View.GONE);
                }
            } else{
            System.out.println(appointment.get_whoCanceled());
            if (appointment.get_whoCanceled() != null) {
                if (appointment.get_whoCanceled().equals("expert") || appointment.get_whoCanceled().equals("user")) {

                    if (appointment.get_whoCanceled().equals("user")) {
                        holder.textView_info.setText("randevu talebi sizin tarafınızdan iptal edildi.");
                        holder.textView_info.setTextColor(Color.RED);
                        //    holder.btn_refuse.setVisibility(View.GONE);
                        //    holder.btn_payment.setVisibility(View.GONE);
                    } else {
                        holder.textView_info.setText("randevu talebi uzman tarafınızdan iptal edildi.");
                        holder.textView_info.setTextColor(Color.RED);
                        //    holder.btn_refuse.setVisibility(View.GONE);
                        //   holder.btn_payment.setVisibility(View.GONE);
                    }
                }
            } else {

            }

            }
            holder.btn_see_appointment_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        DisplayAppointmentDetailForUserFragment displayAppointmentDetailForUserFragment = new DisplayAppointmentDetailForUserFragment(appointment,expert);
                        FragmentManager fragmentManager = _fragmentManager;
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.enter_to_right, R.anim.exit_to_right);
                        fragmentTransaction.add(R.id.fragment_layout_appointment_detail_for_user, displayAppointmentDetailForUserFragment).commit();
                        fragmentManager.popBackStack();
                    }catch (Exception e){
                        System.out.println(e.toString());
                    }


                }
            });
        }catch (Exception e){

        }

/*
        holder.btn_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   _iVideoMeetingListener.initiateVideoMeeting(expert);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Uyarı");
                builder.setMessage("Randevuyu iptal etmek istediğinize emin misiniz ?");
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println(appointment.get_appointmentID());

                        FireBaseAppointmentDal fireBaseAppointmentDal = new FireBaseAppointmentDal();
                        fireBaseAppointmentDal.updateAppointment(
                                new Appointment(appointment.get_documentID(), "user", true, appointment.get_payment(), appointment.get_situation()),
                                new IGetAppointmentDataListener() {
                                    @Override
                                    public void onSuccess(ArrayList entity) {
                                        Toast.makeText(context, "Randevu iptal edildi", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onFailed(Exception exception) {
                                        Toast.makeText(context, exception.getMessage(), Toast.LENGTH_LONG).show();
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
                builder.create().show();

            }
        });*/


    }

    private Long countDown(Date date){
        try {

            Date now = new Date();
            long currentTime = now.getTime();
            long newYearDate = date.getTime();
            long countDownToNewYear = newYearDate - currentTime;
            System.out.println(countDownToNewYear);
       //     mCvCountdownView.start(countDownToNewYear);
            return countDownToNewYear;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return _expertArrayList.size();
    }

    public static class AdapterAppointment extends RecyclerView.ViewHolder {
        ImageView imageView_profile;
        ImageView imageView_check_expert;
        TextView textView_name;
        TextView textView_date_appointment;
        TextView textView_date_send_appointment;
        TextView textView_info;
        TextView textView_appointment_price;
        TextView textView_appointment_department;
        //Button btn_payment;
      //  Button btn_refuse;
     //   CountdownView countdownView;
        Button btn_see_appointment_detail;


        public AdapterAppointment(@NonNull View itemView) {
            super(itemView);
            textView_name = itemView.findViewById(R.id.text_name_appointment_for_user);
            textView_date_appointment = itemView.findViewById(R.id.textView_appointment_date_for_user);
            textView_date_send_appointment = itemView.findViewById(R.id.textView_send_date_appointment_for_user);
            textView_info = itemView.findViewById(R.id.textView_appointment_info_for_user);
            textView_appointment_price = itemView.findViewById(R.id.textView_appointment_price_for_user);
            textView_appointment_department = itemView.findViewById(R.id.textView_adapter_appointment_departmant_for_user);
            imageView_profile = itemView.findViewById(R.id.imageView_appointment_adapter_for_user);
            imageView_check_expert = itemView.findViewById(R.id.imageView_check_expert_appointment_adapter_for_user);


            btn_see_appointment_detail = itemView.findViewById(R.id.btn_see_detail_appointment_adapter_for_user);


            /*
            btn_payment = itemView.findViewById(R.id.btn_payment);
            btn_refuse = itemView.findViewById(R.id.btn_appointment_refuse_for_user);

            countdownView = itemView.findViewById(R.id.mycountdown);
*/


        }
    }
}
