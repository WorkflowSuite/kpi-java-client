package workflowsuite.kpi.client.serviceregistry;

public final class TransportSettings {

    private final String _typeCode;
    private final String _name;
    private final String _code;
    private final String _body;

    public TransportSettings(String typeCode, String name, String code, String body) {

        _typeCode = typeCode;
        _name = name;
        _code = code;
        _body = body;
    }

    public String getTypeCode() {
        return _typeCode;
    }

    public String getName() {
        return _name;
    }

    public String getCode() {
        return _code;
    }

    public String getBody() {
        return _body;
    }
}