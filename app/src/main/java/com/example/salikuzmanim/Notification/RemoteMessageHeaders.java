package com.example.salikuzmanim.Notification;

import com.example.salikuzmanim.Concrete.Constants;

import java.util.HashMap;

public class RemoteMessageHeaders {

    public static HashMap<String, String> getRemoteMessageHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(Constants.REMOTE_MSG_AUTHORIZATION,
                "key=AAAAgX-zFPg:APA91bF-QG_xeJBCUxs4mx3WNMd2YSVIc1HkAnXDrgIfrzYB7FQQkpFSK4kbb9dnOSuninnnPRyRr-xTrwlfVmriYpAla_0ZyzgSzee0jl_39Kku-lL1dd_sKxcC0T-0BA6GOtAbZBOH"
        );
        headers.put(Constants.REMOTE_MSG_CONTENT_TYPE, "application/json");
        return headers;
    }
}
