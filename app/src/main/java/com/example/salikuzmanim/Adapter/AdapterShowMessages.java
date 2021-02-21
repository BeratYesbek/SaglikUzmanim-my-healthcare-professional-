package com.example.salikuzmanim.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salikuzmanim.Concrete.Chat;
import com.example.salikuzmanim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterShowMessages extends RecyclerView.Adapter<AdapterShowMessages.ViewHolderShowMessages> {
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RİGHT = 1;
    private Context context;
    private ArrayList<Chat> chats;
    private Uri imageUri;
    private FirebaseUser firebaseUser;

    private  int viewType_check = 0;

    public AdapterShowMessages(ArrayList<Chat> chats, Uri imageUri) {

        this.chats = chats;
        this.imageUri = imageUri;


    }

    @NonNull
    @Override
    public ViewHolderShowMessages onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        if (viewType == MSG_TYPE_RİGHT) {
            View view = layoutInflater.inflate(R.layout.layout_chat_item_right, parent, false);
            viewType_check = MSG_TYPE_RİGHT;
            return new AdapterShowMessages.ViewHolderShowMessages(view);

        } else {
            View view = layoutInflater.inflate(R.layout.layout_chat_item_left, parent, false);
            viewType_check = MSG_TYPE_LEFT;
            return new AdapterShowMessages.ViewHolderShowMessages(view);

        }


    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chats.get(position).getSenderID().equals(firebaseUser.getUid())) {

            return MSG_TYPE_RİGHT;
        } else {

            return MSG_TYPE_LEFT;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderShowMessages holder, int position) {
        Chat chat = chats.get(position);
        holder.textView_show_message.setText(chat.getMessage());


        try {
            if(viewType_check == MSG_TYPE_RİGHT) {
                if (chat.isSeen() == true) {
                    holder.imageView_check_seen.setImageResource(R.drawable.ic_check_seen);
                } else {
                    holder.imageView_check_seen.setImageResource(R.drawable.ic_check_not_seen);
                }
            }
            if(viewType_check == MSG_TYPE_LEFT){
                Picasso.get().load(imageUri).into(holder.imageView_profile);
            }


        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class ViewHolderShowMessages extends RecyclerView.ViewHolder {
        ImageView imageView_profile;
        TextView textView_show_message;
        ImageView imageView_check_seen;

        public ViewHolderShowMessages(@NonNull View itemView) {

            super(itemView);
            imageView_profile = itemView.findViewById(R.id.imageView_show_message_ProfileImage);
            textView_show_message = itemView.findViewById(R.id.textView_show_messages);
            imageView_check_seen = itemView.findViewById(R.id.imageView_check_seen);

        }
    }
}
