package io.github.chayanforyou.fguard.services

import android.os.SystemClock
import android.view.accessibility.AccessibilityEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class FGuardService : BaseBlockingService() {

    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val mutex = Mutex()
    private var lastEventTimeStamp = 0L

    private val monitoredApps = hashSetOf(
        "com.facebook.katana",
    )

    private val blockedDescriptions = hashSetOf(
        "Facebook App",
    )

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.source == null) return

        val root = rootInActiveWindow ?: return
        val packageName = root.packageName?.toString() ?: return

        if (monitoredApps.contains(packageName)) {
            handleMonitoredAppEvent()
        }

        if (event.eventType == AccessibilityEvent.TYPE_VIEW_CLICKED) {
            val description = event.source?.contentDescription?.toString() ?: return
            if (blockedDescriptions.contains(description)) {
                handleBlockedClickEvent()
            }
        }
    }

    private fun handleMonitoredAppEvent() {
        if (!isDelayOver(lastEventTimeStamp, 2000)) return

        coroutineScope.launch {
            mutex.withLock {
                performGlobalAction(GLOBAL_ACTION_BACK)
                lastEventTimeStamp = SystemClock.uptimeMillis()
            }
        }
    }

    private fun handleBlockedClickEvent() {
        coroutineScope.launch {
            mutex.withLock {
                delay(200)
                performGlobalAction(GLOBAL_ACTION_BACK)
            }
        }
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}