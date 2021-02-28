package com.saglikuzmanimm.saglikuzmanim.Adapter.OtherAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Notification;
import com.saglikuzmanimm.saglikuzmanim.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterDisplayNotification extends RecyclerView.Adapter<AdapterDisplayNotification.ViewHolderNotification> {


    private ArrayList<Notification> _notificationArrayList;
    private static RecyclerView _recyclerView;
    private Context _context;
    public AdapterDisplayNotification(ArrayList<Notification> notificationArrayList,RecyclerView _recyclerView){
        this._notificationArrayList = notificationArrayList;
        this._recyclerView = _recyclerView;

    }

    @NonNull
    @Override
    public ViewHolderNotification onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        _context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(_context);
        View view = layoutInflater.inflate(R.layout.notification_reycler_view_item,parent,false);
        return new ViewHolderNotification(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNotification holder, int position) {
        Notification notification = _notificationArrayList.get(position);

        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String notificationDate = sfd.format(new Date(String.valueOf(notification.getTimestamp().toDate())));


        holder._textView_notification_body.setText(notification.get_messageBody());
        holder._textView_notification_title.setText(notification.get_messageTitle());


        holder._textView_notification_time.setText(notificationDate);

    }

    @Override
    public int getItemCount() {
        return _notificationArrayList.size();
    }

    public static class ViewHolderNotification extends RecyclerView.ViewHolder {

         TextView _textView_notification_title;
         TextView _textView_notification_body;
         TextView _textView_notification_time;

        public ViewHolderNotification(@NonNull View itemView) {
            super(itemView);

            _textView_notification_body = itemView.findViewById(R.id.textView_notification_body);
            _textView_notification_title = itemView.findViewById(R.id.textView_notification_item_title);
            _textView_notification_time = itemView.findViewById(R.id.textView_notification_timestamp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    return true;
                }
            });

        }
    }
}
