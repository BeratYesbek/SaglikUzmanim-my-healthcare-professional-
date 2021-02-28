package com.saglikuzmanimm.saglikuzmanim.Fragment.NotificationFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.saglikuzmanimm.saglikuzmanim.Interfaces.IRecyclerViewClick;
import com.saglikuzmanimm.saglikuzmanim.R;


public class DisplayNotificationPaymentFragment extends Fragment implements IRecyclerViewClick {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_display_notification_payment, container, false);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onLongItemClick(int position) {

    }
}