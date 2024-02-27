import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GeneticAlgorithm {
    //遗传算法工具类

    //GA调度算法的参数
    private int popSize=20;//种群个体数量
    private int gmax=50;//迭代代数
    private double crossoverProb=0.8;//交换率
    private double mutationRate=0.01;//变异率

    private ArrayList<PhyMachine> phyMachineList = null;//物理主机列表
    private VMID[] vmIDArray = null;
    private TimeLine timeLine = null;

    private double finalFitness = 0.0;

    public GeneticAlgorithm(int popSize, int gmax,double crossoverProb,double mutationRate,VMID[] vmIDArray,ArrayList<PhyMachine> phyMachineList,TimeLine timeLine){
       setPopSize(popSize);
       setGmax(gmax);
       setCrossoverProb(crossoverProb);
       setMutationRate(mutationRate);
       setVmIDArray(vmIDArray);
       setPhyMachineList(phyMachineList);
       setTimeLine(timeLine);
    }

    public PhyPosInfo[] getScheduleByGA()//对应每一个虚拟机开在某一个物理机上
    {
        //初始化种群
        ArrayList<PhyPosInfo[]> pop=initPopsRandomly(vmIDArray.length,phyMachineList.size(),popSize);

        //执行遗传算法
        pop=GA(pop,gmax,crossoverProb,mutationRate);

        //返回一个最优的个体
        return findBestSchedule(pop);
    }

    public ArrayList<PhyPosInfo[]> initPopsRandomly(int vmNum,int phyNum,int popsize)//参数代表虚拟机的个数、物理机的个数和要提供的种群个体数量
    {
        ArrayList<PhyPosInfo[]> schedules=new ArrayList<PhyPosInfo[]>();
        Fitness fitness;
        for(int i=0;i<popsize;i++)
        {
            //PhyPosInfo里的vmID代表虚拟机的ID，phyID代表物理机的ID，一个PhyPosInfo[]代表一个完整的分配方案，ArrayList<PhyPosInfo[]>则代表由多个个体合成的一个种群
            PhyPosInfo[] schedule=new PhyPosInfo[vmNum];
            for(int j=0;j<vmNum;j++)
            {
                schedule[j] = new PhyPosInfo();
                schedule[j].setVmID(j);
                schedule[j].setPhyID(new Random().nextInt(phyNum));
            }
            fitness = getFitness(schedule);
            if(fitness.getFitness()!=1000/*&&isDistanceEnough(schedule,schedules)==true*/)
              schedules.add(schedule);
            else{
                i--;
            }
        }
        return schedules;
    }

    public ArrayList<PhyPosInfo[]> GA(ArrayList<PhyPosInfo[]> pop,int gmax,double crossoverProb,double mutationRate)
    {
        HashMap<Integer,double[]> segmentForEach=calcSelectionProbs(pop);//Integer里是第几组调度方案的序号，double[]含有两个double，一个代表概率start点，一个代表概率end点
        ArrayList<PhyPosInfo[]> children=new ArrayList<PhyPosInfo[]>();//子代列表
        ArrayList<PhyPosInfo[]> tempParents=new ArrayList<PhyPosInfo[]>();
        int i;
        double minFitness;
        int minIndex;
        double tempFitness;
        while(children.size()<pop.size())
        {
            //selection phase:select two parents each time.
            for(i=0;i<2;i++)
            {
                double prob = new Random().nextDouble();
                for (int j = 0; j < pop.size(); j++)
                {
                    if (isBetween(prob, segmentForEach.get(j)))//落点落到start和end之间则代表选择了这个调度方案
                    {
                        tempParents.add(pop.get(j));
                        break;
                    }
                }
            }
            //cross-over phase.交换
            PhyPosInfo[] p1,p2,p1temp,p2temp;
            p1= tempParents.get(tempParents.size() - 2).clone();//-2是保证之后的递归可以拿到最后两个父代个体
            p1temp= tempParents.get(tempParents.size() - 2).clone();
            p2 = tempParents.get(tempParents.size() -1).clone();
            p2temp = tempParents.get(tempParents.size() -1).clone();
            if(new Random().nextDouble()<crossoverProb)//满足交换概率
            {
                int crossPosition = new Random().nextInt(vmIDArray.length - 1);
                //cross-over operation
                for (i = crossPosition + 1; i < vmIDArray.length; i++)
                {
                    PhyPosInfo temp = p1temp[i];
                    p1temp[i] = p2temp[i];
                    p2temp[i] = temp;
                }
            }
            //choose the children if they are better,else keep parents in next iteration.
            /*children.add(getFitness(p1temp).getFitness() < getFitness(p1).getFitness()  ? p1temp : p1);
            children.add(getFitness(p2temp).getFitness()  < getFitness(p2).getFitness()  ? p2temp : p2);*/

            ArrayList<PhyPosInfo[]> tempList = new ArrayList<>();
            tempList.add(p1);
            tempList.add(p2);
            tempList.add(p1temp);
            tempList.add(p2temp);
            minFitness = getFitness(tempList.get(0)).getFitness();
            minIndex = 0;
            for(i = 0;i<tempList.size();i++){
                tempFitness = getFitness(tempList.get(i)).getFitness();
                if(tempFitness<minFitness){
                    minIndex = i;
                    minFitness = tempFitness;
                }
            }
            children.add(tempList.get(minIndex));
            tempList.remove(minIndex);

            minFitness = getFitness(tempList.get(0)).getFitness();
            minIndex = 0;
            for(i = 0;i<tempList.size();i++){
                tempFitness = getFitness(tempList.get(i)).getFitness();
                if(tempFitness<minFitness){
                    minIndex = i;
                    minFitness = tempFitness;
                }
            }
            children.add(tempList.get(minIndex));


            // mutation phase.
            mutationRate = Math.pow(Math.E,-1.5*0.5*gmax)*Math.sqrt(children.get(0).length)/pop.size();
            if (new Random().nextDouble() < mutationRate)//满足变异概率
            {
                // mutation operations bellow.
                int maxIndex = children.size() - 1;//同理使用-1的原因也是为了递归每次都可以使用最后一个children进行变异

                for (i = maxIndex - 1; i <= maxIndex; i++)
                {
                    operateMutation(children.get(i));
                }
            }
        }

        gmax--;
        return gmax > 0 ? GA(children, gmax, crossoverProb, mutationRate): children;//迭代多少代
    }

    public HashMap<Integer,double[]> calcSelectionProbs(ArrayList<PhyPosInfo[]> parents)
    {
        int size=parents.size();
        double totalFitness=0;
        ArrayList<Double> fits=new ArrayList<Double>();
        HashMap<Integer,Double> probs=new HashMap<Integer,Double>();

        for(int i=0;i<size;i++)
        {
            Fitness fitness=getFitness(parents.get(i));
            if(fitness.getFitness()==1000){
                fits.add(0.0);
                totalFitness+=0.0;
            }else{
                fits.add(1/fitness.getFitness());
                totalFitness+=1/fitness.getFitness();
            }

        }
        for(int i=0;i<size;i++)
        {
            probs.put(i,fits.get(i)/totalFitness );
        }

        return getSegments(probs);
    }

    public boolean isDistanceEnough(PhyPosInfo[] src,ArrayList<PhyPosInfo[]> schedules){
        double rate = 0.08;//每个个体间保持2%的海明距离

        for(PhyPosInfo[] des:schedules){
            if(cacDistance(src,des)/(double)src.length<rate){
                return false;
            }
        }

        return true;
    }

    public double cacDistance(PhyPosInfo[] src,PhyPosInfo[] des){//计算海明距离
        int distance =  0;

        int i;
        int length =  src.length;
        for(i = 0;i < length;i++){
            if(src[i].getPhyID()!=des[i].getPhyID()){
                distance++;
            }
        }

        return (double)distance;
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


    public double getFinalFitness() {
        return finalFitness;
    }

    public void setFinalFitness(double finalFitness) {
        this.finalFitness = finalFitness;
    }

    public HashMap<Integer,double[]> getSegments(HashMap<Integer,Double> probs)
    {
        HashMap<Integer,double[]> probSegments=new HashMap<Integer,double[]>();
        //probSegments保存每个个体的选择概率的起点、终点，以便选择作为交配元素。
        int size=probs.size();
        double start=0;
        double end=0;
        for(int i=0;i<size;i++)
        {
            end=start+probs.get(i);
            double[]segment=new double[2];
            segment[0]=start;
            segment[1]=end;
            probSegments.put(i, segment);
            start=end;
        }

        return probSegments;
    }

    private boolean isBetween(double prob,double[]segment)
    {
        if(segment[0]<=prob&&prob<=segment[1]&&(segment[0]!=segment[1]))//segment[0]!=segment[1]是为了防止零概率的事情被选中
            return true;
        return false;
    }

    public void operateMutation(PhyPosInfo[] child)
    {
        int mutationIndex = new Random().nextInt(vmIDArray.length);
        int newPhyId = new Random().nextInt(phyMachineList.size());
        while (child[mutationIndex].getPhyID() == newPhyId)
        {
            newPhyId = new Random().nextInt(phyMachineList.size());
        }

        child[mutationIndex].setPhyID(newPhyId);
    }

    private PhyPosInfo[] findBestSchedule(ArrayList<PhyPosInfo[]> pop)
    {
        double bestFitness=1000000000;
        int bestIndex=0;
        for(int i=0;i<pop.size();i++)
        {
            PhyPosInfo[] schedule=pop.get(i);
            Fitness fitness=getFitness(schedule);
            if(bestFitness>fitness.getFitness())
            {
                bestFitness=fitness.getFitness();
                bestIndex=i;
            }
        }

        PhyPosInfo[] schedule=pop.get(bestIndex);
        Fitness fitness=getFitness(schedule);

        System.out.println("随机遗传算法结果:");
        System.out.println("适应度和:"+bestFitness);
        System.out.println("负载平衡指数:"+fitness.getVload());
        System.out.println("能耗指数:"+fitness.getElecost());


        finalFitness = bestFitness;

        return pop.get(bestIndex);
    }

    public double cacVLoad(){
        double VLoad = 0;

        int phyCount = phyMachineList.size();
        int i = 0;
        double sum = 0;
        double av = 0;

        for(i = 0;i<phyCount;i++){
            sum = sum + phyMachineList.get(i).getLoad();
        }

        av = sum / phyCount;

        for(i = 0;i<phyCount;i++){
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
                sum = sum + phyMachineList.get(i).getCPUUsedRate()*0.07+0.03;
        }

        av = sum / phyCount;

        return av;
    }









    public int getPopSize() {
        return popSize;
    }

    public void setPopSize(int popSize) {
        this.popSize = popSize;
    }

    public int getGmax() {
        return gmax;
    }

    public void setGmax(int gmax) {
        this.gmax = gmax;
    }

    public double getCrossoverProb() {
        return crossoverProb;
    }

    public void setCrossoverProb(double crossoverProb) {
        this.crossoverProb = crossoverProb;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
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
