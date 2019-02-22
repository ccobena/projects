package org.mobicents.protocols.ss7.simulator.cap;

import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.primitives.ReceivingSideID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

public interface SsfCallMBean {

	void start();

	void stop();
	
	String closeCurrentDialog(CAPDialogCircuitSwitchedCall curDialog);

    void performApplyChargingReport(CAPDialogCircuitSwitchedCall currentCapDialog, String msg)  throws CAPException;

    void performEventReportBCSM_OAnswer(CAPDialogCircuitSwitchedCall currentCapDialog, OAnswerSpecificInfo oAnswerSpecificInfo, ReceivingSideID legID,
            MiscCallInfo miscCallInfo) throws CAPException;

	Long performInitialDP(CAPApplicationContext capAppContext, SccpAddress origAddress, SccpAddress remoteAddress,
			int[] userCallRelatedData) throws CAPException;


}
