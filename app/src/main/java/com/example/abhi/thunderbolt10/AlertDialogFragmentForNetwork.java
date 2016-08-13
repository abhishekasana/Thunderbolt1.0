package com.example.abhi.thunderbolt10;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by abhi on 8/7/2016.
 */
public class AlertDialogFragmentForNetwork extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.newtork_errror)
                .setMessage(R.string.network_unavailable_message)
                .setPositiveButton((R.string.ok),null)
                ;
        AlertDialog alertDialog =builder.create();
        return alertDialog;
    }
}
