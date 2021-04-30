package com.saglikuzmanimm.saglikuzmanim.Constants;

import java.util.HashMap;

public class Constants {
    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_USER_NAME = "userName";

    public static final String KEY_USER_ID = "userID";
    public static final String KEY_FCM_TOKEN = "fcm_token";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PREFERENCE_NAME = "videoMeetingPreference";

    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE_BODY = "message";

    public static final String KEY_USER_PROFILE_IMAGE = "profileImage";
    public static final String KEY_APPOINTMENT_DATE = "appointmentDate";

    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";

    public static final String REMOTE_MSG_TYPE = "invitation";
    public static final String REMOTE_CHAT = "chat";
    public static final String REMOTE_APPOINTMENT = "appointment";

    public static final String REMOTE_MGS_INVITATION = "invitation";
    public static final String REMOTE_MSG_MEETING_TYPE = "meetingType";
    public static final String REMOTE_MSG_INVITER_TOKEN = "inviterToken";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";


    public static final String REMOTE_MSG_INVITATION_RESPONSE = "invitationResponse";

    public static final String REMOTE_MSG_INVITATION_ACCEPTED = "accepted";
    public static final String REMOTE_MSG_INVITATION_REJECTED = "rejected";

    public static final String REMOTE_MSG_INVITATION_CANCELLED = "cancelled";

    public static final String REMOTE_MSG_MEETING_ROOM = "meetingRoom";

    public static HashMap<String, String> getRemoteMessageHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(Constants.REMOTE_MSG_AUTHORIZATION,
                "key=AAAAigZLqbg:APA91bFxSNsQcMcriLk4H8uOtZbgSXCPFhwZOwXfK-vqmcdnAhXh-seonXEq5TlH5WUp0XY6BnI2rGT9xRwsA11tjCQPwu-2d6qHRuCM9niXCaLN3q53AQ-GF1G1ePk98sfEGtWydrju"
        );
        headers.put(Constants.REMOTE_MSG_CONTENT_TYPE, "application/json");
        return headers;
    }

}
