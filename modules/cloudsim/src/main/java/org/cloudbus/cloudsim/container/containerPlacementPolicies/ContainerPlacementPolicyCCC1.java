package org.cloudbus.cloudsim.container.containerPlacementPolicies;

import org.cloudbus.cloudsim.container.core.ContainerVm;

import java.util.List;
import java.util.Set;

public class ContainerPlacementPolicyCCC1 extends ContainerPlacementPolicy{
    @Override
    public ContainerVm getContainerVm(List<ContainerVm> vmList, Object obj, Set<? extends ContainerVm> excludedVmList) {
        /**
         * 1、先找剩余maxMips小于百分之60的vm
         * 2、如果没有找到这样的vm，就找低于百分之70的vm
         * 3、如果还没找就找第一个合适的
         * 经过测试，结果比firstfit功耗下小的情况下很少，在200Host，300vm，1800Container时，功耗少些
         */
        ContainerVm selectedVm = null;

        for (ContainerVm containerVm1 : vmList) {
            if (excludedVmList.contains(containerVm1)) {
                continue;
            }
            //找到mips利用率在60%以下的那个VM
            double containerUsage = containerVm1.getContainerScheduler().getAvailableMips();
            double vmMipsUtilization = 1 - containerUsage/(containerVm1.getMips()*containerVm1.getNumberOfPes());
            if ( vmMipsUtilization < 0.6 ) {
                selectedVm = containerVm1;
                break;
            }
        }
        if (selectedVm == null){
            for (ContainerVm containerVm1 : vmList) {
                if (excludedVmList.contains(containerVm1)) {
                    continue;
                }
                //找到mips利用率在70%以下的那个VM
                double containerUsage = containerVm1.getContainerScheduler().getAvailableMips();
                double vmMipsUtilization = 1 - containerUsage/(containerVm1.getMips()*containerVm1.getNumberOfPes());
                if ( vmMipsUtilization < 0.7 ) {
                    selectedVm = containerVm1;
                    break;
                }
            }
        }
        if (selectedVm == null) {
            for (ContainerVm containerVm1 : vmList) {
                if (excludedVmList.contains(containerVm1)) {
                    continue;
                }
                selectedVm = containerVm1;
                break;
            }
        }

        return selectedVm;
    }
}
