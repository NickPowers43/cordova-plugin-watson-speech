package com.nickpowers.cordova;

import android.util.Log;

import com.ibm.watson.developer_cloud.android.speech_to_text.v1.ISpeechDelegate;
import com.ibm.watson.developer_cloud.android.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.android.speech_to_text.v1.dto.SpeechConfiguration;
import com.ibm.watson.developer_cloud.android.text_to_speech.v1.TextToSpeech;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Nick on 3/23/2016.
 */
public class WatsonSpeechPlugin extends CordovaPlugin implements ISpeechDelegate {

    public static final String ACTION_STT_INITIALIZE = "stt_init";
    public static final String ACTION_STT_START = "stt_start";
    public static final String ACTION_STT_STOP = "stt_stop";

    public static final String ACTION_TTS_INITIALIZE = "tts_init";
    public static final String ACTION_TTS_SYNTHESIZE = "tts_synthesize";

    private CallbackContext callbackContext;

    public WatsonSpeechPlugin()
    {
        this.callbackContext = null;
    }

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        if (ACTION_TTS_SYNTHESIZE.equals(action)) {
            Log.d("NATIVE", "ACTION: TTS_SYNTHESIZE called");

            TextToSpeech.sharedInstance().synthesize(data.getString(0));
            return true;

        } if (ACTION_STT_INITIALIZE.equals(action)) {

            JSONObject credentialsObj = data.getJSONObject(0).getJSONObject("credentials");
            String watsonUrl = credentialsObj.getString("url");
            String watsonSTTUsername = credentialsObj.getString("username");
            String watsonSTTPassword = credentialsObj.getString("password");

            try {
                SpeechConfiguration speechConfiguration = new SpeechConfiguration(SpeechConfiguration.AUDIO_FORMAT_OGGOPUS);
                URI uri = new URI(watsonUrl);
                SpeechToText.sharedInstance().initWithContext(uri, webView.getContext(), speechConfiguration);
                SpeechToText.sharedInstance().setCredentials(watsonSTTUsername, watsonSTTPassword);
                SpeechToText.sharedInstance().setDelegate(this);
                SpeechToText.sharedInstance().setModel("en-US_BroadbandModel");

                this.callbackContext = callbackContext;
                PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
                result.setKeepCallback(true);
                callbackContext.sendPluginResult(result);
                return true;

            } catch(URISyntaxException e) {
                e.printStackTrace();
                PluginResult result = new PluginResult(PluginResult.Status.MALFORMED_URL_EXCEPTION);
                result.setKeepCallback(true);
                callbackContext.sendPluginResult(result);
                return false;
            }

        } else if (ACTION_STT_START.equals(action)) {

            SpeechToText.sharedInstance().recognize();
            return true;

        } else if (ACTION_STT_STOP.equals(action)) {

            SpeechToText.sharedInstance().stopRecording();
            return true;

        } else if (ACTION_TTS_INITIALIZE.equals(action)){
            Log.d("NATIVE", "ACTION: TTS_INIT called");

            JSONObject credentialsObj = data.getJSONObject(0).getJSONObject("credentials");
            String watsonUrl = credentialsObj.getString("url");
            String watsonTTSUsername = credentialsObj.getString("username");
            String watsonTTSPassword = credentialsObj.getString("password");

            try
            {
                URI uri = new URI(watsonUrl);
                TextToSpeech.sharedInstance().initWithContext(uri);
                TextToSpeech.sharedInstance().setCredentials(watsonTTSUsername, watsonTTSPassword);
                TextToSpeech.sharedInstance().setVoice(data.getString(1));

                callbackContext.success();
                Log.d("NATIVE", "ACTION: TTS_INIT succeeded");
                return true;

            } catch (URISyntaxException e)
            {
                e.printStackTrace();
                PluginResult result = new PluginResult(PluginResult.Status.MALFORMED_URL_EXCEPTION);
                callbackContext.sendPluginResult(result);
                Log.d("NATIVE", "ACTION: TTS_INIT failed");
                return false;
            }

        } else {
            return false;
        }
    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onError(String s) {
        if (this.callbackContext != null)
        {
            PluginResult result = new PluginResult(PluginResult.Status.ERROR);
            result.setKeepCallback(true);
            callbackContext.error(s);
            callbackContext.sendPluginResult(result);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onMessage(String msg) {
        if (this.callbackContext != null)
        {
            PluginResult result = new PluginResult(PluginResult.Status.OK);
            result.setKeepCallback(true);
            callbackContext.success(msg);
            callbackContext.sendPluginResult(result);
        }
    }

    @Override
    public void onAmplitude(double v, double v1) {

    }

}
