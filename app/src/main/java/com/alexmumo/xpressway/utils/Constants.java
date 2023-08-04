package com.alexmumo.xpressway.utils;

import com.alexmumo.xpressway.BuildConfig;

public class Constants {
    public static final int CONNECT_TIMEOUT = 60 * 1000;

    public static final int READ_TIMEOUT = 60 * 1000;

    public static final int WRITE_TIMEOUT = 60 * 1000;

    public static final String CONSUMER_KEY = BuildConfig.CONSUMER_KEY;
    public static final String CONSUMER_SECRET = BuildConfig.CONSUMER_SECRET;
    public static final String BASE_URL = "https://sandbox.safaricom.co.ke/";
    public static final String BUSINESS_SHORT_CODE = "174379";
    public static final String PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
    public static final String TRANSACTION_TYPE = "CustomerPayBillOnline";
    public static final String PARTYB = "174379";
    public static final String CALLBACKURL = "https://mydomain.com/path";
}
