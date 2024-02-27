import java.util.ArrayList;
import java.util.Random;

public class TolerantTest {


    public static void main(String[] args)
    {
        /*读取计划信息start*/
        System.out.println("开始进行课程计划信息的读取");
        ArrayList<Lesson> lessonList = Tool.ReadData();
        System.out.println("课程计划信息读取完毕");
        /*读取计划信息end*/

        /*建立索引start*/
        System.out.println("正在建立索引...");
        int i = 0;
        int vmCount = 0;//一个学期申请的虚拟机总台数
        for(i = 0;i < lessonList.size();i++){
            vmCount = vmCount + lessonList.get(i).getVmList().size();
        }
        VMID[] vmIDArray = new VMID[vmCount];

        int j = 0;
        int index = 0;
        int lessonListSize = lessonList.size();
        int vmListSize = 0;
        for(i = 0;i < lessonListSize;i++){
            Lesson lesson = lessonList.get(i);
            vmListSize = lesson.getVmList().size();
            for(j = 0;j<vmListSize;j++){
                vmIDArray[index] = new VMID();
                vmIDArray[index].setLessonID(i);
                vmIDArray[index].setVmID(j);
                index++;
            }
        }
        System.out.println("索引建立完成!");
        /*建立索引end*/

        /*创建物理资源start*/
        System.out.println("正在创建物理资源...");
        ArrayList<PhyMachine> phyMachineList = new ArrayList<>();
        for(i = 0;i < 50;i++){
            PhyMachine phyMachine = new PhyMachine(i,28,64,0,0);
            phyMachineList.add(phyMachine);
        }
        /*for(i = 10;i < 20;i++){
            PhyMachine phyMachine = new PhyMachine(i,14,32,0,0);
            phyMachineList.add(phyMachine);
        }
        for(i = 20;i < 30;i++){
            PhyMachine phyMachine = new PhyMachine(i,20,50,0,0);
            phyMachineList.add(phyMachine);
        }
        for(i = 30;i < 40;i++){
            PhyMachine phyMachine = new PhyMachine(i,17,40,0,0);
            phyMachineList.add(phyMachine);
        }
        for(i = 40;i < 50;i++){
            PhyMachine phyMachine = new PhyMachine(i,18.5,45,0,0);
            phyMachineList.add(phyMachine);
        }*/
        System.out.println("创建物理资源完成");
        /*创建物理资源end*/

        /*创建时间线start*/
        System.out.println("正在创建时间线...");
        TimeLine timeLine = new TimeLine();

        for(Lesson lesson:lessonList){
            ArrayList<TimeBlock> timeBlocks = Tool.getTimeBlockFromLesson(lesson);
            for(TimeBlock timeBlock:timeBlocks){
                timeLine.AddTimeBlock(timeBlock);
            }
        }

        System.out.println("时间线创建完成");
        /*创建时间线end*/

        /*遗传算法求解start*/
        /*System.out.println("开始进行带计划性信息的遗传算法求解...");

        GeneticAlgorithm GA = new GeneticAlgorithm(1000,50,0.8,0.01,vmIDArray,phyMachineList,timeLine);
        PhyPosInfo[] schedule = GA.getScheduleByGA();

        System.out.println("遗传算法求解结束");*/
        /*遗传算法求解end*/

        /*展示遗传算法求解结果start*/
        /*System.out.println("\n*******************调度结果如下*********************\n");
        System.out.println("课程ID\t虚拟机ID\t物理机ID\n");
        int phyID = 0;
        int vmIndex = 0;
        for(i = 0;i < schedule.length ;i++){
            vmIndex = schedule[i].getVmID();
            phyID = schedule[i].getPhyID();

            System.out.println(vmIDArray[vmIndex].getLessonID()+"\t"+vmIDArray[vmIndex].getVmID()+"\t"+phyID+"\n");
        }
        System.out.println("\n*******************调度结果结束*********************\n");*/
        /*展示遗传算法求解结果end*/


        /*接下来使用传统算法进行调度*/
        /*传统算法求解start*/
        System.out.println("开始进行通用云调度算法求解...");
        NormalSchedule NS = new NormalSchedule(vmIDArray,phyMachineList,timeLine,lessonList.size(), null, 0);
        PhyPosInfo[] normalSchedule = NS.getScheduleByGA();
        RandomSchedule RS = new RandomSchedule(vmIDArray,phyMachineList,timeLine,lessonList.size());
        PhyPosInfo[] rsSchedule = RS.getScheduleByGA();
        RotateSchedule RoS = new RotateSchedule(vmIDArray,phyMachineList,timeLine,lessonList.size());
        PhyPosInfo[] rosSchedule = RoS.getScheduleByGA();
        System.out.println("通用云调度算法求解结束");
        /*传统算法求解end*/

        /*展示通用算法求解结果start*/
        /*System.out.println("\n*******************调度结果如下*********************\n");
        System.out.println("课程ID\t虚拟机ID\t物理机ID\n");

        phyID = 0;
        vmIndex = 0;
        for(i = 0;i < normalSchedule.length ;i++){
            vmIndex = normalSchedule[i].getVmID();
            phyID = normalSchedule[i].getPhyID();

            System.out.println(vmIDArray[vmIndex].getLessonID()+"\t"+vmIDArray[vmIndex].getVmID()+"\t"+phyID+"\n");
        }
        System.out.println("\n*******************调度结果结束*********************\n");*/
        /*展示通用算法求解结果end*/

        /*if(GA.getFinalFitness()>RS.getFinalFitness()){
            System.out.println("通用云调度效果更好！");
        }else if(GA.getFinalFitness()<RS.getFinalFitness()){
            System.out.println("计划调度效果更好！");
        }else{
            System.out.println("两种调度效果持平！");
        }*/

        GeneticAlgorithmTest GATest = new GeneticAlgorithmTest(1000,50,0.8,0.01,vmIDArray,phyMachineList,timeLine,normalSchedule,null);
        PhyPosInfo[] eliteSchedule =GATest.getScheduleByGA();



/*********************************************ChangeTolerant-EGAPT*********************************************************/
        System.out.println("");
        System.out.println("Tolerant-Change-EGAPT:");

/*读取计划信息start*/
        System.out.println("开始进行课程计划信息(包含意外)的读取");
        ArrayList<Lesson> lessonList_Acc = Tool.ReadAccData();
        System.out.println("课程计划信息(包含意外)读取完毕");
        /*读取计划信息end*/

        /*建立索引start*/
        System.out.println("正在建立索引...");
        i = 0;
        vmCount = 0;//一个学期申请的虚拟机总台数
        for(i = 0;i < lessonList_Acc.size();i++){
            vmCount = vmCount + lessonList_Acc.get(i).getVmList().size();
        }
        vmIDArray = new VMID[vmCount];

        j = 0;
        index = 0;
        lessonListSize = lessonList_Acc.size();
        vmListSize = 0;
        for(i = 0;i < lessonListSize;i++){
            Lesson lesson = lessonList_Acc.get(i);
            vmListSize = lesson.getVmList().size();
            for(j = 0;j<vmListSize;j++){
                vmIDArray[index] = new VMID();
                vmIDArray[index].setLessonID(i);
                vmIDArray[index].setVmID(j);
                index++;
            }
        }
        System.out.println("索引建立完成!");
        /*建立索引end*/

        /*创建时间线start*/
        System.out.println("正在创建时间线...");
        timeLine = new TimeLine();

        for(Lesson lesson:lessonList_Acc){
            ArrayList<TimeBlock> timeBlocks = Tool.getTimeBlockFromLesson(lesson);
            for(TimeBlock timeBlock:timeBlocks){
                timeLine.AddTimeBlock(timeBlock);
            }
        }

        System.out.println("时间线创建完成");
        /*创建时间线end*/

        //先将意外发生前已经调度的虚拟机进行lock
        int accidentVMIndex = 70;
        int accidentLessonIndex = 1;

        for(i = 0;i<accidentVMIndex;i++){
            vmIDArray[i].setIsLocked(1);
        }

        NormalSchedule NS_Acc = new NormalSchedule(vmIDArray,phyMachineList,timeLine,lessonList_Acc.size(), eliteSchedule, accidentLessonIndex);
        normalSchedule = NS_Acc.getScheduleByGA();

        GeneticAlgorithmTest EGAPT_Acc = new GeneticAlgorithmTest(1000,50,0.8,0.01,vmIDArray,phyMachineList,timeLine, normalSchedule, eliteSchedule);
        PhyPosInfo[] eliteSchedule_Acc =EGAPT_Acc.getScheduleByGA();

        /*********************************************NOAccTolerant-EGAPT*********************************************************/
        System.out.println("");
        System.out.println("NOTolerant-EGAPT:");

        PhyPosInfo[] schedule_noTol=new PhyPosInfo[vmCount];
        for(i=0;i<vmCount;i++){
            schedule_noTol[i] = new PhyPosInfo();
        }

        int leftEdge = 69;
        int rightEdge = 100;
        for(i = 0; i<=leftEdge; i++){
            schedule_noTol[i].setVmID(i);
            schedule_noTol[i].setPhyID(eliteSchedule[i].getPhyID());
        }

        j = i;
        for(i = rightEdge; i<vmCount; i++){
            schedule_noTol[i].setVmID(i);
            schedule_noTol[i].setPhyID(eliteSchedule[j].getPhyID());
            j++;
        }

        //填充Acc虚拟机的调度情况
        Random rand = new Random();

        for(i = leftEdge + 1; i<rightEdge ;i++){
            schedule_noTol[i].setVmID(i);
            schedule_noTol[i].setPhyID(rand.nextInt(phyMachineList.size()));
        }

        //计算适应度
        Fitness fitnessNoAcc = EGAPT_Acc.getFitness(schedule_noTol);
        System.out.println("No-Acc-EGAPT:");
        System.out.println("适应度和:"+fitnessNoAcc.getFitness());
        System.out.println("负载平衡指数:"+fitnessNoAcc.getVload());
        System.out.println("能耗指数:"+fitnessNoAcc.getElecost());
    }

}


