

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.oss.driver.shaded.codehaus.jackson.JsonFactory;
import com.datastax.oss.driver.shaded.codehaus.jackson.JsonParser;
import com.datastax.oss.driver.shaded.codehaus.jackson.JsonToken;
import com.datastax.oss.driver.shaded.fasterxml.jackson.databind.JsonNode;
import com.datastax.oss.driver.shaded.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;

public class Timer_Task extends TimerTask {

    private String name;
    private static Logger logger = LoggerFactory.getLogger(Timer_Task.class);
    private static Map<String, IAM> callSetupMap = new HashMap<>();
    private String JSON_DIR = "data";
    List<String> opcs = new ArrayList<String>();
    List<String> dpcs = new ArrayList<String>();
    List<String> cics = new ArrayList<String>();
    List<String> messageTypes = new ArrayList<String>();
    String sourceIp;
    String destIp;
    String caller;
    String called;
    long frame_time;
    String isup_cause_indicator;
    int count;

    public Timer_Task(String n) {
        this.name = n;
    }


    public void run() {
        System.out.println(Thread.currentThread().getName() + " " + name + " the file has executed successfully ");
        if ("Task1".equalsIgnoreCase(name)) {

            File data = Paths.get(JSON_DIR).toFile();


            List<File> JsontoBeProcessed = new ArrayList<>(0);

            if (data.exists()) {
                for (File f : data.listFiles()) {
                    String name = f.getName().toLowerCase();
                    if (name.endsWith(".json")) {
                        File JsontoBeProcessedFile = new File(f.getParent(),
                                f.getName() + "." + new Date().getTime() + ".processed");
                        // Rename file (or directory)
                        logger.info("observeJsonFiles:File > " + f.getAbsolutePath());
                        f.renameTo(JsontoBeProcessedFile);
                        JsontoBeProcessed.add(JsontoBeProcessedFile);
                    }
                }
            }


            if (JsontoBeProcessed.size() > 0) {
                JsontoBeProcessed.forEach(file -> {
                    Thread t = new Thread(new Runnable() {

                        @Override
                        public void run() {

                            processJsonFile(file);

                        }
                    });
                    t.start();
                });
            }

        }
    }

    public void processJsonFile(File file) {

        try {

            String name = file.getName();
            Cluster cluster = Cluster.builder().withoutMetrics()
                    .withCredentials("cassandra", "cassandra")
                    .withClusterName("Test Cluster")
                    .addContactPoint("192.168.4.73")
                    //.addContactPoint("115.186.130.251")

                    //    .addContactPoint("127.0.0.1")
                    .withPort(9042).build();
            // .withPort(20073).build();
            Session session = cluster.connect("netsniff");

            logger.info("starting first if for camel");
            logger.info("processJsonFile: " + name);

            if (name.contains("camel")) {
                try {

                    JSONParser jsonparser = new JSONParser();
                    FileReader reader = new FileReader(file);
                    Object obj = jsonparser.parse(reader);
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(String.valueOf(obj));
                    Calendar cal = Calendar.getInstance();

                    for (JsonNode node : root.findParents("camel.InitialDPArg_element")) {


                        String imsi = node.findValue("e212.imsi").asText();

                        String msisdn = node.findValue("e164.msisdn").asText();


                        logger.info("Value for e212.imsi is :" + imsi);
                        logger.info("Value for e164.msisdn is :" + msisdn);


                        logger.info("adding values");
                        session.execute("INSERT INTO msisdn_imsi (msisdn,imsi,dt) VALUES (?,?,?);", msisdn, imsi, cal.getTime());
                        logger.info("Values Added");
                    }

                } catch (Exception e) {
                    logger.error(e.getMessage(), (Throwable) e);
                    e.printStackTrace();
                }


            } else {

                try {
                    logger.info("Starting Else ");
                    //JSONParser jsonparser = new JSONParser();
                    String jsonData = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                    logger.info("Got the file in Json data");


                    JsonFactory factory = new JsonFactory();
                    JsonParser parser = factory.createJsonParser(jsonData);


                    while (!parser.isClosed()) {
                        JsonToken jsonToken = parser.nextToken();


                        if (JsonToken.FIELD_NAME.equals(jsonToken)) {
                            String fieldName = parser.getCurrentName();
                            System.out.println(fieldName);
                            if (fieldName.equals("_source")) {
                                count = 0;
                            }

                            jsonToken = parser.nextToken();

                            if ("frame.time_epoch".equals(fieldName)) {
                                frame_time = parser.getValueAsLong();


                            } else if ("ip.src".equals(fieldName)) {
                                sourceIp = parser.getText();
                            } else if ("ip.dst".equals(fieldName)) {
                                destIp = parser.getText();
                            } else if ("isup.calling".equals(fieldName)) {
                                caller = parser.getText();
                            } else if ("isup.called".equals(fieldName)) {
                                called = parser.getText();
                            } else if ("isup.cause_indicator".equals(fieldName)) {
                                isup_cause_indicator = parser.getText();
                            } else if ("m3ua.protocol_data_opc".equals(fieldName)) {
                                opcs.add(parser.getText());
                            } else if ("m3ua.protocol_data_dpc".equals(fieldName)) {
                                dpcs.add(parser.getText());
                            } else if ("bicc.cic".equals(fieldName)) {
                                cics.add(parser.getText());
                            } else if ("isup.message_type".equals(fieldName)) {
                                messageTypes.add(parser.getText());
                            }

                        }

                        if (jsonToken.START_OBJECT.equals(jsonToken)) {
                            count++;
                        }
                        if (jsonToken.END_OBJECT.equals(jsonToken)) {
                            count--;
                        }


                        if (count == 0) {
                            if (messageTypes.size() == 0 && opcs.size() == 0 && dpcs.size() == 0 && cics.size() == 0) {
                                continue;
                            }
                            System.out.println("size of list is" + messageTypes.size());
                            for (int i = 0; i < messageTypes.size(); i++) {
                                String messageType = messageTypes.get(i);
                                if (messageType.equals("1") || messageType.equals("6") || messageType.equals("12")) {
                                    String cic = cics.get(i);
                                    String opc = opcs.get(i);
                                    String dpc = dpcs.get(i);
                                    String id = cic + "-" + opc + "-" + dpc;
                                    String revertId = cic + "-" + dpc + "-" + opc;

                                    if (messageType.equals("1")) {
                                        IAM iamObj = new IAM(sourceIp, destIp, frame_time, dpc, opc, called, caller, messageType, "null", "null", null, cic);
                                        iamObj.setId(id);
                                        callSetupMap.put(id, iamObj);
                                    } else if (messageType.equals("6")) {
                                        IAM iamObj = callSetupMap.remove(revertId);


                                        if (iamObj == null) {
                                            logger.warn("No such key found in the callSetupMap for revertId:" + revertId);
                                            continue;
                                        }

                                        logger.debug("Found the iamObj from map");
                                        iamObj.setAcmTime(frame_time);

                                        // session.execute("INSERT INTO call (id,calle,caller,cic,dest_ip,dpc,identity,init_dt,lac,opc,setup_dt,sig_point_code,source_ip) VALUES (?,?,?,?,?,?,?,?,?,?,?);", iamObj.getId(), iamObj.getIsup_called(), iamObj.getIsup_calling(), iamObj.getCic(), iamObj.getIp_dst(), iamObj.getDpc(), iamObj.getIsup_call_identity(),iamObj.get, iamObj.getIsup_location_number(), iamObj.getOpc(), iamObj.getIsup_signalling_point_code(), iamObj.getIp_src());
                                        session.execute("INSERT INTO call (id,calle,caller,cic,dest_ip,dpc,identity,init_dt,lac,opc,setup_dt,sig_point_code,source_ip) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);", iamObj.getId(), iamObj.getIsup_called(), iamObj.getIsup_calling(), iamObj.getCic(), iamObj.getIp_dst(), iamObj.getDpc(), iamObj.getIsup_call_identity(), iamObj.getFrame_time(), iamObj.getIsup_location_number(), iamObj.getOpc(), iamObj.getAcmTime(), iamObj.getIsup_signalling_point_code(), iamObj.getIp_src());

                                    } else if (messageType.equals("12")) {
                                        logger.info("starting el if for rel packet msg type is 12");

                                        logger.info("Value of isup.cause_indicator is:" + isup_cause_indicator);

                                     /*   frame_time=frame_time*1000;
                                        Date date = new Date(frame_time);
                                        System.out.println("time is"+frame_time);
                                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
                                        //format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
                                        String formatted = format.format(date);
                                        System.out.println("date is"+formatted);
                                        //formatted = formatted.substring(0, formatted.indexOf(".") + 4);
                                        System.out.println("now date is"+formatted);
                                        String pattern = "dd/MM/yyyy HH:mm:ss.SSS";
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                                        Date date1 = simpleDateFormat.parse(formatted);
                                        logger.info("Rel time is:"+frame_time);

                                      */
                                        logger.info("Rel time is:" + frame_time);

                                        logger.info("adding values for simple id");
                                        String query1 = "UPDATE call SET disconn_reason='" + isup_cause_indicator + "' WHERE id='" + id + "' IF EXISTS";
                                        //String query1 = "UPDATE call SET disconn_reason='" + isup_cause_indicator + "', disconn_dt='" + date + "' WHERE id='" + id + "' IF EXISTS";

                                        ResultSet r = session.execute(query1);
                                        logger.info("adding values for alternate id");

                                        String query2 = "UPDATE call SET disconn_reason='" + isup_cause_indicator + "' WHERE id='" + revertId + "' IF EXISTS";
                                        // String query2 = "UPDATE call SET disconn_reason='" + isup_cause_indicator + "', disconn_dt='" + date + "' WHERE id='" + revertId + "' IF EXISTS";

                                        if (r == null || r.all().size() < 1) {
                                            session.execute(query2);
                                            logger.info("Values Added for alternate is");
                                        }

                                    }
                                }

                            }
                            opcs.clear();
                            dpcs.clear();
                            cics.clear();
                            messageTypes.clear();
                        }
                    }


                } catch (Exception e) {
                    logger.error(e.getMessage(), (Throwable) e);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), (Throwable) e);
            e.printStackTrace();
        }
    }
}