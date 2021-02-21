package com.example.salikuzmanim.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salikuzmanim.Concrete.JobAdvertisement;
import com.example.salikuzmanim.Concrete.User;
import com.example.salikuzmanim.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterDisplayJobAdvertisementToExpert extends RecyclerView.Adapter<AdapterDisplayJobAdvertisementToExpert.AddListHolderForExpert> {


    private Dialog showDialog;

    private ArrayList<JobAdvertisement> advertisementArrayList;
    private ArrayList<User> userArrayList;


    public AdapterDisplayJobAdvertisementToExpert(ArrayList<JobAdvertisement> advertisementArrayList, ArrayList<User> userArrayList) {
        this.advertisementArrayList = advertisementArrayList;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public AddListHolderForExpert onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_add_for_expert, parent, false);


        showDialog = new Dialog(context);
        showDialog.setContentView(R.layout.fragment_show_ad_detail);
        showDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        return new AddListHolderForExpert(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AddListHolderForExpert holder, final int position) {

        JobAdvertisement jobAdvertisement = advertisementArrayList.get(position);
        User user = userArrayList.get(position);


        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = sfd.format(new Date(String.valueOf(jobAdvertisement.get_timestamp().toDate())));

        SpannableString ss1 = new SpannableString(" Bölge: " + jobAdvertisement.get_job_advertisement_location());
        SpannableString ss2 = new SpannableString(" Uzman: " + jobAdvertisement.get_job_advertisement_department());
        SpannableString ss3 = new SpannableString("İlan Tarihi: " + date.substring(0,11));

        ForegroundColorSpan colorBlue = new ForegroundColorSpan(Color.BLUE);

        ss1.setSpan(colorBlue, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss2.setSpan(colorBlue, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss3.setSpan(colorBlue, 0, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        holder.textView_adapter_adapter_job_advertisement__title.setText(jobAdvertisement.get_job_advertisement_title());
        holder.textView_adapter_adapter_job_advertisement_explanation.setText(jobAdvertisement.get_job_advertisement_explanation());
        holder.textView_adapter_job_advertisement_first_and_last_name.setText((user.get_firstName() + " " + user.get_lastName().toUpperCase()));
        holder.textView_adapter_job_advertisement_location.setText(ss1);
        holder.textView_adapter_job_advertisement_department.setText(ss2);
        holder.textView_adapter_job_advertisement_date.setText(ss3);


        if (user.get_profileImage() != null) {
            Picasso.get().load(user.get_profileImage()).into(holder.imageView_adapter_job_advertisement_profile);
        } else {
            holder.imageView_adapter_job_advertisement_profile.setImageResource(R.drawable.ic_profile);
        }


        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_detail(position, view);
            }
        });

    }

    public void show_detail(int position, View view) {

        JobAdvertisement jobAdvertisement = advertisementArrayList.get(position);
        User user = userArrayList.get(position);


        SpannableString ss1 = new SpannableString(" Bölge: " + jobAdvertisement.get_job_advertisement_location());
        SpannableString ss2 = new SpannableString(" Uzman: " + jobAdvertisement.get_job_advertisement_department());

        ForegroundColorSpan colorBlue = new ForegroundColorSpan(Color.BLUE);

        ss1.setSpan(colorBlue, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss2.setSpan(colorBlue, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView dialog_name = showDialog.findViewById(R.id.user_name);
        TextView dialog_detail = showDialog.findViewById(R.id.detail);
        TextView dialog_title = showDialog.findViewById(R.id.title);
        TextView dialog_explanation = showDialog.findViewById(R.id.explanation);
        ImageView dialog_imageView = showDialog.findViewById(R.id.image_show_dialog);

        dialog_name.setText((user.get_firstName() + " " + user.get_lastName()).toUpperCase());
        dialog_detail.setText(ss1 + " " + ss2);
        dialog_title.setText(jobAdvertisement.get_job_advertisement_title());
        dialog_explanation.setText(jobAdvertisement.get_job_advertisement_explanation());
        if (user.get_profileImage() != null) {
            Picasso.get().load(user.get_profileImage()).into(dialog_imageView);
        } else {
            dialog_imageView.setImageResource(R.drawable.ic_profile);
        }

        showDialog.show();

    }

    @Override
    public int getItemCount() {
        return advertisementArrayList.size();
    }

    public static class AddListHolderForExpert extends RecyclerView.ViewHolder {
        TextView textView_adapter_adapter_job_advertisement__title;
        TextView textView_adapter_adapter_job_advertisement_explanation;
        TextView textView_adapter_job_advertisement_location;
        TextView textView_adapter_job_advertisement_department;
        TextView textView_adapter_job_advertisement_date;
        TextView textView_adapter_job_advertisement_first_and_last_name;
        ImageView imageView_adapter_job_advertisement_profile;
        ProgressBar progressBar;
        Button button;

        public AddListHolderForExpert(@NonNull View itemView) {
            super(itemView);

            textView_adapter_adapter_job_advertisement__title = itemView.findViewById(R.id.textView_add_title_for_expert);
            textView_adapter_adapter_job_advertisement_explanation = itemView.findViewById(R.id.textView_explain_add_for_expert);
            textView_adapter_job_advertisement_location = itemView.findViewById(R.id.textView_location_show_for_expert);
            textView_adapter_job_advertisement_department = itemView.findViewById(R.id.textView_expert_show_for_expert);
            textView_adapter_job_advertisement_date = itemView.findViewById(R.id.textView_date_show_for_expert);
            textView_adapter_job_advertisement_first_and_last_name = itemView.findViewById(R.id.textView_first_and_last_name_add_for_expert);
            imageView_adapter_job_advertisement_profile = itemView.findViewById(R.id.image_expert_profile_for_add);

            button = itemView.findViewById(R.id.btn_show_detail);


        }
    }
}
