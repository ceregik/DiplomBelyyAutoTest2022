package data;

import java.util.Date;

public class ResponseGet {

    private Date time;

    public ResponseGet(Date time) {
        this.time = time;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ResponseGet{" +
                "time=" + time +
                '}';
    }
}
