package gov.togg.server;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import gov.togg.server.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Client client = RecentClient.getRecentClient().getClient();
        if (client == null) {
            binding.linearLayoutClientState.setVisibility(View.INVISIBLE);
            binding.connectionStatus.setText( getString(R.string.no_connected_client));
        } else {
            binding.linearLayoutClientState.setVisibility(View.VISIBLE);
            binding.connectionStatus.setText( getString(R.string.last_connected_client_info));
            binding.txtPackageName.setText(client.getClientPackageName());
            binding.txtServerPid.setText(client.getClientProcessId());
            binding.txtData.setText(client.getClientData());
            binding.txtIpcMethod.setText(client.getIpcMethod());
        }
    }
}