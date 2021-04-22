package com.example.mypantry;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.mypantry.activity.MainActivity;

public class DeadlineService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private Notification notify ;

    public DeadlineService(String name, String Channel) {
        super(name);
        notify =  new Notification(Channel);
    }

    public DeadlineService() {
        super("DeadlineService");
        notify =  new Notification("CHANNEL_ID");
    }

    protected void onHandleIntent(@Nullable Intent intent) {
        int i = 1;
        while(i > 0){
            try {
                Thread.sleep(3000);
                notify.sendNotify(this, MainActivity.class,12+i,"Ciao "+i,"La tua applicazione é viva"+i);
                i--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        onDestroy();
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
