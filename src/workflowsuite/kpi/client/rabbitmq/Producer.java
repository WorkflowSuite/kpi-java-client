package workflowsuite.kpi.client.rabbitmq;

import org.jetbrains.annotations.NotNull;
import workflowsuite.kpi.client.KpiMessage;

public interface Producer {
    boolean TrySendMessage(@NotNull KpiMessage message);
}
