import java.util.ArrayList;
import java.util.Date;

public class Lesson {

    private int lessonID = 0;
    private ArrayList<LessonTime> timeList = null;
    private String lessonName = "";
    private ArrayList<VirtualMachine> vmList = null;

    public Lesson(int lessonID,String lessonName,ArrayList<LessonTime> timeList,ArrayList<VirtualMachine> vmList){
        this.lessonID = lessonID;
        this.lessonName = lessonName;
        this.timeList = timeList;
        this.vmList = vmList;
    }

    public ArrayList<LessonTime> getTimeList() {
        return timeList;
    }

    public void setTimeList(ArrayList<LessonTime> timeList) {
        this.timeList = timeList;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public ArrayList<VirtualMachine> getVmList() {
        return vmList;
    }

    public void setVmList(ArrayList<VirtualMachine> vmList) {
        this.vmList = vmList;
    }

    public int getLessonID() {
        return lessonID;
    }

    public void setLessonID(int lessonID) {
        this.lessonID = lessonID;
    }
}
