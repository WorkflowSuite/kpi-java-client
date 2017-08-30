package workflowsuite.kpi.client;

public interface MessageProducer {

    /**
     * Send message to the server.
     * @param message Message which will be send.
     * @return {@code true} if message was send, otherwise {@code false}.
     */
    boolean trySendMessage(KpiMessage message);
}
