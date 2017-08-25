package workflowsuite.kpi.client;

import org.jetbrains.annotations.NotNull;

public interface MessageProducer {

    boolean TrySendMessage(@NotNull KpiMessage message);

    public static MessageProducer nop = new NullProducer();
}
