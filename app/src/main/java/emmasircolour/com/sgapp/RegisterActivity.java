package emmasircolour.com.sgapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private AllAppSettings AppSettings;
    public final int REQUEST_CODE = 101;
    public String phonenumber,firstname,lastname  ,email,password,title ,deviceId;
    String url_register="https://cowzambia.com/SGAppapi/registration.php";
    String url_sgregistration="https://cowzambia.com/SGAppapi/sgregistration.php";
    String url_getSG="https://cowzambia.com/SGAppapi/getsg.php";
    String url_saveSGUser="https://cowzambia.com/SGAppapi/savesguser.php";
    String URL_COLLECTION_TOKEN="https://sandbox.momodeveloper.mtn.com/collection/token/";
    String URL_CREATE_USER="https://sandbox.momodeveloper.mtn.com/v1_0/apiuser";
    String OcpSubscriptionkey="c7a74ff2f82f42da983abd858d0e483b";
    String URL_CHECK_USER="https://sandbox.momodeveloper.mtn.com/v1_0/apiuser/"+deviceId;

    String URL_GEN_API_KEY="https://sandbox.momodeveloper.mtn.com/v1_0/apiuser/"+deviceId+"/apikey";
    public int gender;
    String PREF_DEVICE_ID="Device_id";
    String PREF_PHONE_NUMBER="Phone";
    String PREF_USER_PASS="Password";
    String PREF_FISRT_NAME="Firstname";
    String PREF_LAST_NAME="Lastname";
    String PREF_USER_EMAIL="Email";
    String PREF_USER_SET="No";

    String PREF_API_USER_SET="Api_User_Set";
    String apiuserset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(this);
        deviceId = SP.getString(PREF_DEVICE_ID , "None");
        apiuserset=SP.getString(PREF_API_USER_SET, "None");
        String OcpSubscriptionkey="c7a74ff2f82f42da983abd858d0e483b";
        String URL_CHECK_USER="https://sandbox.momodeveloper.mtn.com/v1_0/apiuser/"+deviceId;
        URL_CHECK_USER="https://sandbox.momodeveloper.mtn.com/v1_0/apiuser/"+deviceId;


       // AppSettings=new AllAppSettings(this);

        TextView btn=findViewById(R.id.alreadyHaveAccount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });


        Button buttonRegister =  findViewById(R.id.btnRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //phonenumber,firstname ,lastname ,gender ,email,password,title ,deviceId
                EditText EditTextphonenumber,EditTextfirstname ,EditTextlastname  ,EditTextemail,EditTextpassword ;
                EditTextphonenumber=  findViewById(R.id.inputPhoneNumber);
                EditTextfirstname=  findViewById(R.id.inputFirstname);
                EditTextlastname= findViewById(R.id.inputLastName);
                EditTextemail= findViewById(R.id.inputEmail);
                EditTextpassword= findViewById(R.id.inputPassword);
                title="Mr";
                phonenumber=EditTextphonenumber.getText().toString();
                firstname=EditTextfirstname.getText().toString();
                lastname=EditTextlastname.getText().toString();
                email=EditTextemail.getText().toString();
                password=EditTextpassword.getText().toString();
                gender=1;




                new DoRegistrationUploadTask().execute();

                //Toast.makeText(getApplicationContext(), "Button working "+AppSettings.url_register, Toast.LENGTH_SHORT).show();



            }
        });




    }







  //upload registration
    // // add the prameters phonenumber,firstname ,lastname ,gender ,email,password,title ,deviceId
  private void saveRegistration(final String phonenumber,final String firstname ,final String lastname,final int gender ,final String email,final String password,final String title,final String deviceId)
  {


      // Save the registration to remote server
      StringRequest stringRequest = new StringRequest(Request.Method.POST, this.url_register,
              new Response.Listener<String>() {
                  @Override
                  public void onResponse(String response) {
                      try {
                          System.out.println("Check Response"+ response+"URL "+url_register);

                          JSONObject obj = new JSONObject(response);
                          if (!obj.getBoolean("error")) {

                              System.out.println("Check Response"+ response+"SAVED");
                              if(apiuserset.matches("No")){
                                  creatApiUser();
                                  //checkApiUser();
                              }

                              setUserConfig();
                              startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                              //sending the broadcast to refresh the list
                              //context.sendBroadcast(new Intent(MainActivity.DATA_SAVED_BROADCAST));
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
              // add the prameters phonenumber,firstname ,lastname ,gender ,email,password,title ,deviceId
              params.put("phonenumber", phonenumber);
              params.put("firstname",firstname);
              params.put("lastname",lastname );
              params.put("gender",String.valueOf(gender));
              params.put("email",email);
              params.put("password",password);
              params.put("title",title);
              params.put("deviceid",deviceId);


              return params;
          }
      };

      VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

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
                SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                deviceId = SP.getString(PREF_DEVICE_ID , "None");
                //System.out.println("Installation string: "+ deviceId);
                saveRegistration(phonenumber,firstname,lastname,gender,email,password,title,deviceId);

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

//set user if successsfully registered
public void setUserConfig(){
    SharedPreferences sharedPrefs = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
    SharedPreferences.Editor editor = sharedPrefs.edit();
    editor = sharedPrefs.edit();
    editor.putString(PREF_PHONE_NUMBER, phonenumber);
    editor.putString(PREF_USER_PASS, password);
    editor.putString(PREF_LAST_NAME, lastname);
    editor.putString(PREF_FISRT_NAME, firstname);
    editor.putString(PREF_USER_EMAIL, email);
    editor.putString(PREF_USER_SET, "Yes");
    editor.commit();
}
    public void setApiUser(){
        SharedPreferences sharedPrefs = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor = sharedPrefs.edit();
        editor.putString(PREF_API_USER_SET, "Yes");
        editor.commit();
    }
    //create momo api user
    //create momo api user
    private void creatApiUser()
    {


        // StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_NAME,
        StringRequest stringRequest = new StringRequest(Request.Method.POST, this.URL_CREATE_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        setApiUser();
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
//check api user
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


}

