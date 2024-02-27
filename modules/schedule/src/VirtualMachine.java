public class VirtualMachine {

    private int vmID;//虚拟机ID
    private double cpuRequest;//CPU资源请求大小
    private double memRequest;//内存资源请求大小

    public int getVmID() {
        return vmID;
    }

    public void setVmID(int vmID) {
        this.vmID = vmID;
    }

    public double getCpuRequest() {
        return cpuRequest;
    }

    public void setCpuRequest(double cpuRequest) {
        this.cpuRequest = cpuRequest;
    }

    public double getMemRequest() {
        return memRequest;
    }

    public void setMemRequest(double memRequest) {
        this.memRequest = memRequest;
    }
}
