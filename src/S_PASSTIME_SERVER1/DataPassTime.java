package S_PASSTIME_SERVER1;

import java.util.List;
import java.util.Map;

public class DataPassTime {
    private String host;
    private int port;
    private boolean concurMode;
    private boolean showSendRes;
    private Map<String, List<String>> clientsMap;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean isConcurMode() {
        return concurMode;
    }

    public boolean isShowSendRes() {
        return showSendRes;
    }

    public Map<String, List<String>> getClientsMap() {
        return clientsMap;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setConcurMode(boolean concurMode) {
        this.concurMode = concurMode;
    }

    public void setShowSendRes(boolean showSendRes) {
        this.showSendRes = showSendRes;
    }

    public void setClientsMap(Map<String, List<String>> clientsMap) {
        this.clientsMap = clientsMap;
    }

    @Override
    public String toString() {
        return "DataPassTime{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", concurMode=" + concurMode +
                ", showSendRes=" + showSendRes +
                ", clientsMap=" + clientsMap +
                '}';
    }
}
