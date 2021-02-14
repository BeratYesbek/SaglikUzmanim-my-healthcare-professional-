package com.example.salikuzmanim;

import android.content.Context;

import com.example.salikuzmanim.DataBaseManager.FireBaseTokensDal;
import com.example.salikuzmanim.Concrete.Token;
import com.onesignal.OneSignal;

public class OneSignalManager {

    private static final String ONESIGNAL_APP_ID = "4fb0e75a-565a-4ffc-897f-4d576d24aaff";

    public void startSignal(Context context){
        try{
            // Enable verbose OneSignal logging to debug issues if needed.
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
            // OneSignal Initialization
            OneSignal.initWithContext(context);
            OneSignal.setAppId(ONESIGNAL_APP_ID);
            String token = OneSignal.getDeviceState().getUserId().toString();

            FireBaseTokensDal fireBaseTokensDal = new FireBaseTokensDal();
            fireBaseTokensDal.getTokensCheck(new Token(token,null));
        }catch (Exception e){

        }

    }
}
