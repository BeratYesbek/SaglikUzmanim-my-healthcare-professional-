package com.example.salikuzmanim.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.salikuzmanim.R;
import com.example.salikuzmanim.Interfaces.SingleChoiceLister;

public class SingleChoiceExpertFragment extends DialogFragment {
    int position =0;
    SingleChoiceLister mLister;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mLister = (SingleChoiceLister) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String list[] = getActivity().getResources().getStringArray(R.array.choiceExpert);
        builder.setTitle("Sağlık Uzmanı Seç").setSingleChoiceItems(list, position, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                position = i;
            }
        }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mLister.onPossitiveButtonClicked(list,position,"Departmant");
            }
        }).setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mLister.onNegativeButtonClicked();
            }
        });

        return builder.create();
    }
}
