package xyz.heart.sms.shared.util

import android.text.InputType
import android.widget.EditText

import xyz.heart.sms.shared.data.Settings
import xyz.heart.sms.shared.data.pojo.KeyboardLayout

object KeyboardLayoutHelper {

    fun applyLayout(editText: EditText, layout: KeyboardLayout = Settings.keyboardLayout) {
        var inputType = editText.inputType
        val imeOptions = editText.imeOptions

        when (layout) {
            KeyboardLayout.DEFAULT -> inputType = inputType or InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE
            KeyboardLayout.SEND -> { }
            KeyboardLayout.ENTER -> { }
        }

        editText.inputType = inputType
        editText.imeOptions = imeOptions
    }
}
