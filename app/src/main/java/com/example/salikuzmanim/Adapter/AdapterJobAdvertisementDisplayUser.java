package com.example.salikuzmanim.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salikuzmanim.Activity.ActivityUser.JobAdvertisementUpdateActivity;
import com.example.salikuzmanim.Concrete.JobAdvertisement;
import com.example.salikuzmanim.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AdapterJobAdvertisementDisplayUser extends RecyclerView.Adapter<AdapterJobAdvertisementDisplayUser.AdListHolder> {

    private ArrayList<JobAdvertisement> _jobAdvertisementArrayList;


    public AdapterJobAdvertisementDisplayUser(ArrayList<JobAdvertisement> jobAdvertisement) {
        this._jobAdvertisementArrayList = jobAdvertisement;

    }

    @NonNull
    @Override
    public AdapterJobAdvertisementDisplayUser.AdListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_ad, parent, false);
        return new AdListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterJobAdvertisementDisplayUser.AdListHolder holder, final int position) {
        try {
            JobAdvertisement jobAdvertisement = _jobAdvertisementArrayList.get(position);
            holder.textViewTitle.setText(jobAdvertisement.get_job_advertisement_title());
            holder.textViewExplaination.setText(jobAdvertisement.get_job_advertisement_explanation());

            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String date = sfd.format(new Date(String.valueOf(jobAdvertisement.get_timestamp().toDate())));

            holder.textViewDate.setText(date);
            holder.textViewCustomDetail.setText(jobAdvertisement.get_job_advertisement_department() + ", " + jobAdvertisement.get_job_advertisement_location());




            holder.button_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    btn_edit_work(position, view);
                }
            });

            holder.button_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn_delete_work(position);
                }
            });

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return _jobAdvertisementArrayList.size();
    }


    public void btn_edit_work(int position, View view) {

         Intent intentToUpdateActivity = new Intent(view.getContext(), JobAdvertisementUpdateActivity.class);
         JobAdvertisement jobAdvertisement = _jobAdvertisementArrayList.get(position);
         intentToUpdateActivity.putExtra("title",jobAdvertisement.get_job_advertisement_title());
         intentToUpdateActivity.putExtra("explanation",jobAdvertisement.get_job_advertisement_explanation());
         intentToUpdateActivity.putExtra("location",jobAdvertisement.get_job_advertisement_location());
         intentToUpdateActivity.putExtra("department",jobAdvertisement.get_job_advertisement_department());
         intentToUpdateActivity.putExtra("documentID",jobAdvertisement.get_documentID());
         view.getContext().startActivity(intentToUpdateActivity);



    }

    public void btn_delete_work(int position) {

    }


    public static class AdListHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewExplaination;
        TextView textViewDate;
        TextView textViewCustomDetail;

        Button button_edit;
        Button button_delete;

        public AdListHolder(@NonNull View itemView) {
            super(itemView);
            textViewExplaination = itemView.findViewById(R.id.text_view_fragment_ad_explanation);
            textViewTitle = itemView.findViewById(R.id.text_view_fragment_ad_title);
            textViewDate = itemView.findViewById(R.id.text_view_fragment_ad_date);
            textViewCustomDetail = itemView.findViewById(R.id.text_view_fragment_ad_customDetail);

            button_delete = itemView.findViewById(R.id.btn_ad_delete);
            button_edit = itemView.findViewById(R.id.btn_ad_edit);
        }
    }

}
