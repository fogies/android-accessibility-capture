package uw.AccessibilityReport;

import com.google.android.apps.common.testing.accessibility.framework.AccessibilityInfoCheckResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccessibilityTestData {
    Map<CharSequence, PackageChecks> packageResults; //(package name) -> (accessibility check results)

    public AccessibilityTestData() {
        packageResults = new HashMap<>();
    }

    public void storeCheckResults(CharSequence packageName, List<AccessibilityInfoCheckResult> results,
                                            AccessibilityTestService.AccessibilityCheckType checkType,
                                            int numNodes) {
        if (!packageResults.containsKey(packageName)) {
            packageResults.put(packageName, new PackageChecks(packageName));
        }
        PackageChecks packageChecks = packageResults.get(packageName);
        packageChecks.storeCheckResults(results, checkType, numNodes);
        // add the checks

    }

    public String resultsToJSON() {
        JSONArray results = new JSONArray();
        for (CharSequence packageName : packageResults.keySet()) {
            JSONObject packageJSON = packageResults.get(packageName).getJSONResult();
            results.put(packageJSON);
        }
        return results.toString();
    }


    public void printResults() {
        for (CharSequence packageName : packageResults.keySet()) {
            packageResults.get(packageName).printResults();
        }
    }
}
