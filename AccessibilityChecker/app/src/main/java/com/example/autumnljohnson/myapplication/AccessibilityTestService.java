package com.example.autumnljohnson.myapplication;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
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

    private static final String TAG = "AccessibilityService";
    private static AccessibilityTestData data;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "Service is connected");
        try {
            data = new AccessibilityTestData();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        //AccessibilityServiceInfo info = new AccessibilityServiceInfo();
       // info.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED |
              //            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED |
               //           AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        //this.setServiceInfo(info);

        //AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        // String[] packages = {"org.collegeboard.qotd"};
        // info.packageNames = packages;
        // setServiceInfo(info);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //data.printResults();
        Log.i("CHECKER_JSON_RESULT", data.resultsToJSON());
        return super.onUnbind(intent);
    }

    @Override
    public void onInterrupt() {
        data.printResults();
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent e) {

        Log.d(TAG, "on accessibility event");
        // node that can be used to traverse the window content, represented as a tree of such objects.
        AccessibilityNodeInfo node = e.getSource(); // should I be gettin the root?

        if (node != null) { // node.getPackageName().equals("org.collegeboard.qotd")
            //AccessibilityNodeInfoCompat currNodeCompat = new AccessibilityNodeInfoCompat(node);
            //AccessibilityNodeInfoCompat rootNodeCompat = new AccessibilityNodeInfoCompat(
            //                                    AccessibilityNodeInfoUtils.getRoot(currNodeCompat));
            //AccessibilityNodeInfo rootNode = rootNodeCompat.
            try {
                /*
                Log.d(TAG, "event type : " + e.getEventType());
                Log.d(TAG, "content description : " + e.getContentDescription());
                Log.d(TAG, "package name : " + e.getPackageName());
                Log.d(TAG, "source : " + e.getSource());
                Log.d(TAG, "window id : " + e.getWindowId());
                Log.d(TAG, "event time : " + e.getEventTime());
                */
            } catch ( Exception e2 ) {
                Log.d(TAG, e2.getMessage());
            }


            // get the number of nodes in the hierarchy (number of nodes that tests will be run on)
            int numNodes = AccessibilityNodeInfoUtils.searchAllFromBfs(getApplicationContext(),
                    new AccessibilityNodeInfoCompat(node), WIDE_OPEN_FILTER).size();

            if (e.getPackageName().equals("com.ecg.close5")) {
                Log.d(TAG,"Wow");

                Set<AccessibilityInfoHierarchyCheck> checks = AccessibilityCheckPreset.getInfoChecksForPreset(AccessibilityCheckPreset.VERSION_2_0_CHECKS);

                // 6 total checks (one is Warnings, other are Errors)
                for (AccessibilityInfoHierarchyCheck check : checks) {
                    try {
                        List<AccessibilityInfoCheckResult> checkResults =
                                check.runCheckOnInfoHierarchy(node, getApplicationContext());
                        data.storeCheckResults(e.getPackageName()+", screen "+Integer.toString(e.getWindowId()), checkResults, getCheckType(check), numNodes);
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
}