import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Tool {

    static String testData = "data/planData.txt";//虚拟机请求测试文件路径
    static String accidentData = "data/planData_acc.txt";//有意外的虚拟机请求测试文件路径
    static String longData = "data/planData_long.txt";//有意外的虚拟机请求测试文件路径

    public static Date StrToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String DateToStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String str = format.format(date);
        return str;
    }

    public static ArrayList<Lesson> ReadData() {
        ArrayList<Lesson> lessonList = new ArrayList<>();

        try {
            String input = FileUtils.readFileToString(new File(testData), "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(input);

            JSONArray lessonListArray = jsonObject.getJSONArray("lessonList");

            int i = 0;
            for(i = 0;i < lessonListArray.size();i++){
                JSONObject lessonInfo = lessonListArray.getJSONObject(i);
                String lessonName = lessonInfo.getString("lessonName");//课程名
                JSONArray lessonTimeArray = lessonInfo.getJSONArray("lessonTime");

                int j = 0;
                ArrayList<LessonTime> timeList = new ArrayList<>();
                for(j = 0;j < lessonTimeArray.size();j++){
                   JSONObject time = lessonTimeArray.getJSONObject(j);
                   String startTime_S = time.getString("startTime");
                   Date startTime = StrToDate(startTime_S);
                   String endTime_S = time.getString("endTime");
                   Date endTime = StrToDate(endTime_S);
                   LessonTime lessonTime = new LessonTime();
                   lessonTime.setStartTime(startTime);
                   lessonTime.setEndTime(endTime);
                   timeList.add(lessonTime);
                }

                JSONObject request = lessonInfo.getJSONObject("request");
                int requestCount = request.getInteger("count");
                double cpuRequest = request.getDouble("cpu");
                double memRequest  = request.getDouble("mem");
                int k = 0;
                ArrayList<VirtualMachine> vmList = new ArrayList<>();
                for(k = 0;k < requestCount;k++){
                    VirtualMachine vm = new VirtualMachine();
                    vm.setCpuRequest(cpuRequest);
                    vm.setMemRequest(memRequest);
                    vm.setVmID(k);
                    vmList.add(vm);
                }

                Lesson lesson = new Lesson(i,lessonName,timeList,vmList);
                lessonList.add(lesson);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return lessonList;
    }

    public static ArrayList<Lesson> ReadAccData() {
        ArrayList<Lesson> lessonList = new ArrayList<>();

        try {
            String input = FileUtils.readFileToString(new File(accidentData), "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(input);

            JSONArray lessonListArray = jsonObject.getJSONArray("lessonList");

            int i = 0;
            for(i = 0;i < lessonListArray.size();i++){
                JSONObject lessonInfo = lessonListArray.getJSONObject(i);
                String lessonName = lessonInfo.getString("lessonName");//课程名
                JSONArray lessonTimeArray = lessonInfo.getJSONArray("lessonTime");

                int j = 0;
                ArrayList<LessonTime> timeList = new ArrayList<>();
                for(j = 0;j < lessonTimeArray.size();j++){
                    JSONObject time = lessonTimeArray.getJSONObject(j);
                    String startTime_S = time.getString("startTime");
                    Date startTime = StrToDate(startTime_S);
                    String endTime_S = time.getString("endTime");
                    Date endTime = StrToDate(endTime_S);
                    LessonTime lessonTime = new LessonTime();
                    lessonTime.setStartTime(startTime);
                    lessonTime.setEndTime(endTime);
                    timeList.add(lessonTime);
                }

                JSONObject request = lessonInfo.getJSONObject("request");
                int requestCount = request.getInteger("count");
                double cpuRequest = request.getDouble("cpu");
                double memRequest  = request.getDouble("mem");
                int k = 0;
                ArrayList<VirtualMachine> vmList = new ArrayList<>();
                for(k = 0;k < requestCount;k++){
                    VirtualMachine vm = new VirtualMachine();
                    vm.setCpuRequest(cpuRequest);
                    vm.setMemRequest(memRequest);
                    vm.setVmID(k);
                    vmList.add(vm);
                }

                Lesson lesson = new Lesson(i,lessonName,timeList,vmList);
                lessonList.add(lesson);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return lessonList;
    }

    public static ArrayList<Lesson> ReadLongData() {
        ArrayList<Lesson> lessonList = new ArrayList<>();

        try {
            String input = FileUtils.readFileToString(new File(longData), "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(input);

            JSONArray lessonListArray = jsonObject.getJSONArray("lessonList");

            int i = 0;
            for(i = 0;i < lessonListArray.size();i++){
                JSONObject lessonInfo = lessonListArray.getJSONObject(i);
                String lessonName = lessonInfo.getString("lessonName");//课程名
                JSONArray lessonTimeArray = lessonInfo.getJSONArray("lessonTime");

                int j = 0;
                ArrayList<LessonTime> timeList = new ArrayList<>();
                for(j = 0;j < lessonTimeArray.size();j++){
                    JSONObject time = lessonTimeArray.getJSONObject(j);
                    String startTime_S = time.getString("startTime");
                    Date startTime = StrToDate(startTime_S);
                    String endTime_S = time.getString("endTime");
                    Date endTime = StrToDate(endTime_S);
                    LessonTime lessonTime = new LessonTime();
                    lessonTime.setStartTime(startTime);
                    lessonTime.setEndTime(endTime);
                    timeList.add(lessonTime);
                }

                JSONObject request = lessonInfo.getJSONObject("request");
                int requestCount = request.getInteger("count");
                double cpuRequest = request.getDouble("cpu");
                double memRequest  = request.getDouble("mem");
                int k = 0;
                ArrayList<VirtualMachine> vmList = new ArrayList<>();
                for(k = 0;k < requestCount;k++){
                    VirtualMachine vm = new VirtualMachine();
                    vm.setCpuRequest(cpuRequest);
                    vm.setMemRequest(memRequest);
                    vm.setVmID(k);
                    vmList.add(vm);
                }

                Lesson lesson = new Lesson(i,lessonName,timeList,vmList);
                lessonList.add(lesson);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return lessonList;
    }

    public static ArrayList<TimeBlock> getTimeBlockFromLesson(Lesson lesson){
        ArrayList<TimeBlock> timeBlocks = new ArrayList<>();

        for(LessonTime lessonTime:lesson.getTimeList()){
            Date temp = lessonTime.getStartTime();
            while(AddHalfHour(temp).equals(lessonTime.getEndTime())!=true){
                TimeBlock timeBlock = new TimeBlock(temp,AddHalfHour(temp));
                timeBlock.AddLesson(lesson);
                timeBlocks.add(timeBlock);
                temp = AddHalfHour(temp);
            }
            TimeBlock timeBlock = new TimeBlock(temp,AddHalfHour(temp));
            timeBlock.AddLesson(lesson);
            timeBlocks.add(timeBlock);
        }

        return timeBlocks;
    }

    public static Date AddHalfHour(Date oldDate){
        Date newDate = new Date(oldDate.getTime() + 1800000);

        return newDate;
    }

}
