package emmasircolour.com.sgapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class SGRegisterConfirmDialog extends DialogFragment {

    public SGRegisterConfirmDialog() {

        // Empty constructor required for DialogFragment

    }



    public static SGRegisterConfirmDialog newInstance(String title,String confirmDetails) {

        SGRegisterConfirmDialog frag = new SGRegisterConfirmDialog();

        Bundle args = new Bundle();

        args.putString("title", title);
        args.putString( "confirmDetails",confirmDetails);

        frag.setArguments(args);

        return frag;

    }



    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getArguments().getString("title");
        String confirmDetails = getArguments().getString("confirmDetails");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle(title);

        alertDialogBuilder.setMessage(confirmDetails);

        alertDialogBuilder.setPositiveButton("SAVE",  new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {


               // ((AddSelfReg)getActivity()).doPositiveClick();


                // on success

            }

        });

        alertDialogBuilder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

                if (dialog != null) {

                    dialog.dismiss();

                }

            }



        });



        return alertDialogBuilder.create();

    }



}