package com.example.autumnljohnson.myapplication;

import android.util.Log;
import com.example.autumnljohnson.myapplication.AccessibilityTestService.AccessibilityCheckType;

import com.google.android.apps.common.testing.accessibility.framework.AccessibilityInfoCheckResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by autumnljohnson on 1/15/16.
 *
 */
public class PackageChecks {
    public static final String TAG = "ASResults";
    public Map<AccessibilityCheckType, AccessibilityCheckData> results;
    public CharSequence packageName;


    public PackageChecks(CharSequence packageName) {
        results = new HashMap<>();
        this.packageName = packageName;

    }

    public void storeCheckResults(List<AccessibilityInfoCheckResult> checkResults, AccessibilityCheckType checkType, int numNodes) {
        if (!results.containsKey(checkType)) {
            results.put(checkType, new AccessibilityCheckData(checkType));
        }
        AccessibilityCheckData checkData = results.get(checkType);
        checkData.storeCheckResults(checkResults, numNodes);
    }

    public void printResults() {
        Log.i(TAG, "package name: " + packageName);
        for (AccessibilityCheckType check : results.keySet()) {
            AccessibilityCheckData checkResults = results.get(check);
            checkResults.printCheckResults();
        }
    }

    public JSONObject getJSONResult() {
        try {
            JSONObject packageJSONResult = new JSONObject();
            packageJSONResult.put("packageName", packageName);

            JSONArray checksJSONList = new JSONArray();
            for (AccessibilityCheckType check : results.keySet()) {
                AccessibilityCheckData checkData = results.get(check);
                JSONObject checkJSON = new JSONObject();
                checkJSON.put("checkType", check.name());
                checkJSON.put("numNodesChecked", checkData.numNodesChecked);
                checkJSON.put("totalErrors", checkData.numErrors);
                JSONArray messagesJSONList = new JSONArray();
                for (CharSequence message : checkData.messageToCounts.keySet()) {
                    JSONObject messageAndCount = new JSONObject();
                    messageAndCount.put("message", message);
                    messageAndCount.put("count", checkData.messageToCounts.get(message));
                    messagesJSONList.put(messageAndCount);
                }
                checkJSON.put("messages", messagesJSONList);
                checksJSONList.put(checkJSON);
            }
            packageJSONResult.put("checks", checksJSONList);
            return packageJSONResult;
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }


}
