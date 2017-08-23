package workflowsuite.kpi.client.rabbitmq;

import org.jetbrains.annotations.NotNull;
import workflowsuite.kpi.client.KpiMessage;

public class NullProducer implements Producer {
    @Override
    public boolean TrySendMessage(@NotNull KpiMessage message) {
        return true;
    }
}
