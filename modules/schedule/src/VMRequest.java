import java.text.SimpleDateFormat;
import java.util.Date;

public class VMRequest {
    //虚拟机请求

    private int vmID;//虚拟机ID
    private double cpuRequest;//CPU资源请求大小
    private double memRequest;//内存资源请求大小
    private Date startDate;//虚拟机的课程周期开始点
    private Date endDate;//虚拟机的课程周期结束点

    public VMRequest(int vmID,double cpuRequest,double memRequest,Date startDate,Date endDate){
        setVmID(vmID);
        setCpuRequest(cpuRequest);
        setMemRequest(memRequest);
        setStartDate(startDate);
        setEndDate(endDate);
    }

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStartDateString(){//返回虚拟机课程开启时间
        SimpleDateFormat bartDateFormat = new SimpleDateFormat
                ("EEEE-MMMM-dd-yyyy");
        return bartDateFormat.format(startDate);
    }

    public String getEndDateString(){//返回虚拟机课程结束时间
        SimpleDateFormat bartDateFormat = new SimpleDateFormat
                ("EEEE-MMMM-dd-yyyy");
        return bartDateFormat.format(endDate);
    }
}
