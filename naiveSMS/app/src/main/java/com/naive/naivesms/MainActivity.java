package com.naive.naivesms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText mEtText;
    private Button mBtSlect;
    private Button mBtSend;
    private List<ContactInfo> mContactInfo;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        mEtText = (EditText) findViewById(R.id.et_text);
        mBtSlect = (Button) findViewById(R.id.bt_select);
        mBtSend = (Button) findViewById(R.id.bt_send);
        mContactInfo = new ArrayList<>();
        mBtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = mEtText.getText().toString().trim();
                if (TextUtils.isEmpty(txt)) {
                    Toast.makeText(context, ".......", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (ContactInfo contactInfo : mContactInfo) {
                    if (txt.contains("#name")) {
                        String newStr = txt.replaceAll("#name", contactInfo.getName());
                        sendSms(context, contactInfo.getPhone(), newStr);
                    } else {
                        sendSms(context, contactInfo.getPhone(), txt);
                    }
                }
                Toast.makeText(context, "发送完成了!!", Toast.LENGTH_SHORT).show();

            }
        });
        mBtSlect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, SelectContactActivity.class), 1);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            mContactInfo = (List<ContactInfo>) data.getSerializableExtra("list");
            String s = "";
            for (ContactInfo contactInfo : mContactInfo) {
                s = s + contactInfo.getName() + "/" + contactInfo.getPhone() + ",";
            }
            mBtSlect.setText(s);
            mBtSlect.setTextSize(12);
        }
    }


    public static void sendSms(Context ctx, String address, String msg) {

        SmsManager smsManager = SmsManager.getDefault();

        ArrayList<String> message = smsManager.divideMessage(msg); // 对短信内容进行切割，防止内容过多

        // 具体的发送广播所需要的intent
        Intent intent = new Intent();
        // 定义一个唯一的字符串
        intent.setAction("com.naive.naiveSMS");
        // PendingIntent 是对intent 和这个intent 要干的事，进行封装
        PendingIntent sentIntent = PendingIntent.getBroadcast(ctx, 88, intent, PendingIntent.FLAG_ONE_SHOT);

        for (String text : message) {
            /*
             * 在4.0 之前 smsManager.sendTextMessage 只发送短信，不会将短信插入至短信数据库。
			 */
            smsManager.sendTextMessage(
                    address, // 收短信的人的号码
                    null, // 短信服务中心的号码
                    text, // 文本内容
                    sentIntent,  // 发送成功后的隐式意图
                    null); // 对方接收成功后的隐式意图

        }


    }
}
