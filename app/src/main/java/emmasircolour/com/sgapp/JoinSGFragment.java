package emmasircolour.com.sgapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JoinSGFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JoinSGFragment extends Fragment {

    private DatabaseHelper db;

    String  sgnumber, sgname, sgformationdate,deviceid,shareoutdate;//cycle, approvalcount, savebasevalue, noofweeks
    int  cycle, approvalcount, savebasevalue, noofweeks;
    String PREF_DEVICE_ID="Device_id", deviceId,inputsgnumber,inputsgname;
    String PREF_PHONE_NUMBER="Phone",userPhone,datejoinedgroup;
    String PREF_GROUP_JOINED="Joined_Group";
    Double loanservicecharge;
    String url_getSG="https://cowzambia.com/SGAppapi/getsg.php";
    String url_saveSGUser="https://cowzambia.com/SGAppapi/savesguser.php";
    AlertDialog.Builder builder;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public JoinSGFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JoinSGFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JoinSGFragment newInstance(String param1, String param2) {
        JoinSGFragment fragment = new JoinSGFragment();
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
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_join_s_g, container, false);
        View view = inflater.inflate(R.layout.fragment_join_s_g,container,false);
        PREF_DEVICE_ID="Device_id";
        db = new DatabaseHelper(getContext());

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getContext());
        deviceId = SP.getString(PREF_DEVICE_ID , "None");
        userPhone=SP.getString(PREF_PHONE_NUMBER , "None");
        Button buttonRegister= view.findViewById(R.id.buttonSearch);

        EditText inputPhoneNumber= view.findViewById(R.id.inputPhoneNumber);
        EditText inputSavingsGroupName= view.findViewById(R.id.inputGroupName);



//onclick for search button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        inputsgname=inputSavingsGroupName.getText().toString();
                        inputsgnumber=inputPhoneNumber.getText().toString();
                        downloadgetsg();




            }
        });







        return view;
    }

    //search for savings group
    private void downloadgetsg() {
        int MY_SOCKET_TIMEOUT_MS=5000;
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Searching...");
        progressDialog.show();

        //final String name = txtName.getText().toString().trim();
        System.out.println("Check URL : "+url_getSG);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url_getSG,
                new Response.Listener<String>() {
                    int numCwacs=0;
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Check Response : "+response);
                        if (response==null)
                        {
                            Log.i("JSON", "Null: " );
                            progressDialog.dismiss();
                        }
                        else {
                            System.out.println("Check Response : "+response);
                            //dismss dialog
                            //progressDialog.dismiss();
                            Date date= new Date();
                            // lowercase YYYY
                            SimpleDateFormat formatLowerCase = new SimpleDateFormat("yyyy-MM-dd");
                            datejoinedgroup=formatLowerCase.format(date);
                            System.out.println("Validation Date: " + formatLowerCase.format(date));


                            try {
                                //JSONObject jsonObj = new JSONObject(response);
                                // Getting JSON Array node
                                JSONArray jsonarray = new JSONArray(response);
                                numCwacs=jsonarray.length();
                                //JSONArray jsonArray = jsonObj.getJSONArray("result");
                                // numCwacs = jsonArray.length();
                                System.out.println("Check Response Array : "+jsonarray);
                                //JSONArray jsonArray = new JSONArray(response);
                                //JSONObject obj = new JSONObject(response);

                                //declare a jsonArray

                                //JSONArray jsonArray = new JSONArray(obj);




                                Log.i("JSON", "Number of Savings Group: " + numCwacs);
                                if (numCwacs==0){
                                    progressDialog.dismiss();
                                }
                                System.out.println("Count "+jsonarray.length());
                                System.out.println(jsonarray);
                                if (numCwacs >= 1) {
                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                                        cycle=jsonobject.getInt("cycle");
                                        approvalcount=jsonobject.getInt("approvalcount");
                                        savebasevalue=jsonobject.getInt("savebasevalue");
                                        noofweeks=jsonobject.getInt("noofweeks");
                                        sgnumber=jsonobject.getString("sgnumber");
                                        sgname=jsonobject.getString("sgname");
                                        sgformationdate=jsonobject.getString("sgformationdate");
                                        shareoutdate=jsonobject.getString("shareoutdate");
                                        deviceid =jsonobject.getString("deviceid");
                                        loanservicecharge=jsonobject.getDouble("loanservicecharge");


                                    }






                                    //Thread.sleep(10);
                                    progressDialog.dismiss();
                                    //alert the user if the savings has been found
                                    builder = new AlertDialog.Builder(getContext());

                                    StringBuilder detConfirmation = new StringBuilder();
                                    detConfirmation.append("Savings Group Name :"+sgname);
                                    detConfirmation.append(System.getProperty("line.separator"));
                                    detConfirmation.append("Savings Group Number :"+sgnumber);
                                    detConfirmation.append(System.getProperty("line.separator"));
                                    detConfirmation.append("Group Formation Date :"+sgformationdate);
                                    detConfirmation.append(System.getProperty("line.separator"));
                                    detConfirmation.append("Loan Service Charge %:"+loanservicecharge);
                                    detConfirmation.append(System.getProperty("line.separator"));
                                    detConfirmation.append("Savings Cycle :"+cycle);
                                    detConfirmation.append(System.getProperty("line.separator"));
                                    detConfirmation.append("Shareout Date :"+shareoutdate);
                                    detConfirmation.append(System.getProperty("line.separator"));
                                    detConfirmation.append("Approval Count :"+approvalcount);
                                    detConfirmation.append(System.getProperty("line.separator"));
                                    detConfirmation.append("Weekly Minimum Savings Value :"+savebasevalue);
                                    detConfirmation.append(System.getProperty("line.separator"));
                                    detConfirmation.append("No of Weeks:"+noofweeks);



                                    builder.setMessage(detConfirmation)
                                            .setCancelable(false)
                                            .setTitle("Savings Group Details")
                                            .setPositiveButton("Join", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {


                                                    //Toast.makeText(getContext(),"saved", Toast.LENGTH_SHORT).show();
                                           if (db.usersavingsgroup(sgnumber,userPhone,datejoinedgroup,1,1)>=1) {
                                            //System.out.println("Added User");
                                               SharedPreferences sharedPrefs = android.preference.PreferenceManager.getDefaultSharedPreferences(getContext());
                                               SharedPreferences.Editor editor = sharedPrefs.edit();
                                               editor = sharedPrefs.edit();
                                               editor.putString(PREF_GROUP_JOINED, sgnumber);
                                               editor.commit();

                                               if (db.addSavingsGroup(sgnumber, sgname, sgformationdate, cycle, shareoutdate, approvalcount, savebasevalue, noofweeks, deviceid,loanservicecharge) >= 1) {
                                                   Toast.makeText(getContext(),"Congrats Joined!! ", Toast.LENGTH_SHORT).show();
                                                   new JoinSGFragment.DoJoinedUserUploadTask().execute();
                                               }
                                           }

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



                                   // Toast.makeText(getContext(),numCwacs+" Savings Group Found", Toast.LENGTH_LONG).show();

                                    // updateSqliteCBV(cbvserial, cbvuuid, cwac_key, cbvId, cbvFname, cbvLName, cbvNRC, cbvPhone, cbvGender, cbvsdob, cbvAddress, cbvsNextOfKin, cbvsNextOfKinPhone, cbvdateOfEngagement, 2, deviceId,cbvactive);







                                } else {
                                    //if there is some error
                                    //saving the name to sqlite with status unsynced


                                }


                                //catch
                            } catch (JSONException e) {

                                Log.i("JSON", "Null: " + response);

                                e.printStackTrace();
                            }

                        }

                        //dismiss dialpg


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        System.out.println(" Error"+error);
                        //hundle error

                        error.printStackTrace();
                        Log.d("SWL", "error:" + error);
                        int errorCode = 0;
                        if (error instanceof TimeoutError) {
                            errorCode = -7;
                        } else if (error instanceof NoConnectionError) {
                            errorCode = -1;
                        } else if (error instanceof AuthFailureError) {
                            errorCode = -6;
                        } else if (error instanceof ServerError) {
                            errorCode = 0;
                        } else if (error instanceof NetworkError) {
                            errorCode = -1;
                        } else if (error instanceof ParseError) {
                            errorCode = -8;
                        }
                        Log.d("SWL", "error:" + error+errorCode);





                        Toast.makeText(getContext(), "Error "+ error, Toast.LENGTH_LONG).show();

                        Log.d("Web Service url",url_getSG);
                    }
                })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sgnumber", inputsgnumber );
                params.put("sgname", inputsgname );
                return params;
            }
        };

        //here
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        //end
        System.out.println("Check Payload : "+stringRequest);
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
    //end upload

    private void saveSGUser(final String sgnumber,  final String userphonenumber,  final String datejoined,  final int syncode, final int memberactive)
    {


        // Save the registration to remote server
        StringRequest stringRequest = new StringRequest(Request.Method.POST, this.url_saveSGUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("Check Response"+ response+"URL "+url_saveSGUser);

                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {

                                System.out.println("Check Response"+ response+"SAVED remotely");

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
                //final String sgnumber,  final String userphonenumber,  final String datejoined,  final int syncode, final int memberactive
                params.put("sgnumber", sgnumber);
                params.put("userphonenumber",userphonenumber);
                params.put("datejoined",datejoined );
                params.put("syncode",String.valueOf(syncode));
                params.put("memberactive",String.valueOf(memberactive));



                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    //upload registration

    private class DoJoinedUserUploadTask extends AsyncTask<Void, Integer,Integer> {

        protected Integer doInBackground(Void... params) {
            //for (int i = 0; i <= 10; i++) {
            int progressStatus=0;
            //rec2Syc=0;

            try {

                //rec2Syc=cursor.getCount();

                //simpleProgressBar.setVisibility(View.VISIBLE);
                //phonenumber,firstname ,lastname ,gender ,email,password,title ,deviceId

                //System.out.println("Installation string: "+ deviceId);
                saveSGUser(sgnumber,userPhone,datejoinedgroup,1,1);

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