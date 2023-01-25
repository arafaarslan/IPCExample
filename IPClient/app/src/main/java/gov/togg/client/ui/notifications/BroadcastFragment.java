package gov.togg.client.ui.notifications;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import gov.togg.client.databinding.FragmentBroadcastBinding;

public class BroadcastFragment extends Fragment implements View.OnClickListener {

    private FragmentBroadcastBinding binding;

    private final String PID = "pid";
    private final String PACKAGE_NAME = "package_name";
    private final String DATA = "data";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentBroadcastBinding.inflate(inflater, container, false);
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
        sendBroadcast();
        showBroadcastTime();
    }


    private void sendBroadcast(){
        Intent intent = new Intent("broadcastexample");
        intent.setPackage("gov.togg.client.ui.notifications");
        intent.putExtra(PACKAGE_NAME, getActivity().getApplicationContext().getPackageName());
        intent.putExtra(PID, 25+"");
        intent.putExtra(DATA, binding.edtClientData.getText().toString());
        intent.setComponent(new ComponentName("gov.togg.server", "gov.togg.server.IPCBroadcastReceiver"));
        getActivity().getApplicationContext().sendBroadcast(intent);
    }

    private void showBroadcastTime(){
        Calendar cal = Calendar.getInstance();
        String time =cal.get(Calendar.HOUR)+ ":" + cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND);
        binding.linearLayoutClientInfo.setVisibility(View.VISIBLE);
        binding.txtDate.setText(time);
    }
}








