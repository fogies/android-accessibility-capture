package com.example.autumnljohnson.myapplication;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.autumnljohnson.myapplication.AccessibilityTestService.AccessibilityCheckType;
import com.google.android.apps.common.testing.accessibility.framework.AccessibilityCheckResult;
import com.google.android.apps.common.testing.accessibility.framework.AccessibilityCheckResultUtils;
import com.google.android.apps.common.testing.accessibility.framework.AccessibilityInfoCheckResult;
import com.google.android.apps.common.testing.accessibility.framework.TouchTargetSizeInfoCheck;
import com.googlecode.eyesfree.utils.AccessibilityNodeInfoUtils;

/**
 * Created by autumnljohnson on 1/18/16.
 */
public class AccessibilityCheckData {
    public static final String TAG = "ASResults";

    public AccessibilityCheckType checkType;
    public int numNodesChecked; // total number of nodes this check type was run on
    public int numErrors; // total number of error messages recieved
    public Map<CharSequence, Integer> messageToCounts; // (error description) -> (error count)

    public AccessibilityCheckData(AccessibilityCheckType checkType) {
        this.checkType = checkType;
        messageToCounts = new HashMap<>();
    }

    public void storeCheckResults(List<AccessibilityInfoCheckResult> checkResults, int numNodes) {
        List<AccessibilityInfoCheckResult> errors = AccessibilityCheckResultUtils.getResultsForType(
                checkResults, AccessibilityCheckResult.AccessibilityCheckResultType.ERROR);
        for (AccessibilityInfoCheckResult error : errors) {
            CharSequence errorMessage = error.getMessage();

            // parse error message to keep consistent
            if (checkType == AccessibilityCheckType.TOUCH_TARGET_SIZE) {
                int index = errorMessage.toString().indexOf("Actual");
                errorMessage = errorMessage.subSequence(0, index - 1);
            }

            if (!messageToCounts.containsKey(error.getMessage())) {
                messageToCounts.put(errorMessage, 1);
            } else {
                int count = messageToCounts.get(errorMessage);
                messageToCounts.put(errorMessage, count + 1);
            }
        }

        numErrors += errors.size();
        int currNumNotRun = AccessibilityCheckResultUtils.getResultsForType(
                checkResults, AccessibilityCheckResult.AccessibilityCheckResultType.NOT_RUN).size();
        numNodesChecked += (numNodes - currNumNotRun);
    }


    public void printCheckResults() {
        Log.i(TAG, "check type: " + checkType.name());
        Log.i(TAG, "total num errors: " + numErrors);
        Log.i(TAG, "total num nodes checked: " + numNodesChecked);
        for (CharSequence message : messageToCounts.keySet()) {
            Log.i(TAG, "error message: " + message);
            Log.i(TAG, "count: " + messageToCounts.get(message));
        }
    }
}
