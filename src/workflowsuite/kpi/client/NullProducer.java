package workflowsuite.kpi.client;

import org.jetbrains.annotations.NotNull;

final class NullProducer implements MessageProducer {
    @Override
    public boolean TrySendMessage(@NotNull KpiMessage message) {
        return true;
    }
}
