package fr.ec.producthunt.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by huber on 08/04/2017.
 */

public class RefreshReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Nous mettons à jour l'affichage",Toast.LENGTH_SHORT).show();
    }
}
