package xyz.heart.sms.shared.util.billing

interface PurchasedItemCallback {
    fun onItemPurchased(productId: String)
    fun onPurchaseError(message: String)
}
