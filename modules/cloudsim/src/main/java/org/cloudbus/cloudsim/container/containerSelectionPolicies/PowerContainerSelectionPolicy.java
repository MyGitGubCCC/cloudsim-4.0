package org.cloudbus.cloudsim.container.containerSelectionPolicies;


import org.cloudbus.cloudsim.container.core.Container;
import org.cloudbus.cloudsim.container.core.PowerContainer;
import org.cloudbus.cloudsim.container.core.PowerContainerHost;
import org.cloudbus.cloudsim.container.core.PowerContainerVm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sareh on 31/07/15.
 */
public abstract class PowerContainerSelectionPolicy {

    /**
     * Gets the containers to migrate.
     *
     * @param host the host
     * @return the container to migrate
     */
    public abstract Container getContainerToMigrate(PowerContainerHost host);

    /**
     * Gets the migratable containers.获取可以迁移的容器
     *
     * @param host the host
     * @return the migratable containers
     */
    protected List<PowerContainer> getMigratableContainers(PowerContainerHost host) {
        List<PowerContainer> migratableContainers= new ArrayList<>();
        for (PowerContainerVm vm : host.<PowerContainerVm> getVmList()) {
            //检查vm是否在迁移中
            if (!vm.isInMigration()) {
                for (Container container: vm.getContainerList()){
                    //如果容器没有在迁移，并且该容器上的虚拟机也并没有把该容器放到迁移列表中。
                    if(!container.isInMigration() && !vm.getContainersMigratingIn().contains(container)){
                        migratableContainers.add((PowerContainer) container);}

                }



            }
        }
        return migratableContainers;
    }

}
