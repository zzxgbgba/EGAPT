public class PhyMachine {

    //物理机信息

    private int phyID;//物理机ID
    private double cpuFrequency;//CPU资源大小 单位Ghz
    private double mem;//内存资源大小 单位GB
    private double cpuUsed;//被占用的CPU资源大小
    private double memUsed;//被占用的内存资源大小
    private double cpuAvalible;//可用的CPU资源大小
    private double memAvalible;//可用的内存资源大小

    public PhyMachine(int phyID,double cpuFrequency,double mem,double cpuUsed,double memUsed){
        setPhyID(phyID);
        setCpuFrequency(cpuFrequency);
        setMem(mem);
        setCpuUsed(cpuUsed);
        setMemUsed(memUsed);
        setCpuAvalible(cpuFrequency-cpuUsed);
        setMemAvalible(mem-memUsed);
    }

    public void addCPUCost(double cpuCost){
        cpuUsed = cpuUsed + cpuCost;
    }

    public void addMEMCost(double memCost){
        memUsed = memUsed + memCost;
    }

    public double getCPUUsedRate(){
        double CPUUsedRate = this.cpuUsed/this.cpuFrequency;
        return CPUUsedRate;
    }

    public double getMEMUsedRate(){
        double MEMUsedRate = this.memUsed/this.mem;
        return MEMUsedRate;
    }

    public double getLoad(){//获得此物理机的负载情况
        double Load = getCPUUsedRate()*0.5 + getMEMUsedRate()*0.5;

        return Load;
    }

    public int getPhyID() {
        return phyID;
    }

    public void setPhyID(int phyID) {
        this.phyID = phyID;
    }

    public double getCpuFrequency() {
        return cpuFrequency;
    }

    public void setCpuFrequency(double cpuFrequency) {
        this.cpuFrequency = cpuFrequency;
    }

    public double getMem() {
        return mem;
    }

    public void setMem(double mem) {
        this.mem = mem;
    }

    public double getCpuUsed() {
        return cpuUsed;
    }

    public void setCpuUsed(double cpuUsed) {
        this.cpuUsed = cpuUsed;
    }

    public double getMemUsed() {
        return memUsed;
    }

    public void setMemUsed(double memUsed) {
        this.memUsed = memUsed;
    }

    public double getCpuAvalible() {
        return cpuAvalible;
    }

    public void setCpuAvalible(double cpuAvalible) {
        this.cpuAvalible = cpuAvalible;
    }

    public double getMemAvalible() {
        return memAvalible;
    }

    public void setMemAvalible(double memAvalible) {
        this.memAvalible = memAvalible;
    }
}
