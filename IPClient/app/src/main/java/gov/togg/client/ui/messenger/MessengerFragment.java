package gov.togg.client.ui.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import gov.togg.client.R;
import gov.togg.client.databinding.FragmentMessengerBinding;
import gov.togg.myaidl.IIPCExample;

public class MessengerFragment extends Fragment implements View.OnClickListener {

    private FragmentMessengerBinding binding;

    private final String PID = "pid";
    private final String CONNECTION_COUNT = "connection_count";
    private final String PACKAGE_NAME = "package_name";
    private final String DATA = "data";

    // Is bound to the service of remote process
    private Boolean isBound = false;

    // Messenger on the server
    private Messenger serverMessenger = null;

    // Messenger on the client
    private Messenger clientMessenger = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMessengerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnConnect.setOnClickListener(this);
        return root;
    }

    @Override
    public void onDestroy() {
        doUnbindService();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Handle messages from the remote service
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            // Update UI with remote process info
            Bundle bundle = msg.getData();
            binding.linearLayoutClientInfo.setVisibility(View.VISIBLE);
            binding.btnConnect.setText(getString(R.string.disconnect));
            binding.txtServerPid.setText(bundle.getInt(PID) + "");
            binding.txtServerConnectionCount.setText(bundle.getInt(CONNECTION_COUNT) + "");
        }
    };

    @Override
    public void onClick(View v) {
        if (isBound) {
            doUnbindService();
        } else {
            doBindService();
        }
    }

    /*ComponentName verisini tutarak onServiceDisconnected method cagirilmaktadir.
    * AKsi halde unbind ile onServiceDisconnected tetiklenmemektedir.*/
    private ComponentName cn = null;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            cn = componentName;
            serverMessenger = new Messenger(iBinder);
            // Ready to send messages to remote service
            sendMessageToServer();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            cn = null;
            clearUI();
            serverMessenger = null;
        }
    };


    private void doBindService() {
        clientMessenger = new Messenger(handler);
        Intent intent = new Intent("messengerexample");
        intent.setPackage("gov.togg.server");
        getActivity().getApplicationContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    private void doUnbindService() {
        try {
            if (isBound) {
                getActivity().getApplicationContext().unbindService(mConnection);
                mConnection.onServiceDisconnected(cn);
                isBound = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void clearUI() {
        binding.txtServerPid.setText("");
        binding.txtServerConnectionCount.setText("");
        binding.btnConnect.setText(getString(R.string.connect));
        binding.linearLayoutClientInfo.setVisibility(View.INVISIBLE);
    }

    private void sendMessageToServer() {
        if (!isBound) return;

        Message message = Message.obtain(handler);
        Bundle bundle = new Bundle();
        bundle.putString(DATA, binding.edtClientData.getText().toString());
        bundle.putString(PACKAGE_NAME, getActivity().getApplicationContext().getPackageName());
        bundle.putInt(PID, 35);
        message.setData(bundle);
        message.replyTo = clientMessenger; // we offer our Messenger object for communication to be two-way
        try {
            serverMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            message.recycle();
        }
    }
}