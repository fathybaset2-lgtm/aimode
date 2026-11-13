package com.me.aimode;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class AiService extends AccessibilityService {
    static volatile long armedAt = 0;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (armedAt == 0) return;
        if (System.currentTimeMillis() - armedAt > 10000) { armedAt = 0; return; }
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) return;
        AccessibilityNodeInfo n = find(root);
        if (n != null && click(n)) armedAt = 0;
    }

    private AccessibilityNodeInfo find(AccessibilityNodeInfo node) {
        if (node == null) return null;
        CharSequence t = node.getText();
        CharSequence d = node.getContentDescription();
        String s = ((t == null ? "" : t.toString()) + " " + (d == null ? "" : d.toString())).toLowerCase();
        if (s.contains("ai mode")) return node;
        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo r = find(node.getChild(i));
            if (r != null) return r;
        }
        return null;
    }

    private boolean click(AccessibilityNodeInfo node) {
        AccessibilityNodeInfo n = node;
        while (n != null) {
            if (n.isClickable()) return n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            n = n.getParent();
        }
        return false;
    }

    @Override
    public void onInterrupt() { }
}
