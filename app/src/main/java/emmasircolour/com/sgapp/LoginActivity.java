package emmasircolour.com.sgapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class LoginActivity extends AppCompatActivity {
 String PREF_DEVICE_ID="Device_id";
    String PREF_USER_SET="No";
    String PREF_PHONE_NUMBER="Phone";
    String PREF_USER_PASS="Password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView btn=findViewById(R.id.textViewSignUp);
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String installId = SP.getString(PREF_DEVICE_ID , "None");
        String userset = SP.getString(PREF_USER_SET , "No");
        String userpassword = SP.getString(PREF_USER_PASS , "No");
        String userphone = SP.getString(PREF_PHONE_NUMBER , "No");
       // generate unique installation id
        if (installId.matches("None")){
            setUserConfig();
        }
        //check if user set, if set go to main activity for testing only
        if(userset.matches("Yes")){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        //login
        Button buttonLogin =  findViewById(R.id.btnlogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //phonenumber,firstname ,lastname ,gender ,email,password,title ,deviceId
                EditText EditTextphonenumber,EditTextfirstname ,EditTextlastname  ,EditTextemail,EditTextpassword ;
                EditTextphonenumber=  findViewById(R.id.inputPhoneNumber);
                EditTextpassword= findViewById(R.id.inputPassword);

                String phonenumber=EditTextphonenumber.getText().toString();
                String password=EditTextpassword.getText().toString();

                if (phonenumber.matches(userphone) & password.matches(userpassword)){

                    if(userset.matches("Yes")){
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }

                }







                //Toast.makeText(getApplicationContext(), "Button working "+AppSettings.url_register, Toast.LENGTH_SHORT).show();



            }
        });


    }
//generate install id
    public void setUserConfig(){
        SharedPreferences sharedPrefs = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor = sharedPrefs.edit();
        String uniqueID = UUID.randomUUID().toString();
        System.out.println("Installation string: "+uniqueID);
        editor.putString(PREF_DEVICE_ID, uniqueID);

        editor.commit();
    }


}
