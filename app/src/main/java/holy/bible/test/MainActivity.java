package holy.bible.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Test test = new Test();

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyHandler handler = new MyHandler();

        Thread a = new Thread() {
            @Override
            public void run() {
                synchronized (test) {
                    Log.d("test1234", "1");
                    try {
                        test.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("test1234", "4");

                }
                test.print();
            }
        };
        a.start();



        Thread b = new Thread() {
            @Override
            public void run() {
                synchronized (test) {
                    Log.d("test1234", "2");
                    try {
                        test.wait(2000);
                        test = new Test();
                        test.notifyAll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("test1234", "3");


                }
                MainActivity.this.test.print();
            }
        };
        b.start();

    }


}
