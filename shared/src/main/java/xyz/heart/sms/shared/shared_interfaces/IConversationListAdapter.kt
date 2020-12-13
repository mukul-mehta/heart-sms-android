package xyz.heart.sms.shared.shared_interfaces

import xyz.heart.sms.shared.data.SectionType
import xyz.heart.sms.shared.data.model.Conversation
import xyz.heart.sms.shared.data.pojo.ReorderType

interface IConversationListAdapter {

    val conversations: MutableList<Conversation>
    val sectionCounts: MutableList<SectionType>

    fun findPositionForConversationId(id: Long): Int
    fun getCountForSection(sectionType: Int): Int
    fun removeItem(position: Int, type: ReorderType): Boolean

    fun notifyItemChanged(position: Int)
    fun notifyItemRangeInserted(start: Int, end: Int)
    fun notifyItemInserted(item: Int)
}
