/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim;

/**
 * The UtilizationModelNull class is a simple model, according to which a Cloudlet always require
 * zero capacity for a given resource all the time.
 * 这是一个简单的模型类，根据这个模型，Cloudlet始终要求给定资源的容量为零。
 * 
 * @author Anton Beloglazov
 * @since CloudSim Toolkit 2.0
 */
public class UtilizationModelNull implements UtilizationModel {

	@Override
	public double getUtilization(double time) {
		return 0;
	}

}
