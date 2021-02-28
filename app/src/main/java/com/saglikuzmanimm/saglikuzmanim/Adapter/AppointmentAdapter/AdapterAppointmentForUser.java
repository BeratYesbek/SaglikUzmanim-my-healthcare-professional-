package com.saglikuzmanimm.saglikuzmanim.Adapter.AppointmentAdapter;

import android.content.Context;
import android.graphics.Color;
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

import com.saglikuzmanimm.saglikuzmanim.Calculator.AppointmentTimeCalculator;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Appointment;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Expert;
import com.saglikuzmanimm.saglikuzmanim.Fragment.AppointmentDetailFragment.DisplayAppointmentDetailForUserFragment;
import com.saglikuzmanimm.saglikuzmanim.R;
import com.saglikuzmanimm.saglikuzmanim.Time.TimeCalculator;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterAppointmentForUser extends RecyclerView.Adapter<AdapterAppointmentForUser.AdapterAppointment> {


    private ArrayList<Appointment> _appointmentArrayList;
    private ArrayList<Expert> _expertArrayList;
    private FragmentManager _fragmentManager;

    private Context context;

    public AdapterAppointmentForUser(ArrayList<Appointment> appointmentArrayList, ArrayList<Expert> expertArrayList, FragmentManager fragmentManager) {
        this._appointmentArrayList = appointmentArrayList;
        this._expertArrayList = expertArrayList;

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

        try {
            Appointment appointment = _appointmentArrayList.get(position);
            Expert expert = _expertArrayList.get(position);


            holder.textView_name.setText((expert.get_firstName() + " " + expert.get_lastName()).toUpperCase());
            holder.textView_appointment_department.setText(expert.get_department());
            if (expert.get_profileImage() != null) {
                Picasso.get().load(expert.get_profileImage()).into(holder.imageView_profile);
            } else {
                holder.imageView_profile.setImageResource(R.drawable.ic_profile);
            }
            if (expert.get_check_expert() != true) {
                holder.imageView_check_expert.setVisibility(View.INVISIBLE);
            }


            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String date_appointment = sfd.format(new Date(String.valueOf(appointment.get_timestamp_appointment_date().toDate())));
            String date_sendAppointment = sfd.format(new Date(String.valueOf(appointment.get_timestamp_sendTo_time().toDate())));

            holder.textView_date_appointment.setText(date_appointment);
            holder.textView_date_send_appointment.setText(date_sendAppointment);
            if (appointment.get_abort() == false) {


                if (appointment.get_situation() == true) {
                    holder.textView_info.setText("Uzman tarafından onaylandı ödeme yapamnız bekleniliyor...");
                    holder.textView_info.setTextColor(Color.parseColor("#04AD37"));
                    if (appointment.get_payment() == true) {
                        holder.textView_info.setText("Ödeme yapıldı. Lütfen randevu tarihini bekleyiniz... ");

                    }
                    TimeCalculator timeCalculator = new AppointmentTimeCalculator();
                    Long remainingTime = timeCalculator.calculateRemainingTime(appointment.get_timestamp_appointment_date().toDate());
                    if (remainingTime < 0) {
                        AppointmentTimeCalculator appointmentTimeCalculator = new AppointmentTimeCalculator();
                        remainingTime = appointmentTimeCalculator.remainingFifteenMinutesCalculator(appointment.get_timestamp_appointment_date().toDate());
                        holder.textView_info.setText("Kullanıcının 15 dakika içersinde arama yapması bekleniliyor");
                        holder.textView_info.setTextColor(Color.parseColor("#FFD500"));
                        if (remainingTime < 0) {
                            holder.textView_info.setText("Randevu Tarihi Geçti");
                            holder.textView_info.setTextColor(Color.parseColor("#CF06D6"));
                        }
                    }

                } else {
                    holder.textView_info.setText("Uzmanınız tarafından onay bekleniliyor...");
                    holder.textView_info.setTextColor(Color.BLUE);
                }
            } else {
                System.out.println(appointment.get_whoCanceled());
                if (appointment.get_whoCanceled() != null) {
                    if (appointment.get_whoCanceled().equals("expert") || appointment.get_whoCanceled().equals("user")) {

                        if (appointment.get_whoCanceled().equals("user")) {
                            holder.textView_info.setText("randevu talebi sizin tarafınızdan iptal edildi.");
                            holder.textView_info.setTextColor(Color.RED);

                        } else {
                            holder.textView_info.setText("randevu talebi uzman tarafınızdan iptal edildi.");
                            holder.textView_info.setTextColor(Color.RED);
                        }
                    }
                } else {

                }

            }


            holder.btn_see_appointment_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        DisplayAppointmentDetailForUserFragment displayAppointmentDetailForUserFragment = new DisplayAppointmentDetailForUserFragment(appointment, expert);
                        FragmentManager fragmentManager = _fragmentManager;
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.enter_to_right, R.anim.exit_to_right);
                        fragmentTransaction.add(R.id.fragment_layout_appointment_detail_for_user, displayAppointmentDetailForUserFragment).commit();
                        fragmentManager.popBackStack();
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
            });
        } catch (Exception e) {

        }


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


        }
    }
}
