package com.xiaoyiz.automate;

import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;


@SuppressLint("NewApi")
public class MyAccessibilityService extends AccessibilityService {

    private static final String TAG = "Xiaoyi";

    public static String appName = "";

    public static int step = -1;
    public static boolean startSell = false;

    public int m_price = 63;
    public String m_description = "WWOOWOWOWOW";
    public String m_zip = "90024, United States";

    private void dumpIdResNames(AccessibilityNodeInfo root) {
        if (root == null) {
            return;
        }
        if (root.getViewIdResourceName() != null) {
            Log.i(TAG, "Id: " + root.getViewIdResourceName().toString());
        }
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            dumpIdResNames(root.getChild(i));
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (startSell) {
            printAllNodes(getRootInActiveWindow());
            switch (event.getEventType()) {
                //case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                    Log.i(TAG, appName + ", WINDOW_STATE_CHANGED, Step" + step);
                    if (appName.equals("Close5")) {
                        goToStepClose5();
                    } else if (appName.equals("OfferUp")) {
                        goToStepOfferUp(event);
                    }
                    step += 1;
            }
        }
    }

    private void goToStepOfferUp(AccessibilityEvent event) {
        switch (step) {
            // Open action drawer.
            case 0:
                clickAtPosition(84, 156, getRootInActiveWindow());
                break;
            // Create new listing.
            case 1:
                clickAtTextButton("Post New Offer", getRootInActiveWindow());
                break;
            // Click gallery icon.
            case 2:
                SystemClock.sleep(5000);
                clickAtTextButton("Select Photo", getRootInActiveWindow());
                break;
            // Find image.
            case 3:
                SystemClock.sleep(1000);
                clickAtTextButton("5.png", getRootInActiveWindow());
                break;
            // Process image.
            case 4:
                // Crop image.
                SystemClock.sleep(3000);
                clickAtTextButton("Choose", getRootInActiveWindow());
                // Next step.
                SystemClock.sleep(1000);
                clickAtTextButton("Next", getRootInActiveWindow());
                break;
            // Input listing info.
            case 5:
                inputPrice(m_price, getRootInActiveWindow());
                SystemClock.sleep(1000);
                inputDescription(m_description, getRootInActiveWindow());
                SystemClock.sleep(1000);
                // Click location icon.
                clickAtPosition(540, 1602, getRootInActiveWindow());
                SystemClock.sleep(1000);
                inputLocation(m_zip, getRootInActiveWindow());
                SystemClock.sleep(3000);
                // Click the first result.
                clickAtPosition(540, 432+1, getRootInActiveWindow());
                SystemClock.sleep(1000);
                // Click Save button.
                clickAtPosition(1080-1, 84+1, getRootInActiveWindow());
                break;
            /*
            case 6:
                step = 1;
                // Wait for listing appear?
                SystemClock.sleep(5000);
                // Click + button.
                clickAtPosition(948, 1644, getRootInActiveWindow());
                // We will go to step 2.
                break;
                */
        }
    }

    private void goToStepClose5() {
        //event.getPackageName().equals("com.android.settings");
        switch (step) {
            // Open action drawer.
            case 0:
                clickAtPosition(84, 84, getRootInActiveWindow());
                break;
            // Create new listing.
            case 1:
                clickAtTextButton("New Listing", getRootInActiveWindow());
                break;
            // Click gallery icon.
            case 2:
                SystemClock.sleep(1000);
                clickAtPosition(138, 1602, getRootInActiveWindow());
                break;
            // Find image.
            case 3:
                SystemClock.sleep(1000);
                clickAtTextButton("5.png", getRootInActiveWindow());
                break;
            // Process image.
            case 4:
                // Crop image.
                SystemClock.sleep(3000);
                clickAtTextButton("Choose", getRootInActiveWindow());
                // Next step.
                SystemClock.sleep(1000);
                clickAtTextButton("Next", getRootInActiveWindow());
                break;
            // Input listing info.
            case 5:
                inputPrice(m_price, getRootInActiveWindow());
                SystemClock.sleep(1000);
                inputDescription(m_description, getRootInActiveWindow());
                SystemClock.sleep(1000);
                // Click location icon.
                clickAtPosition(540, 1602, getRootInActiveWindow());
                SystemClock.sleep(1000);
                inputLocation(m_zip, getRootInActiveWindow());
                SystemClock.sleep(3000);
                // Click the first result.
                clickAtPosition(540, 432+1, getRootInActiveWindow());
                SystemClock.sleep(1000);
                // Click Save button.
                clickAtPosition(1080-1, 84+1, getRootInActiveWindow());
                break;
            /*
            case 6:
                step = 1;
                // Wait for listing appear?
                SystemClock.sleep(5000);
                // Click + button.
                clickAtPosition(948, 1644, getRootInActiveWindow());
                // We will go to step 2.
                break;
                */
        }
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        return true;

    }

    @Override
    public void onInterrupt() {
    }

    private void clickAtPosition(int x, int y, AccessibilityNodeInfo node) {
        if (node == null) return;

        if (node.getChildCount() == 0) {
            Rect buttonRect = new Rect();
            node.getBoundsInScreen(buttonRect);
            if (buttonRect.contains(x, y)) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return;
            }
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                clickAtPosition(x, y, node.getChild(i));
            }
        }
    }

    private void clickAtTextButton(String text, AccessibilityNodeInfo node) {
        if (node == null) return;

        List<AccessibilityNodeInfo> results = node.findAccessibilityNodeInfosByText(text);
        if (results != null && !results.isEmpty()) {
            for (AccessibilityNodeInfo result: results) {
                if (result.getClassName().equals("android.widget.TextView")) {
                    if (result.getParent() != null) {
                        result.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                } else {
                    result.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        } else {
            Log.i(TAG, text + " Not found.");
        }
    }

    private void inputPrice(int price, AccessibilityNodeInfo node) {
        if (node == null) return;

        if (node.getChildCount() == 0) {
            if (node.getText() != null && node.getText().toString().contains("Price")) {
                node.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", String.valueOf(price).subSequence(0, 1));
                clipboard.setPrimaryClip(clip);
                node.performAction(AccessibilityNodeInfo.ACTION_PASTE);
                if (price >= 10) {
                    clip = ClipData.newPlainText("label", String.valueOf(price).substring(1));
                    clipboard.setPrimaryClip(clip);
                    node.performAction(AccessibilityNodeInfo.ACTION_PASTE);
                }
            }
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                inputPrice(price, node.getChild(i));
            }
        }
    }

    private void inputDescription(String description, AccessibilityNodeInfo node) {
        if (node == null) return;

        if (node.getChildCount() == 0) {
            if (node.getText() != null && node.getText().toString().contains("description")) {
                node.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", description);
                clipboard.setPrimaryClip(clip);
                node.performAction(AccessibilityNodeInfo.ACTION_PASTE);
            }
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                inputDescription(description, node.getChild(i));
            }
        }
    }

    private void inputLocation(String zip, AccessibilityNodeInfo node) {
        if (node == null) return;

        if (node.getChildCount() == 0) {
            if (node.getText() != null && node.getText().toString().contains("location")) {
                Log.i(TAG, node.toString());
                node.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", zip);
                clipboard.setPrimaryClip(clip);
                node.performAction(AccessibilityNodeInfo.ACTION_PASTE);
            }
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                inputLocation(zip, node.getChild(i));
            }
        }
    }

    private void printAllNodes(AccessibilityNodeInfo info) {
        if (info == null) return;
        if (info.getChildCount() == 0) {
            //if(info.getClassName().toString().equals("android.widget.EditText")) {
            //    Log.i(TAG, "Text: " + info.getText());
            //}
            //Log.i(TAG, "Type: " + info.getClassName());
            //Log.i(TAG, "Text: " + info.getText());
            Log.i(TAG, "String: " + info.toString());
            /*
            buttonRect = new Rect();
            info.getBoundsInParent(buttonRect);
            if (buttonRect.contains(84,84)) {}
            Log.i(TAG, "Center：(" + tempRect.centerX() + "," + tempRect.centerY() + ")");
            Log.i(TAG, "Height and Width：" + tempRect.height() + "," + tempRect.width());
            List<AccessibilityNodeInfo.AccessibilityAction> temp = info.getActionList();
            for (AccessibilityNodeInfo.AccessibilityAction t: temp) {
                Log.i(TAG, "Action: "+t);
            }
              */

            //if (tempRect.centerX() == 84 && tempRect.centerY() == 84 && tempRect.height() == 168 && tempRect.width() == 168) {
            //    Log.i(TAG, "Yoooooo");
                //info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            //}
/*
            if (info.getText() != null && info.getText().toString().contains("Price")) {
                ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", String.valueOf(price).subSequence(0,1));
                clipboard.setPrimaryClip(clip);
                info.performAction(AccessibilityNodeInfo.ACTION_PASTE);
                if (price >= 10) {
                    clip = ClipData.newPlainText("label", String.valueOf(price).substring(1));
                    clipboard.setPrimaryClip(clip);
                    info.performAction(AccessibilityNodeInfo.ACTION_PASTE);
                }
            }
            if (info.getText() != null && info.getText().toString().equals("$4")) {
                ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", "80");
                clipboard.setPrimaryClip(clip);
                info.performAction(AccessibilityNodeInfo.ACTION_PASTE);
            }
            */

        } else {
            Log.i(TAG, "Parent: " + info.toString());
            //Log.i(TAG, "A parent node");
            //info.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            for (int i = 0; i < info.getChildCount(); i++) {
                printAllNodes(info.getChild(i));
                    /*
                if(info.getChild(i)!=null){
                    if(info.getChild(i).getText() != null && info.getChild(i).getText().toString().equals("Price")) {
                        Log.i(TAG, "Wow");
                        //info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                    printAllNodes(info.getChild(i));
                }*/
            }
        }
    }
}
