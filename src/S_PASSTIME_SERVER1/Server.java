/**
 * @author Suchner Dominik S19036
 */

package S_PASSTIME_SERVER1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server extends Thread {

    private String host;
    private int port;
    private boolean stopped;
    private Selector selector = null;
    private ServerSocketChannel serverChannel;
    private ByteBuffer bbuf;
    private StringBuffer request;
    private Charset charset = StandardCharsets.UTF_8;
    private RequestHandler reqHandler;
    private String serverLog;

    Server(String host, int port) {
        this.host = host;
        this.port = port;
        this.bbuf = ByteBuffer.allocate(1024);
        this.request = new StringBuffer();
        this.reqHandler = new RequestHandler();

        try {
            serverChannel = ServerSocketChannel.open();
            serverChannel.socket().bind(new InetSocketAddress(host, port));
            serverChannel.configureBlocking(false);
            selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

}

    public void startServer() {
        if (host == null) return;
        this.stopped = true;
        this.start();
    }

    public void stopServer() {
        Thread.currentThread().interrupt();
        stopped = true;
    }

    public String getServerLog() {
        return serverLog;
    }

    @Override
    public void run() {
        if (!isStopped()) return;
        while (isStopped()) {
            try {
                selector.select();
                Set keys = selector.selectedKeys();
                Iterator iter = keys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = (SelectionKey) iter.next();
                    iter.remove();
                    if (key.isAcceptable()) {
                        SocketChannel cc = serverChannel.accept();
                        cc.configureBlocking(false);
                        cc.register(selector, SelectionKey.OP_READ);
                        continue;
                    }

                    if (key.isReadable()) {
                        SocketChannel cc = (SocketChannel) key.channel();
                        serviceRequest(cc);
                        continue;
                    }

                }
            } catch (Exception exc) {
                exc.printStackTrace();
                continue;
            }

        }
    }

    private void serviceRequest(SocketChannel socketChannel) {
        if (!socketChannel.isOpen()) return;

        request.setLength(0);
        bbuf.clear();

        try {
            readLoop:
            while (true) {
                int n = socketChannel.read(bbuf);
                if (n > 0) {
                    bbuf.flip();
                    CharBuffer cbuf = charset.decode(bbuf);
                    while (cbuf.hasRemaining()) {
                        char c = cbuf.get();
                        if (c == '\r' || c == '\n')
                            break readLoop;
                        request.append(c);
                    }
                }
            }

            String massage = request.toString();
            reqHandler.appendLog(massage,socketChannel);
            reqHandler.handleResponse(massage,socketChannel);
            serverLog = reqHandler.getServerLog();

        } catch (Exception exc) {
            exc.printStackTrace();
            try {
                socketChannel.close();
                socketChannel.socket().close();
            } catch (Exception e) {
                return;
            }
        }
    }


    public String getDate() {
        DateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        Calendar cal = Calendar.getInstance();
        String stringDate = sdf.format(cal.getTime());
        return stringDate;
    }

    public boolean isStopped() {
        return stopped;
    }

}
