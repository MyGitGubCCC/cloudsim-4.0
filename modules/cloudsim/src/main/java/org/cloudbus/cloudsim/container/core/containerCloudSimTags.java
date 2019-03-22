package org.cloudbus.cloudsim.container.core;

public class containerCloudSimTags {
    /**
     * 下面cloudsim消息传递的信息标志符
     * Starting constant value for network-related tags
     **/
    private static final int ContainerSimBASE = 400;
    /**
     * Denotes the receiving of a cloudlet  in the data center broker
     * entity.
     * 表示在数据中心代理实体中接收cloudlet。
     */
    public static final int FIND_VM_FOR_CLOUDLET = ContainerSimBASE + 1;

    /**
     * Denotes the creating a new VM is required in the data center.
     * Invoked in the data center broker.
     * 表示数据中心需要创建一个新的VM。
     * 在数据中心代理中调用。
     */
    public static final int CREATE_NEW_VM = ContainerSimBASE + 2;
    /**
     * Denotes the containers are submitted to the data center.
     * Invoked in the data center broker.
     * 表示容器已提交到数据中心。
     * 在数据中心代理中调用。
     */
    public static final int CONTAINER_SUBMIT = ContainerSimBASE + 3;

    /**
     * Denotes the containers are created in the data center.
     * Invoked in the data center.
     * 表示容器已经在数据中心中创建好。
     * 在数据中心中调用。
     */
    public static final int CONTAINER_CREATE_ACK = ContainerSimBASE + 4;
    /**
     * Denotes the containers are migrated to another Vm.
     * Invoked in the data center.
     * 表示容器已迁移到另一个Vm。
     * 在数据中心中调用。
     */
    public static final int CONTAINER_MIGRATE = ContainerSimBASE + 10;
    /**
     * Denotes a new VM is created in data center by the local scheduler
     * Invoked in the data center.
     * 表示本地调度程序在数据中心中创建了一个新VM。
     * 在数据中心中调用。
     */
    public static final int VM_NEW_CREATE = ContainerSimBASE + 11;


    private containerCloudSimTags() {
        // TODO Auto-generated constructor stub
        /** Private Constructor
         * 这个Tags不能被实例化，所以每次实例化都抛出异常
         * */
        throw new UnsupportedOperationException("ContainerCloudSim Tags cannot be instantiated");

    }
}
