/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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


import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SimulatorConfigurationData {

    public static final String SCCP = "sccp";
    public static final String TEST_CAP_SSF = "testCapSsf";
    public static final String CAP = "cap";

    private SccpConfigurationData sccpConfigurationData = new SccpConfigurationData();
    private CapSsfConfigurationData testCapSsfConfigurationData = new CapSsfConfigurationData();
    private CapConfigurationData capConfigurationData = new CapConfigurationData();

    public SccpConfigurationData getSccpConfigurationData() {
        return sccpConfigurationData;
    }

    public void setSccpConfigurationData(SccpConfigurationData sccpConfigurationData) {
        this.sccpConfigurationData = sccpConfigurationData;
    }

    public CapConfigurationData getCapConfigurationData() {
        return capConfigurationData;
    }

    public void setCapConfigurationData(CapConfigurationData capConfigurationData) {
        this.capConfigurationData = capConfigurationData;
    }

    public CapSsfConfigurationData getTestCapSsfConfigurationData() {
        return testCapSsfConfigurationData;
    }

    public void setTestCapSsfConfigurationData(CapSsfConfigurationData testCapSsfConfigurationData) {
        this.testCapSsfConfigurationData = testCapSsfConfigurationData;
    }


    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<SimulatorConfigurationData> CONFIGURATION_DATA_XML = new XMLFormat<SimulatorConfigurationData>(
            SimulatorConfigurationData.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, SimulatorConfigurationData data) throws XMLStreamException {

            SccpConfigurationData sccp = xml.get(SCCP, SccpConfigurationData.class);
            if (sccp != null)
                data.setSccpConfigurationData(sccp);

            CapConfigurationData cap = xml.get(CAP, CapConfigurationData.class);
            if (cap != null)
                data.setCapConfigurationData(cap);

            CapSsfConfigurationData capSsf = xml.get(TEST_CAP_SSF, CapSsfConfigurationData.class);
            if (capSsf != null)
                data.setTestCapSsfConfigurationData(capSsf);

            // while (xml.hasNext()) {
            // Object o = xml.getNext();
            // }
        }

        @Override
        public void write(SimulatorConfigurationData data, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {

        	xml.add(data.getSccpConfigurationData(), SCCP, SccpConfigurationData.class);
        	xml.add(data.getCapConfigurationData(), CAP, CapConfigurationData.class);
        	xml.add(data.getTestCapSsfConfigurationData(), TEST_CAP_SSF, CapSsfConfigurationData.class);
        }
    };

}
