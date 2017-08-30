package workflowsuite.kpi.client;

public interface MessageProducer {

    boolean trySendMessage(KpiMessage message);
}
