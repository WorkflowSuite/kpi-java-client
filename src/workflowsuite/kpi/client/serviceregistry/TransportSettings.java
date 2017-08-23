package workflowsuite.kpi.client.serviceregistry;

public final class TransportSettings {
    private String _typeCode;
    private String _name;
    private String _code;
    private String _body;

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

    public void setTypeCode(String typeCode) {
        this._typeCode = typeCode;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setCode(String code) {
        this._code = code;
    }

    public void setBody(String body) {
        this._body = body;
    }
}