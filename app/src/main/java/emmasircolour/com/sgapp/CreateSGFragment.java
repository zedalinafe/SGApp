package emmasircolour.com.sgapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateSGFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateSGFragment extends Fragment {


    private DatabaseHelper db;
    String  sgnumber, sgname, sgformationdate,deviceid,shareoutdate;//cycle, approvalcount, savebasevalue, noofweeks
    int  cycle, approvalcount, savebasevalue, noofweeks;
    Double loanservicecharge;
    String PREF_DEVICE_ID="Device_id", deviceId;
    String url_sgregistration="https://cowzambia.com/SGAppapi/sgregistration.php";
    private AllAppSettings AppSettings;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public int fyear,fmonth,fday;
    private int mYear, mMonth, mDay, mHour, mMinute;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AlertDialog.Builder builder;

    public CreateSGFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment testFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateSGFragment newInstance(String param1, String param2) {
        CreateSGFragment fragment = new CreateSGFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
View view = inflater.inflate(R.layout.fragment_create_group,container,false);
        PREF_DEVICE_ID="Device_id";
        db = new DatabaseHelper(getContext());
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getContext());
        deviceId = SP.getString(PREF_DEVICE_ID , "None");
        Button buttonRegister= view.findViewById(R.id.btnRegisterSG);

        EditText inputFormationDate= view.findViewById(R.id.inputFormationDate);
        EditText inputSavingsGroupName= view.findViewById(R.id.inputSavingsGroupName);
        EditText inputSGPhoneNumber= view.findViewById(R.id.inputSGPhoneNumber);
        EditText inputCycle= view.findViewById(R.id.inputCycle);
        EditText inputApprovalCount= view.findViewById(R.id.inputApprovalCount);
        EditText inputBaseValue= view.findViewById(R.id.inputBaseValue);
        EditText inputloanservicecharge= view.findViewById(R.id.inputloanservicecharge);


        builder = new AlertDialog.Builder(getContext());

        //register sg
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//sgphonenumber, sgname, sgformationdate,deviceid,shareoutdate;//cycle, approvalcount, savebasevalue, noofweeks
                sgformationdate=inputFormationDate.getText().toString();
                cycle=Integer.parseInt( inputCycle.getText().toString());
                noofweeks=((cycle*31)/7);
                approvalcount=Integer.parseInt(inputApprovalCount.getText().toString());
                shareoutdate=addDates(sgformationdate,cycle*31);
                savebasevalue=Integer.parseInt(inputBaseValue.getText().toString());
                sgname=inputSavingsGroupName.getText().toString();
                sgnumber=inputSGPhoneNumber.getText().toString();
                loanservicecharge=Double.valueOf(inputloanservicecharge.getText().toString());
                StringBuilder detConfirmation = new StringBuilder();
                detConfirmation.append("Savings Group Name :"+inputSavingsGroupName.getText().toString());
                detConfirmation.append(System.getProperty("line.separator"));
                detConfirmation.append("Savings Group Number :"+inputSGPhoneNumber.getText().toString());
                detConfirmation.append(System.getProperty("line.separator"));
                detConfirmation.append("Group Formation Date :"+inputFormationDate.getText().toString());
                detConfirmation.append(System.getProperty("line.separator"));
                detConfirmation.append("Savings Cycle :"+inputCycle.getText().toString());
                detConfirmation.append(System.getProperty("line.separator"));
                detConfirmation.append("Loan Service Charge % :"+inputloanservicecharge.getText().toString());
                detConfirmation.append(System.getProperty("line.separator"));
                detConfirmation.append("Shareout Date :"+shareoutdate);
                detConfirmation.append(System.getProperty("line.separator"));
                detConfirmation.append("Approval Count :"+inputApprovalCount.getText().toString());
                detConfirmation.append(System.getProperty("line.separator"));
                detConfirmation.append("Weekly Minimum Savings Value :"+inputBaseValue.getText().toString());
                detConfirmation.append(System.getProperty("line.separator"));
                detConfirmation.append("No of Weeks:"+noofweeks);


                builder.setMessage(detConfirmation)
                        .setCancelable(false)
                        .setTitle("Confirm Savings Group Details")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //Toast.makeText(getContext(),"saved", Toast.LENGTH_SHORT).show();
                                new DoRegistrationUploadTask().execute();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                alert.show();

            }
        });


        //generate the date picker
        inputFormationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

// Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                // Create the new DatePickerDialog instance.
                // Create a new OnDateSetListener instance. This listener will be invoked when user click ok button in DatePickerDialog.
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        StringBuffer strBuf = new StringBuffer();
                        //strBuf.append("You select date is ");
                        final String MonthM,DayM,YearM;

                        strBuf.append(year);
                        strBuf.append("-");
                        month=month+1;
                        fmonth=month;
                        fyear=year;fday=dayOfMonth;
                        if (month <= 9)
                        {

                            MonthM="0"+month;
                            strBuf.append( MonthM);
                        }else{
                            //MonthM=(String.valueOf(mMonth+1));
                            strBuf.append(month);
                        }
                        strBuf.append("-");
                        if (dayOfMonth < 10)
                        {
                            DayM="0"+dayOfMonth;
                            strBuf.append(DayM);
                        }else
                        {
                            strBuf.append(dayOfMonth);
                            //DayM=(String.valueOf(mDay));
                        }

                        //TextView datePickerValueTextView = (TextView)findViewById(R.id.datePickerValue);
                        String classStartDate=strBuf.toString();
                        //classEndDate=(addDates(classStartDate));
                        inputFormationDate.setText(classStartDate);

                    }
                };

                // Get current year, month and day.
                Calendar now = Calendar.getInstance();
                int year = now.get(java.util.Calendar.YEAR);
                int month = now.get(java.util.Calendar.MONTH);
                int day = now.get(java.util.Calendar.DAY_OF_MONTH);
                // Create the new DatePickerDialog instance.
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), onDateSetListener, year, month, day);
                // Set dialog icon and title.
                //datePickerDialog.setIcon(R.drawable.if_snowman);
                datePickerDialog.setTitle("Please select formation  date.");
                // Popup the dialog.
                datePickerDialog.show();
                //date picker end here



            }
        });

        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_test, container, false);

        //radio button cycle



        return view;


    }


    //upload savings group registration
     // phonenumber, sgname, sgformationdate, cycle, shareoutdate, approvalcount, savebasevalue, noofweeks,deviceid
    private void saveRegistration(final String sgnumber,final String sgname ,final String sgformationdate,final int cycle ,final String shareoutdate,final int approvalcount,final int savebasevalue,final int noofweeks,final String deviceId,final Double loanservicecharge)
    {


        // Save the registration to remote server
        StringRequest stringRequest = new StringRequest(Request.Method.POST, this.url_sgregistration,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("Check Response"+ response+"URL "+url_sgregistration);

                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {

                                System.out.println("Check Response"+ response+"SAVED");
                                long succuss=db.addSavingsGroup(sgnumber,sgname,sgformationdate,cycle,shareoutdate,approvalcount,savebasevalue,noofweeks,deviceId,loanservicecharge);
                                if (succuss>0){
                                    System.out.println(" Saved to sqlite");
                                }
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                // phonenumber, sgname, sgformationdate, cycle, shareoutdate, approvalcount, savebasevalue, noofweeks,deviceid
                params.put("sgnumber", sgnumber);
                params.put("sgname",sgname);
                params.put("sgformationdate",sgformationdate );
                params.put("cycle",String.valueOf(cycle));
                params.put("shareoutdate",shareoutdate);
                params.put("approvalcount",String.valueOf(approvalcount));
                params.put("savebasevalue",String.valueOf(savebasevalue));
                params.put("deviceid",deviceId);
                params.put("noofweeks",String.valueOf(noofweeks));
                params.put("loanservicecharge",String.valueOf(loanservicecharge));


                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    public String addDates(String olddate,int numcycle){
        //Given Date in String format
        String oldDate = olddate;
        //System.out.println("Date before Addition: "+oldDate);
        //Specifying date format that matches the given date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try{
            //Setting the date to the given date
            c.setTime(sdf.parse(oldDate));
        }catch(ParseException e){
            e.printStackTrace();
        }

        //Number of Days to add
        c.add(Calendar.DAY_OF_MONTH, numcycle);
        //Date after adding the days to the given date
        String newDate = sdf.format(c.getTime());
        //Displaying the new Date after addition of Days
        //System.out.println("Date after Addition: "+newDate);
        return newDate;
    }

    //upload registration

    private class DoRegistrationUploadTask extends AsyncTask<Void, Integer,Integer> {

        protected Integer doInBackground(Void... params) {
            //for (int i = 0; i <= 10; i++) {
            int progressStatus=0;
            //rec2Syc=0;

            try {

                //rec2Syc=cursor.getCount();

                //simpleProgressBar.setVisibility(View.VISIBLE);
                //phonenumber,firstname ,lastname ,gender ,email,password,title ,deviceId

                //System.out.println("Installation string: "+ deviceId);
                saveRegistration(sgnumber,sgname,sgformationdate,cycle,shareoutdate,approvalcount,savebasevalue,noofweeks,deviceId,loanservicecharge);

                //publishProgress(rec2Syc);


                //Thread.sleep(2000);

                //---report its progress---





                //hid the progressbar





                Thread.sleep(2000);

            } catch (Exception e) {
                Log.d("Threading", e.getLocalizedMessage());
            }
            // if (isCancelled()) break;
            //}

            return 1;
        }
        protected void onProgressUpdate(Integer...progress) {

            // txtView1.setText(("")+String.valueOf(rec2Syc)+"/"+ String.valueOf(numCBVs)+" Uploaded");
            //progressUpload=rec2Syc;
            Log.d("Threading", "updating...");
        }

        //---when all the uploads have been done---
        protected void onPostExecute(Integer progress) {

            //simpleProgressBar.setVisibility(View.INVISIBLE);
            // AlertDialog.Builder builder = new AlertDialog.Builder(UploadData.this);
            //builder.setTitle("Upload Result");
            //builder.setMessage(rec2Syc + " SG Member Uploaded Successfully");
            //builder.setPositiveButton("OK",
            // new DialogInterface.OnClickListener() {
            // public void onClick(DialogInterface dialog,int which) {

            //ok clicked

            // }
            // });
            /// builder.show();
            //end alert






        }

    }
    //end registration upload



}



