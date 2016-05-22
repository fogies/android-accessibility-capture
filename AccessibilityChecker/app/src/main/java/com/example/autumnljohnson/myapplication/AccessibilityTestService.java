package com.example.autumnljohnson.myapplication;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;

import com.google.android.apps.common.testing.accessibility.framework.EditableContentDescInfoCheck;
import com.googlecode.eyesfree.utils.AccessibilityNodeInfoUtils;

import com.google.android.apps.common.testing.accessibility.framework.AccessibilityInfoHierarchyCheck;
import com.google.android.apps.common.testing.accessibility.framework.AccessibilityCheckPreset;
import com.google.android.apps.common.testing.accessibility.framework.AccessibilityInfoCheckResult;
import com.google.android.apps.common.testing.accessibility.framework.AccessibilityInfoHierarchyCheck;
import com.google.android.apps.common.testing.accessibility.framework.ClickableSpanInfoCheck;
import com.google.android.apps.common.testing.accessibility.framework.DuplicateClickableBoundsInfoCheck;
import com.google.android.apps.common.testing.accessibility.framework.SpeakableTextPresentInfoCheck;
import com.google.android.apps.common.testing.accessibility.framework.TouchTargetSizeInfoCheck;
import com.googlecode.eyesfree.utils.AccessibilityNodeInfoUtils;
import com.googlecode.eyesfree.utils.NodeFilter;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
/**
 * Created by Autumn Johnson
 */

public class AccessibilityTestService extends AccessibilityService {

    public String packageName = "";
    private static final String TAG = "AccessibilityService";
    private static AccessibilityTestData data;
    private BroadcastReceiver yourReceiver;
    private static final String ACTION="xiaoyiz.triggerIntent";

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Bundle extras = intent.getExtras();
        packageName = (String) extras.get("packageName");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "Service is connected");
        try {
            data = new AccessibilityTestData();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Xiaoyi", "Wow");
        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(ACTION);
        this.yourReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String output = intent.getStringExtra("actionName");
                Log.i("Xiaoyi", output);
            }
        };
        // Registers the receiver so that your service will listen for
        // broadcasts
        this.registerReceiver(this.yourReceiver, theFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Do not forget to unregister the receiver!!!
        this.unregisterReceiver(this.yourReceiver);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //data.printResults();
        Log.i(TAG, "CHECKER_RESULT -> " + data.resultsToJSON());
        return super.onUnbind(intent);
    }

    @Override
    public void onInterrupt() {
        data.printResults();
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent e) {

        if (e.getPackageName() == null) {
            return;
        }
        Log.d(TAG, "on accessibility event");
        // node that can be used to traverse the window content, represented as a tree of such objects.
        AccessibilityNodeInfo node = getRootInActiveWindow();

        if (node != null) {
            // get the number of nodes in the hierarchy (number of nodes that tests will be run on)
            int numNodes = AccessibilityNodeInfoUtils.searchAllFromBfs(getApplicationContext(),
                    new AccessibilityNodeInfoCompat(node), WIDE_OPEN_FILTER).size();

            if (e.getPackageName().equals(packageName)) {
                String name = e.getPackageName()+", screen "+Integer.toString(e.getWindowId());
                Log.i(TAG, "--------------");
                Log.i(TAG, "Name -> " + name);

                printAllNodes(getRootInActiveWindow(), null);

                Set<AccessibilityInfoHierarchyCheck> checks = AccessibilityCheckPreset.getInfoChecksForPreset(AccessibilityCheckPreset.VERSION_2_0_CHECKS);

                // 6 total checks (one is Warnings, other are Errors)
                for (AccessibilityInfoHierarchyCheck check : checks) {
                    try {
                        List<AccessibilityInfoCheckResult> checkResults =
                                check.runCheckOnInfoHierarchy(node, getApplicationContext());
                        data.storeCheckResults(e.getPackageName(), checkResults, getCheckType(check), numNodes);
                        Log.i(TAG, "CHECKER_RESULT -> " + data.resultsToJSON());
                        data = new AccessibilityTestData();
                    } catch (Exception ex) { // Probably a DuplicateClickableBounds error
                        Log.d(TAG, ex.getMessage());
                    }
                }
            }

            /*
            Bundle bundle = new Bundle();
            bundle = bundle.getBundle(AccessibilityCheckMetadata.METADATA_KEY_SCREEN_CAPTURE_BITMAP);
            List<AccessibilityInfoCheckResult> warnings = AccessibilityCheckResultUtils.getResultsForType(
                    results, AccessibilityCheckResult.AccessibilityCheckResultType.WARNING);
            for (int i = 0; i < warnings.size(); i++) {
                Log.d(TAG, "warning " + e.getPackageName() + ":" + warnings.get(0).getMessage().toString());
            }
            */
        }
    }

    private static final NodeFilter WIDE_OPEN_FILTER = new NodeFilter() {
        @Override
        public boolean accept(Context context, AccessibilityNodeInfoCompat node) {
            return true;
        }
    };

    private AccessibilityCheckType getCheckType(AccessibilityInfoHierarchyCheck check) {
        if (check instanceof SpeakableTextPresentInfoCheck) {
            return AccessibilityCheckType.SPEAKABLE_TEXT_PRESENT;
        } else if (check instanceof ClickableSpanInfoCheck) {
            return AccessibilityCheckType.CLICKABLE_SPAN;
        } else if (check instanceof DuplicateClickableBoundsInfoCheck) {
            return AccessibilityCheckType.DUPLICATE_CLICKABLE_BOUNDS;
        } else if (check instanceof TouchTargetSizeInfoCheck) {
            return AccessibilityCheckType.TOUCH_TARGET_SIZE;
        } else {
            return AccessibilityCheckType.EDITABLE_CONTENT_DESC;
        }
    }

    public enum AccessibilityCheckType {
        SPEAKABLE_TEXT_PRESENT,
        CLICKABLE_SPAN,
        EDITABLE_CONTENT_DESC,
        DUPLICATE_CLICKABLE_BOUNDS,
        TOUCH_TARGET_SIZE;
    }

    private void printAllNodes(AccessibilityNodeInfo info, AccessibilityNodeInfo parent) {
        if (info == null) return;
        if (info.getChildCount() == 0) {
            Log.i(TAG, "TREE_RESULT -> " + "Current Node = " + info.toString());
            Rect bounds = new Rect();
            info.getBoundsInScreen(bounds);
            Log.i(TAG, "Node Center Coordinate -> x = " + bounds.centerX() + ", y = " + bounds.centerY());
        } else {
            for (int i = 0; i < info.getChildCount(); i++) {
                printAllNodes(info.getChild(i), info);
            }
        }
    }

}