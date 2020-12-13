package xyz.heart.sms.shared.data.model

import android.database.Cursor
import xyz.heart.sms.encryption.EncryptionUtils


interface DatabaseTable {

    fun getCreateStatement(): String

    fun getTableName(): String

    fun getIndexStatements(): Array<String>

    fun fillFromCursor(cursor: Cursor)

    fun encrypt(utils: EncryptionUtils)

    fun decrypt(utils: EncryptionUtils)

}