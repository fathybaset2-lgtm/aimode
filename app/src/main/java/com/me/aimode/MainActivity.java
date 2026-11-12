package com.me.aimode;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends Activity {
    private static final String URL = "https://www.google.com/search?udm=50";
    private static final String GOOGLE_APP = "com.google.android.googlequicksearchbox";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setPackage(GOOGLE_APP);
        try {
            startActivity(i);
        } catch (ActivityNotFoundException e) {
            i.setPackage(null);
            try { startActivity(i); } catch (Exception ignored) {}
        }
        finish();
    }
}
