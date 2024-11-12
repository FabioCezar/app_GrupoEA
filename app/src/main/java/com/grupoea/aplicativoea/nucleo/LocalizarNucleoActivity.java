package com.grupoea.aplicativoea.nucleo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.grupoea.aplicativoea.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LocalizarNucleoActivity extends AppCompatActivity {
    EditText et_placa;
    Button bt_localizar;
    TextView tv_cpfReu, tv_numProcessoCiente, tv_placa, tv_ano, tv_cor, tv_chassi, tv_renavam,
            tv_marca, tv_modelo;

    TextView tv_naoLocalizada;
    String placa;
    ScrollView sv_detalhesPlaca;

    String url = "https://grupoea.net.br/app/getCarro.php";

    RequestQueue requestQueue;

    Intent i;
    String id;
    String nome;

    //confirmação de voltar
    boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizar_nucleo);

        et_placa = findViewById(R.id.et_placa);
        bt_localizar = findViewById(R.id.bt_localizar);
        sv_detalhesPlaca = findViewById(R.id.sv_detalhesPlaca);

        //campos do bd
        tv_cpfReu = findViewById(R.id.tv_cpfReu);
        tv_numProcessoCiente = findViewById(R.id.tv_numProcessoCliente);
        tv_placa = findViewById(R.id.tv_placa);
        tv_ano = findViewById(R.id.tv_ano);
        tv_cor = findViewById(R.id.tv_cor);
        tv_chassi = findViewById(R.id.tv_chassi);
        tv_renavam = findViewById(R.id.tv_renavam);
        tv_marca = findViewById(R.id.tv_marca);
        tv_modelo = findViewById(R.id.tv_modelo);

        tv_naoLocalizada = findViewById(R.id.tv_naoLocalizada);


        requestQueue = Volley.newRequestQueue(LocalizarNucleoActivity.this);

        i = getIntent();
        id = i.getStringExtra("id");
        nome = i.getStringExtra("nome");

        bt_localizar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sv_detalhesPlaca.setVisibility(View.INVISIBLE);
                tv_naoLocalizada.setVisibility(View.INVISIBLE);

                boolean validado = true;
                placa = et_placa.getText().toString().trim();

                if (placa.isEmpty()) {
                    et_placa.setError("Campo obrigatório");
                    et_placa.requestFocus();
                    validado = false;
                } else if (placa.length() < 7) {
                    et_placa.setError("Placa incompleta");
                    et_placa.requestFocus();
                    validado = false;
                }


                if (validado) {
                    consultarPlaca();
                }


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        doubleBackToExitPressedOnce = false;
    }

    private void consultarPlaca() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isErro = jsonObject.getBoolean("erro");
                            et_placa.setText("");
                            if (isErro) {
                                tv_naoLocalizada.setVisibility(View.VISIBLE);
                            } else {
                                String cpfReu = jsonObject.getString("cpfReu");
                                String numProcessoCliente = jsonObject.getString(
                                        "numProcessoCliente");
                                String placa = jsonObject.getString(
                                        "placa");
                                String ano = jsonObject.getString("ano");
                                String cor = jsonObject.getString("cor");
                                String chassi = jsonObject.getString("chassi");
                                String renavam = jsonObject.getString("renavam");
                                String marca = jsonObject.getString("marca");
                                String modelo = jsonObject.getString("modelo");


                                tv_cpfReu.setText(cpfReu);
                                tv_numProcessoCiente.setText(numProcessoCliente);
                                tv_placa.setText(placa);
                                tv_ano.setText(ano);
                                tv_cor.setText(cor);
                                tv_chassi.setText(chassi);
                                tv_renavam.setText(renavam);
                                tv_marca.setText(marca);
                                tv_modelo.setText(modelo);


                                sv_detalhesPlaca.setVisibility(View.VISIBLE);

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
                params.put("placa", placa);

                return params;
            }
        };
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Clique em VOLTAR novamente para sair", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 4000);
    }
}