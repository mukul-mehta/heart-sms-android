package xyz.heart.sms.fragment

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.dragselectrecyclerview.DragSelectRecyclerView
import xyz.heart.sms.R
import xyz.heart.sms.activity.ImageViewerActivity
import xyz.heart.sms.activity.MediaGridActivity
import xyz.heart.sms.adapter.MediaGridAdapter
import xyz.heart.sms.shared.activity.AbstractSettingsActivity
import xyz.heart.sms.shared.data.DataSource
import xyz.heart.sms.shared.data.MediaMessage
import xyz.heart.sms.shared.data.Settings
import xyz.heart.sms.shared.data.model.Conversation
import xyz.heart.sms.shared.data.model.Message
import xyz.heart.sms.shared.util.ActivityUtils
import xyz.heart.sms.shared.util.listener.MediaSelectedListener
import java.lang.Exception

class MediaGridFragment : Fragment(), MediaSelectedListener {

    private val fragmentActivity: Activity? by lazy { activity }

    val conversation: Conversation? by lazy { DataSource.getConversation(fragmentActivity!!, arguments.getLong(ARG_CONVERSATION_ID)) }
    private val messages: List<Message> by lazy { DataSource.getMediaMessages(fragmentActivity!!, conversation!!.id) }

    private var selectIsActive = false
    private var recyclerView: DragSelectRecyclerView? = null
    private var adapter: MediaGridAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            setUpToolbar()
        } catch (e: Exception) {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_media_grid, container, false)
        recyclerView = root.findViewById<View>(R.id.recycler_view) as DragSelectRecyclerView
        adapter = MediaGridAdapter(messages, this)

        recyclerView?.layoutManager = GridLayoutManager(fragmentActivity, resources.getInteger(R.integer.images_column_count))
        recyclerView?.adapter = adapter!!

        return root
    }

    private fun setUpToolbar() {
        fragmentActivity?.title = conversation!!.title

        val toolbar = (fragmentActivity as AbstractSettingsActivity).toolbar
        if (Settings.useGlobalThemeColor) {
            toolbar?.setBackgroundColor(Settings.mainColorSet.color)
            ActivityUtils.setStatusBarColor(fragmentActivity, Settings.mainColorSet.colorDark, Settings.mainColorSet.color)
        } else {
            toolbar?.setBackgroundColor(conversation!!.colors.color)
            ActivityUtils.setStatusBarColor(fragmentActivity, conversation!!.colors.colorDark, Settings.mainColorSet.color)
        }
    }

    override fun onSelected(messageList: List<MediaMessage>, selectedPosition: Int) {
        if (selectIsActive) {
            adapter?.setSelected(selectedPosition, !messageList[selectedPosition].selected)

            if (!messageList.any { it.selected }) {
                activateSelectMode(false)
            }
        } else {
            val intent = Intent(fragmentActivity, ImageViewerActivity::class.java)
            intent.putExtra(ImageViewerActivity.EXTRA_CONVERSATION_ID, arguments.getLong(ARG_CONVERSATION_ID))
            intent.putExtra(ImageViewerActivity.EXTRA_MESSAGE_ID, messageList[selectedPosition].message.id)
            startActivity(intent)
        }
    }

    override fun onStartDrag(index: Int) {
        activateSelectMode(true)
        recyclerView?.setDragSelectActive(true, index)
    }

    fun destroyActionMode() {
        adapter?.messages?.forEach { it.selected = false }
        adapter?.notifyDataSetChanged()
    }

    fun deleteSelected() {
        adapter?.messages
                ?.filter { it.selected }
                ?.forEach { DataSource.deleteMessage(fragmentActivity!!, it.message.id) }

        if (adapter != null && adapter?.messages != null) {
            adapter?.messages = adapter?.messages?.filter { !it.selected }!!
            adapter?.notifyDataSetChanged()
        }

        selectIsActive = false
    }

    private fun activateSelectMode(activate: Boolean) {
        selectIsActive = activate

        if (activate) {
            (fragmentActivity as MediaGridActivity).startMultiSelect()
        } else {
            (fragmentActivity as MediaGridActivity).stopMultiSelect()
            destroyActionMode()
        }
    }

    companion object {
        private val ARG_CONVERSATION_ID = "conversation_id"

        fun newInstance(conversationId: Long): MediaGridFragment {
            val fragment = MediaGridFragment()
            val args = Bundle()
            args.putLong(ARG_CONVERSATION_ID, conversationId)
            fragment.arguments = args

            return fragment
        }
    }
}
