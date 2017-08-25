package workflowsuite.kpi.client.serviceregistry;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class TransportSettings {
    private String typeCode = "";
    private String name = "";
    private String code = "";
    private String body = "";

    public TransportSettings(String typeCode, String name, String code, String body) {

        this.typeCode = typeCode;
        this.name = name;
        this.code = code;
        this.body = body;
    }

    @Contract(pure = true)
    @NotNull
    public String getTypeCode() {
        return this.typeCode;
    }

    @Contract(pure = true)
    @NotNull
    public String getName() {
        return this.name;
    }

    @Contract(pure = true)
    @NotNull
    public String getCode() {
        return this.code;
    }

    @Contract(pure = true)
    @NotNull
    public String getBody() {
        return this.body;
    }

    public void setTypeCode(@NotNull String typeCode) {
        this.typeCode = typeCode;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public void setCode(@NotNull String code) {
        this.code = code;
    }

    public void setBody(@NotNull String body) { this.body = body; }
}