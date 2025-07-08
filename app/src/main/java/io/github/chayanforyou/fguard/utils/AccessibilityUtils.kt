package io.github.chayanforyou.fguard.utils

import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings

fun isAccessibilityServiceEnabled(
    context: Context,
    serviceClass: Class<out AccessibilityService>
): Boolean {
    val serviceName = ComponentName(context, serviceClass).flattenToString()
    val enabledServices = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
    ) ?: return false
    val isAccessibilityEnabled = Settings.Secure.getInt(
        context.contentResolver,
        Settings.Secure.ACCESSIBILITY_ENABLED,
        0
    )
    return isAccessibilityEnabled == 1 && enabledServices.contains(serviceName)
}

fun openAccessibilityServiceScreen(context: Context, serviceClass: Class<*>) {
    try {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        val componentName = ComponentName(context.packageName, serviceClass.name)
        intent.putExtra(":settings:fragment_args_key", componentName.flattenToString())

        val bundle = Bundle()
        bundle.putString(":settings:fragment_args_key", componentName.flattenToString())
        intent.putExtra(":settings:show_fragment_args", bundle)

        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
        // Fallback to general Accessibility Settings
        context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }
}