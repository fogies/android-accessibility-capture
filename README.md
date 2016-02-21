# android-accessibility-capture

### How to start capture
1. Enter package name (e.g. com.offerup) in app screen.
2. Press "SET PACKAGE" button
3. Press "ACTIVATE ACCESSIBILITY" button and enable our service
4. Launch the app you want to test, and start app traversal

### See log output in commandline
##### adb logcat *:I | grep AccessibilityService
If you only want to see tree result, you can use TREE_RESULT to filter

If you only want to see checker result, you can use CHECKER_RESULT to filter


### Sample output:
Name -> com.offerup, screen 15

TREE_RESULT -> Current Node = android.view.accessibility.AccessibilityNodeInfo@8000e4ed; boundsInParent: Rect(0, 0 - 168, 168); boundsInScreen: Rect(0, 72 - 168, 240); packageName: com.offerup; className: android.widget.ImageButton; text: null; error: null; maxTextLength: -1; contentDescription: Navigate up; viewIdResName: null; checkable: false; checked: false; focusable: true; focused: false; selected: false; clickable: true; longClickable: false; contextClickable: false; enabled: true; password: false; scrollable: false; actions: [AccessibilityAction: ACTION_FOCUS - null, AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_CLICK - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_NEXT_AT_MOVEMENT_GRANULARITY - null, AccessibilityAction: ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY - null, AccessibilityAction: ACTION_SET_SELECTION - null, AccessibilityAction: ACTION_UNKNOWN - null]

Node Center Coordinate -> x = 84, y = 156

TREE_RESULT -> Current Node = android.view.accessibility.AccessibilityNodeInfo@8000ac9e; boundsInParent: Rect(0, 0 - 132, 144); boundsInScreen: Rect(564, 84 - 696, 228); packageName: com.offerup; className: android.widget.TextView; text: null; error: null; maxTextLength: -1; contentDescription: Search; viewIdResName: com.offerup:id/search_item; checkable: false; checked: false; focusable: true; focused: false; selected: false; clickable: true; longClickable: true; contextClickable: false; enabled: true; password: false; scrollable: false; actions: [AccessibilityAction: ACTION_FOCUS - null, AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_CLICK - null, AccessibilityAction: ACTION_LONG_CLICK - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_UNKNOWN - null]

Node Center Coordinate -> x = 630, y = 156

...

CHECKER_RESULT -> [{"packageName":"com.offerup","checks":[{"checkType":"DUPLICATE_CLICKABLE_BOUNDS","numNodesChecked":25,"totalErrors":0,"messages":[]}]}]

CHECKER_RESULT -> [{"packageName":"com.offerup","checks":[{"checkType":"TOUCH_TARGET_SIZE","numNodesChecked":19,"totalErrors":5,"messages":[{"message":"View is too small of a touch target. Minimum touch target size is 48x48dp.","count":1}]}]}]

CHECKER_RESULT -> [{"packageName":"com.offerup","checks":[{"checkType":"SPEAKABLE_TEXT_PRESENT","numNodesChecked":20,"totalErrors":13,"messages":[{"message":"View is missing speakable text needed for a screen reader","count":13}]}]}]

CHECKER_RESULT -> [{"packageName":"com.offerup","checks":[{"checkType":"EDITABLE_CONTENT_DESC","numNodesChecked":0,"totalErrors":0,"messages":[]}]}]

CHECKER_RESULT -> [{"packageName":"com.offerup","checks":[{"checkType":"CLICKABLE_SPAN","numNodesChecked":5,"totalErrors":0,"messages":[]}]}]

CHECKER_RESULT -> [{"packageName":"com.offerup","checks":[{"checkType":"EDITABLE_CONTENT_DESC","numNodesChecked":5,"totalErrors":0,"messages":[]}]}]