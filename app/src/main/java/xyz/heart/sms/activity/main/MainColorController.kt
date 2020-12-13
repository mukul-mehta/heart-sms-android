package xyz.heart.sms.activity.main

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import xyz.heart.sms.R
import xyz.heart.sms.activity.MessengerActivity
import xyz.heart.sms.shared.data.Settings
import xyz.heart.sms.shared.data.pojo.BaseTheme
import xyz.heart.sms.shared.util.ActivityUtils
import xyz.heart.sms.shared.util.ColorUtils
import xyz.heart.sms.shared.util.TimeUtils

class MainColorController(private val activity: AppCompatActivity) {

    private val toolbar: Toolbar by lazy { activity.findViewById<View>(R.id.toolbar) as Toolbar }
    private val fab: FloatingActionButton by lazy { activity.findViewById<View>(R.id.fab) as FloatingActionButton }
    private val navigationView: NavigationView by lazy { activity.findViewById<View>(R.id.navigation_view) as NavigationView }
    private val conversationListContainer: View by lazy { activity.findViewById<View>(R.id.conversation_list_container) }

    fun colorActivity() {
        ColorUtils.checkBlackBackground(activity)
        ActivityUtils.setTaskDescription(activity)

        if (!Build.FINGERPRINT.contains("robolectric")) {
            TimeUtils.setupNightTheme(activity)
        }
    }

    fun configureGlobalColors() {
        if (Settings.isCurrentlyDarkTheme(activity)) {
            activity.window.navigationBarColor = Color.BLACK
        }

        toolbar.setBackgroundColor(Settings.mainColorSet.color)
        fab.backgroundTintList = ColorStateList.valueOf(Settings.mainColorSet.colorAccent)

        val states = arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked))

        val baseColor = if (activity.resources.getBoolean(R.bool.is_night)) "FFFFFF" else "000000"
        val iconColors = intArrayOf(Color.parseColor("#77$baseColor"), Settings.mainColorSet.colorAccent)
        val textColors = intArrayOf(Color.parseColor("#DD$baseColor"), Settings.mainColorSet.colorAccent)

        navigationView.itemIconTintList = ColorStateList(states, iconColors)
        navigationView.itemTextColor = ColorStateList(states, textColors)
        navigationView.post {
            ColorUtils.adjustStatusBarColor(Settings.mainColorSet.color, Settings.mainColorSet.colorDark, activity)

            val header = navigationView.findViewById<View>(R.id.header)
            header?.setBackgroundColor(Settings.mainColorSet.colorDark)
        }

        if (Settings.baseTheme == BaseTheme.BLACK) {
            conversationListContainer.setBackgroundColor(Color.BLACK)
        }
    }

    fun configureNavigationBarColor() {
        val isMessengerActivity = activity is MessengerActivity
        when {
            Settings.baseTheme == BaseTheme.BLACK -> ActivityUtils.setUpNavigationBarColor(activity, Color.BLACK, isMessengerActivity)
            Settings.isCurrentlyDarkTheme(activity) -> ActivityUtils.setUpNavigationBarColor(activity, -1223, isMessengerActivity) // random. the activity utils will handle the dark color
            else -> ActivityUtils.setUpNavigationBarColor(activity, Color.WHITE, isMessengerActivity)
        }
    }
}