package com.grupoea.aplicativoea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.grupoea.aplicativoea.localizador.LocalizarActivity;
import com.grupoea.aplicativoea.nucleo.LocalizarNucleoActivity;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText et_login, et_senha;
    Button bt_login;
    ProgressBar pb_login;
    String login, senha;

    String url = "https://grupoea.net.br/app/getUsuario.php";

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_login = findViewById(R.id.et_login);
        et_senha = findViewById(R.id.et_senha);
        bt_login = findViewById(R.id.bt_login);
        pb_login = findViewById(R.id.pb_login);

        requestQueue = Volley.newRequestQueue(this);

        //handleSSLHandshake();

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validado = true;

                login = et_login.getText().toString().trim();
                senha = et_senha.getText().toString().trim();

                if (senha.isEmpty()) {
                    et_senha.setError("Campo obrigatório");
                    et_senha.requestFocus();
                    validado = false;
                }
                if (login.isEmpty()) {
                    et_login.setError("Campo obrigatório");
                    et_login.requestFocus();
                    validado = false;
                }

                if (validado) {
                    validarLogin();
                }

            }
        });
    }

    private void validarLogin() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isErro = jsonObject.getBoolean("erro");

                            if (isErro){
                                Toast.makeText(MainActivity.this, "Login ou senha inválidos", Toast.LENGTH_SHORT).show();
                            }else{
                                String perfil = jsonObject.getString("perfil");
                                String id = jsonObject.getString("id");
                                String nome = jsonObject.getString("nome");
                                pb_login.setVisibility(View.VISIBLE);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (perfil.equals("nucleo")){
                                            Intent i = new Intent(MainActivity.this, LocalizarNucleoActivity.class);
                                            i.putExtra("id", id);
                                            i.putExtra("nome", nome);
                                            startActivity(i);
                                            finish();
                                        } else{
                                            Intent i = new Intent(MainActivity.this, LocalizarActivity.class);
                                            i.putExtra("id", id);
                                            i.putExtra("nome", nome);
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                },3000);

                            }
                        } catch (Exception e) {
                            Log.v("LogLogin", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LogLogin", error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("login", login);
                params.put("senha", senha);

                return params;
            }
        };
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }


//    @SuppressLint("TrulyRandom")
//    public static void handleSSLHandshake() {
//        try {
//            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//                public X509Certificate[] getAcceptedIssuers() {
//                    return new X509Certificate[0];
//                }
//
//                @Override
//                public void checkClientTrusted(X509Certificate[] certs, String authType) {
//                }
//
//                @Override
//                public void checkServerTrusted(X509Certificate[] certs, String authType) {
//                }
//            }};
//
//            SSLContext sc = SSLContext.getInstance("SSL");
//            sc.init(null, trustAllCerts, new SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String arg0, SSLSession arg1) {
//                    return true;
//                }
//            });
//        } catch (Exception ignored) {
//        }
//    }








}