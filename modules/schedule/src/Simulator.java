import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class Simulator {

    /*CPU规格参数
    * 1mips = 4Mhz
    * Intel Xeon E5-2680 v2
    * 主频 2.8Ghz = 2800Mhz = 700mips
    * 10核
    * */

    /*
    * 内存规格参数
    * 64GB
    * */

    /*
    * 硬盘规格参数
    * 每台虚拟机50GB，故总共的大小为50GB*560 = 28TB = 30TB，故总存储大小为30TB
    * */

    /*
    * 数据中心规格参数
    * 20台物理机，CPU、内存采用上述规格，硬盘大小30TB
    *
    * */

    //物理机参数配置
    private int physicalVMCount = 20;//物理机数量
    private int coreCount = 10;//CPU核心数目
    private double cpuFrequency = 2800;//CPU频率，单位MHz
    private double memory = 65536;//内存大小，单位MB

    public void Run(){

        int num_user = 1; // number of cloud users
        Calendar calendar = Calendar.getInstance();
        boolean trace_flag = false;

        CloudSim.init(num_user, calendar, trace_flag);

        @SuppressWarnings("unused")
        Datacenter datacenter0 = createDatacenter("Datacenter_0");

        // #3 step: Create Broker
        DatacenterBroker broker = createBroker();
        int brokerId = broker.getId();



    }

    public Datacenter createDatacenter(String name){
        List<Host> hostList = new ArrayList<Host>();//物理主机列表
        List<Pe> peList = new ArrayList<Pe>();//每台物理机的CPU核心列表

        int mips = (int)cpuFrequency/4;

        int i = 0;
        for(i = 0;i<coreCount;i++){
            peList.add(new Pe(i, new PeProvisionerSimple(mips))); // need to store MIPS Rating
        }

        int hostId = 0;
        int ram = 65536; // host memory (MB)
        long storage = 10000000; // host storage
        int bw = 10000;

        for(i = 0;i<physicalVMCount;i++){
            hostId = i;
            hostList.add(new Host(hostId, new RamProvisionerSimple(ram),
                    new BwProvisionerSimple(bw), storage, peList,
                    new VmSchedulerTimeShared(peList)));
        }

        String arch = "x86"; // system architecture
        String os = "Linux"; // operating system
        String vmm = "Xen";
        double time_zone = 10.0; // time zone this resource located
        double cost = 3.0; // the cost of using processing in this resource
        double costPerMem = 0.05; // the cost of using memory in this resource
        double costPerStorage = 0.001; // the cost of using storage in this
        // resource
        double costPerBw = 0.001; // the cost of using bw in this resource

        //we are not adding SAN devices by now
        LinkedList<Storage> storageList = new LinkedList<Storage>();


        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem,
                costPerStorage, costPerBw);

        // 6. Finally, we need to create a PowerDatacenter object.
        Datacenter datacenter = null;
        try
        {
            datacenter = new Datacenter(name, characteristics,
                    new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return datacenter;

    }

    private static DatacenterBroker createBroker()
    {

        DatacenterBroker broker = null;
        try
        {
            broker = new DatacenterBroker("Broker");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return broker;
    }
}
