package S_PASSTIME_SERVER1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class RequestHandler {

    private SocketChannel socketChannel;
    private StringBuffer remsg;
    private StringBuilder serverLog;
    private HashMap<Integer, String> clients;
    private HashMap<Integer, StringBuilder> clientsLog;
    private Charset charset = StandardCharsets.UTF_8;

    public RequestHandler() {
        this.remsg = new StringBuffer();
        this.clients = new HashMap<>();
        this.clientsLog = new HashMap<>();
        this.serverLog = new StringBuilder();
    }

    public void handleResponse(String msg, SocketChannel socketChannel) {

        try {
            switch (msg) {
                case "login":
                    writeResp(socketChannel, "logged in");
                    break;
                case "bye":
                    writeResp(socketChannel, "logged out");
                    socketChannel.close();
                    socketChannel.socket().close();
                    break;
                case "bye and log transfer":
                    writeResp(socketChannel, getClientLog(socketChannel.socket().getPort()));
                    socketChannel.close();
                    socketChannel.socket().close();
                    break;
                default:
                    String[] request = msg.split(" ");
                    writeResp(socketChannel, Time.passed(request[0], request[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void appendLog(String msg, SocketChannel socketChannel) {

        int clientPort = socketChannel.socket().getPort();
        String clientName = clients.get(clientPort);
        String date = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        String[] request;

        String msgSwitch;

        if(msg.startsWith("login")) msgSwitch = "login";
        else msgSwitch = msg;

        switch (msgSwitch) {
            case "login":
                request = msg.split(" ");
                clients.put(clientPort, request[1]);
                clientsLog.put(clientPort, new StringBuilder().append("logged in\n"));
                serverLog.append(request[1] + " logged in at " + date + "\n");
                break;
            case "bye":
            case "bye and log transfer":
                clientsLog.get(clientPort).append("logged out");
                serverLog.append(clientName + " logged out at " + date + "\n");
                break;
            default:
                request = msg.split(" ");
                clientsLog.get(clientPort).append("Request: " + request[0] + " " + request[1] + "\n");
                clientsLog.get(clientPort).append("Result:\n" + Time.passed(request[0], request[1]) + "\n");
                serverLog.append(clientName + " request at " + date + ": \"" + msg + "\"\n");
                break;
        }
    }

    private void writeResp(SocketChannel sc, String msg) throws IOException {
        remsg.setLength(0);
        if (msg != null) remsg.append(msg);
        ByteBuffer buf = charset.encode(CharBuffer.wrap(remsg));
        sc.write(buf);
    }

    public String getClientLog(int port) {
        StringBuilder ret = new StringBuilder();
        String name = clients.get(port);
        ret.append("=== " + name + " log start ===\n");
        ret.append(clientsLog.get(port).toString() + "\n");
        ret.append("=== " + name + " log end ===\n");
        return ret.toString();
    }

    public String getServerLog() {
        return serverLog.toString();
    }

}
