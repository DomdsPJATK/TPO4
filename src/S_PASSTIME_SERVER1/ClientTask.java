/**
 *
 *  @author Suchner Dominik S19036
 *
 */

package S_PASSTIME_SERVER1;


import java.util.List;
import java.util.concurrent.FutureTask;

public class ClientTask extends FutureTask<String> {

    public ClientTask(Client client, List<String> reqList, boolean showRes){
        super(() -> {
            client.connect();
            client.send("login " + client.getClientId());
            for(String req : reqList) {
                String res = client.send(req);
                if (showRes) System.out.println(res);
            }
            String clog = client.send("bye and log transfer");
            return clog;
        });
    }

    public static ClientTask create(Client client, List<String> reqList, boolean showRes) {
       return new ClientTask(client,reqList,showRes);
    }


}
