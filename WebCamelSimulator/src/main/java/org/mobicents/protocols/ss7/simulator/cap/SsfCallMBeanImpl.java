package org.mobicents.protocols.ss7.simulator.cap;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Properties;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPDialogListener;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPMessage;
import org.mobicents.protocols.ss7.cap.api.CAPParameterFactory;
import org.mobicents.protocols.ss7.cap.api.CAPProvider;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.ODisconnectSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPDialogState;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGeneralAbortReason;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPUserAbortReason;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CallingPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.ReceivingSideID;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ActivityTestRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ActivityTestResponse;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingReportRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.AssistRequestInstructionsRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPServiceCircuitSwitchedCallListener;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationReportRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationRequestRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CancelRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CollectInformationRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectToResourceRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ContinueRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ContinueWithArgumentRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectForwardConnectionRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectForwardConnectionWithArgumentRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectLegRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectLegResponse;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.EstablishTemporaryConnectionRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.EventReportBCSMRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.FurnishChargingInformationRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.InitiateCallAttemptRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.InitiateCallAttemptResponse;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.MoveLegRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.MoveLegResponse;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.PlayAnnouncementRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationResponse;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ReleaseCallRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ResetTimerRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.SendChargingInformationRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.SpecializedResourceReportRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddress;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeDurationChargingResult;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeInformation;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.simulator.configurationData.CamelConfigurationData;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

import javolution.text.TextBuilder;

public class SsfCallMBeanImpl extends NotificationBroadcasterSupport implements CAPDialogListener, CAPServiceCircuitSwitchedCallListener, SsfCallMBean {

	private static final Logger logger = Logger.getLogger(SsfCallMBeanImpl.class);
	private long sequenceNumber = 0;
	public static String SOURCE_NAME = "SSF_CALL_MBEAN";

	private CAPProvider capProvider;
    private CAPParameterFactory paramFact;
    private CAPDialogCircuitSwitchedCall currentCapDialog;
    private CallContent cc;

    private int maxCallDuration;
    private int currentCallDuration;
    private int maxCallPeriodDuration;
    private int progressCallDuration;
    private CamelConfigurationData camelConfigurationData = null;
    private long acrWaitTime;
    private int countApplyChargingReport = 0;
    private int countApplyCharging = 0;
    private int countReleaseCall = 0;
    //private String currentRequestDef = "";


    public SsfCallMBeanImpl(CamelConfigurationData camelConfigurationData, String appProfileName, int callDuration, int acrWaitTime) throws NamingException {

    	this.setupLog4j(appProfileName);
    	this.setCamelConfigurationData(camelConfigurationData);
    	this.setMaxCallDuration(callDuration);
    	this.setAcrWaitTime(acrWaitTime);
    	this.setCurrentCallDuration(0);
    	this.setProgressCallDuration(0);
    	

    	Properties props = new Properties();
    	props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
    	props.setProperty(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
    	props.setProperty(Context.PROVIDER_URL, "jnp://127.0.0.1:1099");
    	InitialContext ctx = new InitialContext(props);

    	try {
        	String providerJndiName = "java:/restcomm/ss7/cap";
        	this.capProvider = ((CAPProvider) ctx.lookup(providerJndiName));
        } finally {
            ctx.close();
        }

        setParamFact(capProvider.getCAPParameterFactory());

        capProvider.addCAPDialogListener(this);
        capProvider.getCAPServiceCircuitSwitchedCall().addCAPServiceListener(this);
    }

    public CAPProvider getCAPProvider() {
        return capProvider;
    }

    @Override
	public void start() {
        // Make the circuitSwitchedCall service activated
        capProvider.getCAPServiceCircuitSwitchedCall().acivate();
        currentCapDialog = null;
    }

    @Override
	public void stop() {
        capProvider.getCAPServiceCircuitSwitchedCall().deactivate();
    }

    @Override
	public void performApplyChargingReport(String msg) {

    	CAPDialogCircuitSwitchedCall curDialog = currentCapDialog;
        String uData = "";

        if (curDialog == null)
        	this.sendNotif(SOURCE_NAME, "The current dialog does not exist. Start it previousely or wait of starting by a peer", uData, Level.DEBUG);

        try {
            TimeInformation timeInformation = capProvider.getCAPParameterFactory().createTimeInformation(this.currentCallDuration);
            TimeDurationChargingResult timeDurationChargingResult = capProvider.getCAPParameterFactory()
                    .createTimeDurationChargingResult(
                    		this.getCamelConfigurationData().getApplyChargingReportRequest().getTimeDurationChargingResult().getPartyToCharge(),
                            timeInformation,
                            this.getCamelConfigurationData().getApplyChargingReportRequest().getTimeDurationChargingResult().getLegActive(),
                            this.getCamelConfigurationData().getApplyChargingReportRequest().getTimeDurationChargingResult().getCallLegReleasedAtTcpExpiry(),
                            this.getCamelConfigurationData().getApplyChargingReportRequest().getTimeDurationChargingResult().getExtensions(),
                            this.getCamelConfigurationData().getApplyChargingReportRequest().getTimeDurationChargingResult().getAChChargingAddress());
            curDialog.addApplyChargingReportRequest(timeDurationChargingResult);
            curDialog.send();
            this.setCountApplyChargingReport(this.getCountApplyChargingReport() + 1);
            uData = "Current Call Duration : " + this.currentCallDuration;
            this.sendNotif(SOURCE_NAME, "Apply Charging Report Sent : ", uData, Level.INFO);

        } catch (CAPException ex) {
            this.sendNotif(SOURCE_NAME, "Exception when sending applyChargingReport", ex.toString(), Level.DEBUG);
        }
    }

    @Override
	public void performInitialDP(CAPApplicationContext capAppContext, SccpAddress origAddress, SccpAddress remoteAddress) throws CAPException {

    	currentCapDialog = capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(capAppContext, origAddress, remoteAddress);
    	Calendar now = Calendar.getInstance();
        TimeAndTimezone timeAndTimezone = capProvider.getCAPParameterFactory().createTimeAndTimezone(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1 , now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), 10);
        currentCapDialog.addInitialDPRequest(
        		this.getCamelConfigurationData().getInitialDPRequest().getServiceKey(),
        		this.getCamelConfigurationData().getInitialDPRequest().getCalledPartyNumber(), //null
        		this.getCamelConfigurationData().getInitialDPRequest().getCallingPartyNumber(),
        		this.getCamelConfigurationData().getInitialDPRequest().getCallingPartysCategory(),
        		this.getCamelConfigurationData().getInitialDPRequest().getCGEncountered(), //null
        		this.getCamelConfigurationData().getInitialDPRequest().getIPSSPCapabilities(),
        		this.getCamelConfigurationData().getInitialDPRequest().getLocationNumber(),
        		this.getCamelConfigurationData().getInitialDPRequest().getOriginalCalledPartyID(), //null
        		this.getCamelConfigurationData().getInitialDPRequest().getExtensions(), //null
        		this.getCamelConfigurationData().getInitialDPRequest().getHighLayerCompatibility(),
        		this.getCamelConfigurationData().getInitialDPRequest().getAdditionalCallingPartyNumber(), //null
        		this.getCamelConfigurationData().getInitialDPRequest().getBearerCapability(),
        		this.getCamelConfigurationData().getInitialDPRequest().getEventTypeBCSM(),
        		this.getCamelConfigurationData().getInitialDPRequest().getRedirectingPartyID(), //null
        		this.getCamelConfigurationData().getInitialDPRequest().getRedirectionInformation(),//null
        		this.getCamelConfigurationData().getInitialDPRequest().getCause(),//null
        		this.getCamelConfigurationData().getInitialDPRequest().getServiceInteractionIndicatorsTwo(),//null
        		this.getCamelConfigurationData().getInitialDPRequest().getCarrier(),//null
        		this.getCamelConfigurationData().getInitialDPRequest().getCugIndex(),//null
        		this.getCamelConfigurationData().getInitialDPRequest().getCugInterlock(),//null
        		this.getCamelConfigurationData().getInitialDPRequest().getCugOutgoingAccess(), //false
        		this.getCamelConfigurationData().getInitialDPRequest().getIMSI(),
        		this.getCamelConfigurationData().getInitialDPRequest().getSubscriberState(),//null
        		this.getCamelConfigurationData().getInitialDPRequest().getLocationInformation(),
        		this.getCamelConfigurationData().getInitialDPRequest().getExtBasicServiceCode(),
        		this.getCamelConfigurationData().getInitialDPRequest().getCallReferenceNumber(),
        		this.getCamelConfigurationData().getInitialDPRequest().getMscAddress(),
        		this.getCamelConfigurationData().getInitialDPRequest().getCalledPartyBCDNumber(),
        		timeAndTimezone,
        		this.getCamelConfigurationData().getInitialDPRequest().getCallForwardingSSPending(),//false
        		this.getCamelConfigurationData().getInitialDPRequest().getInitialDPArgExtension());//null

        // This will initiate the TC-BEGIN with INVOKE component
        currentCapDialog.send();

        this.cc = new CallContent();
        this.cc.step = Step.initialDPSent;
        this.cc.calledPartyNumber = this.getCamelConfigurationData().getInitialDPRequest().getCalledPartyNumber();
        this.cc.callingPartyNumber = this.getCamelConfigurationData().getInitialDPRequest().getCallingPartyNumber();

        String uData = "";
        if (this.cc.callingPartyNumber != null)
        	uData = "Calling Number : " + this.cc.callingPartyNumber.getCallingPartyNumber().getAddress() + "\n";
        if (this.cc.calledPartyNumber != null)
        	uData = uData + "Called Number : " + this.cc.calledPartyNumber;

        this.sendNotif(SOURCE_NAME, "Initial DP Sent : ", uData, Level.INFO);
    }

    @Override
	public void performEventReportBCSM_OAnswer(OAnswerSpecificInfo oAnswerSpecificInfo, ReceivingSideID legID,
            MiscCallInfo miscCallInfo) throws CAPException {
        if (currentCapDialog != null && this.cc != null) {
            EventSpecificInformationBCSM eventSpecificInformationBCSM = this.capProvider.getCAPParameterFactory()
                    .createEventSpecificInformationBCSM(oAnswerSpecificInfo);
            currentCapDialog.addEventReportBCSMRequest(EventTypeBCSM.oAnswer, eventSpecificInformationBCSM, legID,
                    miscCallInfo, null);
            currentCapDialog.send();
            this.cc.step = Step.answered;
            String uData = "";
            this.sendNotif(SOURCE_NAME, "Event Report BCSM Answer Sent : ", uData, Level.INFO);
        }
    }

    public void performEventReportBCSM_ODisconnect(ODisconnectSpecificInfo oDisconnectSpecificInfo, ReceivingSideID legID,
            MiscCallInfo miscCallInfo) throws CAPException {
        if (currentCapDialog != null && this.cc != null) {
            EventSpecificInformationBCSM eventSpecificInformationBCSM = this.capProvider.getCAPParameterFactory()
                    .createEventSpecificInformationBCSM(oDisconnectSpecificInfo);
            currentCapDialog.addEventReportBCSMRequest(EventTypeBCSM.oDisconnect, eventSpecificInformationBCSM, legID,
                    miscCallInfo, null);
            currentCapDialog.send();
            this.cc.step = Step.disconnected;
            String uData = "";
            this.sendNotif(SOURCE_NAME, "Event Report BCSM Disconnect Sent : ", uData, Level.INFO);
        }
    }

    @Override
    public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind) {
    	if (currentCapDialog != null && this.cc != null && this.cc.step != Step.disconnected) {
            this.cc.requestReportBCSMEventRequest = ind;
            ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
        }
    }

    @Override
    public void onActivityTestRequest(ActivityTestRequest ind) {
        if (currentCapDialog != null && this.cc != null && this.cc.step != Step.disconnected) {
            this.cc.activityTestInvokeId = ind.getInvokeId();
        }
    }

    @Override
    public void onActivityTestResponse(ActivityTestResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onContinueRequest(ContinueRequest ind) {
        this.cc.step = Step.callAllowed;
        ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
        // sending Continue to use the original calledPartyAddress
    }

    @Override
    public void onConnectRequest(ConnectRequest ind) {
    	if (currentCapDialog != null && this.cc != null && this.cc.step != Step.disconnected) {
    		this.cc.step = Step.callAllowed;
            this.cc.destinationRoutingAddress = ind.getDestinationRoutingAddress();
            ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
        }
        // sending Connect to force routing the call to a new number
    }

    @Override
    public void onDialogTimeout(CAPDialog capDialog) {
        if (currentCapDialog != null && this.cc != null && this.cc.step != Step.disconnected) {
            // if the call is still up - keep the sialog alive
            currentCapDialog.keepAlive();
        }
    }

    @Override
    public void onDialogDelimiter(CAPDialog capDialog) {
        if (currentCapDialog != null && this.cc != null && this.cc.step != Step.disconnected) {
            if (this.cc.activityTestInvokeId != null) {
                try {
                    currentCapDialog.addActivityTestResponse(this.cc.activityTestInvokeId);
                    this.cc.activityTestInvokeId = null;
                    currentCapDialog.send();
                } catch (CAPException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onErrorComponent(CAPDialog capDialog, Long invokeId, CAPErrorMessage capErrorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRejectComponent(CAPDialog capDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInvokeTimeout(CAPDialog capDialog, Long invokeId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCAPMessage(CAPMessage capMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInitialDPRequest(InitialDPRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onApplyChargingRequest(ApplyChargingRequest arg0) {
        this.setCountApplyCharging(this.getCountApplyCharging() + 1);
        //System.out.println("He recibido un ACR : " + this.getCountApplyCharging());
    	
        String uData = "";
        try{
            if (this.maxCallDuration > 0){
                this.maxCallPeriodDuration = (int) arg0.getAChBillingChargingCharacteristics().getMaxCallPeriodDuration();
                if (this.currentCallDuration <= this.maxCallDuration){
                	if ( (this.currentCallDuration + this.maxCallPeriodDuration )> this.maxCallDuration ){
                        int lastCallPeriodDuration = this.maxCallDuration - this.currentCallDuration;
                        this.setMaxCallPeriodDuration(lastCallPeriodDuration);
                    }
                    this.currentCallDuration = this.currentCallDuration + this.maxCallPeriodDuration;
                    if (this.acrWaitTime > 0)
                        Thread.sleep(this.acrWaitTime);
                    this.performApplyChargingReport("");
                }else{
                //do nothing, Release call should have been received at this stage
                }
                double updateProgress = ((double)this.currentCallDuration/(double)this.maxCallDuration) * 100;
                updateProgress = Math.floor(updateProgress);
                uData = "";
                System.out.println("UpdatePercent : " + updateProgress + " - Current Call Duration : " + this.currentCallDuration + " - Max Call Duration : " + this.maxCallDuration);
                if (this.progressCallDuration < updateProgress){
                    this.progressCallDuration = (int) updateProgress;
                    uData = this.progressCallDuration + "% completed - CurrentCallDuration : " + this.currentCallDuration + " MaxCallDuration : " + this.maxCallDuration;
                }
                //System.out.println("ProgressCallDuration : " + this.progressCallDuration);
            }
            if (currentCapDialog != null && this.cc != null && this.cc.step != Step.disconnected) {
            	this.cc.step = Step.answered;
            }
            this.sendNotif(SOURCE_NAME, "Apply Charging Received :", uData, Level.INFO);
        }catch(InterruptedException e){
            this.sendNotif(SOURCE_NAME, "Exception when applying acrWaitTime", e.toString(), Level.DEBUG);
        	e.printStackTrace();
        }
    }

    @Override
    public void onEventReportBCSMRequest(EventReportBCSMRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onApplyChargingReportRequest(ApplyChargingReportRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReleaseCallRequest(ReleaseCallRequest ind) {

    	this.closeCurrentDialog(ind.getCAPDialog());
    	this.cc = null;
    	this.setCountReleaseCall(this.getCountReleaseCall() + 1);
    	String uData = this.progressCallDuration + "% completed - CurrentCallDuration : " + this.currentCallDuration + " MaxCallDuration : " + this.maxCallDuration;
        this.sendNotif(SOURCE_NAME, "Release Call Received :", uData, Level.INFO);
        this.setCurrentCallDuration(0);
        this.setMaxCallDuration(0);
        this.setMaxCallPeriodDuration(0);
        //this.setProgressCallDuration(0);        
                 
    }

    @Override
	public String closeCurrentDialog(CAPDialogCircuitSwitchedCall curDialog) {

        //CAPDialogCircuitSwitchedCall curDialog = currentCapDialog;
        if (curDialog != null) {
            try {
                if (curDialog.getState() == CAPDialogState.Active)
                    curDialog.close(false);
                else
                    curDialog.abort(CAPUserAbortReason.no_reason_given);
                this.doRemoveDialog();
                return "The current dialog has been closed";
            } catch (CAPException e) {
                this.doRemoveDialog();
                this.sendNotif(SOURCE_NAME, "Exception when closing a dialog", e.toString(), Level.DEBUG);
                return "Exception when closing the current dialog: " + e.toString();
            }
        } else {
            return "No current dialog";
        }
    }

    private void doRemoveDialog() {
    	currentCapDialog = null;
        // currentRequestDef = "";
    }
    @Override
    public void onCallGapRequest(org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CallGapRequest ind) {
        // TODO Auto-generated method stub

    }

	@Override
	public void onCallInformationRequestRequest(CallInformationRequestRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCallInformationReportRequest(CallInformationReportRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAssistRequestInstructionsRequest(AssistRequestInstructionsRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEstablishTemporaryConnectionRequest(EstablishTemporaryConnectionRequest ind) {
        // TODO Auto-generated method stub
        this.closeCurrentDialog(ind.getCAPDialog());
        String uData = this.progressCallDuration + "% completed - CurrentCallDuration : " + this.currentCallDuration + " MaxCallDuration : " + this.maxCallDuration;
        this.sendNotif(SOURCE_NAME, "Establish Temporary Connection Received :", uData, Level.DEBUG);
        this.setCurrentCallDuration(0);
        this.setMaxCallDuration(0);
        this.setMaxCallPeriodDuration(0);
        
    }

    @Override
    public void onDisconnectForwardConnectionRequest(DisconnectForwardConnectionRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectToResourceRequest(ConnectToResourceRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResetTimerRequest(ResetTimerRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFurnishChargingInformationRequest(FurnishChargingInformationRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendChargingInformationRequest(SendChargingInformationRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSpecializedResourceReportRequest(SpecializedResourceReportRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayAnnouncementRequest(PlayAnnouncementRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPromptAndCollectUserInformationRequest(PromptAndCollectUserInformationRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPromptAndCollectUserInformationResponse(PromptAndCollectUserInformationResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCancelRequest(CancelRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogAccept(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason, CAPUserAbortReason userReason) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogProviderAbort(CAPDialog capDialog, PAbortCauseType abortCause) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogClose(CAPDialog capDialog) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogRelease(CAPDialog capDialog) {
        this.currentCapDialog = null;
        this.cc = null;
    }

    @Override
    public void onDialogNotice(CAPDialog capDialog, CAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
        // TODO Auto-generated method stub

    }

    private enum Step {
        initialDPSent, callAllowed, answered, disconnected;
    }

    private class CallContent {
        public Step step;
        public Long activityTestInvokeId;

        public CalledPartyNumberCap calledPartyNumber;
        public CallingPartyNumberCap callingPartyNumber;
        public RequestReportBCSMEventRequest requestReportBCSMEventRequest;
        public DestinationRoutingAddress destinationRoutingAddress;
    }

    @Override
    public void onContinueWithArgumentRequest(ContinueWithArgumentRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnectLegRequest(DisconnectLegRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnectLegResponse(DisconnectLegResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnectForwardConnectionWithArgumentRequest(DisconnectForwardConnectionWithArgumentRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInitiateCallAttemptRequest(InitiateCallAttemptRequest initiateCallAttemptRequest) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInitiateCallAttemptResponse(InitiateCallAttemptResponse initiateCallAttemptResponse) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMoveLegRequest(MoveLegRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMoveLegResponse(MoveLegResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCollectInformationRequest(CollectInformationRequest ind) {
        // TODO Auto-generated method stub

    }

	public CamelConfigurationData getCamelConfigurationData() {
		return camelConfigurationData;
	}

	public void setCamelConfigurationData(CamelConfigurationData camelConfigurationData) {
		this.camelConfigurationData = camelConfigurationData;
	}



	public int getCountApplyChargingReport() {
		return countApplyChargingReport;
	}

	public void setCountApplyChargingReport(int countApplyChargingReport) {
		this.countApplyChargingReport = countApplyChargingReport;
	}

	public int getCountApplyCharging() {
		return countApplyCharging;
	}

	public void setCountApplyCharging(int countApplyCharging) {
		this.countApplyCharging = countApplyCharging;
	}

	public CAPParameterFactory getParamFact() {
		return paramFact;
	}

	public void setParamFact(CAPParameterFactory paramFact) {
		this.paramFact = paramFact;
	}

	public void setMaxCallDuration(int maxCallDuration) {
        this.maxCallDuration = maxCallDuration;
    }

    public void setCurrentCallDuration(int currentCallDuration) {
        this.currentCallDuration = currentCallDuration;
    }

    public void setMaxCallPeriodDuration(int maxCallPeriodDuration) {
        this.maxCallPeriodDuration = maxCallPeriodDuration;
    }

    public void setProgressCallDuration(int percentCallDuration) {
        this.progressCallDuration = percentCallDuration;
    }

    public void setAcrWaitTime(long AcrWaitTime) {
        this.acrWaitTime = AcrWaitTime;
    }

    public int getMaxCallDuration() {
        return this.maxCallDuration;
    }

    public int getCurrentCallDuration() {
        return this.currentCallDuration;
    }

    public int getMaxCallPeriodDuration() {
        return this.maxCallPeriodDuration;
    }

    public int getProgressCallDuration() {
        return this.progressCallDuration;
    }

   public long getACRWaitTime() {
        return this.acrWaitTime;
    }

	public int getCountReleaseCall() {
		return countReleaseCall;
	}

	public void setCountReleaseCall(int countReleaseCall) {
		this.countReleaseCall = countReleaseCall;
	}

	private void setupLog4j(String appName) {

		TextBuilder textBuilder = TextBuilder.newInstance();
    	URL url = this.getClass().getClassLoader().getResource(appName + ".log4j.properties");

        //File f = new File("./" + propFileName);
        //System.out.println(f.getAbsolutePath());
        if (url != null){
        	String path = url.getFile();
        	textBuilder.append(path);
            File f = new File(textBuilder.toString());

		    if (f.exists()) {

		        try {
		            InputStream inStreamLog4j = new FileInputStream(f);
		            Properties propertiesLog4j = new Properties();

		            propertiesLog4j.load(inStreamLog4j);
		            PropertyConfigurator.configure(propertiesLog4j);
		        } catch (Exception e) {
		            e.printStackTrace();
		            BasicConfigurator.configure();
		        }
		    }else {
		        BasicConfigurator.configure();
		    }
        }else{
			BasicConfigurator.configure();
		}
	    // logger.setLevel(Level.TRACE);
	    logger.debug("log4j configured");

	}

	public void sendNotif(String source, String msg, Throwable e, Level logLevel) {
	    StringBuilder sb = new StringBuilder();
	    for (StackTraceElement st : e.getStackTrace()) {
	        if (sb.length() > 0)
	            sb.append("\n");
	        sb.append(st.toString());
	    }
	    this.doSendNotif(source, msg + " - " + e.toString(), sb.toString());

	    //logger.log(logLevel, msg, e);
	}

	public void sendNotif(String source, String msg, String userData, Level logLevel) {

	   this.doSendNotif(source, msg, userData);
	   logger.log(Level.INFO, msg + "\n" + userData);
	}

	private synchronized void doSendNotif(String source, String msg, String userData) {
	    Notification notif = new Notification("SS7_EVENT" + "-" + source, this, ++sequenceNumber,
	            System.currentTimeMillis(), msg);
	    notif.setUserData(userData);
	    this.sendNotification(notif);
	}

}
