package com.saglikuzmanimm.saglikuzmanim.Adapter.OtherAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Comment;
import com.saglikuzmanimm.saglikuzmanim.R;

import java.util.ArrayList;

public class AdapterDisplayComment extends RecyclerView.Adapter<AdapterDisplayComment.ViewHolderComment> {


    private ArrayList<Comment> _commentArrayList;

    public AdapterDisplayComment(ArrayList<Comment> commentArrayList) {

        this._commentArrayList = commentArrayList;

    }


    @NonNull
    @Override
    public ViewHolderComment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_comment_item, parent, false);
        return new ViewHolderComment(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderComment holder, int position) {

        Comment comment = _commentArrayList.get(position);
        holder.textView_comment.setText(comment.get_comment());
        holder.textView_timestamp.setText(comment.get_timestamp().toDate().toString());
    }

    @Override
    public int getItemCount() {
        return _commentArrayList.size();
    }

    public static class ViewHolderComment extends RecyclerView.ViewHolder {
        TextView textView_comment;
        TextView textView_timestamp;

        public ViewHolderComment(@NonNull View itemView) {
            super(itemView);
            textView_comment = itemView.findViewById(R.id.textView_comment_item);
            textView_timestamp = itemView.findViewById(R.id.textView_comment_timestamp);
        }
    }
}
