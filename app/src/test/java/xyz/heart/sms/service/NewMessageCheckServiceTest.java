package xyz.heart.sms.service;

import org.junit.Test;

import xyz.heart.sms.MessengerSuite;
import xyz.heart.sms.shared.data.model.Message;
import xyz.heart.sms.shared.service.NewMessagesCheckService;

import static org.junit.Assert.*;

public class NewMessageCheckServiceTest extends MessengerSuite {

    @Test
    public void typesMatch() {
        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_RECEIVED, Message.TYPE_RECEIVED));
        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_SENT, Message.TYPE_SENT));
        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_SENDING, Message.TYPE_SENDING));
        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_ERROR, Message.TYPE_ERROR));
        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_DELIVERED, Message.TYPE_DELIVERED));
    }

    @Test
    public void typesDoNotMatch() {
        assertFalse(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_RECEIVED, Message.TYPE_SENDING));
        assertFalse(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_RECEIVED, Message.TYPE_SENT));
        assertFalse(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_RECEIVED, Message.TYPE_ERROR));
        assertFalse(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_RECEIVED, Message.TYPE_DELIVERED));

        assertFalse(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_SENDING, Message.TYPE_RECEIVED));
        assertFalse(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_SENT, Message.TYPE_RECEIVED));
        assertFalse(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_DELIVERED, Message.TYPE_RECEIVED));
        assertFalse(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_ERROR, Message.TYPE_RECEIVED));
    }

    @Test
    public void newMessageSendingTypesAreEquivalent() {
        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_SENT, Message.TYPE_SENDING));
        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_SENT, Message.TYPE_SENT));
        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_SENT, Message.TYPE_DELIVERED));

        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_SENDING, Message.TYPE_SENDING));
        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_SENDING, Message.TYPE_SENT));
        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_SENDING, Message.TYPE_DELIVERED));

        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_DELIVERED, Message.TYPE_SENDING));
        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_DELIVERED, Message.TYPE_SENT));
        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_DELIVERED, Message.TYPE_DELIVERED));
    }

    @Test
    public void oldMessageSendingTypesAreEquivalent() {
        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_SENDING, Message.TYPE_SENT));
        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_SENT, Message.TYPE_SENT));
        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_DELIVERED, Message.TYPE_SENT));

        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_SENDING, Message.TYPE_SENDING));
        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_SENT, Message.TYPE_SENDING));
        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_DELIVERED, Message.TYPE_SENDING));

        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_SENDING, Message.TYPE_DELIVERED));
        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_SENT, Message.TYPE_DELIVERED));
        assertTrue(NewMessagesCheckService.Companion.typesAreEqual(Message.TYPE_DELIVERED, Message.TYPE_DELIVERED));
    }
}
