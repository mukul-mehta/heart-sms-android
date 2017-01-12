package xyz.klinker.messenger.util;

import android.text.InputType;
import android.widget.EditText;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import xyz.klinker.messenger.MessengerSuite;
import xyz.klinker.messenger.data.Settings;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class KeyboardLayoutHelperTest extends MessengerSuite {

    private static final int DEFAULT_INPUT_TYPE =
            InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT;

    private KeyboardLayoutHelper helper;

    @Mock
    private Settings settings;
    @Mock
    private EditText editText;

    @Before
    public void setUp() {
        when(editText.getInputType()).thenReturn(DEFAULT_INPUT_TYPE);
    }

    @Test
    public void shouldAcceptDefaultKeyboard() {
        settings.keyboardLayout = Settings.KeyboardLayout.DEFAULT;
        helper = new KeyboardLayoutHelper(settings);

        helper.applyLayout(editText);
        verify(editText).setInputType(DEFAULT_INPUT_TYPE |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
    }

    @Test
    public void shouldAcceptEnterKeyboard() {
        settings.keyboardLayout = Settings.KeyboardLayout.ENTER;
        helper = new KeyboardLayoutHelper(settings);

        helper.applyLayout(editText);
        verify(editText).setInputType(DEFAULT_INPUT_TYPE | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
    }

    @Test
    public void shouldNotChangeForSendKeyboard() {
        settings.keyboardLayout = Settings.KeyboardLayout.SEND;
        helper = new KeyboardLayoutHelper(settings);

        helper.applyLayout(editText);
        verify(editText).setInputType(DEFAULT_INPUT_TYPE);
    }
}