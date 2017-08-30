package workflowsuite.kpi.client.serviceregistry;

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

    public String getTypeCode() {
        return this.typeCode;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public String getBody() {
        return this.body;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setBody(String body) {
        this.body = body; }
}
