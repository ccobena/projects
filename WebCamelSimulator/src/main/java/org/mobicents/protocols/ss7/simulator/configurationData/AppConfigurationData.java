/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.simulator.configurationData;

import java.io.File;
import java.io.FileInputStream;

import javolution.text.TextBuilder;
import javolution.xml.XMLBinding;
import javolution.xml.XMLObjectReader;

/**
 * @author ccobena
 */

public class AppConfigurationData {

    private String APP_NAME = "SS7 SIMULATOR";
    //private Map<String, Object> inputParametersMap = new HashMap<String, Object>();
	//private Simulator simulator = null;

	private static final String CLASS_ATTRIBUTE = "type";
	private static final String PERSIST_CAMEL_FILE_NAME = "camel_configuration.xml";
	private static final String CAMEL_CONFIGURATION_DATA = "CamelConfigurationData";

	private static final String PERSIST_SIMULATOR_FILE_NAME = "simulator2.xml";
    private static final String SIMULATOR_CONFIGURATION_DATA = "configurationData";
	private String persistDir = null;
	private final TextBuilder persistCamelFile = TextBuilder.newInstance();
	private final TextBuilder persistSimulatorFile = TextBuilder.newInstance();
    private static final XMLBinding binding = new XMLBinding();
    public static String SIMULATOR_HOME_VAR = "SIMULATOR_HOME";
    private CamelConfigurationData camelConfigurationData = new CamelConfigurationData();
    private SimulatorConfigurationData simulatorConfigurationData = new SimulatorConfigurationData();

        public AppConfigurationData(String appName) throws Throwable {
            this.APP_NAME = appName;
        	String sim_home = System.getenv(AppConfigurationData.SIMULATOR_HOME_VAR);
            if (sim_home != null)
                sim_home += File.separator + "config";

        	loadSimulatorConfigurationDataFromXMLFile(sim_home);
            loadCamelConfigurationDataFromXMLFile (sim_home);

/*
        	simulator.openTest(this.inputParametersMap, this.camelConfigurationData);
        	boolean callCompleted = false;
        	while (!callCompleted){
        		Thread.sleep(200);
        	    if (simulator.getActualProgress() >= 100 || simulator.getReleaseCallReceived() || simulator.getestablishTemporaryConnectionReceived())
        	           callCompleted = true;
        	 }
*/
    }

    private void loadSimulatorConfigurationDataFromXMLFile(String persistDir) {

    	binding.setClassAttribute(CLASS_ATTRIBUTE);
        this.persistSimulatorFile.clear();

        if (persistDir != null) {
            this.persistSimulatorFile.append(persistDir).append(File.separator).append(APP_NAME).append("_")
                    .append(PERSIST_SIMULATOR_FILE_NAME);
        } else {
        	this.persistSimulatorFile.append(this.getClass().getClassLoader().getResource(APP_NAME + "_" + PERSIST_SIMULATOR_FILE_NAME).getFile());
        }

        File fn = new File(persistSimulatorFile.toString());

        XMLObjectReader reader = null;
        try {
            if (!fn.exists()) {
            	System.out.println("Error while reading the XML Simulator Configuration file : file not found: ");
                System.exit(1);
            }

            reader = XMLObjectReader.newInstance(new FileInputStream(fn));
            reader.setBinding(binding);
            this.setSimulatorConfigurationData(reader.read(SIMULATOR_CONFIGURATION_DATA, SimulatorConfigurationData.class));

            reader.close();

        } catch (Exception ex) {
        	ex.printStackTrace();
        }

    }

    private void loadCamelConfigurationDataFromXMLFile(String persistDir) {

    	this.persistDir = persistDir;
		binding.setClassAttribute(CLASS_ATTRIBUTE);
        this.persistCamelFile.clear();
        if (this.persistDir != null){
            this.persistCamelFile.append(persistDir).append(File.separator).append(APP_NAME).append("_")
                    .append(PERSIST_CAMEL_FILE_NAME);
        }
        else{
        	this.persistCamelFile.append(this.getClass().getClassLoader().getResource(APP_NAME + "_" + PERSIST_CAMEL_FILE_NAME).getFile());
        }
        File fn = new File(persistCamelFile.toString());

        XMLObjectReader reader = null;
        try {
            if (!fn.exists()) {
                System.out.println("Error while reading the XML Camel Configuration file : file not found: ");
                System.exit(1);
            }

            reader = XMLObjectReader.newInstance(new FileInputStream(fn));
            reader.setBinding(binding);
            this.setCamelConfigurationData(reader.read(CAMEL_CONFIGURATION_DATA, CamelConfigurationData.class));
            reader.close();
        } catch (Exception ex) {
              ex.printStackTrace();
        }
    }


	public CamelConfigurationData getCamelConfigurationData() {
		return camelConfigurationData;
	}


	public void setCamelConfigurationData(CamelConfigurationData camelConfigurationData) {
		this.camelConfigurationData = camelConfigurationData;
	}

	public SimulatorConfigurationData getSimulatorConfigurationData() {
		return simulatorConfigurationData;
	}

	public void setSimulatorConfigurationData(SimulatorConfigurationData simulatorConfigurationData) {
		this.simulatorConfigurationData = simulatorConfigurationData;
	}

}
