package org.mobicents.protocols.ss7.simulator.cap;

import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.primitives.ReceivingSideID;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

public interface SsfCallMBean {

	void start();

	void stop();

    String closeCurrentDialog();

    void performInitialDP(CAPApplicationContext capAppContext, SccpAddress origAddress, SccpAddress remoteAddress)  throws CAPException;

    void performApplyChargingReport(String msg)  throws CAPException;

    void performEventReportBCSM_OAnswer(OAnswerSpecificInfo oAnswerSpecificInfo, ReceivingSideID legID,
            MiscCallInfo miscCallInfo) throws CAPException;


}
