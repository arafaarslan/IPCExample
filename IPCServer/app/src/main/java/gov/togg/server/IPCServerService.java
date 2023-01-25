package gov.togg.server;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import gov.togg.myaidl.IIPCExample;


public class IPCServerService extends Service {

    int connectionCount = 0;

    private Messenger mMessenger = new Messenger(new IncomingHandler());
    private final String PID = "pid";
    private final String CONNECTION_COUNT = "connection_count";
    private final String PACKAGE_NAME = "package_name";
    private final String DATA = "data";

    public IPCServerService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        connectionCount++;
        if(intent.getAction().equals("aidlexample")){
            return binder;
        }else if(intent.getAction().equals("messengerexample")){
            return mMessenger.getBinder();
        }
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        RecentClient.getRecentClient().setClient(null);
        return super.onUnbind(intent);
    }

    private final IIPCExample.Stub binder = new IIPCExample.Stub() {

        @Override
        public int getPid() throws RemoteException {
            return 1;
        }

        @Override
        public int getConnectionCount() throws RemoteException {
            return connectionCount;
        }

        @Override
        public void setDisplayedValue(String packageName, int pid, String data) throws RemoteException {
            if (data == null || TextUtils.isEmpty(data)) {

            } else {
                Client c = new Client();
                c.setClientPackageName(packageName);
                c.setClientProcessId(getPid() + "");
                c.setClientData(data);
                c.setIpcMethod("AIDL");
                RecentClient.getRecentClient().setClient(c);
            }
        }
    };

    // Messenger IPC - Message Handler
    class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // Get message from client. Save recent connected client info.
            Bundle receivedBundle = msg.getData();
            Client client = new Client(
                    receivedBundle.getString(PACKAGE_NAME),
                    new String(receivedBundle.getInt(PID)+""),
                    receivedBundle.getString(DATA),
                    "Messenger"
            );

            // Send message to the client. The message contains server info
            Message message = Message.obtain(this ,0);
            Bundle bundle =new Bundle();
            bundle.putInt(CONNECTION_COUNT, connectionCount);
            bundle.putInt(PID, 55);
            message.setData(bundle);
            // The service can save the msg.replyTo object as a local variable
            // so that it can send a message to the client at any time
            try {
                msg.replyTo.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}


