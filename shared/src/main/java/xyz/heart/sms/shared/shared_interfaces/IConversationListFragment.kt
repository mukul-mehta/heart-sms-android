package xyz.heart.sms.shared.shared_interfaces

interface IConversationListFragment {

    val isFragmentAdded: Boolean
    val expandedId: Long
    val adapter: IConversationListAdapter?

    fun checkEmptyViewDisplay()
}
