package workflowsuite.kpi.client;

public interface MessageProducer {
    MessageProducer DISCARD = new DiscardProducer();

    /**
     * Send message to the server.
     * @param message Message which will be send.
     * @return {@code true} if message was send, otherwise {@code false}.
     */
    boolean trySendMessage(KpiMessage message);

    final class DiscardProducer implements MessageProducer {
        @Override
        public boolean trySendMessage(KpiMessage message) {
            return true;
        }
    }
}