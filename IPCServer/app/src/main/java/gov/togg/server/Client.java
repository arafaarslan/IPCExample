package gov.togg.server;

public class Client {
    private String clientPackageName;
    private String clientProcessId;
    private String clientData;
    private String ipcMethod;

    public Client() {
    }

    public Client(String clientPackageName, String clientProcessId, String clientData, String ipcMethod) {
        this.clientPackageName = clientPackageName;
        this.clientProcessId = clientProcessId;
        this.clientData = clientData;
        this.ipcMethod = ipcMethod;
    }

    public void setClientPackageName(String clientPackageName) {
        this.clientPackageName = clientPackageName;
    }

    public void setClientProcessId(String clientProcessId) {
        this.clientProcessId = clientProcessId;
    }

    public void setClientData(String clientData) {
        this.clientData = clientData;
    }

    public void setIpcMethod(String ipcMethod) {
        this.ipcMethod = ipcMethod;
    }

    public String getClientPackageName() {
        return clientPackageName;
    }

    public String getClientProcessId() {
        return clientProcessId;
    }

    public String getClientData() {
        return clientData;
    }

    public String getIpcMethod() {
        return ipcMethod;
    }
}
