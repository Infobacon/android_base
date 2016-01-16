package com.usach.tbdgrupo7.iservifast.utilities;

/**
 * Created by AKiniyalocts on 2/23/15.
 */
public class Constants {
    /*
      Logging flag
     */
    public static final boolean LOGGING = false;

    /*
      Your imgur client id. You need this to upload to imgur.

      More here: https://api.imgur.com/
     */
    public static final String MY_IMGUR_CLIENT_ID = "a18c5f08f522f81";
    public static final String MY_IMGUR_CLIENT_SECRET = "7847f33f1e3b3f0a2fb1fd58d7c06ac3812a4789";

    /*
      Redirect URL for android.
     */
    public static final String MY_IMGUR_REDIRECT_URL = "http://www.google.cl";

    /*
      Client Auth
     */
    public static String getClientAuth() {
        return "Client-ID " + MY_IMGUR_CLIENT_ID;
    }

}
