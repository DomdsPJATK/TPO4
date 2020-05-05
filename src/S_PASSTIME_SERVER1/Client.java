/**
 *
 *  @author Suchner Dominik S19036
 *
 */

package S_PASSTIME_SERVER1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Client {

    private String host;
    private int port;
    private String clientId;
    private SocketChannel socket = null;
    private static ByteBuffer inBuf = ByteBuffer.allocateDirect(1024);

    public Client(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.clientId = id;
    }

    public void connect() {
        try {
            socket = SocketChannel.open(new InetSocketAddress(host, port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String send(String toWrite) {
        Charset charset = StandardCharsets.UTF_8;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            CharBuffer cbuf = CharBuffer.wrap(toWrite + "\n");
            ByteBuffer outBuf = charset.encode(cbuf);
            socket.write(outBuf);

            while (true) {
                inBuf.clear();
                int readBytes = socket.read(inBuf);
                if (readBytes == 0) {
                    continue;
                }
                else if (readBytes == -1) {
                    socket.close();
                    break;
                }
                else {
                    inBuf.flip();
                    cbuf = charset.decode(inBuf);
                    stringBuilder.append(cbuf);
                    break;
                }
            }

        } catch(Exception exc) {
            exc.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getClientId() {
        return clientId;
    }

}
