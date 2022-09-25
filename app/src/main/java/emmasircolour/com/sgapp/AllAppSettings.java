package emmasircolour.com.sgapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AllAppSettings extends Activity {
    public String url_register,url_sgregistration;
    public final int REQUEST_CODE = 101;
    public String PREF_DEVICE_ID="Device_id", deviceId,inputsgnumber,inputsgname;
    public String PREF_PHONE_NUMBER="Phone",userPhone,datejoinedgroup;

    public String url_getSG="https://cowzambia.com/SGAppapi/getsg.php";
    public String url_saveSGUser="https://cowzambia.com/SGAppapi/savesguser.php";
    public String URL_COLLECTION_TOKEN="https://sandbox.momodeveloper.mtn.com/collection/token/";
    public  String URL_CREATE_USER="https://sandbox.momodeveloper.mtn.com/v1_0/apiuser";
    public  String OcpSubscriptionkey="c7a74ff2f82f42da983abd858d0e483b"; //c7a74ff2f82f42da983abd858d0e483b
    public String URL_GEN_API_KEY;
    public  String userApikey,userpass,URL_CHECK_USER;
    public String PREF_API_KEY="GenApikey";
    public  String PREF_ACCESS_TOKEN="AccessToken";
    public  String PREF_DATE_ACCESSTOKEN_GEN="DateGenAccessToken";
    public String PREF_DATE_APIKEY_GEN="DateGenApikey";

    AlertDialog.Builder builder;


    public AllAppSettings(RegisterActivity registerActivity) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(this);
        deviceId = SP.getString(PREF_DEVICE_ID , "None");
        userApikey=SP.getString(PREF_API_KEY , "None");
        userPhone=SP.getString(PREF_PHONE_NUMBER , "None");

        url_register="https://cowzambia.com/SGAppapi/registration.php";
        url_sgregistration="https://cowzambia.com/SGAppapi/sgregistration.php";
        url_getSG="https://cowzambia.com/SGAppapi/getsg.php";
         url_saveSGUser="https://cowzambia.com/SGAppapi/savesguser.php";
         URL_COLLECTION_TOKEN="https://sandbox.momodeveloper.mtn.com/collection/token/";
        URL_CREATE_USER="https://sandbox.momodeveloper.mtn.com/v1_0/apiuser";
        OcpSubscriptionkey="c7a74ff2f82f42da983abd858d0e483b";
        URL_CHECK_USER="https://sandbox.momodeveloper.mtn.com/v1_0/apiuser/"+deviceId;

        URL_GEN_API_KEY="https://sandbox.momodeveloper.mtn.com/v1_0/apiuser/"+deviceId+"/apikey";


    }

    public AllAppSettings() {
       // SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplication());
       // deviceId = SP.getString(PREF_DEVICE_ID , "None");
       // userApikey=SP.getString(PREF_API_KEY , "None");
      //  userPhone=SP.getString(PREF_PHONE_NUMBER , "None");

        url_register="https://cowzambia.com/SGAppapi/registration.php";
        url_sgregistration="https://cowzambia.com/SGAppapi/sgregistration.php";
        url_getSG="https://cowzambia.com/SGAppapi/getsg.php";
        url_saveSGUser="https://cowzambia.com/SGAppapi/savesguser.php";
        URL_COLLECTION_TOKEN="https://sandbox.momodeveloper.mtn.com/collection/token/";
        URL_CREATE_USER="https://sandbox.momodeveloper.mtn.com/v1_0/apiuser";
        OcpSubscriptionkey="c7a74ff2f82f42da983abd858d0e483b";
        URL_CHECK_USER="https://sandbox.momodeveloper.mtn.com/v1_0/apiuser/"+deviceId;

        URL_GEN_API_KEY="https://sandbox.momodeveloper.mtn.com/v1_0/apiuser/"+deviceId+"/apikey";

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

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }
    //end here create user
    //create momo gen apk key
    public void genApiKey(String user)
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

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }
    public void setApiKey(String newAPIKey){
        SharedPreferences sharedPrefs = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
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
        SharedPreferences sharedPrefs = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
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

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

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

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

    //generate the unqnic transaction id
public String GenTxId(){
    String uniqueID = UUID.randomUUID().toString();
    System.out.println("Txid string: "+uniqueID);
    return uniqueID;
}
 public long FindTimeDifference(String start_date, String end_date)
    {
        long difference_In_Hours=0;

        // SimpleDateFormat converts the
        // string format to date object
        SimpleDateFormat sdf
                = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        // Try Block
        try {

            // parse method is used to parse
            // the text from a string to
            // produce the date
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            // Calucalte time difference
            // in milliseconds
            long difference_In_Time
                    = d2.getTime() - d1.getTime();

            // Calucalte time difference in
            // seconds, minutes, hours, years,
            // and days
            long difference_In_Seconds
                    = (difference_In_Time
                    / 1000)
                    % 60;

            long difference_In_Minutes
                    = (difference_In_Time
                    / (1000 * 60))
                    % 60;

            difference_In_Hours
                    = (difference_In_Time
                    / (1000 * 60 * 60))
                    % 24;

            long difference_In_Years
                    = (difference_In_Time
                    / (1000l * 60 * 60 * 24 * 365));

            long difference_In_Days
                    = (difference_In_Time
                    / (1000 * 60 * 60 * 24))
                    % 365;

            // Print the date difference in
            // years, in days, in hours, in
            // minutes, and in seconds

            System.out.print(
                    "Difference "
                            + "between two dates is: ");

            System.out.println(
                    difference_In_Years
                            + " years, "
                            + difference_In_Days
                            + " days, "
                            + difference_In_Hours
                            + " hours, "
                            + difference_In_Minutes
                            + " minutes, "
                            + difference_In_Seconds
                            + " seconds");
        }

        // Catch the Exception
        catch (ParseException e) {
            e.printStackTrace();
        }
        return difference_In_Hours;
    }




}
