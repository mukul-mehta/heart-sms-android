package xyz.heart.sms.shared.service

import xyz.heart.sms.shared.util.billing.ProductPurchased

class SimpleLifetimeSubscriptionCheckService : SimpleSubscriptionCheckService() {
    override fun handleBestProduct(best: ProductPurchased) {
        if (best.productId == "lifetime") {
            writeLifetimeSubscriber()
        }
    }
}
