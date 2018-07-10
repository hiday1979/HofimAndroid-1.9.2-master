package com.wigitech.yam.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

/**
 * Created by user on 10/05/2016.
 */
public class ShowDialogAsync extends AsyncTask<Void, Void, Void> {

    private final Context mContext;
    private final String mTitle;
    private final String mContent;
    private DialogInterface.OnClickListener mListener;
    private AlertDialog mDialog;


    public ShowDialogAsync(Context context, String title, String content, DialogInterface.OnClickListener listener) {

        this.mContext = context;
        this.mTitle = title;
        this.mContent = content;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog = new AlertDialog.Builder(mContext).
                setTitle(mTitle).
                setMessage(mContent).
                setNeutralButton("Ok", mListener).
                create();
        mDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        super.onPostExecute(aVoid);
    }
}
