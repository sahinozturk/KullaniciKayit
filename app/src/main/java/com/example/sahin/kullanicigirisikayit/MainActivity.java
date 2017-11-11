package com.example.sahin.kullanicigirisikayit;

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

import java.net.URI;

public class MainActivity extends AppCompatActivity {
EditText ad,soyad,tel,mail,pass;
static SharedPreferences sha;
static SharedPreferences.Editor edi;
     static  String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ad=(EditText)findViewById(R.id.txtGirisAdiniz);
        soyad=(EditText)findViewById(R.id.txtSoyadi);
        tel=(EditText)findViewById(R.id.txtTelefon);
        mail=(EditText)findViewById(R.id.txtEmail);
        pass=(EditText)findViewById(R.id.txtPassword);
        sha=getSharedPreferences("kullanici",MODE_PRIVATE);
        edi=sha.edit();
        String kisid=sha.getString("kid","");
        if(!kisid.equals(""))
        {
            Intent in=new Intent(MainActivity.this,Profil.class);
            startActivity(in);
            finish();

        }

    }

    public void opKaydet(View view)
    {
        String adi=this.ad.getText().toString();
        String soyadi=this.soyad.getText().toString();
        String teli=this.tel.getText().toString();
        String email=this.tel.getText().toString();
        String passw=this.pass.getText().toString();


        //Json blut hesabına kullanıcı kayıt için girilmesi gereken bilgileri hazırlıyoruz
        //Bu url girildiği zaman bilgilerde eksiklik veya yanlışlık yok ise
        //kayıt işlemi gerçekleşilecektir
        String url="http://jsonbulut.com/json/userRegister.php?ref=cb226ff2a31fdd460087fedbb34a6023&"+
                "userName="+adi+"&" +
                "userSurname="+soyadi+"&" +
                "userPhone="+teli+"&" +
                "userMail="+email+"&" +
                "userPass="+passw+"";


        this.ad.setText("");
        this.soyad.setText("");
        this.tel.setText("");
        this.mail.setText("");
        this.pass.setText("");



        new jsonVerl(url,MainActivity.this).execute();



    }

    public void opGirisEkrani(View view)
    {
        Intent i=new Intent(MainActivity.this,LoginEkrani.class);
        startActivity(i);
    }

    static class jsonVerl extends AsyncTask<Void,Void,Void>
    {
        String url = "";
        String data = "";
        Context cnx;

        ProgressDialog pro;
        public jsonVerl (String url, Context cnx)
        {
            this.url = url;
            this.cnx = cnx;
            pro = new ProgressDialog(cnx);
            pro.setMessage("İşlem yaplıyor. Lütfen Bekleyiniz.");
            pro.show();
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }




        @Override
        protected Void doInBackground(Void... voids)
        {

            try
            {
                data = Jsoup.connect(url).ignoreContentType(true).get().body().text();

            }
            catch (Exception e)
            {
                Log.e("Data Json Hatası", "doinBackground: ", e);
            }

            return null;
        }

        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            try
            {
                JSONObject obj=new JSONObject(data);
                Boolean durum=obj.getJSONArray("user").getJSONObject(0).getBoolean("durum");

                if(durum)
                {

                    String kid =  obj.getJSONArray("user").getJSONObject(0).getString("kullaniciId");
                    edi.putString("kid",kid);
                    edi.commit();
                    Intent i=new Intent(cnx,Profil.class);
                    cnx.startActivity(i);


                    Log.e("kid", kid);
                    Log.e("durum kıs","kullanıcı");

                }
            }
            catch (Exception e)
            {

            }

            pro.dismiss();
        }



    }


}
