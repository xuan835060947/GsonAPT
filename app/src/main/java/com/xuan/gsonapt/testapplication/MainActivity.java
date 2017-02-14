package com.xuan.gsonapt.testapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xuan.gsonapt.GsonAPT;
import com.xuan.gsonapt.testapplication.bean.OtherBean;
import com.xuan.gsonapt.testapplication.bean.SuperBean;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = MainActivity.class.getSimpleName();
    private Button mBtnClick;
    private EditText mEtNumber;
    private TextView mTvSpendTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnClick = (Button) findViewById(R.id.btn_click);
        mEtNumber = (EditText) findViewById(R.id.et_number);
        mTvSpendTime = (TextView) findViewById(R.id.tv_spendTime);
        mBtnClick.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_click:
                try {
                    testSuperBeanJsonParse();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void testSuperBeanJsonParse() throws IOException {
        SuperBean superBean = new SuperBean();
        HashMap<String, OtherBean> map = new HashMap<>();
        map.put("你好", new OtherBean("map的bean"));
        superBean.setHashMap(map);
        String jsonStr = GsonAPT.toJson(superBean);
        Log.e(TAG, jsonStr);

        String numStr = mEtNumber.getText().toString();
        mTvSpendTime.setText("num : " + numStr);
        int num;
        if (numStr == null || numStr.length() == 0) {
            num = 1;
        } else {
            num = Integer.parseInt(numStr);
        }
        toJsonTest(num, superBean);
        fromJsonTest(num, jsonStr, SuperBean.class);

        toJsonTest(num, new OtherBean("hello"));
        String otherBeanJsonStr = new Gson().toJson(new OtherBean("hello"));
        fromJsonTest(num, otherBeanJsonStr, OtherBean.class);
    }

    private void toJsonTest(int num, Object object) {
        Gson gson = new Gson();
        GsonAPT.setGson(gson);
        long gsonStart = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            gson.toJson(object);
        }
        long gsonEnd = System.currentTimeMillis();
        mTvSpendTime.append("\n"+object.getClass()+" Gson toJson spend time : " + (gsonEnd - gsonStart));

        long gsonAPTStart = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            GsonAPT.toJson(object);
        }
        long gsonAPTEnd = System.currentTimeMillis();
        mTvSpendTime.append("\n"+object.getClass()+" GsonAPT toJson spend time : " + (gsonAPTEnd - gsonAPTStart));
    }

    private void fromJsonTest(int num, String jsonStr, Type type) {
        Gson gson = new Gson();
        GsonAPT.setGson(gson);
        long gsonFromJsonStart = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            gson.fromJson(jsonStr, type);
        }
        long gsonFromJsonEnd = System.currentTimeMillis();
        mTvSpendTime.append("\n"+type+" Gson fromJson spend time : " + (gsonFromJsonEnd - gsonFromJsonStart));

        long gsonAPTFromJsonStart = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            GsonAPT.fromJson(jsonStr, type);
        }
        long gsonAPTFromJsonEnd = System.currentTimeMillis();
        mTvSpendTime.append("\n"+type+" GsonAPT fromJson spend time : " + (gsonAPTFromJsonEnd - gsonAPTFromJsonStart));
    }
}
