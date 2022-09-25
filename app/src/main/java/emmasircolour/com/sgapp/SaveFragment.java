package emmasircolour.com.sgapp;


import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SaveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaveFragment extends Fragment {
    private DatabaseHelper db;
    private ApiAccess Access;
    private AllAppSettings AppSettings;
    public String transactionId ,currency,externalId, payerMessage,payeeNote;
    String  sgnumber, sgname, sgformationdate,deviceid,shareoutdate;//cycle, approvalcount, savebasevalue, noofweeks
    int  cycle, approvalcount, savebasevalue, noofweeks;
    public String PREF_DEVICE_ID="Device_id", deviceId,inputsgnumber,inputsgname;
    String PREF_PHONE_NUMBER="Phone",userPhone,datejoinedgroup;
    String PREF_GROUP_JOINED="Joined_Group";
    Double loanservicecharge;
    public String url_getSG="https://cowzambia.com/SGAppapi/getsg.php";
    public String url_saveSGUser="https://cowzambia.com/SGAppapi/savesguser.php";
    public String URL_COLLECTION_TOKEN="https://sandbox.momodeveloper.mtn.com/collection/token/";
    public String URL_CREATE_USER="https://sandbox.momodeveloper.mtn.com/v1_0/apiuser";
    public String URL_REQUEST2_PAY="https://sandbox.momodeveloper.mtn.com/collection/v1_0/requesttopay";
    public String OcpSubscriptionkey="c7a74ff2f82f42da983abd858d0e483b"; //c7a74ff2f82f42da983abd858d0e483b
    public String base64EncodedactiveAccessToken;
    public String URL_GEN_API_KEY;
    public String userApikey,userpass,URL_CHECK_USER;
    public String PREF_API_KEY="GenApikey";
    public String PREF_ACCESS_TOKEN="AccessToken";
    public String PREF_DATE_ACCESSTOKEN_GEN="DateGenAccessToken";
    public String PREF_DATE_APIKEY_GEN="DateGenApikey";
    public String oldtokendate, activeAccessToken;

    AlertDialog.Builder builder;
    EditText InputSaveAmount;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SaveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SaveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SaveFragment newInstance(String param1, String param2) {
        SaveFragment fragment = new SaveFragment();
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
       // return inflater.inflate(R.layout.fragment_save, container, false);
        View view = inflater.inflate(R.layout.fragment_save,container,false);
        PREF_DEVICE_ID="Device_id";
        db = new DatabaseHelper(getContext());
        Access= new ApiAccess();
        AppSettings=new AllAppSettings();


        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getContext());
        deviceId = SP.getString(PREF_DEVICE_ID , "None");
        userApikey=SP.getString(PREF_API_KEY , "None");
        userPhone=SP.getString(PREF_PHONE_NUMBER , "None");//PREF_DATE_APIKEY_GEN,PREF_GROUP_JOINED
        sgnumber=SP.getString(PREF_GROUP_JOINED, "None");
        oldtokendate= (SP.getString(PREF_DATE_ACCESSTOKEN_GEN , "2022-08-01 10:00:00"));

        URL_CHECK_USER="https://sandbox.momodeveloper.mtn.com/v1_0/apiuser/"+deviceId;

        URL_GEN_API_KEY="https://sandbox.momodeveloper.mtn.com/v1_0/apiuser/"+deviceId+"/apikey";

        Button buttonWeeklySave= view.findViewById(R.id.buttonWeeklySave);
        InputSaveAmount=view.findViewById(R.id.inputSaveAmount);
        buttonWeeklySave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // genApiKey(deviceId);
                SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getContext());
                deviceId = SP.getString(PREF_DEVICE_ID , "None");
                userApikey=SP.getString(PREF_API_KEY , "None");
                userPhone=SP.getString(PREF_PHONE_NUMBER , "None");//PREF_DATE_APIKEY_GEN
                oldtokendate= (SP.getString(PREF_DATE_ACCESSTOKEN_GEN , "2022-08-01 10:00:00"));

                Date date= new Date();
                SimpleDateFormat formatLowerCase = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String todayNow =formatLowerCase.format(date);
                long timepassed=  AppSettings.FindTimeDifference(oldtokendate,todayNow);

                if (timepassed>=1){
                    genBeareToken(deviceId,userApikey);
                }
                //get the valid accesstoken PREF_ACCESS_TOKEN
                activeAccessToken= (SP.getString(PREF_ACCESS_TOKEN , "2022-08-01 10:00:00"));
                System.out.println("Token Plain Text "+activeAccessToken);
                System.out.println("APIKEY "+userApikey);
                System.out.println("User "+deviceId);
               // OcpSubscriptionkey=Base64.encodeToString(OcpSubscriptionkey.getBytes(), Base64.NO_WRAP);
                base64EncodedactiveAccessToken = Base64.encodeToString(activeAccessToken.getBytes(), Base64.NO_WRAP);
                //activeAccessToken="Bearer Token "+base64EncodedactiveAccessToken;
                //activeAccessToken=Base64.encodeToString(activeAccessToken.getBytes(), Base64.NO_WRAP);
                transactionId=GenTxId();


                currency="EUR";
                externalId=transactionId;
                payerMessage="Weekly SG Save";
                payeeNote=userPhone;
                JSONObject payer = new JSONObject();
                try {
                    payer.put("partyIdType", "MSISDN");
                    payer.put("partyId", sgnumber);
                    System.out.println("SG Phone: "+sgnumber);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                DepositeWkySave(InputSaveAmount.getText().toString(),currency,externalId,payer,payerMessage,payeeNote,base64EncodedactiveAccessToken);
               // Request2Pay();
                // checkApiUser();
              String test=  "";
                     // genApiKey(test);
               // creatApiUser();
                //genApiKey(deviceId);
               // genBeareToken(deviceId,userApikey);


                System.out.println("Hours Passed last Gen: "+timepassed);
                Toast.makeText(getContext(),"Saved Sucessfully !! ", Toast.LENGTH_SHORT).show();


            }
        });









      return view;
    }
    //create momo api user
    private void creatApiUser()
    {


        // StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_NAME,
        StringRequest stringRequest = new StringRequest(Request.Method.POST, this.URL_CREATE_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);

                    }
                }) {


            //authentication headers
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonBody = new JSONObject();
                try {
                    // jsonBody.put("username", serverUserName);
                    // jsonBody.put("password", serverPass);
                    // jsonBody.put("serial", (String.valueOf(cbvserial1)));
                    //jsonBody.put("sgid",(String.valueOf(sgid)));
                    jsonBody.put("providerCallbackHost","https://webhook.site/c05d8e15-3e1f-41ea-830e-4ca0cb14eb5f");

                } catch (JSONException e) {
                    //System.out.println("Exception :" +e);
                    e.printStackTrace();
                }

                final String requestBody = jsonBody.toString();
                System.out.println("Request Body "+ requestBody);
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                // headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                // headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Content-Type", " application/json");
                headers.put("Ocp-Apim-Subscription-Key",OcpSubscriptionkey); //authentication
                headers.put("X-Reference-Id", deviceId);//5a12097b-1355-4f05-b476-e45286be48ee
                //headers.put("X-Reference-Id", "0508432a-2fd7-4dae-b17f-5f8157c19557");

                return headers;
            }

            //end authentication headers
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                //params.put("synCode",(String.valueOf(synCode)));
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);


    }
    //end here create user
    //create momo gen apk key
    private void genApiKey(String user)
    {
        String URL_GEN_API_KEY="test";

        // StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_NAME,
        StringRequest stringRequest = new StringRequest(Request.Method.POST, this.URL_GEN_API_KEY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("Check Response"+response);
                            JSONObject obj = new JSONObject(response);
                            setApiKey(obj.getString("apiKey"));
                            System.out.println("Check apiKey: "+obj.getString("apiKey"));
genBeareToken(deviceId,obj.getString("apiKey"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);

                    }
                }) {



            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                // headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                // headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Content-Type", " application/json");
                headers.put("Ocp-Apim-Subscription-Key",OcpSubscriptionkey); //authentication
               // headers.put("X-Reference-Id", deviceId);

                return headers;
            }

            //end authentication headers
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                //params.put("synCode",(String.valueOf(synCode)));
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);


    }
    public void setApiKey(String newAPIKey){
        SharedPreferences sharedPrefs = android.preference.PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor = sharedPrefs.edit();
        Date date= new Date();
        SimpleDateFormat formatLowerCase = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dategen=formatLowerCase.format(date);
        System.out.println("Validation Date: " + formatLowerCase.format(date));
        // System.out.println("Installation string: "+uniqueID);
        editor.putString(PREF_API_KEY, newAPIKey);
        editor.putString(PREF_DATE_APIKEY_GEN, dategen);
        editor.commit();
    }
    public void setAcessToken(String newAcessToken){
        SharedPreferences sharedPrefs = android.preference.PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor = sharedPrefs.edit();
        Date date= new Date();
        SimpleDateFormat formatLowerCase = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dategen=formatLowerCase.format(date);
        // System.out.println("Installation string: "+uniqueID);
        editor.putString(PREF_ACCESS_TOKEN, newAcessToken);
        editor.putString(PREF_DATE_ACCESSTOKEN_GEN, dategen);
        editor.commit();
    }
    //bearer token generation
    private void genBeareToken(String user,String pass)
    {
        String serverPass;


        // StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_NAME,
        StringRequest stringRequest = new StringRequest(Request.Method.POST, this.URL_COLLECTION_TOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("Check Response"+response);
                            JSONObject obj = new JSONObject(response);
                            setAcessToken(obj.getString("access_token"));
                            System.out.println("Check Access Token: "+obj.getString("access_token"));

                        } catch (JSONException e) {
                            System.out.println(e.getMessage().toString());
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());

                    }
                }) {



            @Override
            public Map getHeaders() throws AuthFailureError {
                String credentials =  user+":"+pass;
                //credentials="75760e5d-acbf-4e35-b2c4-9bb107fcfea6"+":"+"479a40e8f3d6417682b0a4455893e54a";
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                HashMap headers = new HashMap();
                // headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                // headers.put("Content-Type", "application/json; charset=utf-8");

                credentials="Basic "+base64EncodedCredentials;

                headers.put("Authorization",credentials); //authentication
                // headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Content-Type", " application/json");
                headers.put("Ocp-Apim-Subscription-Key",OcpSubscriptionkey); //authentication
                // headers.put("X-Reference-Id", deviceId);

                return headers;
            }

            //end authentication headers
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Username", user);
                params.put("Password", pass);
                //params.put("synCode",(String.valueOf(synCode)));
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }
    //check api user
    private void checkApiUser()
    {


        // StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_NAME,
        StringRequest stringRequest = new StringRequest(Request.Method.GET, this.URL_CHECK_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("Check Response"+response);

                            JSONObject obj = new JSONObject(response);

                            if (obj.getString("targetEnvironment")!=null) {

                                System.out.println("User Set In :"+obj.getString("targetEnvironment"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);

                    }
                }) {


            //authentication headers
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonBody = new JSONObject();
                try {
                    // jsonBody.put("username", serverUserName);
                    // jsonBody.put("password", serverPass);
                    // jsonBody.put("serial", (String.valueOf(cbvserial1)));
                    //jsonBody.put("sgid",(String.valueOf(sgid)));
                    jsonBody.put("providerCallbackHost","https://www.cowzambia.com");

                } catch (JSONException e) {
                    //System.out.println("Exception :" +e);
                    e.printStackTrace();
                }

                final String requestBody = jsonBody.toString();
                System.out.println("Request Body "+ requestBody);
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                // headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                // headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Content-Type", " application/json");
                headers.put("Ocp-Apim-Subscription-Key",OcpSubscriptionkey); //authentication
                //headers.put("X-Reference-Id", deviceId);

                return headers;
            }

            //end authentication headers
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                //params.put("synCode",(String.valueOf(synCode)));
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);


    }
    //save the weekly deposit
    private void DepositeWkySave(String amount,String currency,String externalId,JSONObject Payer,String payerMessage,String payeeNote,String AccToken)
    {



        StringRequest stringRequest = new StringRequest(Request.Method.POST, this.URL_REQUEST2_PAY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("Check Response"+response);

                            JSONObject obj = new JSONObject(response);

                            if (obj.getString("targetEnvironment")!=null) {

                                System.out.println("User Set In :"+obj.getString("targetEnvironment"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            //This indicates that the reuest has either time out or there is no connection

                        } else if (error instanceof AuthFailureError) {
                            // Error indicating that there was an Authentication Failure while performing the request
                            System.out.println(error.getMessage());

                            try {
                               String  json = new String(error.networkResponse.data,
                                        HttpHeaderParser.parseCharset(error.networkResponse.headers));
                                System.out.println(json);

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }


                        } else if (error instanceof ServerError) {
                            //Indicates that the server responded with a error response

                        } else if (error instanceof NetworkError) {
                            //Indicates that there was network error while performing the request

                        } else if (error instanceof ParseError) {
                            // Indicates that the server response could not be parsed

                        }

                    }
                }) {



            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonBody = new JSONObject();
                try {
                    //String amount,String currency,String externalId,JSONObject Payer,String payerMessage,String payeeNote
                    jsonBody.put("amount", amount);
                    jsonBody.put("currency", currency);
                    jsonBody.put("externalId", externalId);
                    jsonBody.put("Payer",Payer);
                    jsonBody.put("payerMessage",payerMessage);
                    jsonBody.put("payeeNote",payeeNote);
                    //jsonBody.put("providerCallbackHost","https://webhook.site/c05d8e15-3e1f-41ea-830e-4ca0cb14eb5f");
                } catch (JSONException e) {
                    System.out.println("Exception :" +e);
                    e.printStackTrace();
                }

                final String requestBody = jsonBody.toString();
                System.out.println("Request Body "+ requestBody);
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
            //headers from here
            //parameters
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //headers.put("Authorization",activeAccessToken);
               String cred="Bearer "+base64EncodedactiveAccessToken;
              //params.put("Authorization","Bearer "+AccToken); //authentication
                //params.put("synCode",(String.valueOf(synCode)));
                return params;
            }

//authentication headers
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();

                //  headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Content-Type", " application/json");
                headers.put("Ocp-Apim-Subscription-Key",OcpSubscriptionkey); //authentication
                headers.put("X-Reference-Id", transactionId);
                headers.put("X-Target-Environment", "sandbox");
                headers.put("X-Callback-Url", "https://webhook.site/c05d8e15-3e1f-41ea-830e-4ca0cb14eb5f");
              //  activeAccessToken="Bearer "+activeAccessToken;
               activeAccessToken=Base64.encodeToString(activeAccessToken.getBytes(), Base64.NO_WRAP);
                System.out.println("Base 64 Token "+activeAccessToken);
               // headers.put("Token",activeAccessToken);

                String cred="Bearer Token "+AccToken;
                //headers.put("Authorization",cred);//authentication
              headers.put("Authorization","Bearer "+activeAccessToken);
                return headers;
            }

            //end authentication headers

        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);


    }
//generate the new transaction id
    public String GenTxId(){
        String uniqueID = UUID.randomUUID().toString();
        System.out.println("Txid string: "+uniqueID);
        return uniqueID;
    }

//test
public void Request2Pay()
{
    HttpClient httpclient = HttpClients.createDefault();

    try
    {
        URIBuilder builder = new URIBuilder("https://sandbox.momodeveloper.mtn.com/collection/v1_0/requesttopay");

        URI uri = builder.build();
        HttpPost request = new HttpPost(uri);
        request.setHeader("Authorization", "Bearer "+base64EncodedactiveAccessToken);
        request.setHeader("X-Callback-Url", "");
        request.setHeader("X-Reference-Id", transactionId);
        request.setHeader("X-Target-Environment", "sandbox");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Ocp-Apim-Subscription-Key", OcpSubscriptionkey);


        // Request body
        StringEntity reqEntity = new StringEntity("{}");
        request.setEntity(reqEntity);

        HttpResponse response = httpclient.execute(request);
        System.out.println(" New request "+request);
     //   HttpEntity entity = response.getEntity();

        if (response != null)
        {
            System.out.println(response);
        }
    }
    catch (Exception e)
    {
        System.out.println(e.getMessage());
    }
}


}