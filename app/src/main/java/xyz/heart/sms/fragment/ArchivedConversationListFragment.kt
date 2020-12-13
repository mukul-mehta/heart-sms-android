package xyz.heart.sms.fragment

import android.view.View
import com.google.android.material.navigation.NavigationView

import xyz.heart.sms.R
import xyz.heart.sms.activity.MessengerActivity
import xyz.heart.sms.adapter.view_holder.ConversationViewHolder
import xyz.heart.sms.fragment.conversation.ConversationListFragment

class ArchivedConversationListFragment : ConversationListFragment() {

    override fun noConversationsText() = getString(R.string.no_archived_messages_description)

    // always consume the back event and send us to the conversation list
    override fun onBackPressed(): Boolean {
        if (!super.onBackPressed()) {
            val navView = activity?.findViewById<View>(R.id.navigation_view) as NavigationView?
            navView?.menu?.findItem(R.id.drawer_conversation)?.isChecked = true

            activity?.title = getString(R.string.app_title)
            (activity as MessengerActivity).displayConversations()
        }

        return true
    }

    override fun onConversationContracted(viewHolder: ConversationViewHolder) {
        super.onConversationContracted(viewHolder)

        val navView = activity?.findViewById<View>(R.id.navigation_view) as NavigationView?
        navView?.menu?.findItem(R.id.drawer_archived)?.isChecked = true
    }
}
