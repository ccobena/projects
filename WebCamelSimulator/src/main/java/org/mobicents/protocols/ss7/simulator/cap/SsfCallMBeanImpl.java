package org.mobicents.protocols.ss7.simulator.cap;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
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
    //private CAPDialogCircuitSwitchedCall currentCapDialog;
    
    private CamelConfigurationData camelConfigurationData = null;

// Next counters could be used to get Total statistics.    
    private int countInitialDP = 0;
    private int countEventReportBCSM = 0;
    private int countApplyCharging = 0;
    private int countApplyChargingReport = 0;
    private int countReleaseCall = 0;
    private int countEstablishTemporaryConnection = 0;
    
    /* Map to control data related to each dialog. 
       Key=DialogId , 
       Value=Integer Array with data related to every call.
       Array[0] -> Current Call Duration 	
       Array[1] -> Max Call Duration
       Array[2] -> Acr Wait Time
       Array[3] -> Progress Call (%)	
    	
    */
    private Map <Long,int[]> callRelatedDataByDialog = new HashMap <Long, int[]>();
    private static SsfCallMBeanImpl instance;


    public SsfCallMBeanImpl() throws NamingException {
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
    
    public static synchronized SsfCallMBeanImpl getInstance() throws NamingException{
    	if (instance == null){
    		instance = new SsfCallMBeanImpl();	
    	}
    	return instance;
    }
    
    public void init (CamelConfigurationData camelConfigurationData, String appProfileName, int [] userCallRelatedData){

    	this.setupLog4j(appProfileName);
    	this.setCamelConfigurationData(camelConfigurationData);
    	//this.setProgressCallDuration(0);    	
    }

    public CAPProvider getCAPProvider() {
        return capProvider;
    }

    @Override
	public void start() {
        // Make the circuitSwitchedCall service activated
    	if (!capProvider.getCAPServiceCircuitSwitchedCall().isActivated()){
    		capProvider.getCAPServiceCircuitSwitchedCall().acivate();
    		//currentCapDialog = null;
    	}	
    }

    @Override
	public void stop() {
    	if (capProvider.getCAPServiceCircuitSwitchedCall().isActivated())
    		capProvider.getCAPServiceCircuitSwitchedCall().deactivate();
    }
    
    @Override
	public Long performInitialDP(CAPApplicationContext capAppContext, SccpAddress origAddress, SccpAddress remoteAddress, int [] userCallRelatedData) throws CAPException {
    	
    	CAPDialogCircuitSwitchedCall currentCapDialog = capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(capAppContext, origAddress, remoteAddress);
    	
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
        Long localDialogId = currentCapDialog.getLocalDialogId();
        
        this.callRelatedDataByDialog.put(localDialogId, userCallRelatedData);
        currentCapDialog.setUserObject(userCallRelatedData);
        currentCapDialog.send();
        
        this.setCountInitialDP(this.getCountInitialDP() + 1);
        Map <String,String> uData = new HashMap <String, String>();
        uData.put("uMessage", "Calling Number=" + this.getCamelConfigurationData().getInitialDPRequest().getCallingPartyNumber().getCallingPartyNumber().getAddress());
        uData.put("uLocalDialogId", String.valueOf(localDialogId.longValue()));
        
        
        this.sendNotif(SOURCE_NAME, "Initial DP Sent :" , uData, Level.INFO);
        
        return localDialogId;
    }

    @Override
	public void performApplyChargingReport(CAPDialogCircuitSwitchedCall curDialog, String msg) {
    	
    	//CAPDialogCircuitSwitchedCall curDialog = currentCapDialog;
    	Map <String,String> uData = new HashMap <String, String>();
        
        if (curDialog == null){
        	uData.put("uMessage", "");
        	this.sendNotif(SOURCE_NAME, "The current dialog does not exist. Start it previousely or wait of starting by a peer", uData, Level.DEBUG);
        }
        
        try {
        	int [] callRelatedData = (int []) curDialog.getUserObject();
        	int currentCallDuration = callRelatedData [0];
            TimeInformation timeInformation = capProvider.getCAPParameterFactory().createTimeInformation(currentCallDuration);
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
            
            uData.put("uMessage", "Current Call Duration=" + currentCallDuration);
            uData.put("uLocalDialogId", String.valueOf(curDialog.getLocalDialogId().longValue()));
            
            this.sendNotif(SOURCE_NAME, "Apply Charging Report Sent :", uData, Level.INFO);

        } catch (CAPException ex) {
        	uData.put("uMessage", ex.toString());
            this.sendNotif(SOURCE_NAME, "Exception when sending applyChargingReport", uData, Level.DEBUG);
        }
    }

    
    @Override
	public void performEventReportBCSM_OAnswer(CAPDialogCircuitSwitchedCall currentCapDialog, OAnswerSpecificInfo oAnswerSpecificInfo, ReceivingSideID legID,
            MiscCallInfo miscCallInfo) throws CAPException {
        if (currentCapDialog != null) {
            EventSpecificInformationBCSM eventSpecificInformationBCSM = this.capProvider.getCAPParameterFactory()
                    .createEventSpecificInformationBCSM(oAnswerSpecificInfo);
            currentCapDialog.addEventReportBCSMRequest(EventTypeBCSM.oAnswer, eventSpecificInformationBCSM, legID,
                    miscCallInfo, null);
            currentCapDialog.send();
            Map <String,String> uData = new HashMap <String, String>();
            uData.put("uMessage", "");
            uData.put("uLocalDialogId", String.valueOf(currentCapDialog.getLocalDialogId().longValue()));
            this.sendNotif(SOURCE_NAME, "Event Report BCSM Answer Sent : ", uData, Level.INFO);
        }
    }

    public void performEventReportBCSM_ODisconnect(CAPDialogCircuitSwitchedCall currentCapDialog,ODisconnectSpecificInfo oDisconnectSpecificInfo, ReceivingSideID legID,
            MiscCallInfo miscCallInfo) throws CAPException {
        if (currentCapDialog != null) {
            EventSpecificInformationBCSM eventSpecificInformationBCSM = this.capProvider.getCAPParameterFactory()
                    .createEventSpecificInformationBCSM(oDisconnectSpecificInfo);
            currentCapDialog.addEventReportBCSMRequest(EventTypeBCSM.oDisconnect, eventSpecificInformationBCSM, legID,
                    miscCallInfo, null);
            currentCapDialog.send();
            Map <String,String> uData = new HashMap <String, String>();
            uData.put("uMessage", "");
            uData.put("uLocalDialogId", String.valueOf(currentCapDialog.getLocalDialogId().longValue()));
            
            this.sendNotif(SOURCE_NAME, "Event Report BCSM Disconnect Sent : ", uData, Level.INFO);
        }
    }

    @Override
    public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind) {
    	if (ind.getCAPDialog() != null ) {
            ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
        }
    }

    @Override
    public void onActivityTestRequest(ActivityTestRequest ind) {
        if (ind.getCAPDialog() != null) {
            //this.cc.activityTestInvokeId = ind.getInvokeId();
        }
    }

    @Override
    public void onActivityTestResponse(ActivityTestResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onContinueRequest(ContinueRequest ind) {
        ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
        // sending Continue to use the original calledPartyAddress
    }

    @Override
    public void onConnectRequest(ConnectRequest ind) {
    	if (ind.getCAPDialog() != null) {
    		ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
        }
        // sending Connect to force routing the call to a new number
    }

    @Override
    public void onDialogTimeout(CAPDialog capDialog) {
        if (capDialog != null) {
            // if the call is still up - keep the sialog alive
            capDialog.keepAlive();
        }
    }

    @Override
    public void onDialogDelimiter(CAPDialog capDialog) {
        /*
    	if (capDialog != null && this.cc != null && this.cc.step != Step.disconnected) {
            if (this.cc.activityTestInvokeId != null) {
                try {
                    capDialog.addActivityTestResponse(this.cc.activityTestInvokeId);
                    this.cc.activityTestInvokeId = null;
                    currentCapDialog.send();
                } catch (CAPException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }*/
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
    	CAPDialogCircuitSwitchedCall currentCapDialog = arg0.getCAPDialog();
    	Long localDialogId = currentCapDialog.getLocalDialogId();
        this.setCountApplyCharging(this.getCountApplyCharging() + 1);
           
        int [] callRelatedData = (int []) currentCapDialog.getUserObject();
        int currentCallDuration = callRelatedData[0];
        int maxCallDuration = callRelatedData[1];
        int acrWaitTime  = callRelatedData[2];
        int progressCallDuration = callRelatedData[3];
        
        Map <String,String> uData = new HashMap <String, String>();
        String uMessage = "";
        boolean mustSendApplyChargingReport = false;
        
        try{
            if (maxCallDuration > 0){
                int maxCallPeriodDuration = (int) arg0.getAChBillingChargingCharacteristics().getMaxCallPeriodDuration();
                if (currentCallDuration <= maxCallDuration){
                	if ( (currentCallDuration + maxCallPeriodDuration )> maxCallDuration ){
                        int lastCallPeriodDuration = maxCallDuration - currentCallDuration;
                        maxCallPeriodDuration = lastCallPeriodDuration;
                    }
                    currentCallDuration = currentCallDuration + maxCallPeriodDuration;
                    mustSendApplyChargingReport = true;                    
                }else{
                //do nothing, Release call should have been received at this stage
                }
                double updateProgress = ((double)currentCallDuration/(double)maxCallDuration) * 100;
                updateProgress = Math.floor(updateProgress);
                                
                if (progressCallDuration < updateProgress){
                    progressCallDuration = (int) updateProgress;
                    uMessage = progressCallDuration + "% completed - CurrentCallDuration=" + currentCallDuration + " MaxCallDuration=" + maxCallDuration;
                }                
            }
            if (mustSendApplyChargingReport){
	            if (acrWaitTime > 0) Thread.sleep(acrWaitTime);

	            callRelatedData[0] = currentCallDuration;
	            callRelatedData[3] = progressCallDuration;
	            currentCapDialog.setUserObject(callRelatedData);
	            
	            this.callRelatedDataByDialog.put(localDialogId, callRelatedData);
	            this.performApplyChargingReport(currentCapDialog,"");
            }
            
            uData.put("uMessage", uMessage);
            uData.put("uLocalDialogId", String.valueOf(localDialogId.longValue()));
            
            
            this.sendNotif(SOURCE_NAME, "Apply Charging Received :" , uData, Level.INFO);
        }catch(InterruptedException e){
        	uData.put("uMessage", e.toString());
            this.sendNotif(SOURCE_NAME, "Exception when applying acrWaitTime", uData, Level.DEBUG);
        	e.printStackTrace();
        }
    }

    @Override
    public void onEventReportBCSMRequest(EventReportBCSMRequest ind) {
        // TODO Auto-generated method stub
    	Long localDialogId = ind.getCAPDialog().getLocalDialogId();
    	Map <String,String> uData = new HashMap <String, String>();
        String uMessage = "";
        uData.put("uMessage", uMessage);
        uData.put("uLocalDialogId", String.valueOf(localDialogId.longValue()));
        this.sendNotif(SOURCE_NAME, "Event Report BCSM Received :" , uData, Level.INFO);
   
    }

    @Override
    public void onApplyChargingReportRequest(ApplyChargingReportRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReleaseCallRequest(ReleaseCallRequest ind) {

    	Long localDialogId = ind.getCAPDialog().getLocalDialogId();
    	int [] callRelatedData = (int []) ind.getCAPDialog().getUserObject();
        int currentCallDuration = callRelatedData[0];
        int maxCallDuration = callRelatedData[1];
        int progressCallDuration = callRelatedData[3];
        
    	this.closeCurrentDialog(ind.getCAPDialog());
    	this.setCountReleaseCall(this.getCountReleaseCall() + 1);
    	
    	
    	String uMessage = progressCallDuration + "% completed - CurrentCallDuration=" + currentCallDuration + " MaxCallDuration=" + maxCallDuration;
    	Map <String,String> uData = new HashMap <String, String>();
        uData.put("uMessage", uMessage);
        uData.put("uLocalDialogId", String.valueOf(localDialogId.longValue()));
        
    	this.sendNotif(SOURCE_NAME, "Release Call Received :", uData, Level.INFO);                
                 
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
                this.doRemoveDialog(curDialog.getLocalDialogId());
                return "The current dialog has been closed";
            } catch (CAPException e) {
                this.doRemoveDialog(curDialog.getLocalDialogId());
                Map <String,String> uData = new HashMap <String, String>();
                uData.put("uMessage", e.toString());
                this.sendNotif(SOURCE_NAME, "Exception when closing a dialog", uData, Level.DEBUG);
                return "Exception when closing the current dialog: " + e.toString();
            }
        } else {
            return "No current dialog";
        }
    }

    private void doRemoveDialog(Long dialogRemoved) {
    	this.callRelatedDataByDialog.remove(dialogRemoved);
    	//currentCapDialog = null;
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
    	int [] callRelatedData = (int []) ind.getCAPDialog().getUserObject();
        int currentCallDuration = callRelatedData[0];
        int maxCallDuration = callRelatedData[1];
        int progressCallDuration = callRelatedData[3];
        Long localDialogId = ind.getCAPDialog().getLocalDialogId();
        
        this.closeCurrentDialog(ind.getCAPDialog());
        this.setCountEstablishTemporaryConnection(this.getCountEstablishTemporaryConnection() + 1);
        
        String uMessage = progressCallDuration + "% completed - CurrentCallDuration=" + currentCallDuration + " MaxCallDuration=" + maxCallDuration;
        
        Map <String,String> uData = new HashMap <String, String>();
        uData.put("uMessage", uMessage);
        uData.put("uLocalDialogId", String.valueOf(localDialogId.longValue()));
        
        this.sendNotif(SOURCE_NAME, "Establish Temporary Connection Received :", uData, Level.DEBUG);
        
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
        //this.currentCapDialog = null;
        //this.cc = null;
    }

    @Override
    public void onDialogNotice(CAPDialog capDialog, CAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
        // TODO Auto-generated method stub

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

	public int getProgressCallDuration(Long localDialogId) {
		if (localDialogId != null){
			if (callRelatedDataByDialog.containsKey(localDialogId)){
				return this.callRelatedDataByDialog.get(localDialogId)[3];
			}else{
				return 0;
			}
		}else{
			return 0;
		}	
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

	public void sendNotif(String source, String msg, Map <String,String> userData, Level logLevel) {

	   this.doSendNotif(source, msg, userData);
	   String userMessage = "";
	   if (userData.containsKey("uMessage")){
		   userMessage = userData.get("uMessage");
	   }
	   if (userData.containsKey("uLocalDialogId")){
		   userMessage += " LocalDialogId=" + userData.get("uLocalDialogId");
	   }
	   logger.log(Level.INFO, msg + "\n" + userMessage);
	}

	private synchronized void doSendNotif(String source, String msg, Map <String,String> userData) {
	    Notification notif = new Notification("SS7_EVENT" + "-" + source, this, ++sequenceNumber,
	            System.currentTimeMillis(), msg);
	    notif.setUserData(userData);
	    this.sendNotification(notif);
	}

	public int getCountEventReportBCSM() {
		return countEventReportBCSM;
	}

	public void setCountEventReportBCSM(int countEventReportBCSM) {
		this.countEventReportBCSM = countEventReportBCSM;
	}

	public int getCountEstablishTemporaryConnection() {
		return countEstablishTemporaryConnection;
	}

	public void setCountEstablishTemporaryConnection(int countEstablishTemporaryConnection) {
		this.countEstablishTemporaryConnection = countEstablishTemporaryConnection;
	}

	public int getCountInitialDP() {
		return countInitialDP;
	}

	public void setCountInitialDP(int countInitialDP) {
		this.countInitialDP = countInitialDP;
	}

}
