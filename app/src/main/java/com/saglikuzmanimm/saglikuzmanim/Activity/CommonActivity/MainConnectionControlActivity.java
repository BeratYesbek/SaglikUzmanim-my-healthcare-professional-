package com.saglikuzmanimm.saglikuzmanim.Activity.CommonActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.saglikuzmanimm.saglikuzmanim.Activity.ActivityExpert.FragmentActivityForExpert;
import com.saglikuzmanimm.saglikuzmanim.Activity.ActivityUser.NavigationUserActivity;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.ExpertManager;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.UserManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Expert;
import com.saglikuzmanimm.saglikuzmanim.Concrete.User;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase.FireBaseExpertDal;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase.FireBaseUserDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetExpertListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetUserListener;
import com.saglikuzmanimm.saglikuzmanim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainConnectionControlActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_connection_controller);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if(firebaseUser != null){
                Expert expert = new Expert();
                expert.set_ID(firebaseUser.getUid());
                ExpertManager expertManager = new ExpertManager(new FireBaseExpertDal());
                expertManager.getData(expert, new IGetExpertListener() {
                    @Override
                    public void onSuccess(ArrayList<Expert> expertArrayList) {
                        Intent intent = new Intent(getApplicationContext(), FragmentActivityForExpert.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailed(Exception exception) {
                        UserManager userManager = new UserManager(new FireBaseUserDal());
                        userManager.getData(null, new IGetUserListener() {
                            @Override
                            public void onSuccess(ArrayList<User> userArrayList) {
                                Intent intent = new Intent(getApplicationContext(), NavigationUserActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailed(Exception exception) {
                                Intent intent = new Intent(getApplicationContext(), MainChoiceActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }
                });
            }else{
                Intent intent = new Intent(getApplicationContext(), MainChoiceActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }

}
