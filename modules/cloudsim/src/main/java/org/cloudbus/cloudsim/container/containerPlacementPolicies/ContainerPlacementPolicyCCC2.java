package org.cloudbus.cloudsim.container.containerPlacementPolicies;

import org.cloudbus.cloudsim.container.core.Container;
import org.cloudbus.cloudsim.container.core.ContainerVm;
import org.cloudbus.cloudsim.container.core.PowerContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ContainerPlacementPolicyCCC2 extends ContainerPlacementPolicy{

    public static final int HOST_MIPS_AVG = 14745;
    public static final int HOST_RAM_AVG = 6827;
    public static final int HOST_BW_AVG = 1000000;

    @Override
    public ContainerVm getContainerVm(List<ContainerVm> vmList, Object obj, Set<? extends ContainerVm> excludedVmList) {
        /**
         * 1、对虚拟机进行分类，cpu、内存、带宽
         * 2、判断容器
         * 3、选择和容器一个类的vm
         * 4、计算容器资源占vm剩余资源的百分比和，再加上是否存在基础镜像
         *
         */

        ContainerVm selectedVm = null;

        List<ContainerVm> containerVmListForCPU = new ArrayList<>();
        List<ContainerVm> containerVmListForRAM = new ArrayList<>();
        List<ContainerVm> containerVmListForBW = new ArrayList<>();


        //1、对虚拟机进行分类，cpu、内存、带宽
        for (ContainerVm containerVm : vmList) {
            double cpuVMtoHost = containerVm.getContainerScheduler().getAvailableMips()*1.0 / HOST_MIPS_AVG;
            double ramVMtoHost = containerVm.getContainerRamProvisioner().getAvailableVmRam()*1.0 / HOST_RAM_AVG;
            double bwVMtoHost = containerVm.getContainerBwProvisioner().getAvailableVmBw()*1.0 / HOST_BW_AVG;
            if (cpuVMtoHost >= ramVMtoHost && cpuVMtoHost >= bwVMtoHost)
                containerVmListForCPU.add(containerVm);
            else if (ramVMtoHost >= cpuVMtoHost && ramVMtoHost >= bwVMtoHost)
                containerVmListForRAM.add(containerVm);
            else if (bwVMtoHost >= cpuVMtoHost && bwVMtoHost >= ramVMtoHost)
                containerVmListForBW.add(containerVm);
        }

        //2、判断容器
        int containerType = 0;
        if (obj instanceof PowerContainer) {
            PowerContainer container = (PowerContainer)obj;
            double cpuContainertoHost = container.getWorkloadMips()*1.0 / HOST_MIPS_AVG;
            double ramContainertoHost = container.getRam()*1.0 / HOST_RAM_AVG;
            double bwContainertoHost = container.getBw()*1.0 / HOST_BW_AVG;
            //3、选择和容器一个类的vm
            if (cpuContainertoHost >= ramContainertoHost && cpuContainertoHost >= bwContainertoHost){
                //从CPU型里面找最合适的vm
                selectedVm = getVm(containerVmListForCPU,container,excludedVmList);
            }
            else if (ramContainertoHost >= cpuContainertoHost && ramContainertoHost >= bwContainertoHost) {
                //从ram型里面找最合适的vm
                selectedVm = getVm(containerVmListForRAM,container,excludedVmList);
            }
            else if (bwContainertoHost >= cpuContainertoHost && bwContainertoHost >= ramContainertoHost) {
                //从bw型里面找最合适的vm
                selectedVm = getVm(containerVmListForBW,container,excludedVmList);
            }
        }
        return selectedVm;
    }

    //4、计算容器资源占vm剩余资源的百分比和
    private ContainerVm getVm(List<ContainerVm> vmList,PowerContainer container,Set<? extends ContainerVm> excludedVmList) {
        ContainerVm containerVm = null;
        double maxCCC = Double.MIN_VALUE;
        for (ContainerVm containerVm1 : vmList) {
            if (excludedVmList.contains(containerVm1)) {
                continue;
            }
            if (containerVm1.getContainerScheduler().getAvailableMips() < container.getWorkloadMips()
                    || containerVm1.getContainerRamProvisioner().getAvailableVmRam() <container.getRam()
                    || containerVm1.getContainerBwProvisioner().getAvailableVmBw() < container.getBw()){
                continue;
            }
            double containerCCC = container.getWorkloadMips() *1.0 / containerVm1.getContainerScheduler().getAvailableMips()
                    + container.getRam() *1.0 / containerVm1.getContainerRamProvisioner().getAvailableVmRam()
                    + container.getBw() *1.0 / containerVm1.getContainerBwProvisioner().getAvailableVmBw();
            if (containerCCC > maxCCC){
                maxCCC = containerCCC;
                containerVm = containerVm1;
            }
        }
        return containerVm;
    }

}
