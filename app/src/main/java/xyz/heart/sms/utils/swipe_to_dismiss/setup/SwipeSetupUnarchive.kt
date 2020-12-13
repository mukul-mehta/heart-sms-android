package xyz.heart.sms.utils.swipe_to_dismiss.setup

import xyz.heart.sms.adapter.conversation.ConversationListAdapter
import xyz.heart.sms.utils.swipe_to_dismiss.actions.BaseSwipeAction
import xyz.heart.sms.utils.swipe_to_dismiss.actions.SwipeDeleteAction
import xyz.heart.sms.utils.swipe_to_dismiss.actions.SwipeUnarchiveAction

@Suppress("DEPRECATION")
class SwipeSetupUnarchive(adapter: ConversationListAdapter) : SwipeSetupBase(adapter) {

    override fun getLeftToRightAction(): BaseSwipeAction {
        return SwipeUnarchiveAction()
    }

    override fun getRightToLeftAction(): BaseSwipeAction {
        return SwipeDeleteAction()
    }

}
