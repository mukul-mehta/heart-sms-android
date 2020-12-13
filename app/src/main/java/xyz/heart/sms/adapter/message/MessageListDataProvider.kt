package xyz.heart.sms.adapter.message

import android.content.Context
import android.database.Cursor
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import xyz.heart.sms.R
import xyz.heart.sms.activity.MessengerActivity
import xyz.heart.sms.fragment.message.MessageListFragment
import xyz.heart.sms.fragment.message.load.MessageListLoader
import xyz.heart.sms.shared.data.DataSource
import xyz.heart.sms.shared.data.model.Message
import xyz.heart.sms.shared.util.CursorUtil
import xyz.heart.sms.shared.util.DensityUtil
import xyz.heart.sms.shared.util.SnackbarAnimationFix

class MessageListDataProvider(private val adapter: MessageListAdapter, private val fragment: MessageListFragment,
                              initialCursor: Cursor) {

    private val activity: MessengerActivity? by lazy { fragment.activity as MessengerActivity }

    var messages = initialCursor

    fun addMessage(recycler: RecyclerView, newMessages: Cursor) {
        val initialCount = adapter.itemCount

        CursorUtil.closeSilent(messages)
        messages = newMessages

        val finalCount = adapter.itemCount

        if (initialCount == finalCount) {
            // message was probably marked as sent
            adapter.notifyItemChanged(finalCount - 1)
        } else if (initialCount > finalCount) {
            // deleted a message
            adapter.notifyDataSetChanged()
        } else {
            if (finalCount - 2 >= 0) {
                // with the new paddings, we need to notify the second to last item too
                adapter.notifyItemChanged(finalCount - 2)
            }

            adapter.notifyItemInserted(finalCount - 1)

            if (Math.abs((recycler.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() - initialCount) < 4) {
                // near the bottom, scroll to the new item
                recycler.layoutManager?.scrollToPosition(finalCount - 1)
            } else if (messages.moveToLast()) {
                val message = Message()
                message.fillFromCursor(messages)
                if (message.type == Message.TYPE_RECEIVED) {
                    val text = recycler.context.getString(R.string.new_message)
                    adapter.snackbar = Snackbar
                            .make(recycler, text, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.read) { recycler.layoutManager?.scrollToPosition(finalCount - 1) }

                    try {
                        (adapter.snackbar!!.view.layoutParams as CoordinatorLayout.LayoutParams)
                                .bottomMargin = DensityUtil.toDp(recycler.context, 56)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    SnackbarAnimationFix.apply(adapter.snackbar!!)
                    adapter.snackbar!!.show()
                }
            }
        }
    }

    fun onMessageDeleted(context: Context, conversationId: Long, position: Int) {
        val source = DataSource

        val messageList = source.getMessages(context, conversationId, 1)
        if (messageList.isEmpty() && activity != null) {
            activity!!.navController.drawerItemClicked(R.id.menu_delete_conversation)
        } else {
            val message = messageList[0]

            val conversation = source.getConversation(context, conversationId)
            source.updateConversation(context, conversationId, true, message.timestamp,
                    if (message.type == Message.TYPE_SENT || message.type == Message.TYPE_SENDING)
                        context.getString(R.string.you) + ": " + message.data else message.data,
                    message.mimeType, conversation != null && conversation.archive)

            fragment.setConversationUpdateInfo(if (message.type == Message.TYPE_SENDING)
                context.getString(R.string.you) + ": " + message.data else message.data!!)
        }
    }
}