package com.example.test3;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class FloatButtonService extends Service{
    //менеджер к которому цепляем кнопку что бы все время быть на верху
    private WindowManager windowManager;
    private ImageView chatHead;
    private WindowManager.LayoutParams params;
    private RelativeLayout topView;
    private UnmaskRelativeLayout contentView;

    @Override
    public void onCreate() {
        super.onCreate();

        //инициализируем его
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        //создаем нашу кнопку что бы отобразить
        chatHead = new ImageView(this);
        chatHead.setImageResource(R.drawable.start3);

        //задаем параметры для картинки, что бы была
        //своего размера, что бы можно было перемещать по экрану
        //что бы была прозрачной, и устанавливается ее стартовое полодение
        //на экране при создании
        params= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        //кол перемещения тоста по экрану при помощи touch
        chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private boolean shouldClick;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        shouldClick = true;
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        if(shouldClick)
                            Toast.makeText(getApplicationContext(), "Клик по тосту случился!", Toast.LENGTH_LONG).show();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        shouldClick = false;
                        params.x = initialX
                                + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY
                                + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(chatHead, params);
                        return true;
                }
                return false;
            }
        });
        windowManager.addView(chatHead, params);


        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
    }

    //удалем тост если была команда выключить сервис
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null)
            windowManager.removeView(chatHead);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
