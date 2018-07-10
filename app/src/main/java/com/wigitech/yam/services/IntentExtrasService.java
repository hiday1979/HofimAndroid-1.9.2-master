package com.wigitech.yam.services;

/**
 * Created by user on 23/06/2016.
 */
public final class IntentExtrasService {

    //we use package name prefix as best practice when app interacts with other apps
    //for example: 'com.wigitech.hofim.coastId'

    private static final String COMPANY_PREFIX = "com.wigitech.yam.";

    //region CoastMapActivity
    public static final String COAST_ID = String.format("%scoastId", COMPANY_PREFIX);
    //endregion
}
