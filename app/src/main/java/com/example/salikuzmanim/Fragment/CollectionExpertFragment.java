package com.example.salikuzmanim.Fragment;

import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.salikuzmanim.DataBaseManager.FireBaseExpertDal;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetListDataListener;
import com.example.salikuzmanim.Concrete.Order;
import com.example.salikuzmanim.R;
import com.example.salikuzmanim.gridViewClickManager.ReyclerViewShowExpertForUserActivity;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;


public class CollectionExpertFragment extends Fragment {
    Button btn_order_to_experts;
    Button btn_close_fragment;
    RadioGroup radioGroup;
    RadioButton radioButton;
    int index = -1;

    private String departmant;

    public CollectionExpertFragment(String departmant) {
        this.departmant = departmant;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_expert, container, false);
        radioGroup = view.findViewById(R.id.radio_group_for_order);
        btn_close_fragment = view.findViewById(R.id.btn_close_order_expert_fragment);
        btn_order_to_experts = view.findViewById(R.id.btn_order_to_experts);


        btn_close_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                Transition transition = new Slide(Gravity.RIGHT);
                transition.setDuration(800);
                transition.addTarget(view);

                TransitionManager.beginDelayedTransition(container, transition);
                view.setVisibility(View.GONE);


            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton = view.findViewById(i);

                if (radioButton.getText().equals("Artan fiyat")) {
                    index = 0;
                } else if (radioButton.getText().equals("Azalan fiyat")) {
                    index = 1;
                } else if (radioButton.getText().equals("Azalan Puan")) {
                    index = 2;
                } else if (radioButton.getText().equals("Artan Puan")) {
                    index = 3;
                } else {
                    index = -1;
                }

            }
        });
        btn_order_to_experts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                System.out.println(index);
                if (index != -1) {
                    String accordingToWhat;
                    Object orderby;
                    FireBaseExpertDal fireBaseExpertDal = new FireBaseExpertDal();
                    Order order = new Order();
                    switch (index) {

                        case 0:

                            accordingToWhat = "appointmentPrice";
                            orderby = Query.Direction.ASCENDING;
                            order.setOrderBy(orderby);
                            order.setAccording_to_what(accordingToWhat);
                            order.setDepartment(departmant);
                            fireBaseExpertDal.getAllExpert(order, new IGetListDataListener() {
                                @Override
                                public void onSuccess(ArrayList entity) {
                                    ReyclerViewShowExpertForUserActivity.accessActivity(entity, true);
                                }

                                @Override
                                public void onFailed(Exception exception) {

                                    ReyclerViewShowExpertForUserActivity.accessActivity(null, false);
                                }
                            });
                            closeFragment(view, container);

                            break;
                        case 1:

                            accordingToWhat = "appointmentPrice";
                            orderby = Query.Direction.DESCENDING;
                            order.setOrderBy(orderby);
                            order.setAccording_to_what(accordingToWhat);
                            order.setDepartment(departmant);
                            fireBaseExpertDal.getAllExpert(order, new IGetListDataListener() {
                                @Override
                                public void onSuccess(ArrayList entity) {
                                    ReyclerViewShowExpertForUserActivity.accessActivity(entity, true);
                                }

                                @Override
                                public void onFailed(Exception exception) {

                                    ReyclerViewShowExpertForUserActivity.accessActivity(null, false);
                                }
                            });
                            closeFragment(view, container);
                            break;
                        case 2:

                            accordingToWhat = "point";
                            orderby = Query.Direction.DESCENDING;
                            order.setOrderBy(orderby);
                            order.setAccording_to_what(accordingToWhat);
                            order.setDepartment(departmant);
                            fireBaseExpertDal.getAllExpert(order, new IGetListDataListener() {
                                @Override
                                public void onSuccess(ArrayList entity) {
                                    ReyclerViewShowExpertForUserActivity.accessActivity(entity, true);
                                }

                                @Override
                                public void onFailed(Exception exception) {

                                    ReyclerViewShowExpertForUserActivity.accessActivity(null, false);
                                }
                            });
                            closeFragment(view, container);
                            break;
                        case 3:

                            accordingToWhat = "point";
                            orderby = Query.Direction.ASCENDING;
                            order.setOrderBy(orderby);
                            order.setAccording_to_what(accordingToWhat);
                            order.setDepartment(departmant);
                            fireBaseExpertDal.getAllExpert(order, new IGetListDataListener() {
                                @Override
                                public void onSuccess(ArrayList entity) {
                                    ReyclerViewShowExpertForUserActivity.accessActivity(entity, true);
                                }

                                @Override
                                public void onFailed(Exception exception) {
                                    ReyclerViewShowExpertForUserActivity.accessActivity(null, false);
                                }
                            });
                            closeFragment(view, container);
                            break;
                        default:
                            Toast.makeText(getContext(), "Bir hata meydana geldi", Toast.LENGTH_LONG);

                    }

                } else {
                    Toast.makeText(getContext(), "Seçim yapınız", Toast.LENGTH_LONG).show();
                }
            }
        });


        return view;
    }

    public void closeFragment(View view, ViewGroup container) {
        Transition transition = new Slide(Gravity.RIGHT);
        transition.setDuration(800);
        transition.addTarget(view);

        TransitionManager.beginDelayedTransition(container, transition);
        view.setVisibility(View.GONE);

    }


}