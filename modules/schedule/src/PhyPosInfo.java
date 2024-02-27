public class PhyPosInfo {

    //记录某个虚拟机开启在某台物理机上

    private int vmID;//虚拟机ID,这个实际上相当于Main函数里的索引  vmIDArray的下标
    private int phyID;//物理机ID

    public int getVmID() {
        return vmID;
    }

    public void setVmID(int vmID) {
        this.vmID = vmID;
    }

    public int getPhyID() {
        return phyID;
    }

    public void setPhyID(int phyID) {
        this.phyID = phyID;
    }

    public PhyPosInfo clone(){
        PhyPosInfo clone_PhyPosInfo = new PhyPosInfo();
        clone_PhyPosInfo.setVmID(this.vmID);
        clone_PhyPosInfo.setPhyID(this.phyID);

        return clone_PhyPosInfo;
    }
}
