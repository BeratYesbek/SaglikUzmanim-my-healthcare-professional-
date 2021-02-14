package com.example.salikuzmanim.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salikuzmanim.DataBaseManager.FireBaseManager;
import com.example.salikuzmanim.Concrete.Appointment;
import com.example.salikuzmanim.Concrete.User;
import com.example.salikuzmanim.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterAppointmentForExpert extends RecyclerView.Adapter<AdapterAppointmentForExpert.adapterAppointemnt> {


    private ArrayList<Appointment> _appointmentArrayList;
    private ArrayList<User> _userArrayList;


    public AdapterAppointmentForExpert(ArrayList<Appointment> appointmentArrayList, ArrayList<User> userArrayList) {
        this._appointmentArrayList = appointmentArrayList;
        this._userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public adapterAppointemnt onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_adapter_appointments, parent, false);
        return new AdapterAppointmentForExpert.adapterAppointemnt(view);


    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull adapterAppointemnt holder, int position) {
        try {
            Appointment appointment = _appointmentArrayList.get(position);
            User user = _userArrayList.get(position);

            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String date_appointment = sfd.format(new Date(String.valueOf(appointment.get_timestamp_appointment_date().toDate())));
            String date_sendAppointment = sfd.format(new Date(String.valueOf(appointment.get_timestamp_appointment_date().toDate())));

            holder.textView_name.setText((user.get_firstName() + " " + user.get_lastName()).toUpperCase());
            holder.textView_date_appointment.setText(date_appointment);
            holder.textView_send_to_date_appointment.setText(date_sendAppointment);

            if (user.get_profileImage() != null) {
                Picasso.get().load(user.get_profileImage()).into(holder.imageView_profile);
            }


            if (appointment.get_abort() == false) {
                if (appointment.get_situation() == true) {
                    holder.btn_approved.setVisibility(View.GONE);
                    if (appointment.get_payment() == false) {
                        holder.textView_info.setText("Hastanın ödeme yapması bekleniliyor...");

                    } else {

                        holder.textView_info.setText("Hasta tarafından ödeme gerçekleştirildi lütfen randevu tarihini bekleyiniz.");
                        holder.textView_info.setTextColor(Color.parseColor("#04AD37"));
                        holder.btn_approved.setText("");
                        holder.btn_approved.setVisibility(View.VISIBLE);
                        holder.btn_approved.setBackgroundResource(R.drawable.custom_button6);
                        holder.btn_approved.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //do something later
                            }
                        });
                    }
                }
                holder.btn_approved.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //    Boolean _situation = true;
                        //    FireBaseManager fireBaseManager = new FireBaseManager();
/*
                     fireBaseManager.updateAppointment(new Appointment(whosendUid.get(position),toWhomUid.get(position),date.get(position),EXTACT_TİME.get(position),_situation
                             ,abort.get(position),payment.get(position),time_to_send.get(position),
                             documentID.get(position),appointmentUUID.get(position),whoAbort.get(position)));*/
                    }
                });

                holder.btn_dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Boolean _abort = true;
                        String _whoAbort = "expert";
                        FireBaseManager fireBaseManager = new FireBaseManager();
/*
                     fireBaseManager.updateAppointment(new Appointment(whosendUid.get(position),toWhomUid.get(position),date.get(position),EXTACT_TİME.get(position),situation.get(position)
                             ,_abort,payment.get(position),time_to_send.get(position),
                             documentID.get(position),appointmentUUID.get(position),_whoAbort));*/
                    }
                });
            } else {
                if (appointment.get_whoCanceled().equals("expert") || appointment.get_whoCanceled().equals("user")) {

                    if (appointment.get_whoCanceled().equals("expert")) {
                        holder.textView_info.setText("BU RANDEVU TALEBİ SİZİN TARAFINIZDAN İPTAL EDİLDİ.");
                        holder.textView_info.setTextColor(Color.RED);
                    } else {
                        holder.textView_info.setText("BU RANDEVU TALEBİ HASTA TARAFINDAN İPTAL EDİLDİ.");
                        holder.textView_info.setTextColor(Color.RED);
                    }
                }

                holder.btn_dismiss.setVisibility(View.GONE);
                holder.btn_approved.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }


    }


    @Override
    public int getItemCount() {
        return _appointmentArrayList.size();
    }

    public static class adapterAppointemnt extends RecyclerView.ViewHolder {
        ImageView imageView_profile;
        TextView textView_name;
        TextView textView_date_appointment;
        TextView textView_send_to_date_appointment;
        TextView textView_info;
        TextView textView_appointment_price;
        Button btn_approved;
        Button btn_dismiss;

        public adapterAppointemnt(@NonNull View itemView) {
            super(itemView);

            textView_name = itemView.findViewById(R.id.text_name_appointment_for_expert);
            textView_date_appointment = itemView.findViewById(R.id.textView_appointment_date_for_expert);
            textView_send_to_date_appointment = itemView.findViewById(R.id.textView_send_date_appointment_for_expert);
            textView_appointment_price = itemView.findViewById(R.id.textView_appointment_price_for_expert);
            imageView_profile = itemView.findViewById(R.id.imageView_appointment_adapter_for_expert);
            textView_info = itemView.findViewById(R.id.text_view_info_appointment_for_expert);
            btn_approved = itemView.findViewById(R.id.btn_adapter_approve);
            btn_dismiss = itemView.findViewById(R.id.btn_adapter_dismiss);


        }
    }
}
