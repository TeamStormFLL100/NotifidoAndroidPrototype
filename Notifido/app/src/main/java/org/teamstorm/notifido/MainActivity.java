package org.teamstorm.notifido;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {


    private int mAlarmTime = 0;
    private int mFlashTime = 0;
    private Timer mTimer;
    public static final int LOOP_INTERVAL_MS = 100;
    private ImageButton mButton;
    private TextView mCounterView;
    private ImageView mLogoView;
    private boolean mScreenIsRed = false;
    private RelativeLayout mLayout;
    private TextView mMessageView;
    private int mMessageIndex = 0;

    private String[] mMessages = new String[] {
            "Time for medicine!",
            "Appointment time!",
            "Time to wake up!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayout = (RelativeLayout)findViewById(R.id.activity_main);
        mMessageView = (TextView)findViewById(R.id.message);
        mCounterView = (TextView)findViewById(R.id.counter);
        mLogoView = (ImageView)findViewById(R.id.logo);
        mButton = (ImageButton)findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlarmTime = 30;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        loop();
                    }
                });
            }
        }, 0, LOOP_INTERVAL_MS);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTimer.cancel();
        mTimer = null;
    }

    private void loop() {
        if (mAlarmTime > 0) {
            mAlarmTime--;
            if (mAlarmTime == 0) {
                // TODO: alarm done
                mFlashTime = 30;
            } else {
                mMessageView.setVisibility(View.GONE);
                mButton.setVisibility(View.GONE);
                mCounterView.setVisibility(View.VISIBLE);
                mCounterView.setText("" + (mAlarmTime / 10 + 1));

            }
        } else if (mFlashTime > 0) {
            mCounterView.setVisibility(View.GONE);
            mMessageView.setVisibility(View.VISIBLE);
            mButton.setVisibility(View.INVISIBLE);
            mMessageView.setText(mMessages[mMessageIndex]);
            mScreenIsRed = !mScreenIsRed;
            mFlashTime--;
            int backgroundRes = mScreenIsRed ? android.R.color.holo_red_light : android.R.color.white;
            mLayout.setBackgroundColor(ContextCompat.getColor(this, backgroundRes));
            if (mFlashTime == 0) {
                // flashing done
                mButton.setVisibility(View.VISIBLE);
                mLayout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));
                mMessageIndex++;
                mMessageIndex %= mMessages.length;
            }
        }
    }

}
