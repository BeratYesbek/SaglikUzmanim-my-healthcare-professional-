package com.saglikuzmanimm.saglikuzmanim.Adapter.AdapterMessages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Person;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IRecyclerViewClick;
import com.saglikuzmanimm.saglikuzmanim.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterMessages extends RecyclerView.Adapter<AdapterMessages.ViewHolderMessages> {

    private static IRecyclerViewClick recyclerViewClick;
    private ArrayList<HashMap> hashMapArrayList;
    private ArrayList<Person> person;
    private Context context;


    public AdapterMessages(ArrayList<Person> person, ArrayList<HashMap> hashMapArrayList, IRecyclerViewClick recyclerViewClick) {
        this.person = person;
        this.hashMapArrayList = hashMapArrayList;
        this.recyclerViewClick = recyclerViewClick;
    }

    @NonNull
    @Override
    public ViewHolderMessages onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.chat_list_item, parent, false);
        return new ViewHolderMessages(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMessages holder, int position) {


        try {
            Person _person = person.get(position);

            if (hashMapArrayList.size() != 0) {
                for (int i = 0; i < hashMapArrayList.size(); i++) {
                    HashMap<String, Object> hashMap = hashMapArrayList.get(i);
                    String id = (String) hashMap.get("WhoisntSeen");

                    if (id.equals(_person.get_ID())) {
                        holder.relativeLayout.setVisibility(View.VISIBLE);
                        break;
                    }
                }

            } else {


                holder.relativeLayout.setVisibility(View.INVISIBLE);

            }

            String completeName = _person.get_firstName().toString() + " " + _person.get_lastName().toString();
            System.out.println(completeName);
            holder.textView_name.setText(completeName.toUpperCase().toString());
            if (_person.get_profileImage() != null) {
                Picasso.get().load(_person.get_profileImage()).into(holder.imageView_profile);
            } else {
                holder.imageView_profile.setImageResource(R.drawable.ic_profile);
            }


        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    @Override
    public int getItemCount() {
        return person.size();
    }


    public static class ViewHolderMessages extends RecyclerView.ViewHolder {

        ImageView imageView_profile;
        TextView textView_name;
        RelativeLayout relativeLayout;

        public ViewHolderMessages(@NonNull View itemView) {
            super(itemView);

            imageView_profile = itemView.findViewById(R.id.imageProfile_message_user_profile);
            textView_name = itemView.findViewById(R.id.textView_message_user_name);
            relativeLayout = itemView.findViewById(R.id.relative_layout_show_new_messsage);
            relativeLayout.setVisibility(View.INVISIBLE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClick.onItemClick(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    recyclerViewClick.onLongItemClick(getAdapterPosition());
                    return true;
                }
            });
        }

    }

}
