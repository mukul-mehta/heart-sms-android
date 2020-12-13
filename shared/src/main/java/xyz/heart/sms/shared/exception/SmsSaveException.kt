package xyz.heart.sms.shared.exception

class SmsSaveException(exception: Exception) : IllegalStateException(exception.message)