package org.cloudbus.cloudsim.examples.container;


/*
 * Title:        ContainerCloudSimExample1 Toolkit
 * Description:  ContainerCloudSimExample1 (containerized cloud simulation) Toolkit for Modeling and Simulation
 *               of Containerized Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */


import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModelNull;
import org.cloudbus.cloudsim.container.containerProvisioners.ContainerBwProvisionerSimple;
import org.cloudbus.cloudsim.container.containerProvisioners.ContainerPe;
import org.cloudbus.cloudsim.container.containerProvisioners.ContainerRamProvisionerSimple;
import org.cloudbus.cloudsim.container.containerProvisioners.CotainerPeProvisionerSimple;
import org.cloudbus.cloudsim.container.containerVmProvisioners.ContainerVmBwProvisionerSimple;
import org.cloudbus.cloudsim.container.containerVmProvisioners.ContainerVmPe;
import org.cloudbus.cloudsim.container.containerVmProvisioners.ContainerVmPeProvisionerSimple;
import org.cloudbus.cloudsim.container.containerVmProvisioners.ContainerVmRamProvisionerSimple;
import org.cloudbus.cloudsim.container.core.*;
import org.cloudbus.cloudsim.container.hostSelectionPolicies.*;
import org.cloudbus.cloudsim.container.resourceAllocatorMigrationEnabled.PowerContainerVmAllocationPolicyMigrationAbstractHostSelection;
import org.cloudbus.cloudsim.container.resourceAllocators.ContainerAllocationPolicy;
import org.cloudbus.cloudsim.container.resourceAllocators.ContainerVmAllocationPolicy;
import org.cloudbus.cloudsim.container.resourceAllocators.PowerContainerAllocationPolicySimple;
import org.cloudbus.cloudsim.container.schedulers.ContainerCloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.container.schedulers.ContainerSchedulerTimeSharedOverSubscription;
import org.cloudbus.cloudsim.container.schedulers.ContainerVmSchedulerTimeSharedOverSubscription;
import org.cloudbus.cloudsim.container.utils.IDs;
import org.cloudbus.cloudsim.container.vmSelectionPolicies.PowerContainerVmSelectionPolicy;
import org.cloudbus.cloudsim.container.vmSelectionPolicies.PowerContainerVmSelectionPolicyMaximumUsage;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.power.Helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple example showing how to create a data center with one host, one VM, one container and run one cloudlet on it.
 */
public class ContainerCloudSimExample1 {

    /**
     * The cloudlet list.
     */
    private static List<ContainerCloudlet> cloudletList;

    /**
     * The vmlist.
     */
    private static List<ContainerVm> vmList;

    /**
     * The vmlist.
     */

    private static List<Container> containerList;

    /**
     * The hostList.
     */

    private static List<ContainerHost> hostList;

    /**
     * Creates main() to run this example.
     *
     * @param args the args
     */

    public static void main(String[] args) {
        Log.printLine("Starting ContainerCloudSimExample1...");

        try {
            /**
             * number of cloud Users
             */
            int num_user = 1;
            /**
             *  The fields of calender have been initialized with the current date and time.
             */
            Calendar calendar = Calendar.getInstance();
            /**
             * Deactivating the event tracing
             */
            boolean trace_flag = false;
            /**
             * 1- Like CloudSim the first step is initializing the CloudSim Package before creating any entities.
             * 与CloudSim一样，第一步是在创建任何实体之前初始化CloudSim包
             * 初始化后，将CloudSim中entities和entitiesByName两个List分别
             * 加入了CloudSimShutdown和CloudInformationService两个实体
             *
             */


            CloudSim.init(num_user, calendar, trace_flag);
            /**
             * 2-  Defining the container allocation Policy. This policy determines how Containers are
             * allocated to VMs in the data center.
             *定义容器分配策略。此策略确定如何将容器分配给数据中心中的vm。
             */


            ContainerAllocationPolicy containerAllocationPolicy = new PowerContainerAllocationPolicySimple();

            /**
             * 3-  Defining the VM selection Policy. This policy determines which VMs should be selected for migration
             * when a host is identified as over-loaded.
             * 定义VM选择策略。此策略确定当主机被标识为过载时，应该选择哪些vm进行迁移。
             *
             */

            PowerContainerVmSelectionPolicy vmSelectionPolicy = new PowerContainerVmSelectionPolicyMaximumUsage();


            /**
             * 4-  Defining the host selection Policy. This policy determines which hosts should be selected as
             * migration destination.
             * 定义host选择策略。此策略确定应选择哪些主机作为迁移目的地。
             *
             */
            HostSelectionPolicy hostSelectionPolicy = new HostSelectionPolicyFirstFit();
            /**
             * 5- Defining the thresholds for selecting the under-utilized and over-utilized hosts.
             */

            double overUtilizationThreshold = 0.80;
            double underUtilizationThreshold = 0.70;
            /**
             * 6- The host list is created considering the number of hosts, and host types which are specified
             * in the {@link ConstantsExamples}.
             */
            hostList = new ArrayList<ContainerHost>();
            hostList = createHostList(ConstantsExamples.NUMBER_HOSTS);
            cloudletList = new ArrayList<ContainerCloudlet>();
            vmList = new ArrayList<ContainerVm>();
            /**
             * 7- The container allocation policy  which defines the allocation of VMs to co ntainers.
             */
            ContainerVmAllocationPolicy vmAllocationPolicy = new
                    PowerContainerVmAllocationPolicyMigrationAbstractHostSelection(hostList, vmSelectionPolicy,
                    hostSelectionPolicy, overUtilizationThreshold, underUtilizationThreshold);
            /**
             * 8- The overbooking factor for allocating containers to VMs. This factor is used by the broker for the
             * allocation process.
             * 为vm分配容器的超预定因素。代理将此因子用于分配流程。
             */
            int overBookingFactor = 80;
            ContainerDatacenterBroker broker = createBroker(overBookingFactor);
            int brokerId = broker.getId();
            /**
             * 9- Creating the cloudlet, container and VM lists for submitting to the broker.
             */
            //一个任务对应一个虚拟机，所以都用NUMBER_CLOUDLETS
            cloudletList = createContainerCloudletList(brokerId, ConstantsExamples.NUMBER_CLOUDLETS);
            containerList = createContainerList(brokerId, ConstantsExamples.NUMBER_CLOUDLETS);
            vmList = createVmList(brokerId, ConstantsExamples.NUMBER_VMS);
            /**
             * 10- The address for logging the statistics of the VMs, containers in the data center.
             * 记录数据中心中vm、容器的统计信息的地址。
             * 这个信息在本项目的~/Results下面
             */
            String logAddress = "~/Results";

            /**
             *
             * @SuppressWarnings("unused"),用于抑制编译器产生警告信息。不加注释，e会出现还未被引用的警告
             * 因为在创建的过程中就把数据中心的实体初始化到CloudSim的entities中了，所以e没有掉用数据中心也有数据
             * */

            @SuppressWarnings("unused")
			PowerContainerDatacenter e = (PowerContainerDatacenter) createDatacenter("datacenter",
                    PowerContainerDatacenterCM.class, hostList, vmAllocationPolicy, containerAllocationPolicy,
                    getExperimentName("ContainerCloudSimExample-1", String.valueOf(overBookingFactor)),
                    ConstantsExamples.SCHEDULING_INTERVAL, logAddress,
                    ConstantsExamples.VM_STARTTUP_DELAY, ConstantsExamples.CONTAINER_STARTTUP_DELAY);


            /**
             * 11- Submitting the cloudlet's , container's , and VM's lists to the broker.
             */
            broker.submitCloudletList(cloudletList.subList(0, containerList.size()));
            broker.submitContainerList(containerList);
            broker.submitVmList(vmList);
            /**
             * 12- Determining the simulation termination time according to the cloudlet's workload.
             * 根据cloudlet的工作负载确定模拟终止时间。在86400.00后终止模拟，不再继续。
             */
            CloudSim.terminateSimulation(86400.00);
            /**
             * 13- Starting the simualtion.
             */
            CloudSim.startSimulation();
            /**
             * 14- Stopping the simualtion.
             */
            CloudSim.stopSimulation();
            /**
             * 15- Printing the results when the simulation is finished.
             */
            List<ContainerCloudlet> newList = broker.getCloudletReceivedList();
            printCloudletList(newList);

            Log.printLine("ContainerCloudSimExample1 finished!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("Unwanted errors happen");
        }
    }



    /**
     * It creates a specific name for the experiment which is used for creating the Log address folder.
     * 它为实验创建一个特定的名称，该名称用于创建日志地址文件夹。
     */

    private static String getExperimentName(String... args) {
        StringBuilder experimentName = new StringBuilder();

        for (int i = 0; i < args.length; ++i) {
            if (!args[i].isEmpty()) {
                if (i != 0) {
                    experimentName.append("_");
                }

                experimentName.append(args[i]);
            }
        }

        return experimentName.toString();
    }

    /**
     * Creates the broker.
     *
     * @param overBookingFactor
     * @return the datacenter broker
     */
    private static ContainerDatacenterBroker createBroker(int overBookingFactor) {

        ContainerDatacenterBroker broker = null;

        try {
            broker = new ContainerDatacenterBroker("Broker", overBookingFactor);
        } catch (Exception var2) {
            var2.printStackTrace();
            System.exit(0);
        }

        return broker;
    }

    /**
     * Prints the Cloudlet objects.
     *
     * @param list list of Cloudlets
     */
    private static void printCloudletList(List<ContainerCloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;

        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
                + "Data center ID" + indent + "VM ID" + indent + "Time" + indent
                + "Start Time" + indent + "Finish Time");

        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getCloudletStatusString() == "Success") {
                Log.print("SUCCESS");

                Log.printLine(indent + indent + cloudlet.getResourceId()
                        + indent + indent + indent + cloudlet.getVmId()
                        + indent + indent
                        + dft.format(cloudlet.getActualCPUTime()) + indent
                        + indent + dft.format(cloudlet.getExecStartTime())
                        + indent + indent
                        + dft.format(cloudlet.getFinishTime()));
            }
        }
    }

    /**
     * Create the Virtual machines and add them to the list
     *
     * @param brokerId
     * @param containerVmsNumber
     */
    private static ArrayList<ContainerVm> createVmList(int brokerId, int containerVmsNumber) {
        ArrayList<ContainerVm> containerVms = new ArrayList<ContainerVm>();

        for (int i = 0; i < containerVmsNumber; ++i) {
            ArrayList<ContainerPe> peList = new ArrayList<ContainerPe>();
            //创建虚拟机时候，虚拟机被分成了4类
            int vmType = i / (int) Math.ceil((double) containerVmsNumber / 4.0D);
            for (int j = 0; j < ConstantsExamples.VM_PES[vmType]; ++j) {
                peList.add(new ContainerPe(j,
                        new CotainerPeProvisionerSimple((double) ConstantsExamples.VM_MIPS[vmType])));
            }
            containerVms.add(new PowerContainerVm(IDs.pollId(ContainerVm.class), brokerId,
                    (double) ConstantsExamples.VM_MIPS[vmType], (float) ConstantsExamples.VM_RAM[vmType],
                    ConstantsExamples.VM_BW, ConstantsExamples.VM_SIZE, "Xen",
                    new ContainerSchedulerTimeSharedOverSubscription(peList),
                    new ContainerRamProvisionerSimple(ConstantsExamples.VM_RAM[vmType]),
                    new ContainerBwProvisionerSimple(ConstantsExamples.VM_BW),
                    peList, ConstantsExamples.SCHEDULING_INTERVAL));


        }

        return containerVms;
    }

    /**
     * Create the host list considering the specs listed in the {@link ConstantsExamples}.
     *
     * @param hostsNumber
     * @return
     */


    public static List<ContainerHost> createHostList(int hostsNumber) {
        ArrayList<ContainerHost> hostList = new ArrayList<ContainerHost>();
        for (int i = 0; i < hostsNumber; ++i) {
            int hostType = i / (int) Math.ceil((double) hostsNumber / 3.0D);
            ArrayList<ContainerVmPe> peList = new ArrayList<ContainerVmPe>();
            for (int j = 0; j < ConstantsExamples.HOST_PES[hostType]; ++j) {
                peList.add(new ContainerVmPe(j,
                        new ContainerVmPeProvisionerSimple((double) ConstantsExamples.HOST_MIPS[hostType])));
            }

            hostList.add(new PowerContainerHostUtilizationHistory(IDs.pollId(ContainerHost.class),
                    new ContainerVmRamProvisionerSimple(ConstantsExamples.HOST_RAM[hostType]),
                    new ContainerVmBwProvisionerSimple(1000000L), 1000000L, peList,
                    new ContainerVmSchedulerTimeSharedOverSubscription(peList),
                    ConstantsExamples.HOST_POWER[hostType]));
        }

        return hostList;
    }


    /**
     * Create the data center
     *
     * @param name
     * @param datacenterClass
     * @param hostList
     * @param vmAllocationPolicy
     * @param containerAllocationPolicy
     * @param experimentName
     * @param logAddress
     * @return
     * @throws Exception
     */

    public static ContainerDatacenter createDatacenter(String name, Class<? extends ContainerDatacenter> datacenterClass,
                                                       List<ContainerHost> hostList,
                                                       ContainerVmAllocationPolicy vmAllocationPolicy,
                                                       ContainerAllocationPolicy containerAllocationPolicy,
                                                       String experimentName, double schedulingInterval, String logAddress, double VMStartupDelay,
                                                       double ContainerStartupDelay) throws Exception {
       /* String arch = "x86";
        String os = "Linux";
        String vmm = "Xen";
        //定义时区，构造方法中好像没用，不知道是不是这样的。
        double time_zone = 10.0D;
        //定义机器每秒钟成本大小
        double cost = 3.0D;
        //下面是定义每秒钟使用内存、存储、带宽的成本
        double costPerMem = 0.05D;
        double costPerStorage = 0.001D;
        double costPerBw = 0.0D;
        ContainerDatacenterCharacteristics characteristics = new
                ContainerDatacenterCharacteristics(arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage,
                costPerBw);
        ContainerDatacenter datacenter = new PowerContainerDatacenterCM(name, characteristics, vmAllocationPolicy,
                containerAllocationPolicy, new LinkedList<Storage>(), schedulingInterval, experimentName, logAddress,
                VMStartupDelay, ContainerStartupDelay);

        return datacenter;*/
       //下面调用HelperEx创建数据中心的静态方法，与上面注释掉的代码一样，避免代码重复，调用了HelperEx.createDatacenter(...);
        return HelperEx.createDatacenter(name,datacenterClass,hostList,vmAllocationPolicy,containerAllocationPolicy,experimentName,schedulingInterval,
                logAddress,VMStartupDelay,ContainerStartupDelay);
    }

    /**
     * create the containers for hosting the cloudlets and binding them together.
     *
     * @param brokerId
     * @param containersNumber
     * @return
     */

    public static List<Container> createContainerList(int brokerId, int containersNumber) {
        ArrayList<Container> containers = new ArrayList<Container>();
        //根据容器的数量，把容器分成三类，每一类给予他们不同的属性，三种定义在ConstantsExamples中，分别是PES、RAM和BW的不同
        for (int i = 0; i < containersNumber; ++i) {
            int containerType = i / (int) Math.ceil((double) containersNumber / 3.0D);

            containers.add(new PowerContainer(IDs.pollId(Container.class), brokerId, (double) ConstantsExamples.CONTAINER_MIPS[containerType], ConstantsExamples.
                    CONTAINER_PES[containerType], ConstantsExamples.CONTAINER_RAM[containerType], ConstantsExamples.CONTAINER_BW, 0L, "Xen",
                    new ContainerCloudletSchedulerDynamicWorkload(ConstantsExamples.CONTAINER_MIPS[containerType], ConstantsExamples.CONTAINER_PES[containerType]), ConstantsExamples.SCHEDULING_INTERVAL));
        }

        return containers;
    }

    /**
     * Creating the cloudlet list that are going to run on containers
     *
     * @param brokerId
     * @param numberOfCloudlets
     * @return
     * @throws FileNotFoundException
     */
    public static List<ContainerCloudlet> createContainerCloudletList(int brokerId, int numberOfCloudlets)
            throws FileNotFoundException {
        String inputFolderName = ContainerCloudSimExample1.class.getClassLoader().getResource("workload/planetlab").getPath();
        ArrayList<ContainerCloudlet> cloudletList = new ArrayList<ContainerCloudlet>();
        long fileSize = 300L;
        long outputSize = 300L;
        UtilizationModelNull utilizationModelNull = new UtilizationModelNull();
        java.io.File inputFolder1 = new java.io.File(inputFolderName);
        java.io.File[] files1 = inputFolder1.listFiles();
        int createdCloudlets = 0;
        for (java.io.File aFiles1 : files1) {
            java.io.File inputFolder = new java.io.File(aFiles1.toString());
            java.io.File[] files = inputFolder.listFiles();
            for (int i = 0; i < files.length; ++i) {
                if (createdCloudlets < numberOfCloudlets) {
                    ContainerCloudlet cloudlet = null;

                    try {
                        //这个任务不考虑内存和带宽的使用情况,PE模型使用负载文件planetLab里面的文件。
                        cloudlet = new ContainerCloudlet(IDs.pollId(ContainerCloudlet.class), ConstantsExamples.CLOUDLET_LENGTH, 1,
                                fileSize, outputSize,
                                new UtilizationModelPlanetLabInMemoryExtended(files[i].getAbsolutePath(), 300.0D),
                                utilizationModelNull, utilizationModelNull);
                    } catch (Exception var13) {
                        var13.printStackTrace();
                        System.exit(0);
                    }

                    cloudlet.setUserId(brokerId);
                    cloudletList.add(cloudlet);
                    createdCloudlets += 1;
                } else {

                    return cloudletList;
                }
            }

        }
        return cloudletList;
    }

}
