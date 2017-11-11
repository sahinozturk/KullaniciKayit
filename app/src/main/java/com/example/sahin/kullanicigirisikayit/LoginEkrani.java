package com.example.sahin.kullanicigirisikayit;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

public class LoginEkrani extends AppCompatActivity {

    EditText tmail,tsifre;
SharedPreferences sha;
SharedPreferences.Editor edit;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ekrani);

        tmail=(EditText)findViewById(R.id.txtGirismail);
        tsifre=(EditText)findViewById(R.id.txtGirisSifre);

        sha=getSharedPreferences("kullanici",MODE_PRIVATE);
        edit=sha.edit();

        String kisid=sha.getString("kid","");


        if(!kisid.equals(""))
        {
            Intent i=new Intent(LoginEkrani.this,Profil.class);
            startActivity(i);
            finish();

        }

    }

    public void opGiris(View view)
    {


        String email=tmail.getText().toString();
        String sif=tsifre.getText().toString();

        //bilgileri çekeceğimiz json adresine giriş için gerekli parametreleri url içerisine gömeriz
        String url="http://jsonbulut.com/json/userLogin.php?ref=cb226ff2a31fdd460087fedbb34a6023&" +
                "userEmail="+email+
                "&userPass="+sif+
                "&face=no";



new Jsonveri(url,LoginEkrani.this).execute();


    }

    class Jsonveri extends AsyncTask<Void,Void,Void>
    {
        String data="";
        String url;
        Context cnxt;

        ProgressDialog pro;
        public Jsonveri(String url, Context cnxt)
        {
            this.url=url;
            this.cnxt=cnxt;

            pro=new ProgressDialog(cnxt);
            pro.setMessage("işlem yapılıyor ");
            pro.show();

        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                data= Jsoup.connect(url).ignoreContentType(true).get().body().text();

            }
            catch (Exception ex)
            {

            }

            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            try
            {
                JSONObject obj=new JSONObject(data);

                Boolean durum= obj.getJSONArray("user").getJSONObject(0).getBoolean("durum");

                //Jsonun içerisinde user->içerisinde jsonobjenin birinci elemanı->onun içerisindeki jsonobjenin bilgiler bölümünde
                //-> string olarak yer alan username yi çekiyoruz
                // Sonraki adımlar içinde aynı işlemi yapıyoruz
                String adi= obj.getJSONArray("user").getJSONObject(0).getJSONObject("bilgiler").getString("userName");
                String soyadi= obj.getJSONArray("user").getJSONObject(0).getJSONObject("bilgiler").getString("userSurname");
                String telefon=obj.getJSONArray("user").getJSONObject(0).getJSONObject("bilgiler").getString("userPhone");
                String userId=obj.getJSONArray("user").getJSONObject(0).getJSONObject("bilgiler").getString("userId");
                if(durum)
                {      Log.e("op giriş","1.kısım");
                edit.putString("kid",userId);
                edit.commit();
                    Log.e("kisid","degeri  :  "+userId);
                    Intent i =new Intent(LoginEkrani.this,Profil.class);
                    startActivity(i);
                    finish();
                    Log.e("x","Buraya girdimi");

                }
                else
                {
                    Toast.makeText(cnxt,"Bilgiler yanlış",Toast.LENGTH_SHORT).show();
                }

            }
            catch (Exception e)
            {

            }
pro.dismiss();
        }

    }


}
