package org.cloudbus.cloudsim.container.containerPlacementPolicies;

import org.cloudbus.cloudsim.container.core.ContainerVm;

import java.util.List;
import java.util.Set;

/**
 * Created by sareh fotuhi Piraghaj on 16/12/15.
 * For container placement Least-Full policy.保证满载的VM越少越好
 */
public class ContainerPlacementPolicyLeastFull extends ContainerPlacementPolicy{

    @Override
    public ContainerVm getContainerVm(List<ContainerVm> vmList, Object obj, Set<? extends ContainerVm> excludedVmList) {
        ContainerVm selectedVm = null;
        //先取Double的最大值
        double minMips = Double.MAX_VALUE;
        for (ContainerVm containerVm1 : vmList) {
            if (excludedVmList.contains(containerVm1)) {
                continue;
            }
            //找到可用mips最小的那台VM
            double containerUsage = containerVm1.getContainerScheduler().getAvailableMips();
            if ( containerUsage < minMips ) {
                minMips = containerUsage;
                selectedVm = containerVm1;

            }
           }
        return selectedVm;
    }
}
