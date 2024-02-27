import java.util.ArrayList;
import java.util.Date;

public class TimeBlock {

    //时间数据块
    private Date startTime = null;//时间块开始时间
    private Date endTime = null;//时间块结束时间
    private ArrayList<Lesson> lessonList = null;

    public TimeBlock(Date startTime,Date endTime){
        lessonList = new ArrayList<Lesson>();

        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void AddLesson(Lesson lesson){//添加课程到时间块
        int i = 0;
        int lessonListSize = lessonList.size();
        int flag = 0;
        for( i =0;i<lessonListSize;i++){
            if(lesson.getLessonID() == lessonList.get(i).getLessonID()){
                flag = 1;
                break;
            }
            else if(lesson.getLessonID() < lessonList.get(i).getLessonID()){
                lessonList.add(i,lesson);
                flag = 1;
                break;
            }else{}

        }

        if(flag!=1){
            lessonList.add(lesson);
        }
    }

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

    public ArrayList<Lesson> getLessonList() {
        return lessonList;
    }

    public void setLessonList(ArrayList<Lesson> lessonList) {
        this.lessonList = lessonList;
    }
}
