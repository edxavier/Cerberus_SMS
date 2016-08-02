package com.edxavier.cerberus_sms.fragments.callLog.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.edxavier.cerberus_sms.R;
import com.edxavier.cerberus_sms.db.entities.AreaCode;
import com.edxavier.cerberus_sms.db.entities.CallLog;
import com.edxavier.cerberus_sms.helpers.TextViewHelper;
import com.edxavier.cerberus_sms.helpers.Utils;

import java.util.Date;

/**
 * Created by Eder Xavier Rojas on 19/07/2016.
 */
public class CallHandler extends PhonecallReceiver {
    private static WindowManager manager;
    private static View view;

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
        super.onIncomingCallStarted(ctx, number, start);
        if(checkDrawPermission(ctx)) {
            WindowManager.LayoutParams layoutParams = setupWM(ctx);
            view = View.inflate(ctx.getApplicationContext(), R.layout.floating_window, null);
            setTouchListener(layoutParams);
            ImageView close = (ImageView) view.findViewById(R.id.close_floating);
            TextViewHelper msgText = (TextViewHelper) view.findViewById(R.id.floating_msg);
            CardView cardView = (CardView) view.findViewById(R.id.flaoting_card);
            close.bringToFront();
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.removeView(view);
                }
            });
            if(number!=null){
                AreaCode areaCode = Utils.getOperadoraV3(number, ctx);
                if(areaCode!=null) {
                    String msg = ctx.getResources().getString(R.string.incoming_call_msg);
                    if (areaCode.getArea_operator().length() > 0) {
                        msgText.setRobotoMedium();
                        msgText.setTextSize(12);
                        msgText.setText(msg + " " + areaCode.getArea_operator());
                        if (areaCode.getArea_operator().equals("Claro")) {
                            cardView.setCardBackgroundColor(ctx.getResources().getColor(R.color.md_red_600));
                        } else if (areaCode.getArea_operator().equals("Movistar")) {
                            cardView.setCardBackgroundColor(ctx.getResources().getColor(R.color.md_green_600));
                        }
                        manager.addView(view, layoutParams);
                    } else if (areaCode.getArea_name().length()>0){
                        msgText.setText(msg + " " + areaCode.getArea_name());
                        cardView.setCardBackgroundColor(ctx.getResources().getColor(R.color.md_blue_600));
                        manager.addView(view, layoutParams);
                    }
                }
            }
        }

    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        super.onOutgoingCallStarted(ctx, number, start);
        if(checkDrawPermission(ctx)) {
            WindowManager.LayoutParams layoutParams = setupWM(ctx);
            view = View.inflate(ctx.getApplicationContext(), R.layout.floating_window, null);
            setTouchListener(layoutParams);
            ImageView close = (ImageView) view.findViewById(R.id.close_floating);
            TextViewHelper msgText = (TextViewHelper) view.findViewById(R.id.floating_msg);
            CardView cardView = (CardView) view.findViewById(R.id.flaoting_card);
            close.bringToFront();
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.removeView(view);
                }
            });

            if (number != null) {
                AreaCode areaCode = Utils.getOperadoraV3(number, ctx);
                if (areaCode != null) {
                    String msg = ctx.getResources().getString(R.string.outgoing_call_msg);
                    if( areaCode.getArea_operator().length() > 0) {
                        msgText.setRobotoMedium();
                        msgText.setTextSize(12);
                        msgText.setText(msg + " " + areaCode.getArea_operator());
                        if (areaCode.getArea_operator().equals("Claro")) {
                            cardView.setCardBackgroundColor(ctx.getResources().getColor(R.color.md_red_600));
                        } else if (areaCode.getArea_operator().equals("Movistar")) {
                            cardView.setCardBackgroundColor(ctx.getResources().getColor(R.color.md_green_600));
                        }
                        manager.addView(view, layoutParams);
                    } else if (areaCode.getArea_name().length()>0){
                        msgText.setText(msg + " " + areaCode.getArea_name());
                        cardView.setCardBackgroundColor(ctx.getResources().getColor(R.color.md_blue_600));
                        manager.addView(view, layoutParams);
                    }
                }
            }
        }
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        super.onIncomingCallEnded(ctx, number, start, end);
        try {
            manager.removeView(view);
        }catch (Exception ignored){}
        if(number!=null) {
            number = Utils.formatPhoneNumber(number);
            String name = Utils.getContactName(number);
            int duration = (int) ((end.getTime() - start.getTime()) / 1000 % 60);
            CallLog callLog = new CallLog(name, number, android.provider.CallLog.Calls.INCOMING_TYPE, start, duration);
            callLog.save();
        }
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        super.onOutgoingCallEnded(ctx, number, start, end);
        try {
            manager.removeView(view);
        }catch (Exception ignored){}
        if(number!=null) {
            number = Utils.formatPhoneNumber(number);
            String name = Utils.getContactName(number);
            int duration = (int) ((end.getTime() - start.getTime()) / 1000 % 60);
            CallLog callLog = new CallLog(name, number, android.provider.CallLog.Calls.OUTGOING_TYPE, start, duration);
            callLog.save();
        }
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        super.onMissedCall(ctx, number, start);
        try {
            manager.removeView(view);
        }catch (Exception ignored){}
        if(number!=null) {
            Date end = new Date();
            number = Utils.formatPhoneNumber(number);
            String name = Utils.getContactName(number);
            int duration = (int) ((end.getTime() - start.getTime()) / 1000 % 60);
            CallLog callLog = new CallLog(name, number, android.provider.CallLog.Calls.MISSED_TYPE, start, duration);
            callLog.save();
        }
    }

    boolean checkDrawPermission(Context context) {
        boolean hasPerm = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             hasPerm = Settings.canDrawOverlays(context);
        }
        return hasPerm;
    }

    WindowManager.LayoutParams setupWM(Context ctx){
        manager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(500, 200,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.alpha = 0.9f;
        layoutParams.packageName = ctx.getPackageName();
        layoutParams.buttonBrightness = 1f;
        layoutParams.windowAnimations = android.R.style.Animation_Dialog;

        return layoutParams;
    }

    void setTouchListener(final WindowManager.LayoutParams layoutParams){
        view.setOnTouchListener(new View.OnTouchListener() {
            WindowManager.LayoutParams updatedParameters = layoutParams;
            double x;
            double y;
            double pressedX;
            double pressedY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        x = updatedParameters.x;
                        y = updatedParameters.y;

                        pressedX = event.getRawX();
                        pressedY = event.getRawY();

                        break;

                    case MotionEvent.ACTION_MOVE:
                        updatedParameters.x = (int) (x + (event.getRawX() - pressedX));
                        updatedParameters.y = (int) (y + (event.getRawY() - pressedY));

                        manager.updateViewLayout(view, updatedParameters);

                    default:
                        break;
                }

                return false;
            }
        });
    }
}
