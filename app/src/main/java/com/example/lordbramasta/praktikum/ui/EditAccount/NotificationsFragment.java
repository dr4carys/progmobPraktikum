package com.example.lordbramasta.praktikum.ui.EditAccount;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.lordbramasta.praktikum.Login;
import com.example.lordbramasta.praktikum.R;
import com.example.lordbramasta.praktikum.Server;
import com.example.lordbramasta.praktikum.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class NotificationsFragment extends Fragment implements View.OnClickListener{

    private NotificationsViewModel notificationsViewModel;
    ProgressDialog pDialog;
    ConnectivityManager conMgr;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String url = Server.URL + "editProfile.php";
    int success;
    private static final String TAG = NotificationsFragment.class.getSimpleName();
    //    Button btn_logout;
    TextView txt_id, txt_username,txt_password;
//    Button btn_edit;
    String id,username;
    String tag_json_obj = "json_obj_req";
    SharedPreferences sharedpreferences;
    Boolean session = false;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        conMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getActivity(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        sharedpreferences = requireContext().getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        username = sharedpreferences.getString(TAG_USERNAME, null);
        txt_password = root.findViewById(R.id.txt_password);
        txt_username = root.findViewById(R.id.txt_username);
        gantiShared(id,username);
        Button btn_edit= root.findViewById(R.id.btn_edit);
        btn_edit= root.findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(this);
        Button btn_logout= root.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);
        btn_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(Login.session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_USERNAME, null);
                editor.commit();
                Intent intent = new Intent(getActivity(), Login.class);
                getActivity().finish();
                startActivity(intent);
            }
        });

//
//        final TextView textView = root.findViewById(R.id.text_notifications);
//        notificationsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
    public void gantiShared(String id, String username){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(session_status, true);
        editor.putString(TAG_ID, id);
        editor.putString(TAG_USERNAME, username);
        editor.commit();
    }
    @Override
    public void onClick(View v) {
        String username = txt_username.getText().toString();
        String password = txt_password.getText().toString();
        Integer id1= Integer.parseInt(id);
        // mengecek kolom yang kosong
        if (username.trim().length() > 0 && password.trim().length() > 0) {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
                checkLogin(id1,username, password);
            } else {
                Toast.makeText(getActivity() ,"No Internet Connection", Toast.LENGTH_LONG).show();
            }
        } else {
            // Prompt user to enter credentials
            Toast.makeText(getActivity() ,"Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
        }
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        editor.putBoolean(Login.session_status, false);
//        editor.putString(TAG_ID, null);
//        editor.putString(TAG_USERNAME, null);
//        editor.commit();
//        Intent intent = new Intent(getActivity(), Login.class);
//        getActivity().finish();
//        startActivity(intent);
    }
    private void checkLogin(final Integer id1,final String username, final String password) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("proses update");
        showDialog();
//        JSONObject objec = new JSONObject();
//        obj.put("id1",id1)
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.e("berhasil di update", jObj.toString());

                        Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getActivity(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "update error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("id", String.valueOf(id1));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}