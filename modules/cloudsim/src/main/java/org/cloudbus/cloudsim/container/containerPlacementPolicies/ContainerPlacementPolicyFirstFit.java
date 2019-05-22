package org.cloudbus.cloudsim.container.containerPlacementPolicies;


import org.cloudbus.cloudsim.container.core.ContainerVm;
import org.cloudbus.cloudsim.container.core.PowerContainer;

import java.util.List;
import java.util.Set;

/**
 * Created by sareh fotuhi Piraghaj on 16/12/15.
 * For container placement First Fit policy.
 */

public class ContainerPlacementPolicyFirstFit extends ContainerPlacementPolicy {

    @Override
    public ContainerVm getContainerVm(List<ContainerVm> vmList, Object obj, Set<? extends ContainerVm> excludedVmList) {
        ContainerVm containerVm = null;
        PowerContainer container = (PowerContainer)obj;
        for (ContainerVm containerVm1 : vmList) {
            if (excludedVmList.contains(containerVm1)) {
                continue;
            }
            if (containerVm1.getContainerScheduler().getAvailableMips() < container.getWorkloadMips()
                    || containerVm1.getContainerRamProvisioner().getAvailableVmRam() <container.getRam()
                    || containerVm1.getContainerBwProvisioner().getAvailableVmBw() < container.getBw()){
                continue;
            }
            containerVm = containerVm1;
            break;
        }
        return containerVm;
    }

}
