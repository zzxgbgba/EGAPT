import java.util.ArrayList;

public class TimeLine {
    //时间轴

    private ArrayList<TimeBlock> time = null;

    public TimeLine(){
        time = new ArrayList<TimeBlock>();
    }

    public void AddTimeBlock(TimeBlock timeBlock){
        int i = 0;
        int timeSize = time.size();
        int flag = 0;

        for( i = 0;i<timeSize;i++){
            if(timeBlock.getStartTime().equals(time.get(i).getStartTime())){
                //将这个timeBlock时间块上所有的课添加到时间线上已经存在的相同的时间块上
                for(Lesson lesson:timeBlock.getLessonList()){
                    time.get(i).AddLesson(lesson);
                }

                flag = 1;
                break;
            }
            else if(timeBlock.getStartTime().before(time.get(i).getStartTime())){
                time.add(i,timeBlock);
                flag = 1;
                break;
            }
        }

        if(flag!=1){
            time.add(timeBlock);
        }
    }


    public ArrayList<TimeBlock> getTime() {
        return time;
    }
}
