package xyz.heart.sms.api.implementation

interface AccountInvalidator {

    fun onAccountInvalidated(account: Account)

}