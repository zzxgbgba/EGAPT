import java.util.Date;

public class LessonTime {

    private Date startTime = null;//时间块开始时间
    private Date endTime = null;//时间块结束时间

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
