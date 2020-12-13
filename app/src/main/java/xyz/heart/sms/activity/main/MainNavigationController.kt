package xyz.heart.sms.activity.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import xyz.heart.sms.R
import xyz.heart.sms.activity.MessengerActivity
import xyz.heart.sms.api.implementation.Account
import xyz.heart.sms.fragment.conversation.ConversationListFragment
import xyz.heart.sms.fragment.message.MessageListFragment
import xyz.heart.sms.shared.MessengerActivityExtras
import xyz.heart.sms.shared.data.Settings
import xyz.heart.sms.shared.service.ApiDownloadService
import xyz.heart.sms.shared.util.ColorUtils
import xyz.heart.sms.shared.util.PhoneNumberUtils
import xyz.heart.sms.shared.util.StringUtils
import xyz.heart.sms.shared.util.listener.BackPressedListener

@Suppress("DEPRECATION")
class MainNavigationController(private val activity: MessengerActivity)
    : NavigationView.OnNavigationItemSelectedListener {

    val conversationActionDelegate = MainNavigationConversationListActionDelegate(activity)
    val messageActionDelegate = MainNavigationMessageListActionDelegate(activity)

    val navigationView: NavigationView by lazy { activity.findViewById<View>(R.id.navigation_view) as NavigationView }
    val drawerLayout: DrawerLayout? by lazy { activity.findViewById<View>(R.id.drawer_layout) as DrawerLayout? }

    var conversationListFragment: ConversationListFragment? = null
    var otherFragment: Fragment? = null
    var inSettings = false
    var selectedNavigationItemId: Int = R.id.drawer_conversation

    fun isConversationListExpanded() = conversationListFragment != null && conversationListFragment!!.isExpanded
    fun isOtherFragmentConvoAndShowing() = otherFragment != null && otherFragment is ConversationListFragment && (otherFragment as ConversationListFragment).isExpanded
    fun getShownConversationList() = when {
        isOtherFragmentConvoAndShowing() -> otherFragment as ConversationListFragment
        else -> conversationListFragment
    }

    fun initDrawer() {
        activity.insetController.overrideDrawerInsets()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.postDelayed({
            try {
                if (Account.exists()) {
                    (activity.findViewById<View>(R.id.drawer_header_my_name) as TextView).text = Account.myName
                }

                (activity.findViewById<View>(R.id.drawer_header_my_phone_number) as TextView).text =
                        PhoneNumberUtils.format(PhoneNumberUtils.getMyPhoneNumber(activity))

                if (!ColorUtils.isColorDark(Settings.mainColorSet.colorDark)) {
                    (activity.findViewById<View>(R.id.drawer_header_my_name) as TextView)
                            .setTextColor(activity.resources.getColor(R.color.lightToolbarTextColor))
                    (activity.findViewById<View>(R.id.drawer_header_my_phone_number) as TextView)
                            .setTextColor(activity.resources.getColor(R.color.lightToolbarTextColor))
                }

                // change the text to
                if (!Account.exists()) {
                    navigationView.menu.findItem(R.id.drawer_account).setTitle(R.string.menu_device_texting)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            activity.snoozeController.initSnooze()
        }, 300)
    }

    fun initToolbarTitleClick() {
        activity.toolbar.setOnClickListener {
            val otherFrag = otherFragment
            val fragment = when {
                conversationListFragment != null -> conversationListFragment
                otherFrag is ConversationListFragment -> otherFrag
                else -> return@setOnClickListener
            }

            fragment?.recyclerView?.smoothScrollToPosition(0)
        }
    }

    fun openDrawer(): Boolean {
        if (drawerLayout != null && !drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.openDrawer(GravityCompat.START)
            return true
        }

        return false
    }

    fun closeDrawer(): Boolean {
        if (drawerLayout != null && drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            return true
        }

        return false
    }

    fun backPressed(): Boolean {
        val fragments = activity.supportFragmentManager.fragments

        fragments
                .filter { it is BackPressedListener && (it as BackPressedListener).onBackPressed() }
                .forEach { return true }

        when {
            conversationListFragment == null -> {
                val messageListFragment = findMessageListFragment()
                if (messageListFragment != null) {
                    try {
                        activity.supportFragmentManager.beginTransaction().remove(messageListFragment).commit()
                    } catch (e: Exception) {
                    }
                }

                conversationActionDelegate.displayConversations()
                activity.fab.show()
                drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                return true
            }
            inSettings -> {
                onNavigationItemSelected(R.id.drawer_conversation)
                return true
            }
            else -> return false
        }
    }

    fun findMessageListFragment(): MessageListFragment? =
            activity.supportFragmentManager.findFragmentById(R.id.message_list_container) as? MessageListFragment

    fun drawerItemClicked(id: Int): Boolean {
        conversationListFragment?.swipeHelper?.dismissSnackbars()

        when (id) {
            R.id.drawer_conversation -> return conversationActionDelegate.displayConversations()
            R.id.drawer_archived -> return conversationActionDelegate.displayArchived()
            R.id.drawer_private -> return conversationActionDelegate.displayPrivate()
            R.id.drawer_unread -> return conversationActionDelegate.displayUnread()
            R.id.drawer_schedule -> return conversationActionDelegate.displayScheduledMessages()
            R.id.drawer_mute_contacts -> return conversationActionDelegate.displayBlacklist()
            R.id.drawer_invite -> return conversationActionDelegate.displayInviteFriends()
            R.id.drawer_feature_settings -> return conversationActionDelegate.displayFeatureSettings()
            R.id.drawer_settings -> return conversationActionDelegate.displaySettings()
            R.id.drawer_account -> return conversationActionDelegate.displayMyAccount()
            R.id.drawer_help -> return conversationActionDelegate.displayHelpAndFeedback()
            R.id.drawer_about -> return conversationActionDelegate.displayAbout()
            R.id.drawer_edit_folders -> return conversationActionDelegate.displayEditFolders()
            R.id.menu_view_contact, R.id.drawer_view_contact -> return messageActionDelegate.viewContact()
            R.id.menu_view_media, R.id.drawer_view_media -> return messageActionDelegate.viewMedia()
            R.id.menu_delete_conversation, R.id.drawer_delete_conversation -> return messageActionDelegate.deleteConversation()
            R.id.menu_archive_conversation, R.id.drawer_archive_conversation -> return messageActionDelegate.archiveConversation()
            R.id.menu_conversation_information, R.id.drawer_conversation_information -> return messageActionDelegate.conversationInformation()
            R.id.menu_conversation_blacklist, R.id.drawer_conversation_blacklist -> return messageActionDelegate.conversationBlacklist()
            R.id.menu_conversation_blacklist_all, R.id.drawer_conversation_blacklist_all -> return messageActionDelegate.conversationBlacklistAll()
            R.id.menu_conversation_schedule, R.id.drawer_conversation_schedule -> return messageActionDelegate.conversationSchedule()
            R.id.menu_contact_settings, R.id.drawer_contact_settings -> return messageActionDelegate.contactSettings()
            R.id.menu_call_with_duo -> return messageActionDelegate.callWithDuo()
            R.id.menu_show_bubble -> return messageActionDelegate.showBubble()
            R.id.menu_call -> return if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                messageActionDelegate.callContact()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), MessengerActivityExtras.REQUEST_CALL_PERMISSION)
                    false
                } else {
                    messageActionDelegate.callContact()
                }
            }

            else -> {
                val folder = activity.drawerItemHelper.findFolder(id)
                return if (folder != null) {
                    conversationActionDelegate.displayFolder(folder)
                } else {
                    true
                }
            }
        }
    }

    fun optionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            openDrawer()
            true
        }
        R.id.menu_search -> true
        else -> false
    }

    fun onNavigationItemSelected(itemId: Int) {
        val item = navigationView.menu.findItem(itemId)
        if (item != null) {
            onNavigationItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        closeDrawer()
        selectedNavigationItemId = item.itemId

        if (item.isChecked || ApiDownloadService.IS_RUNNING) {
            return true
        }

        if (item.isCheckable) {
            item.isChecked = true
        }

        if (item.itemId == R.id.drawer_conversation) {
            activity.setTitle(R.string.app_title)
        } else if (item.isCheckable) {
            activity.title = StringUtils.titleize(item.title.toString())
        }

        return drawerItemClicked(item.itemId)
    }
}