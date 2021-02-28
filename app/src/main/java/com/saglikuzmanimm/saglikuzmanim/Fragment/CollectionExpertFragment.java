package com.saglikuzmanimm.saglikuzmanim.Fragment;

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

import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.ExpertManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Expert;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Collection;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase.FireBaseExpertDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetExpertListener;
import com.saglikuzmanimm.saglikuzmanim.R;
import com.saglikuzmanimm.saglikuzmanim.Activity.ActivityUser.ReyclerViewShowExpertForUserActivity;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;


public class CollectionExpertFragment extends Fragment {
    private Button btn_order_to_experts;
    private Button btn_close_fragment;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private int index = -1;

    private String department;

    public CollectionExpertFragment(String department) {
        this.department = department;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection_expert, container, false);
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
                    ExpertManager expertManager = new ExpertManager(new FireBaseExpertDal());
                    Collection collection = new Collection();
                    switch (index) {

                        case 0:

                            accordingToWhat = "appointmentPrice";
                            orderby = Query.Direction.ASCENDING;
                            collection.setOrderBy(orderby);
                            collection.setAccording_to_what(accordingToWhat);
                            collection.setDepartment(department);


                            expertManager.getAllExpert(collection, new IGetExpertListener() {
                                @Override
                                public void onSuccess(ArrayList<Expert> expertArrayList) {
                                    ReyclerViewShowExpertForUserActivity.accessActivity(expertArrayList, true);

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
                            collection.setOrderBy(orderby);
                            collection.setAccording_to_what(accordingToWhat);
                            collection.setDepartment(department);
                            expertManager.getAllExpert(collection, new IGetExpertListener() {
                                @Override
                                public void onSuccess(ArrayList<Expert> expertArrayList) {
                                    ReyclerViewShowExpertForUserActivity.accessActivity(expertArrayList, true);

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
                            collection.setOrderBy(orderby);
                            collection.setAccording_to_what(accordingToWhat);
                            collection.setDepartment(department);
                            expertManager.getAllExpert(collection, new IGetExpertListener() {
                                @Override
                                public void onSuccess(ArrayList<Expert> expertArrayList) {
                                    ReyclerViewShowExpertForUserActivity.accessActivity(expertArrayList, true);

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
                            collection.setOrderBy(orderby);
                            collection.setAccording_to_what(accordingToWhat);
                            collection.setDepartment(department);

                            expertManager.getAllExpert(collection, new IGetExpertListener() {
                                @Override
                                public void onSuccess(ArrayList<Expert> expertArrayList) {
                                    ReyclerViewShowExpertForUserActivity.accessActivity(expertArrayList, true);

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