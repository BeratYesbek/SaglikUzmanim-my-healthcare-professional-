package com.saglikuzmanimm.saglikuzmanim.Fragment.SingleChoiceFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.saglikuzmanimm.saglikuzmanim.Interfaces.SingleChoiceLister;
import com.saglikuzmanimm.saglikuzmanim.R;

public class SingleChoiceDoctorDepartmantFragment extends DialogFragment {

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
        final String list[] = getActivity().getResources().getStringArray(R.array.choiceDoctorDepartmant);

        builder.setTitle("Uzman seç").setSingleChoiceItems(list, position, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                position =i;
            }
        }).setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mLister.onPossitiveButtonClicked(list,position,"DoctorDepartmant");
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
