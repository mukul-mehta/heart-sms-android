package xyz.heart.sms.utils.swipe_to_dismiss.actions

import android.graphics.Color
import xyz.heart.sms.R
import xyz.heart.sms.adapter.conversation.ConversationListAdapter

class SwipeNoAction : BaseSwipeAction() {

    override fun getIcon() = R.drawable.ic_back
    override fun getBackgroundColor() = Color.TRANSPARENT
    override fun onPerform(listener: ConversationListAdapter, index: Int) { }

}