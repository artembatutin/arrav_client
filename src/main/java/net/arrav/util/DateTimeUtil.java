package net.arrav.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {

    /**
     * Gets the date of server.
     */
    public static String getDate() {
        return new SimpleDateFormat("EE MMM dd yyyy").format(new Date());
    }

    /**
     * Gets the current server time and formats it
     *
     * @return the formatted current server time.
     */
    public static String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        return dateFormat.format(new Date());
    }

    /** Gets the time based off a long. */
    public static String getTime(long period) {
        return new SimpleDateFormat("m:ss").format(System.currentTimeMillis() - period);
    }



}
