package com.xuan.gsonapt.testapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xuan.gsonapt.GsonAPT;
import com.xuan.gsonapt.testapplication.bean.testtime.BigBean;
import com.xuan.gsonapt.testapplication.bean.testtime.SmallBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = MainActivity.class.getSimpleName();
    private Button mBtnClick;
    private EditText mEtSmallBeanNumber;
    private EditText mEtBigBeanNumber;
    private TextView mTvSpendTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnClick = (Button) findViewById(R.id.btn_click);
        mEtSmallBeanNumber = (EditText) findViewById(R.id.et_small_bean);
        mEtBigBeanNumber = (EditText) findViewById(R.id.et_big_bean);
        mTvSpendTime = (TextView) findViewById(R.id.tv_spendTime);
        mBtnClick.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_click:
                testTime();
                break;
        }
    }

    public void testTime() {
        SmallBean smallBean = new SmallBean();
        BigBean bigBean = new BigBean();
        List<SmallBean> smallBeanList = new ArrayList<>(1000);
        for (int i = 0; i < 10000; i++) {
            smallBeanList.add(new SmallBean());
        }
        String smallStr = mEtSmallBeanNumber.getText().toString();
        String bigStr = mEtBigBeanNumber.getText().toString();
        mTvSpendTime.setText("num : " + smallStr);
        int smallNum;
        int bigNum;
        if (smallStr == null || smallStr.length() == 0) {
            smallNum = 0;
        } else {
            smallNum = Integer.parseInt(smallStr);
        }
        if (bigStr == null || bigStr.length() == 0) {
            bigNum = 0;
        } else {
            bigNum = Integer.parseInt(smallStr);
        }
        bigBean.setLittleBeanList(smallBeanList);
        String smallBeanStr = GsonAPT.toJson(smallBean);
        String bigBeanStr = GsonAPT.toJson(bigBean);
        Log.e(TAG, "\nsmall string leng : " + smallBeanStr.length());
        Log.e(TAG, "\nbig string leng : " + bigBeanStr.length());

        testToFromJson(smallNum, smallBean, smallBeanStr);

        testBigBean(bigNum, bigBean, bigBeanStr);

        Log.e(TAG, "\ntestTime end !!");
    }

    private void testToFromJson(int num, SmallBean smallBean, String smallStr) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        GsonAPT.setGson(new GsonBuilder().enableComplexMapKeySerialization().create());

        long start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            gson.toJson(smallBean);
        }
        Log.e(TAG, "\nGson toJson small bean " + num + " times : spend time: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            GsonAPT.toJson(smallBean);
        }
        Log.e(TAG, "\nGsonAPT toJson smallBean bean " + num + " times : spend time: " + (System.currentTimeMillis() - start));


        start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            gson.fromJson(smallStr, SmallBean.class);
        }
        Log.e(TAG, "\nGson fromJson small bean " + num + " times : spend time: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            GsonAPT.fromJson(smallStr, SmallBean.class);
        }
        Log.e(TAG, "\nGsonAPT fromJson smallBean bean " + num + " times : spend time: " + (System.currentTimeMillis() - start));
    }

    public void testBigBean(int num, BigBean bigBean, String bigStr) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        GsonAPT.setGson(new GsonBuilder().enableComplexMapKeySerialization().create());

        long start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            gson.toJson(bigBean);
        }
        Log.e(TAG, "\nGson toJson bigBean bean " + num + " times : spend time: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            GsonAPT.toJson(bigBean);
        }
        Log.e(TAG, "\nGsonAPT toJson bigBean bean " + num + " times : spend time: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            gson.fromJson(bigStr, BigBean.class);
        }
        Log.e(TAG, "\nGson fromJson bigBean bean " + num + " times : spend time: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            GsonAPT.fromJson(bigStr, BigBean.class);
        }
        Log.e(TAG, "\nGsonAPT fromJson bigBean bean " + num + " times : spend time: " + (System.currentTimeMillis() - start));

    }
}
