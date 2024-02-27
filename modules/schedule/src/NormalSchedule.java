import java.util.ArrayList;

public class NormalSchedule {

    private ArrayList<PhyMachine> phyMachineList = null;//物理主机列表
    private VMID[] vmIDArray = null;
    private TimeLine timeLine = null;
    private boolean[] hasScheduled = null;//记录课程是否已经被调度
    private double finalFitness = 0.0;
    private PhyPosInfo[] history = null;

    public NormalSchedule(VMID[] vmIDArray, ArrayList<PhyMachine> phyMachineList, TimeLine timeLine, int lessonCount, PhyPosInfo[] history, int hasScheduledNumber) {
        setVmIDArray(vmIDArray);
        setPhyMachineList(phyMachineList);
        setTimeLine(timeLine);
        hasScheduled = new boolean[lessonCount];
        int i = 0;
        for (; i < lessonCount; i++) {
            hasScheduled[i] = false;
        }

        for (PhyMachine phyMachine : phyMachineList) {
            phyMachine.setCpuUsed(0);
            phyMachine.setMemUsed(0);
        }

        this.history = history;
        for (i = 0; i < hasScheduledNumber; i++) {
            hasScheduled[i] = true;
        }
    }

    public PhyPosInfo[] getScheduleByGA()//对应每一个虚拟机开在某一个物理机上
    {
        PhyPosInfo[] schedule = new PhyPosInfo[vmIDArray.length];

        int index = 0;
        int i = 0;
        int locate = 0;

        for (i = 0; i < vmIDArray.length; i++) {
            schedule[i] = new PhyPosInfo();
        }

        for (i = 0; i < vmIDArray.length; i++){
            if(vmIDArray[i].getIsLocked()==1){
                schedule[i].setVmID(i);
                schedule[i].setPhyID(history[i].getPhyID());
            }
        }

        for (TimeBlock timeBlock : timeLine.getTime()) {
            /*复原此时间块下的物理机负载情况 start*/
            clearLoad();
            recoverPhyLoad(timeBlock, schedule);//复原物理机在此时间块下的负载情况

            /*复原此时间块下的物理机负载情况 end*/
            for (Lesson lesson : timeBlock.getLessonList()) {
                if (hasScheduled[lesson.getLessonID()] == false) {
                    for (i = 0; i < vmIDArray.length; i++) {
                        if (vmIDArray[i].getLessonID() == lesson.getLessonID()) {
                            index = i;//这门课在vmIDArray的第一个位置
                            break;
                        }
                    }

                    for (VirtualMachine vm : lesson.getVmList()) {

                        locate = findLowestLoadPhy(vm);
                        schedule[index].setVmID(index);
                        schedule[index].setPhyID(locate);

                        phyMachineList.get(locate).addCPUCost(vm.getCpuRequest());
                        phyMachineList.get(locate).addMEMCost(vm.getMemRequest());


                        index++;
                    }


                    hasScheduled[lesson.getLessonID()] = true;
                }
            }
        }

        Fitness fitness = getFitness(schedule);
        System.out.println("Normal最小负载算法结果:");
        System.out.println("适应度和:" + fitness.getFitness());
        System.out.println("负载平衡指数:" + fitness.getVload());
        System.out.println("能耗指数:" + fitness.getElecost());

        finalFitness = fitness.getFitness();

        return schedule;
    }

    public void recoverPhyLoad(TimeBlock timeBlock, PhyPosInfo[] schedule) {
        //恢复此时间块下的负载


        int index = 0;
        int i = 0;

        //首先找出此时间块下已经被调度的课程
        for (Lesson lesson : timeBlock.getLessonList()) {
            if (hasScheduled[lesson.getLessonID()] == true) {
                //将这门课的负载复原到物理机上
                for (i = 0; i < vmIDArray.length; i++) {
                    if (vmIDArray[i].getLessonID() == lesson.getLessonID()) {
                        index = i;//这门课在vmIDArray的第一个位置
                        break;
                    }
                }

                for (VirtualMachine vm : lesson.getVmList()) {
                    phyMachineList.get(schedule[index].getPhyID()).addCPUCost(vm.getCpuRequest());
                    phyMachineList.get(schedule[index].getPhyID()).addMEMCost(vm.getMemRequest());
                    index++;
                }

            }
        }


    }

    public Fitness getFitness(PhyPosInfo[] schedule)//提取此方案的适应度值
    {
        Fitness fitnessStruct = new Fitness();
        double fitness=0;
        double vload = 0;
        double elecost = 0;
        double vload_sum = 0;
        double elecost_sum = 0;

        //配合计划调度的计算公式
        int index = 0;
        int i = 0;

        for(TimeBlock timeBlock:timeLine.getTime()){

            for(PhyMachine phyMachine:phyMachineList){
                phyMachine.setCpuUsed(0);
                phyMachine.setMemUsed(0);
            }//物理机资源清空

            //首先抓出时间块上的所有虚拟机，整理成Cost列表的形式
            ArrayList<Cost> costList = new ArrayList<>();
            for(Lesson lesson:timeBlock.getLessonList()){
                for(VirtualMachine vm:lesson.getVmList()){
                    Cost cost = new Cost();
                    VMID vmID = new VMID();
                    vmID.setLessonID(lesson.getLessonID());
                    vmID.setVmID(vm.getVmID());
                    cost.setVmID(vmID);
                    cost.setCpuCost(vm.getCpuRequest());
                    cost.setMemCost(vm.getMemRequest());
                    costList.add(cost);
                }
            }

            //通过对比Cost列表和schedule的关系，可以计算这个时间块下的负载均衡
            for(Cost cost:costList){
                for(i = 0;i < vmIDArray.length;i++){
                    if(vmIDArray[i].equalsVMID(cost.getVmID())){
                        index = i;
                        break;
                    }
                }

                for(i = 0;i < schedule.length;i++){
                    if(schedule[i].getVmID()==index){
                        phyMachineList.get(schedule[i].getPhyID()).addCPUCost(cost.getCpuCost());
                        phyMachineList.get(schedule[i].getPhyID()).addMEMCost(cost.getMemCost());
                        break;
                    }
                }
            }

            //假如物理机过载，直接返回无限大的fitness
            for(PhyMachine phy: phyMachineList){
                if(phy.getCPUUsedRate()>1||phy.getMEMUsedRate()>1){
                    fitnessStruct.setFitness(1000);
                    return fitnessStruct;
                }
            }

            //物理机资源占用情况已经计算完成
            //接下来计算某个时间块下的VLoad(Ti)以及能源消耗
            vload = cacVLoad();
            elecost = cacEleVLoad();
            fitness = fitness + vload + elecost;
            vload_sum+=vload;
            elecost_sum+=elecost;
            //System.out.println("cacVLoad():"+cacVLoad());
            //System.out.println("cacEleVLoad():"+cacEleVLoad());
        }

        fitness = fitness / timeLine.getTime().size();
        vload_sum = vload_sum /timeLine.getTime().size();
        elecost_sum = elecost_sum /timeLine.getTime().size();
        fitnessStruct.setFitness(fitness);
        fitnessStruct.setVload(vload_sum);
        fitnessStruct.setElecost(elecost_sum);

        return fitnessStruct;
    }

    private double cacVLoad() {
        double VLoad = 0;

        int phyCount = phyMachineList.size();
        int i = 0;
        double sum = 0;
        double av = 0;

        for (i = 0; i < phyCount; i++) {
            sum = sum + phyMachineList.get(i).getLoad();
        }

        av = sum / phyCount;

        for (i = 0; i < phyCount; i++) {
            VLoad = VLoad + (phyMachineList.get(i).getLoad() - av) * (phyMachineList.get(i).getLoad() - av);
        }

        VLoad = VLoad / phyCount;

        VLoad = Math.sqrt(VLoad);
        return VLoad;
    }

    public double cacEleVLoad(){
        //0.06+0.004*rate*10

        int phyCount = phyMachineList.size();
        int i = 0;
        double sum = 0;
        double av = 0;

        for(i = 0;i<phyCount;i++){
            if(phyMachineList.get(i).getCPUUsedRate()!=0)
                sum = sum + phyMachineList.get(i).getCPUUsedRate()*0.04+0.06;
        }

        av = sum / phyCount;

        return av;
    }

    public void clearLoad() {//清空物理机负载
        for (PhyMachine phyMachine : phyMachineList) {
            phyMachine.setCpuUsed(0);
            phyMachine.setMemUsed(0);
        }
    }

    public int findLowestLoadPhy(VirtualMachine vm ) {//搜索最小负载的物理机
        int i = 0;
        int index = 0;
        for (i = 0; i < phyMachineList.size(); i++) {
            if (phyMachineList.get(i).getLoad() < phyMachineList.get(index).getLoad()) {
                if((phyMachineList.get(i).getCpuUsed()+vm.getCpuRequest())/phyMachineList.get(i).getCpuFrequency()<=1&&(phyMachineList.get(i).getMemUsed()+vm.getMemRequest())/phyMachineList.get(i).getMem()<=1){
                    index = i;
                }
            }
        }

        if(index==0){
            if((phyMachineList.get(index).getCpuUsed()+vm.getCpuRequest())/phyMachineList.get(index).getCpuFrequency()<=1&&(phyMachineList.get(index).getMemUsed()+vm.getMemRequest())/phyMachineList.get(index).getMem()<=1){

            }else{
                System.out.println("所有物理机资源已无法容纳新的虚拟机!");
            }
        }

        return index;
    }

    public double getFinalFitness() {
        return finalFitness;
    }

    public void setFinalFitness(double finalFitness) {
        this.finalFitness = finalFitness;
    }

    public ArrayList<PhyMachine> getPhyMachineList() {
        return phyMachineList;
    }

    public void setPhyMachineList(ArrayList<PhyMachine> phyMachineList) {
        this.phyMachineList = phyMachineList;
    }

    public VMID[] getVmIDArray() {
        return vmIDArray;
    }

    public void setVmIDArray(VMID[] vmIDArray) {
        this.vmIDArray = vmIDArray;
    }

    public TimeLine getTimeLine() {
        return timeLine;
    }

    public void setTimeLine(TimeLine timeLine) {
        this.timeLine = timeLine;
    }
}
