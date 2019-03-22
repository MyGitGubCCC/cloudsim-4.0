package org.cloudbus.cloudsim.container.resourceAllocators;


import org.cloudbus.cloudsim.container.core.ContainerDatacenter;
import org.cloudbus.cloudsim.container.core.ContainerHost;
import org.cloudbus.cloudsim.container.core.ContainerVm;

import java.util.List;
import java.util.Map;

/**
 * Created by sareh on 10/07/15.
 */
public abstract class ContainerVmAllocationPolicy {


    /**
     * The host list.
     */
    private List<? extends ContainerHost> containerHostList;

    /**
     * Allocates a new VmAllocationPolicy object.
     *
     * @param containerHostList Machines available in this Datacentre
     * @pre $none
     * @post $none
     */
    public ContainerVmAllocationPolicy(List<? extends ContainerHost> containerHostList) {
        setContainerHostList(containerHostList);
    }

    /**
     * Allocates a host for a given VM. The host to be allocated is the one that was already
     * reserved.
     * 为给定VM分配主机。要分配的主机是否是已经预留的主机。true表示可以分配，false表示不能
     * @param vm virtual machine which the host is reserved to
     * @return $true if the host could be allocated; $false otherwise
     * @pre $none
     * @post $none
     */
    public abstract boolean allocateHostForVm(ContainerVm vm);

    /**
     * Allocates a specified host for a given VM.
     * 为给定VM分配指定的主机。
     *
     * @param vm virtual machine which the host is reserved to
     * @return $true if the host could be allocated; $false otherwise
     * @pre $none
     * @post $none
     */
    public abstract boolean allocateHostForVm(ContainerVm vm, ContainerHost host);

    /**
     * Optimize allocation of the VMs according to current utilization.
     * 根据当前的使用情况优化vm的分配。
     * <p>
     * //     * @param vmList           the vm list
     * //     * @param utilizationBound the utilization bound
     * //     * @param time             the time
     *
     * @return the array list< hash map< string, object>>
     */
    public abstract List<Map<String, Object>> optimizeAllocation(List<? extends ContainerVm> vmList);

    /**
     * Releases the host used by a VM.
     * 释放VM使用的主机。从主机中移除
     * @param containerVm the vm
     * @pre $none
     * @post $none
     */
    public abstract void deallocateHostForVm(ContainerVm containerVm);

    /**
     * Get the host that is executing the given VM belonging to the given user.
     *
     * @param containerVm the vm
     * @return the Host with the given vmID and userID; $null if not found
     * @pre $none
     * @post $none
     */
    public abstract ContainerHost getHost(ContainerVm containerVm);

    /**
     * Get the host that is executing the given VM belonging to the given user.
     *
     * @param vmId   the vm id
     * @param userId the user id
     * @return the Host with the given vmID and userID; $null if not found
     * @pre $none
     * @post $none
     */
    public abstract ContainerHost getHost(int vmId, int userId);

    /**
     * Sets the host list.
     *
     * @param containerHostList the new host list
     */
    protected void setContainerHostList(List<? extends ContainerHost> containerHostList) {
        this.containerHostList = containerHostList;
    }

    /**
     * Gets the host list.
     *
     * @return the host list
     */
    @SuppressWarnings("unchecked")
    public <T extends ContainerHost> List<T> getContainerHostList() {
        return (List<T>) containerHostList;
    }

    public abstract void setDatacenter(ContainerDatacenter datacenter);


}



