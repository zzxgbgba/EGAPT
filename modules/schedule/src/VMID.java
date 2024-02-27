public class VMID {

    private int lessonID = 0;//课程号
    private int vmID = 0;//虚拟机在此课程下的ID号
    private int isLocked = 0; //若设置为锁定，则调度将不对此虚拟机进行处理，维持虚拟机的初期调度情况，用于容错策略的判断

    public boolean equalsVMID(VMID other){
        if(this.lessonID==other.getLessonID()&&this.vmID==other.getVmID())
        {
            return true;
        }

        return false;
    }

    public int getLessonID() {
        return lessonID;
    }

    public void setLessonID(int lessonID) {
        this.lessonID = lessonID;
    }

    public int getVmID() {
        return vmID;
    }

    public void setVmID(int vmID) {
        this.vmID = vmID;
    }

    public int getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(int isLocked) {
        this.isLocked = isLocked;
    }
}
