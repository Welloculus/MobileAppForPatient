package com.transility.welloculus.utils;

public interface Constants {

    String TAG = "EEHeathCare";
    String BLUETOOTH = "bluetooth";
    String IOT = "IOT";
    int DEFAULT_SYNC_INTERVAL = 1;
    int PAST_HEALTH_DURATION_IN_MINUTES = 5;
    int TIME_ONE_MINUTE = 1 * 60 * 1000;
    int MIN_INTERVAL = 1;
    int MAX_INTERVAL = 60;
    int CRITICAL_MAX_HEART_RATE = 160;
    int CRITICAL_MAX_TEMPERATURE = 35;
    int CRITICAL_MIN_HEART_RATE = 90;
    int CRITICAL_MIN_TEMPERATURE = 18;

    String KEY_SYNC_INTERVAL = "KEY_SYNC_INTERVAL";
    int HTTP_NOT_FOUND = 404;
    int HTTP_SUCCESS = 200;
    //Intent Keys
    String NAME = "name";
    String phoneNumber = "phone_number";
    String email = "email";
    String EXTRAS_PASS = "password";
    String EXTRAS_NEW_PASS = "newPass";
    String CODE = "code";
    String TODO = "TODO";
    String MFCODE = "mfacode";
    String SMS = "sms";
    String SMS1 = "SMS";
    String DESTINATION = "destinatin";
    String DELIVERYMED = "deliveryMed";
    String MODE = "mode";
    String REFRESH = "refresh";
    String CONTINUESIGNIN = "continueSignIn";
    String CONTINUESIGNINBTN = "mBtnContinueSignIn";
    String SOURCE = "source";
    String ATTRIBUTE = "attribute";
    String MAIN = "main";
    String SIGNUP = "signup";

       /* Request Constants*/

    int signUpReq = 1;
    int confirmUser = 2;
    int forgotPassReq = 3;
    int launchUserReq = 4;
    int mfaReq = 5;
    int firstTimeSignReq = 6;
    int confirmSignUp = 10;
    int settingReq = 20;
    int userProfieReq = 23;
    int verifyReq = 21;

    String continuationChallange = "NEW_PASSWORD_REQUIRED";

       /* JSON Constants*/

    String USERNAME = "username";
    String STARTDATE = "startdate";
    String ENDDATE = "enddate";
    String HEARTRATE = "heart_rate";
    String TEMPERTURE = "temperature";

       /* Cognito Constants*/

    String GIVENNAME_KEY = "Given name";
    String FAMILYNAME_KEY = "Family name";
    String BIRTHDATE_KEY = "Birthdate";
    String GENDER_KEY = "Gender";
    String EMAIL_KEY = "Email";
    String PHONENUMBER_KEY = "Phone number";
    String CITY_KEY = "City";
    String VER_EMAIL_KEY = "Email verified";
    String VER_PHONENUMBER_KEY = "Phone number verified";

    String ATTR_KEY_GIVEN_NAME = "given_name";
    String ATTR_KEY_FAMILYNAME_KEY = "family_name";
    String ATTR_KEY_BIRTHDATE_KEY = "custom:birthdate";
    String ATTR_KEY_GENDER_KEY = "gender";
    String ATTR_KEY_EMAIL_KEY = "email";
    String ATTR_KEY_PHONENUMBER_KEY = "phone_number";
    String ATTR_KEY_CITY_KEY = "custom:city";

    String ATTR_KEY_VER_EMAIL_KEY = "email_verified";
    String ATTR_KEY_VER_PHONENUMBER_KEY = "phone_number_verified";



    /* Zephyr Constants*/
    String BROADCAST_HEART_RATE_ACTION = "SEND_HEART_RATE";
    String BROADCAST_DEVICE_CONNECTED = "DEVICE_CONNECTED";
    String EXTRAS_HEART_RATE = "EXTRAS_HEART_RATE";
    String EXTRAS_HEART_RATE_LOG_TIME = "EXTRAS_HEART_RATE_LOG_TIME";
    String EXTRAS_DEVICE_ID = "EXTRAS_DEVICE_ID";
    String EXTRAS_DEVICE_NAME = "EXTRAS_DEVICE_NAME";

    String INVAIIDEXCEPTION = "InvalidParameterException";

}