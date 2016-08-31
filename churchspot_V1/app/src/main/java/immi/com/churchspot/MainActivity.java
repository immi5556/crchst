package immi.com.churchspot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    WebView myBrowser;
    GPSTracker gps;
    double latitude = 0, longitude = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gps = new GPSTracker(MainActivity.this, this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.d("latlong ", latitude + "--" + longitude);
            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        myBrowser = (WebView)findViewById(R.id.mybrowser);
        myBrowser.setWebChromeClient(new MyJavaScriptChromeClient());
        //myBrowser.setWebViewClient(new WebViewClient());
        myBrowser.getSettings().setJavaScriptEnabled(true);
        //myBrowser.loadUrl("file:///android_asset/map_1.html");
        myBrowser.loadUrl("file:///android_asset/map.html");
    }

    public void refreshLocation() {
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();
        //myBrowser.loadUrl("javascript:changeCenter(" + latitude + "," + longitude + ")");
    }

    private class MyJavaScriptChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message,final JsResult result) {
//handle Alert event, here we are showing AlertDialog
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Alert: Church Sport")
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok,
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do your stuff
                                    result.confirm();
                                }
                            }).setCancelable(false).create().show();
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Log.d("sdfsdf", view.getUrl() + " -- " + newProgress);
            //if (newProgress == 100 && view.getUrl().equalsIgnoreCase("file:///android_asset/map.html")){
                //myBrowser.loadUrl("javascript:checkandr()");
            //}
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            myBrowser.loadUrl("javascript:setMap(" + latitude + "," + longitude + ");");
            myBrowser.loadUrl("javascript:getChurches();");
        }
    }
}
