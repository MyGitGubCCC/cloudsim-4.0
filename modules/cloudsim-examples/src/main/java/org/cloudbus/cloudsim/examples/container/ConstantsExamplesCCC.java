package org.cloudbus.cloudsim.examples.container;

import org.cloudbus.cloudsim.power.models.PowerModel;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G4Xeon3040;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G5Xeon3075;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerIbmX3550XeonX5670;

/**
 * In this class the specifications of the Cloudlets, Containers, VMs and Hosts are coded.
 * Regarding to the hosts, the powermodel of each type of the hosts are all included in this class.
 */
public class ConstantsExamplesCCC {

    /**
     * Simulation  parameters including the interval and limit
     */
    public static final boolean ENABLE_OUTPUT = true;
    public static final boolean OUTPUT_CSV = false;
    public static final double SCHEDULING_INTERVAL = 300.0D;
    public static final double SIMULATION_LIMIT = 87400.0D;
    /**
     * Cloudlet specs
     */
    public static final int CLOUDLET_LENGTH = 30;
    public static final int CLOUDLET_PES = 1;

    /**
     * Startup delay for VMs and the containers are mentioned here.
     */
    public static final double CONTAINER_STARTTUP_DELAY = 0.4;//the amount is in seconds
    public static final double VM_STARTTUP_DELAY = 100;//the amoun is in seconds

    /**
     * The available virtual machine types along with the specs.
     */

    public static final int VM_TYPES = 4;
    public static final double[] VM_MIPS = new double[]{1860 / 2, 2660 / 4, 2933 / 4, 2933 / 4};
    public static final int[] VM_PES = new int[]{1, 1, 1, 1};
    public static final float[] VM_RAM = new float[] {(float)400, (float) 1024, (float) 1024, (float) 400};//**MB*
    public static final int[] VM_BW = new int[]{80000,120000,110000,250000};
    public static final int VM_SIZE = 5000;//5G

    /**
     * The available types of container along with the specs.
     */

    public static final int CONTAINER_TYPES = 3;
    public static final int[] CONTAINER_MIPS = new int[]{186, 133, 146};
    public static final int[] CONTAINER_PES = new int[]{1, 1, 1};
    public static final int[] CONTAINER_RAM = new int[]{64, 256, 128};
    public static final int[] CONTAINER_BW = new int[]{25000,12000,8000};

    /**
     * The available types of hosts along with the specs.
     *
     PowerModelSpecPowerHpProLiantMl110G4Xeon3040()
     (1 x [Xeon 3040 1860 MHz, 2 cores], 4GB)

     PowerModelSpecPowerHpProLiantMl110G5Xeon3075()
     1 x [Xeon 3075 2660 MHz, 2 cores], 4GB

     PowerModelSpecPowerIbmX3550XeonX5670()
     2 x [Xeon X5670 2933 MHz, 6 cores], 12GB
     */

    public static final int HOST_TYPES = 3;
    public static final int[] HOST_MIPS = new int[]{1860, 2660, 2933};
    public static final int[] HOST_PES = new int[]{2, 2, 12};
    public static final int[] HOST_RAM = new int[]{4096, 4096, 12288};
    public static final int HOST_BW = 1000000;//1Gbit/s
    public static final int HOST_STORAGE = 10000000;//10000GB
    public static final PowerModel[] HOST_POWER = new PowerModel[]{new PowerModelSpecPowerHpProLiantMl110G4Xeon3040(),
            new PowerModelSpecPowerHpProLiantMl110G5Xeon3075(), new PowerModelSpecPowerIbmX3550XeonX5670()};

    /**
     * The population of hosts, containers, and VMs are specified.
     * The containers population is equal to the cloudlets population as each cloudlet is mapped to each container.
     * However, depending on the simualtion scenario the container's population can also be different from cloudlet's
     * population.
     */


    public static final int NUMBER_HOSTS = 20;
    public static final int NUMBER_VMS = 50;
    public static final int NUMBER_CLOUDLETS = 200;

//-----------------------The Addresses


//    public static String logAddress;
//
//    public String getLogAddress() {
//        return logAddress;
//    }
//
//    public void setLogAddress(String logAddress) {
//        this.logAddress = logAddress;
//    }
//

    public ConstantsExamplesCCC() {
    }
}
