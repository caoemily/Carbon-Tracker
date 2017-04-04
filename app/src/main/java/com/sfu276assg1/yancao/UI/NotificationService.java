package com.sfu276assg1.yancao.UI;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.sfu276assg1.yancao.carbontracker.BillCollection;
import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.JourneyCollection;
import com.sfu276assg1.yancao.carbontracker.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Service to support notification while app is running or at background. Notification will pop up at 9:00.
 */

public class NotificationService extends Service {

    private final String SET_TIME = "09:00:00 PM";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        onTaskRemoved(intent);
        setUpNotification();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        Intent intent = new Intent(getApplicationContext(),this.getClass());
        intent.setPackage(getPackageName());
        startService(intent);
        super.onTaskRemoved(rootIntent);
    }

    private void setUpNotification() {
        String time = new SimpleDateFormat("hh:mm:ss aa").format(new Date());
        if(!time.equals(SET_TIME)){
            return;
        }
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        JourneyCollection journeyCollection = CarbonModel.getInstance().getJourneyCollection();
        BillCollection billCollection = CarbonModel.getInstance().getBillCollection();
        int journeyCount = journeyCollection.countJourneyInOneDate(today);
        boolean isBill = billCollection.isBillInPrevious45days();
        String[] notification = new String [2];
        String notificationContentTitle = getResources().getString(R.string.NOTIFICATION_CONTENT_TITLE);
        Intent intent;

        if(journeyCount==0){
            notification[0] = getResources().getString(R.string.NOTIFICATION_NOBILL_NOJOURNEY_PART1);
            notification[1] = getResources().getString(R.string.NOTIFICATION_NOBILL_NOJOURNEY_PART2);
            intent = new Intent(getApplicationContext(),SelectTransModeActivity.class);

        }
        else if(journeyCount>0 && (!isBill)){
            notification[0] = getResources().getString(R.string.NOTIFICATION_NOBILL_PART1);
            notification[1] = getResources().getString(R.string.NOTIFICATION_NOBILL_PART2);
            intent = new Intent(getApplicationContext(),AddBillActivity.class);
        }
        else {
            notification[0] = getResources().getString(R.string.NOTIFICATION_OTHER);
            notification[1] = " ";
            intent = new Intent(getApplicationContext(),SelectTransModeActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.car)
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle(notificationContentTitle)
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.InboxStyle()
                            .addLine(notification[0])
                            .addLine(notification[1]));

        int notificationId = 0;
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);


        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(notificationId, mBuilder.build());
    }
}
