package emmasircolour.com.sgapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;


import androidx.fragment.app.DialogFragment;

public class YesNoConfirmationDialog extends DialogFragment {

    public YesNoConfirmationDialog() {

        // Empty constructor required for DialogFragment

    }



    public static YesNoConfirmationDialog newInstance(String title, String confirmDetails) {

        YesNoConfirmationDialog frag = new YesNoConfirmationDialog();

        Bundle args = new Bundle();

        args.putString("title", title);
        args.putString( "confirmDetails",confirmDetails);

        frag.setArguments(args);

        return frag;

    }



    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getArguments().getString("title");
        final String confirmDetails = getArguments().getString("confirmDetails");

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle(title);

        alertDialogBuilder.setMessage(confirmDetails);

        alertDialogBuilder.setPositiveButton("Yes",  new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {


                if (confirmDetails.matches("you want to delete this CBV")){
                   // ((UpdateCbv)getActivity()).doPositiveClick();
                }
                else if (confirmDetails.matches("you want to delete this Savings Group")){
                   // ((SGUpdating)getActivity()).doPositiveClick();
                }
                else if (confirmDetails.matches("you want to delete this CLASS")){
                    //you want to remove this beneficiary from this class
                    //((BenInClass)getActivity()).doPositiveClick();
                }
                else if (confirmDetails.matches("you want to remove this beneficiary from this class")){
                    //you want to remove this beneficiary from this class
                    //((AttendanceLog)getActivity()).doPositiveClick();
                }
                else if (confirmDetails.matches("you want to re-enroll this beneficiary")){
                    //you want to remove this beneficiary from this class
                  //  ((ViewPaymentEnrollments)getActivity()).doPositiveClick();
                }
                else if (confirmDetails.matches("you want to re-upload this beneficiary")){
                    //you want to remove this beneficiary from this class
                  //  ((ViewPaymentEnrollmentForReupload)getActivity()).doPositiveClick();
                }
                else  {
                   // ((Admin) getActivity()).doPositiveClick();
                }

//you want to re-upload this beneficiary
                // on success

            }

        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

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