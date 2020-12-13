package xyz.heart.sms.utils.swipe_to_dismiss.actions

import xyz.heart.sms.R
import xyz.heart.sms.adapter.conversation.ConversationListAdapter
import xyz.heart.sms.shared.data.Settings

class SwipeDeleteAction : BaseSwipeAction() {

    override fun getIcon() = R.drawable.ic_delete_sweep
    override fun getBackgroundColor() = Settings.mainColorSet.colorAccent
    override fun onPerform(listener: ConversationListAdapter, index: Int) {
        listener.deleteItem(index)
    }

}