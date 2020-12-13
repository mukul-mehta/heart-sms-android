package xyz.heart.sms.utils.swipe_to_dismiss.actions

import xyz.heart.sms.adapter.conversation.ConversationListAdapter

abstract class BaseSwipeAction {

    abstract fun getBackgroundColor(): Int
    abstract fun getIcon(): Int
    abstract fun onPerform(listener: ConversationListAdapter, index: Int)

}