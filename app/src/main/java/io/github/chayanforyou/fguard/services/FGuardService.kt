package io.github.chayanforyou.fguard.services

import android.os.SystemClock
import android.view.accessibility.AccessibilityEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore

class FGuardService : BaseBlockingService() {

    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val workSemaphore = Semaphore(2)
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
        if (!workSemaphore.tryAcquire()) return

        coroutineScope.launch {
            try {
                performGlobalAction(GLOBAL_ACTION_BACK)
            } finally {
                workSemaphore.release()
            }
        }

        lastEventTimeStamp = SystemClock.uptimeMillis()
    }

    private fun handleBlockedClickEvent() {
        if (!workSemaphore.tryAcquire()) return

        coroutineScope.launch {
            try {
                delay(200)
                performGlobalAction(GLOBAL_ACTION_BACK)
            } finally {
                workSemaphore.release()
            }
        }
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}