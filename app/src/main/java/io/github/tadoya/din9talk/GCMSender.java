package io.github.tadoya.din9talk;


import android.os.AsyncTask;
import android.util.Log;


import com.google.android.gcm.demo.logic.GcmServerSideSender;
import com.google.android.gcm.demo.logic.Message;


import java.io.IOException;

/**
 * Created by seongsikchoi on 2016. 4. 25..
 */
public class GCMSender {

    //protected LoggingService.Logger mLogger;

    String downstream_api_key = "AIzaSyDqVHqov_qZmbH7KIxbVpVu4oOxseDL4T0";
    String downstream_token = StartActivity.Token;
    String downstream_collapse_key = "";
    String downstream_restricted_package_name = "io.github.tadoya.din9talk";
    String downstream_ttl = "";
    String downstream_data;
    String key = "message";
    public GCMSender(String msg){
        this.downstream_data = msg;
        doGcmSend();
    }
    protected void doGcmSend() {
        //final Activity activity = this.getActivity();
        final Message.Builder messageBuilder = new Message.Builder();

        String collapseKey = downstream_collapse_key;
        if ((collapseKey != null) && !"".equals(collapseKey)) {
            messageBuilder.collapseKey(collapseKey.trim());
        }
        String restrictedPackageName = downstream_restricted_package_name;
        if ((restrictedPackageName != null) && !"".equals(restrictedPackageName)) {
            messageBuilder.restrictedPackageName(restrictedPackageName.trim());
        }
        String ttlString = downstream_ttl;
        try {
            int ttl = Integer.parseInt(ttlString);
            messageBuilder.timeToLive(ttl);
        } catch (NumberFormatException e) {
            // ttl not set properly, ignoring
            Log.d("GcmDEMO", "Failed to parse TTL, ignoring: " + ttlString);
        }

        messageBuilder.delayWhileIdle(false);
                //(CheckBox) activity.findViewById(R.id.downstream_delay_while_idle)).isChecked());
        messageBuilder.dryRun(false);
                //((CheckBox) activity.findViewById(R.id.downstream_dry_run)).isChecked());

        messageBuilder.addData("title", "tadoya");
        messageBuilder.addData(key, downstream_data);


        final boolean json = true;//((RadioButton) activity.findViewById(R.id.downstream_radio_json)).isChecked();
        final String apiKey = downstream_api_key;
        final String registrationId = downstream_token;

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                GcmServerSideSender sender = new GcmServerSideSender(apiKey);//, mLogger);
                try {
                    if (json) {
                        sender.sendHttpJsonDownstreamMessage(registrationId,
                                messageBuilder.build());
                    } else {
                        sender.sendHttpPlaintextDownstreamMessage(registrationId, messageBuilder.build());
                    }
                } catch (final IOException e) {
                    return e.getMessage();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    /*Toast.makeText(MainActivity,
                            "send message failed: " + result,
                            Toast.LENGTH_LONG).show();*/
                }
            }
        }.execute();

    }
}
