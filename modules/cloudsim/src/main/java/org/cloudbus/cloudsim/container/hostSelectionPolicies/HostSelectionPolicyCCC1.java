package org.cloudbus.cloudsim.container.hostSelectionPolicies;

import org.cloudbus.cloudsim.container.core.ContainerHost;
import org.cloudbus.cloudsim.container.core.PowerContainerHostUtilizationHistory;
import org.cloudbus.cloudsim.core.CloudSim;

import java.util.List;
import java.util.Set;

/**
 * Created by sareh on 11/08/15.
 */
public class HostSelectionPolicyCCC1 extends HostSelectionPolicy {

    @Override
    public ContainerHost getHost(List<ContainerHost> hostList, Object obj,Set<? extends ContainerHost> excludedHostList) {
        ContainerHost selectedHost = null;
        if(CloudSim.clock() >1.0){
        double maxUsage = Double.MIN_VALUE;
        for (ContainerHost host : hostList) {
            if (excludedHostList.contains(host)) {
                continue;
            }

            if (host instanceof PowerContainerHostUtilizationHistory) {
                double hostUtilization= ((PowerContainerHostUtilizationHistory) host).getUtilizationOfCpu();
                if (hostUtilization < 0.7) {
                    selectedHost = host;
                    break;
                }


            }
        }
        if (selectedHost ==null) {
            for (ContainerHost host : hostList) {
                if (excludedHostList.contains(host)) {
                    continue;
                }

                if (host instanceof PowerContainerHostUtilizationHistory) {
                    double hostUtilization = ((PowerContainerHostUtilizationHistory) host).getUtilizationOfCpu();
                    if (hostUtilization < 0.8) {
                        selectedHost = host;
                        break;
                    }


                }
            }
        }
            if (selectedHost ==null) {
                for (ContainerHost host : hostList) {
                    if (excludedHostList.contains(host)) {
                        continue;
                    }
                    selectedHost = host;
                }
            }

        return selectedHost;
    }else {

//            At the simulation start all the VMs by leastFull algorithms.

            selectedHost = new HostSelectionPolicyFirstFit().getHost(hostList,obj ,excludedHostList);

            return selectedHost;
        }



    }


}
