package workflowsuite.kpi.client;

public interface MessageProducer<T> {
    MessageProducer DISCARD = new DiscardProducer();

    /**
     * Send message to the server.
     * @param message Message which will be send.
     * @return {@code true} if message was send, otherwise {@code false}.
     */
    boolean trySendMessage(T message);

    final class DiscardProducer<T> implements MessageProducer<T> {
        @Override
        public boolean trySendMessage(T message) {
            return true;
        }
    }
}
