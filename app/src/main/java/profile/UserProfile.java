package profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import volley.VolleySingleton;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myblog.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity {

    private String ID,NAME, EMAIL, CREATED_DATE;
    Activity mContext = this;
    private String appURL;
    TextView mId, mName,mEmail,mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent data = getIntent();
        EMAIL = data.getStringExtra("email");
        appURL = "http://192.168.0.144/api/getUserDetail.php?email="+EMAIL;

        mId = findViewById(R.id.txt_Id);
        mName = findViewById(R.id.txt_Name);
        mEmail = findViewById(R.id.txt_Email);
        mDate = findViewById(R.id.txt_CreatedDate);

        getUserDetail();
    }

    private void getUserDetail(){

        if(EMAIL.isEmpty()){
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setMessage("Email cannot be empty");
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }else {

            StringRequest stringRequest = new StringRequest(Request.Method.GET, appURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        ID = jsonObject.getString("id");
                        NAME = jsonObject.getString("name");
                        EMAIL = jsonObject.getString("email");
                        CREATED_DATE = jsonObject.getString("created_date");

                        mId.setText(ID);
                        mName.setText(NAME);
                        mEmail.setText(EMAIL);
                        mDate.setText(CREATED_DATE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse response = error.networkResponse;
                    AlertDialog.Builder alert;

                    if (response != null && response.data != null) {
                        switch (response.statusCode) {

                            case 400:
                            case 404:
                            case 403:
                                alert = new AlertDialog.Builder(mContext);
                                alert.setTitle("Error");
                                alert.setMessage(response.data.toString());
                                alert.setCancelable(false);
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alert.show();
                                break;
                        }
                    } else {
                        alert = new AlertDialog.Builder(mContext);
                        alert.setTitle("Error");
                        alert.setMessage(error.toString());
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alert.show();
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("Accept", "Application/json; charset-UTF-8");
                    return params;
                }

            };
            VolleySingleton.getInstance().addRequestQueue(stringRequest);
        }
    }
}