# android-accessibility-capture

### How to capture
1. Press "ACTIVATE ACCESSIBILITY" button and enable our service "Accessibility Report"
2. Set package name of the app you want to test by <b>"./adb shell am broadcast -a xiaoyiz.setPackage --es packageName xxx.Package.Name"</b>
3. Launch the app you want to test, and start app traversal
4. Pull accessibility tree or errors of current screen while traversal
5. Use "./adb pull fileLocation" to get saved log

### Pull accessibility tree of current screen
<b>./adb shell am broadcast -a xiaoyiz.pullCurrentTree</b> will print in command line

<b>./adb shell am broadcast -a xiaoyiz.pullCurrentTree --es fileName xxxFileName</b> will save file in local storage /storage/emulated/0/xxx.Package.Name/<b>Tree</b>/xxxFileName

### Pull accessibility errors of current screen
<b>./adb shell am broadcast -a xiaoyiz.pullCurrentErrors</b> will print in command line

<b>./adb shell am broadcast -a xiaoyiz.pullCurrentErrors --es fileName xxxFileName</b> will save file in local storage /storage/emulated/0/xxx.Package.Name/<b>Error</b>/xxxFileName

### Pull an element on screen
1. Use "\ " as space between words
2. Use another --es for additional parameter

<b>./adb shell am broadcast -a xiaoyiz.pullElementInfo --es text "Account\ setup"</b>

<b>./adb shell am broadcast -a xiaoyiz.pullElementInfo --es altText "Analog\ clock"</b>

<b>./adb shell am broadcast -a xiaoyiz.pullElementInfo --es viewId android:id/action_bar_title</b>

<b>./adb shell am broadcast -a xiaoyiz.pullElementInfo --es locationX 40 --es locationY 50</b>

Will print in command line


### See log output in command line
##### adb logcat *:I | grep AccessibilityService

### Sample output:
#### pullCurrentTree
android.view.accessibility.AccessibilityNodeInfo@8000e4ed; boundsInParent: Rect(0, 0 - 168, 168); boundsInScreen: Rect(0, 72 - 168, 240); packageName: com.offerup; className: android.widget.ImageButton; text: null; error: null; maxTextLength: -1; contentDescription: Navigate up; viewIdResName: null; checkable: false; checked: false; focusable: true; focused: false; selected: false; clickable: true; longClickable: false; contextClickable: false; enabled: true; password: false; scrollable: false; actions: [AccessibilityAction: ACTION_FOCUS - null, AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_CLICK - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_NEXT_AT_MOVEMENT_GRANULARITY - null, AccessibilityAction: ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY - null, AccessibilityAction: ACTION_SET_SELECTION - null, AccessibilityAction: ACTION_UNKNOWN - null]

android.view.accessibility.AccessibilityNodeInfo@8000ac9e; boundsInParent: Rect(0, 0 - 132, 144); boundsInScreen: Rect(564, 84 - 696, 228); packageName: com.offerup; className: android.widget.TextView; text: null; error: null; maxTextLength: -1; contentDescription: Search; viewIdResName: com.offerup:id/search_item; checkable: false; checked: false; focusable: true; focused: false; selected: false; clickable: true; longClickable: true; contextClickable: false; enabled: true; password: false; scrollable: false; actions: [AccessibilityAction: ACTION_FOCUS - null, AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_CLICK - null, AccessibilityAction: ACTION_LONG_CLICK - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_UNKNOWN - null]

#### pullCurrentErrors
android.view.accessibility.AccessibilityNodeInfo@fbde; boundsInParent: Rect(0, 0 - 238, 56); boundsInScreen: Rect(482, 70 - 720, 126); packageName: com.android.settings; className: android.widget.Switch; text: ON; contentDescription: null; viewIdResName: null; checkable: true; checked: true; focusable: true; focused: false; selected: false; clickable: true; longClickable: false; enabled: true; password: false; scrollable: false; [ACTION_FOCUS, ACTION_SELECT, ACTION_CLEAR_SELECTION, ACTION_CLICK, ACTION_ACCESSIBILITY_FOCUS]
class com.google.android.apps.common.testing.accessibility.framework.TouchTargetSizeInfoCheck
View is too small of a touch target. Minimum touch target size is 48x48dp. Actual size is 119.0x28.0dp (screen density is 2.0).

android.view.accessibility.AccessibilityNodeInfo@d649; boundsInParent: Rect(0, 0 - 680, 80); boundsInScreen: Rect(20, 70 - 700, 150); packageName: com.android.settings; className: android.widget.LinearLayout; text: null; contentDescription: Search; viewIdResName: com.android.settings:id/search_button_container; checkable: false; checked: false; focusable: true; focused: false; selected: false; clickable: true; longClickable: false; enabled: true; password: false; scrollable: false; [ACTION_FOCUS, ACTION_SELECT, ACTION_CLEAR_SELECTION, ACTION_CLICK, ACTION_ACCESSIBILITY_FOCUS, ACTION_NEXT_AT_MOVEMENT_GRANULARITY, ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY, ACTION_SET_SELECTION]
class com.google.android.apps.common.testing.accessibility.framework.TouchTargetSizeInfoCheck
View is too small of a touch target. Minimum touch target size is 48x48dp. Actual size is 340.0x40.0dp (screen density is 2.0).

#### pullElementInfo
android.view.accessibility.AccessibilityNodeInfo@fbde; boundsInParent: Rect(0, 0 - 238, 56); boundsInScreen: Rect(482, 70 - 720, 126); packageName: com.android.settings; className: android.widget.Switch; text: ON; contentDescription: null; viewIdResName: null; checkable: true; checked: true; focusable: true; focused: false; selected: false; clickable: true; longClickable: false; enabled: true; password: false; scrollable: false; [ACTION_FOCUS, ACTION_SELECT, ACTION_CLEAR_SELECTION, ACTION_CLICK, ACTION_ACCESSIBILITY_FOCUS]
