package org.cloudbus.cloudsim.container.containerSelectionPolicies;

import org.cloudbus.cloudsim.container.containerSelectionPolicies.PowerContainerSelectionPolicy;
import org.cloudbus.cloudsim.container.core.Container;
import org.cloudbus.cloudsim.container.core.PowerContainer;
import org.cloudbus.cloudsim.container.core.PowerContainerHost;
import org.cloudbus.cloudsim.container.core.PowerContainerHostUtilizationHistory;
import org.cloudbus.cloudsim.container.utils.Correlation;
import org.cloudbus.cloudsim.Log;
import java.util.List;

/**
 * Created by sareh on 7/08/15.
 * 根据主机与容器，选择他们相关性大的主机
 */
public class PowerContainerSelectionPolicyCor extends PowerContainerSelectionPolicy {
    /**
     * The fallback policy.
     */
    private PowerContainerSelectionPolicy fallbackPolicy;

    /**
     * Instantiates a new power container selection policy maximum correlation.
     *
     * @param fallbackPolicy the fallback policy
     */
    public PowerContainerSelectionPolicyCor(final PowerContainerSelectionPolicy fallbackPolicy) {
        super();
        setFallbackPolicy(fallbackPolicy);
    }

    /*
    * (non-Javadoc)
    *
    * @see PowerContainerSelectionPolicy#getContainerToMigrate
    */
    @Override
    public Container getContainerToMigrate(final PowerContainerHost host) {
        List<PowerContainer> migratableContainers = getMigratableContainers(host);
        if (migratableContainers.isEmpty()) {
            return null;
        }
        //下面的getContainer方法就是为了从host中的migratableContainers找出最合适的Container
        Container container= getContainer(migratableContainers, host);
        migratableContainers.clear();
        if (container!= null) {
//            Log.printConcatLine("We have to migrate the container with ID", container.getId());
            return container;
        } else {
            return getFallbackPolicy().getContainerToMigrate(host);
        }
    }

    /**
     * Gets the fallback policy.
     *
     * @return the fallback policy
     */
    public PowerContainerSelectionPolicy getFallbackPolicy() {
        return fallbackPolicy;
    }


    /**
     * Sets the fallback policy.
     *
     * @param fallbackPolicy the new fallback policy
     */
    public void setFallbackPolicy(final PowerContainerSelectionPolicy fallbackPolicy) {
        this.fallbackPolicy = fallbackPolicy;
    }

    //根据相关性算法来找出Container
    public Container getContainer(List<PowerContainer> migratableContainers, PowerContainerHost host) {

        double[] corResult = new double[migratableContainers.size()];
        Correlation correlation = new Correlation();
        int i = 0;
        double maxValue = -2.0;
        int id = -1;
        if (host instanceof PowerContainerHostUtilizationHistory) {

            double[] hostUtilization = ((PowerContainerHostUtilizationHistory) host).getUtilizationHistory();
            for (Container container : migratableContainers) {
                double[] containerUtilization = ((PowerContainer) container).getUtilizationHistoryList();
                //通过主机上的历史利用率和容器的历史利用率来计算他们的相关性，比如如果容器利用率高，主机的利用率也高，
                //那么他们的相关性肯定比较大
                double cor = correlation.getCor(hostUtilization, containerUtilization);
                if (Double.isNaN(cor)) {
                    cor = -3;
                }
                corResult[i] = cor;
                //找出相关性最大的那个id
                if(corResult[i] > maxValue){
                	maxValue = corResult[i];
                	id = i;
                }
                
                i++;
            }

        }

        if (id == -1) {
            Log.printConcatLine("Problem with correlation list.");
        }

        return migratableContainers.get(id);

    }


}
