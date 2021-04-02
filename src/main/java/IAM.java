

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.TimeZone;

public class IAM {


    String ip_src;
    String ip_dst;
    long frame_time;
    String dpc;
    String opc;
    String isup_called;
    String isup_calling;
    String isup_message_type;
    String isup_location_number;
    String isup_call_identity;
    String isup_signalling_point_code;
    String cic;
    String id;
    long acmTime;
    String relTime;


    public IAM(String ip_src, String ip_dst, long frame_time, String m3ua_protocol_data_dpc, String m3ua_protocol_data_opc, String isup_called, String isup_calling, String isup_message_type, String isup_location_number, String isup_call_identity, String isup_signalling_point_code, String bicc_cic) {
        this.ip_src = ip_src;
        this.ip_dst = ip_dst;
        this.frame_time = frame_time;
        this.dpc = m3ua_protocol_data_dpc;
        this.opc = m3ua_protocol_data_opc;
        this.isup_called = isup_called;
        this.isup_calling = isup_calling;
        this.isup_message_type = isup_message_type;
        this.isup_location_number = isup_location_number;
        this.isup_call_identity = isup_call_identity;
        this.isup_signalling_point_code = isup_signalling_point_code;
        this.cic = bicc_cic;
    }


    public String getIp_src() {
        return ip_src;
    }

    public void setIp_src(String ip_src) {
        this.ip_src = ip_src;
    }

    public String getIp_dst() {
        return ip_dst;
    }

    public void setIp_dst(String ip_dst) {
        this.ip_dst = ip_dst;
    }

    public Date getFrame_time() throws ParseException {


        //SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy HH:mm:ss.SSS");
        //Date parsedDate = dateFormat.parse(frame_time);

        //return parsedDate;
        // return parsedDate.getTime();
        // String dt = frame_time;
        // String pattern = "MMM  d, yyyy HH:mm:ss.SSS";
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        //Date  date = simpleDateFormat.parse(dt);
        //return  date;
        frame_time = frame_time * 1000;
        Date date = new Date(frame_time);
        System.out.println("time is" + frame_time);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        //format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        String formatted = format.format(date);
        System.out.println("date is" + formatted);
        //formatted = formatted.substring(0, formatted.indexOf(".") + 4);
        System.out.println("now date is" + formatted);
        String pattern = "dd/MM/yyyy HH:mm:ss.SSS";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date1 = simpleDateFormat.parse(formatted);
        return date1;


    }

    public void setFrame_time(long frame_time) {
        this.frame_time = frame_time;
    }

    public String getDpc() {
        return dpc;
    }

    public void setDpc(String dpc) {
        this.dpc = dpc;
    }

    public String getOpc() {
        return opc;
    }

    public void setOpc(String opc) {
        this.opc = opc;
    }

    public String getIsup_called() {
        return isup_called;
    }

    public void setIsup_called(String isup_called) {
        this.isup_called = isup_called;
    }

    public String getIsup_calling() {
        return isup_calling;
    }

    public void setIsup_calling(String isup_calling) {
        this.isup_calling = isup_calling;
    }

    public String getIsup_message_type() {
        return isup_message_type;
    }

    public void setIsup_message_type(String isup_message_type) {
        this.isup_message_type = isup_message_type;
    }

    public String getIsup_location_number() {
        return isup_location_number;
    }

    public void setIsup_location_number(String isup_location_number) {
        this.isup_location_number = isup_location_number;
    }

    public String getIsup_call_identity() {
        return isup_call_identity;
    }

    public void setIsup_call_identity(String isup_call_identity) {
        this.isup_call_identity = isup_call_identity;
    }

    public String getIsup_signalling_point_code() {
        return isup_signalling_point_code;
    }

    public void setIsup_signalling_point_code(String isup_signalling_point_code) {
        this.isup_signalling_point_code = isup_signalling_point_code;
    }

    public String getCic() {
        return cic;
    }

    public void setCic(String cic) {
        this.cic = cic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getAcmTime() throws ParseException {

        //  SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy HH:mm:ss.SSS");
        //Date parsedDate = dateFormat.parse(acmTime);
        //return parsedDate;
        //    return parsedDate.getTime();
        //  String dt = acmTime;
        //String pattern = "MMM  d, yyyy HH:mm:ss.SSS";
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        //Date date = simpleDateFormat.parse(dt);
        //return  date;
        acmTime = acmTime * 1000;
        Date date = new Date(acmTime);
        System.out.println("time is" + acmTime);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        // format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        String formatted = format.format(date);
        System.out.println("date is" + formatted);
        //  formatted = formatted.substring(0, formatted.indexOf(".") + 4);
        System.out.println("now date is" + formatted);
        String pattern = "dd/MM/yyyy HH:mm:ss.SSS";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date1 = simpleDateFormat.parse(formatted);
        return date1;

    }

    public void setAcmTime(long acmTime) {
        this.acmTime = acmTime;
    }

    public Date getRelTime() throws ParseException {
        String dt = relTime;
        String pattern = "MMM  d, yyyy HH:mm:ss.SSS";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse(dt);
        return date;


    }

    public void setRelTime(String relTime) {
        this.relTime = relTime;
    }

}
