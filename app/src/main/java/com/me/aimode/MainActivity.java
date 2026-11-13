package com.me.aimode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class MainActivity extends Activity {
    static final String PKG = "com.google.android.googlequicksearchbox";

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        String enabled = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (enabled == null || !enabled.contains(getPackageName())) {
            Toast.makeText(this, "Enable 'Google AI' in Accessibility, then open the app again",
                    Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            finish();
            return;
        }
        Intent i = getPackageManager().getLaunchIntentForPackage(PKG);
        if (i == null) {
            Toast.makeText(this, "Google app not found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        AiService.armedAt = System.currentTimeMillis();
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
