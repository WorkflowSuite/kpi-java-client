package workflowsuite.kpi.client;

final class NullProducer implements MessageProducer {
    @Override
    public boolean trySendMessage(KpiMessage message) {
        return true;
    }
}
