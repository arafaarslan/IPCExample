package gov.togg.client.ui.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import gov.togg.client.MainActivity;
import gov.togg.client.R;
import gov.togg.client.databinding.FragmentAidlBinding;
import gov.togg.myaidl.IIPCExample;


public class AidlFragment extends Fragment implements View.OnClickListener {

    private FragmentAidlBinding binding;

    private IIPCExample iRemoteService = null;
    private boolean connected = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAidlBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnConnect.setOnClickListener(this);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if (connected) {
            disconnectToRemoteService();
            binding.txtServerPid.setText("");
            binding.txtServerConnectionCount.setText("");
            binding.btnConnect.setText(getString(R.string.connect));
            binding.linearLayoutClientInfo.setVisibility(View.INVISIBLE);
            connected = false;
        } else {
            connectToRemoteService();
            binding.linearLayoutClientInfo.setVisibility(View.VISIBLE);
            binding.btnConnect.setText(getString(R.string.disconnect));
            connected = true;
        }
    }

    private void connectToRemoteService() {
        try {
            Intent intent = new Intent("aidlexample");
            intent.setPackage("gov.togg.server");
            if (getActivity() != null && getActivity() instanceof MainActivity)
                getActivity().getApplicationContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            Log.e("ARAFA", e.toString());
        }
    }

    private void disconnectToRemoteService() {
        if (connected) {
            if (getActivity() != null && getActivity() instanceof MainActivity)
                getActivity().getApplicationContext().unbindService(mConnection);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iRemoteService = IIPCExample.Stub.asInterface(iBinder);
            try {
                binding.txtServerPid.setText(iRemoteService.getPid() + "");
                binding.txtServerConnectionCount.setText(iRemoteService.getConnectionCount() + "");
                iRemoteService.setDisplayedValue(getActivity().getPackageName(), 12, binding.edtClientData.getText().toString());
                connected = true;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Toast.makeText(getActivity(), "IPC server has disconnected unexpectedly", Toast.LENGTH_LONG).show();
            iRemoteService = null;
            connected = false;
        }
    };

}