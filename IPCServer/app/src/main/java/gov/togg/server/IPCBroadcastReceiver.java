package gov.togg.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class IPCBroadcastReceiver extends BroadcastReceiver{

    private final String PACKAGE_NAME = "package_name";
    private final String DATA = "data";
    private final String PID = "pid";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null){
            String pn = intent.getStringExtra(PACKAGE_NAME);
            String pid = intent.getStringExtra(PID);
            String data = intent.getStringExtra(DATA);
            Client c= new Client(pn,pid,data,"Broadcast");
            RecentClient.getRecentClient().setClient(c);
        }
    }
}