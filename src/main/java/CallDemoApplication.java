

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.oss.driver.shaded.fasterxml.jackson.databind.JsonNode;
import com.datastax.oss.driver.shaded.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CallDemoApplication {

    public static void main(String[] args) {
        Timer_Task te1 = new Timer_Task("Task1");


        Timer t = new Timer();
        t.schedule(te1, 0, 50000);


    }
}
