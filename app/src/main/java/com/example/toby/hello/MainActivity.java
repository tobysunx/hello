package com.example.toby.hello;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.view.View.OnClickListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.*;
import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button but=(Button) super.findViewById(R.id.leftbutton);
        but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //增加自己的代码.....

                new Thread(networkTask).start();

            }
        });






    }
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");

            // TODO
            // UI界面的更新等相关操作
            final TextView text = (TextView) findViewById(R.id.text);
            text.setText(val);
        }
    };
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
             final TextView text = (TextView) findViewById(R.id.text);
            testget get=new testget(text,"http://snywx.jxutcm.edu.cn/get.php");
            try {
                get.get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Message msg = new Message();
            Bundle data = new Bundle();
            try {
                System.out.println(get.string);
                JSONObject json = new JSONObject(get.string);
                data.putString("value", json.get("state").toString());
                msg.setData(data);
                handler.sendMessage(msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            data.putString("value", "apple");
//            msg.setData(data);
//            handler.sendMessage(msg);

        }
    };
}
class testget{
    String string;
    TextView text;
    String urlstr;
    public testget(TextView text,String urlstr){
        this.text=text;
        this.urlstr=urlstr;
    }

public void get() throws IOException {
//    try {
        URL url=new URL(urlstr);
        URLConnection connection=url.openConnection();
        InputStream is=connection.getInputStream();
        InputStreamReader isr=new InputStreamReader(is);
        BufferedReader br=new BufferedReader(isr);
        String line;
        StringBuilder builder=new StringBuilder();
        while ((line=br.readLine())!=null){
            builder.append(line);

        }
        br.close();
        isr.close();
        is.close();

        this.string=builder.toString();


//    } catch (MalformedURLException e) {
//        e.printStackTrace();
//    } catch (IOException e) {
//        e.printStackTrace();
//    }


}

}