package uw.AccessibilityReport;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.google.android.apps.common.testing.accessibility.framework.AccessibilityCheckResult;
import com.google.android.apps.common.testing.accessibility.framework.AccessibilityCheckResultUtils;
import com.googlecode.eyesfree.utils.AccessibilityNodeInfoUtils;

import com.google.android.apps.common.testing.accessibility.framework.AccessibilityInfoHierarchyCheck;
import com.google.android.apps.common.testing.accessibility.framework.AccessibilityCheckPreset;
import com.google.android.apps.common.testing.accessibility.framework.AccessibilityInfoCheckResult;
import com.google.android.apps.common.testing.accessibility.framework.ClickableSpanInfoCheck;
import com.google.android.apps.common.testing.accessibility.framework.DuplicateClickableBoundsInfoCheck;
import com.google.android.apps.common.testing.accessibility.framework.SpeakableTextPresentInfoCheck;
import com.google.android.apps.common.testing.accessibility.framework.TouchTargetSizeInfoCheck;
import com.googlecode.eyesfree.utils.NodeFilter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class AccessibilityTestService extends AccessibilityService {

    public String packageName;
    private static final String TAG = "AccessibilityService";
    private static AccessibilityTestData data;
    private BroadcastReceiver receiver_set_package;
    private BroadcastReceiver receiver_pull_current_tree;
    private BroadcastReceiver receiver_pull_current_errors;
    private BroadcastReceiver receiver_pull_element_info;

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

        final IntentFilter filter_set_package = new IntentFilter();
        filter_set_package.addAction("xiaoyiz.setPackage");
        receiver_set_package = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "setPackage");
                packageName = intent.getStringExtra("packageName");
                Log.i(TAG, "Set Package:" + packageName);
            }
        };
        this.registerReceiver(receiver_set_package, filter_set_package);

        final IntentFilter filter_pull_current_tree = new IntentFilter();
        filter_pull_current_tree.addAction("xiaoyiz.pullCurrentTree");
        receiver_pull_current_tree = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "pullCurrentTree");
                pullCurrentTree(intent.getStringExtra("fileName"));
            }
        };
        this.registerReceiver(receiver_pull_current_tree, filter_pull_current_tree);

        final IntentFilter filter_pull_current_errors = new IntentFilter();
        filter_pull_current_errors.addAction("xiaoyiz.pullCurrentErrors");
        receiver_pull_current_errors = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "pullCurrentErrors");
                pullCurrentErrors(intent.getStringExtra("fileName"));
            }
        };
        this.registerReceiver(receiver_pull_current_errors, filter_pull_current_errors);

        final IntentFilter filter_pull_element_info = new IntentFilter();
        filter_pull_element_info.addAction("xiaoyiz.pullElementInfo");
        receiver_pull_element_info = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "pullElementInfo");
                pullElementInfo(intent);
            }
        };
        this.registerReceiver(receiver_pull_element_info, filter_pull_element_info);
    }

    public void pullElementInfo(Intent intent) {
        String text = intent.getStringExtra("text");
        String viewId = intent.getStringExtra("viewId");
        String altText = intent.getStringExtra("altText");
        String locationX = intent.getStringExtra("locationX");
        String locationY = intent.getStringExtra("locationY");

        if (text != null) {
            List<AccessibilityNodeInfo> results = getRootInActiveWindow().findAccessibilityNodeInfosByText(text);
            for (AccessibilityNodeInfo result: results) {
                Log.i(TAG, "Find Node: " + result.toString());
            }
            return;
        } else if (viewId != null) {
            List<AccessibilityNodeInfo> results = getRootInActiveWindow().findAccessibilityNodeInfosByViewId(viewId);
            for (AccessibilityNodeInfo result: results) {
                Log.i(TAG, "Find Node: " + result.toString());
            }
            return;
        } else if (altText != null) {
            ArrayList<AccessibilityNodeInfo> results = new ArrayList<>();
            getAccessibilityNodeByAltText(altText, getRootInActiveWindow(), results);
            for (AccessibilityNodeInfo result: results) {
                Log.i(TAG, "Find Node: " + result.toString());
            }
            return;
        } else if (locationX != null && locationY != null){
            ArrayList<AccessibilityNodeInfo> results = new ArrayList<>();
            getAccessibilityNodeByLocation(Integer.parseInt(locationX), Integer.parseInt(locationY), getRootInActiveWindow(), results);
            for (AccessibilityNodeInfo result: results) {
                Log.i(TAG, "Find Node: " + result.toString());
            }
            return;
        }
        Log.i(TAG, "Node Not Found");
    }

    public void getAccessibilityNodeByAltText(String altText, AccessibilityNodeInfo node, ArrayList<AccessibilityNodeInfo> results) {
        if (node == null) return;

        if (node.getChildCount() == 0) {
            if (node.getContentDescription() != null
                    && node.getContentDescription().equals(altText)) results.add(node);
        } else {
            if (node.getContentDescription() != null
                    && node.getContentDescription().equals(altText)) results.add(node);
            for (int i = 0; i < node.getChildCount(); i++) {
                getAccessibilityNodeByAltText(altText, node.getChild(i), results);
            }
        }
    }

    public void getAccessibilityNodeByLocation(int x, int y, AccessibilityNodeInfo node, ArrayList<AccessibilityNodeInfo> results) {
        if (node == null) return;

        if (node.getChildCount() == 0) {
            Rect buttonRect = new Rect();
            node.getBoundsInScreen(buttonRect);
            if (buttonRect.contains(x, y)) results.add(node);
        } else {
            Rect buttonRect = new Rect();
            node.getBoundsInScreen(buttonRect);
            if (buttonRect.contains(x, y)) results.add(node);
            for (int i = 0; i < node.getChildCount(); i++) {
                getAccessibilityNodeByLocation(x, y, node.getChild(i), results);
            }
        }
    }

    public void printAllNodes(AccessibilityNodeInfo info, AccessibilityNodeInfo parent, ArrayList<AccessibilityNodeInfo> results) {
        if (info == null) return;
        if (info.getChildCount() == 0) {
            results.add(info);
            Log.i(TAG, "TREE_RESULT -> " + "Leaf Node = " + info.toString());
            Rect bounds = new Rect();
            info.getBoundsInScreen(bounds);
            Log.i(TAG, "Node Center Coordinate -> x = " + bounds.centerX() + ", y = " + bounds.centerY());
            if (parent != null) Log.i(TAG, "Parent Node is " + parent.toString());
        } else {
            results.add(info);
            Log.i(TAG, "TREE_RESULT -> " + "Non-leaf Node = " + info.toString());
            Rect bounds = new Rect();
            info.getBoundsInScreen(bounds);
            Log.i(TAG, "Node Center Coordinate -> x = " + bounds.centerX() + ", y = " + bounds.centerY());
            for (int i = 0; i < info.getChildCount(); i++) {
                printAllNodes(info.getChild(i), info, results);
            }
        }
    }

    public void pullCurrentTree(String fileName) {
        AccessibilityNodeInfo node = getRootInActiveWindow();
        if (node == null) return;
        ArrayList<AccessibilityNodeInfo> results = new ArrayList<>();
        printAllNodes(node, null, results);
        String resultString = "";
        for (AccessibilityNodeInfo result:results) {
            resultString += result.toString();
            resultString += '\n';
        }
        if (fileName!=null) writeToFile("Tree", fileName, resultString);
    }

    public void pullCurrentErrors(String fileName) {
        AccessibilityNodeInfo node = getRootInActiveWindow();

        int numNodes = AccessibilityNodeInfoUtils.searchAllFromBfs(getApplicationContext(),
                new AccessibilityNodeInfoCompat(node), WIDE_OPEN_FILTER).size();
        Log.i(TAG, "Number of nodes on screen = "+numNodes);

        Set<AccessibilityInfoHierarchyCheck> checks =
                AccessibilityCheckPreset.getInfoChecksForPreset(AccessibilityCheckPreset.LATEST);

        List<AccessibilityInfoCheckResult> results = new LinkedList<AccessibilityInfoCheckResult>();
        for (AccessibilityInfoHierarchyCheck check : checks) {
            results.addAll(check.runCheckOnInfoHierarchy(node, this));
        }
        /*
        for (AccessibilityInfoCheckResult result: results) {
            Log.i(TAG, result.getInfo().toString());
            Log.i(TAG, result.getSourceCheckClass().toString());
            Log.i(TAG, result.getMessage().toString());
            Log.i(TAG, "----------");
        }
        */

        List<AccessibilityInfoCheckResult> errors =
                AccessibilityCheckResultUtils.getResultsForType(results,
                        AccessibilityCheckResult.AccessibilityCheckResultType.ERROR);

        for (AccessibilityInfoCheckResult error:errors) {
            Log.i(TAG, error.getInfo().toString());
            Log.i(TAG, error.getSourceCheckClass().toString());
            Log.i(TAG, error.getMessage().toString());
            Log.i(TAG, "----------");
        }

        String resultString = "";
        for (AccessibilityInfoCheckResult error:errors) {
            resultString += error.getInfo().toString();
            resultString += '\n';
            resultString += error.getSourceCheckClass().toString();
            resultString += '\n';
            resultString += error.getMessage().toString();
            resultString += '\n';
        }

        if (fileName!=null) writeToFile("Errors", fileName, resultString);
    }

    public void writeToFile(String type, String fileName, String content) {
        String app = "NoPackageName";
        if (packageName != null) app = packageName;

        File path = new File(new File(Environment.getExternalStorageDirectory().getAbsolutePath(), app), type);
        if (path.mkdirs()) {
            Log.e(TAG, "Directory created");
        }
        Log.i(TAG, "File Path: " + path.toString());
        File file = new File(path, fileName);
        FileOutputStream stream;
        try {
            stream = new FileOutputStream(file);
            try {
                stream.write(content.getBytes());
            } catch(IOException ie) {
            } finally {
                try { stream.close(); } catch (IOException ie) {
                }
            }
        } catch(IOException ie) {}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver_set_package);
        this.unregisterReceiver(receiver_pull_current_tree);
        this.unregisterReceiver(receiver_pull_current_errors);
        this.unregisterReceiver(receiver_pull_element_info);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "CHECKER_RESULT -> " + data.resultsToJSON());
        return super.onUnbind(intent);
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent e) {

        if (e.getPackageName() == null) return;

        /*
        Log.i(TAG, "on accessibility event");
        // node that can be used to traverse the window content, represented as a tree of such objects.
        AccessibilityNodeInfo node = getRootInActiveWindow();
        if (node == null) return;

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
        */

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