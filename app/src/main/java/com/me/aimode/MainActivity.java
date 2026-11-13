package com.me.aimode;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {
    static final String PKG = "com.google.android.googlequicksearchbox";

    boolean launch(String cls) {
        try {
            Intent i = new Intent();
            i.setClassName(PKG, cls);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        final SharedPreferences sp = getSharedPreferences("p", MODE_PRIVATE);

        String saved = sp.getString("act", null);
        if (saved != null && launch(saved)) { finish(); return; }

        final List<String> names = new ArrayList<>();
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(PKG, PackageManager.GET_ACTIVITIES);
            if (pi.activities != null) {
                for (ActivityInfo a : pi.activities) {
                    if (a.exported && a.permission == null) names.add(a.name);
                }
            }
        } catch (Exception e) { }

        if (names.isEmpty()) {
            Toast.makeText(this, "Google app not found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        Collections.sort(names);

        for (String n : names) {
            if (n.toLowerCase().matches(".*ai_?mode.*") && launch(n)) { finish(); return; }
        }

        ListView lv = new ListView(this);
        lv.setBackgroundColor(0xFF202124);
        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names));
        lv.setOnItemClickListener((p, v, pos, id) -> {
            if (launch(names.get(pos))) finish();
            else Toast.makeText(this, "Not allowed", Toast.LENGTH_SHORT).show();
        });
        lv.setOnItemLongClickListener((p, v, pos, id) -> {
            String n = names.get(pos);
            sp.edit().putString("act", n).apply();
            Toast.makeText(this, "Saved as default", Toast.LENGTH_SHORT).show();
            if (launch(n)) finish();
            return true;
        });
        setContentView(lv);
    }
}
