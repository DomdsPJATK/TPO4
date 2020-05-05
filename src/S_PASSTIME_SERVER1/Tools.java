/**
 *
 *  @author Suchner Dominik S19036
 *
 */

package S_PASSTIME_SERVER1;

import org.yaml.snakeyaml.*;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Tools {

    public static Options createOptionsFromYaml(String fileName) throws Exception {
        InputStream input = new FileInputStream(new File(fileName));
        Yaml yaml = new Yaml(new Constructor(DataPassTime.class));
        DataPassTime data = yaml.load(input);
        return new Options(data.getHost(),data.getPort(),data.isConcurMode(),data.isShowSendRes(),data.getClientsMap());
    }
}
