public class Cost {

    private VMID vmID = null;
    private double cpuCost = 0;
    private double memCost = 0;

    public VMID getVmID() {
        return vmID;
    }

    public void setVmID(VMID vmID) {
        this.vmID = vmID;
    }

    public double getCpuCost() {
        return cpuCost;
    }

    public void setCpuCost(double cpuCost) {
        this.cpuCost = cpuCost;
    }

    public double getMemCost() {
        return memCost;
    }

    public void setMemCost(double memCost) {
        this.memCost = memCost;
    }
}
