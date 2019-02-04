package org.mobicents.protocols.ss7.simulator.view;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.management.Notification;
import javax.management.NotificationListener;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.impl.parameter.GlobalTitle0001Impl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.GlobalTitle0010Impl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.GlobalTitle0011Impl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.GlobalTitle0100Impl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.simulator.cap.SsfCallMBeanImpl;
import org.mobicents.protocols.ss7.simulator.configurationData.AppConfigurationData;
import org.mobicents.protocols.ss7.simulator.configurationData.GlobalTitleType;

import javolution.text.TextBuilder;

@ManagedBean
@ViewScoped
public class WebSimulator implements NotificationListener {

	private static final Logger logger = Logger.getLogger(WebSimulator.class);
	private AppConfigurationData appConfigurationData = null;
	private SsfCallMBeanImpl ssfCallMBeanImpl = null;
	private int actualProgress;
	private boolean releaseCallReceived = false;
	private boolean establishTemporaryConnectionReceived = false;
	private boolean callCompleted = false;
	private String appProfileName;
	private int callDuration;
	private int acrWaitTime;
	private String message;

	public void makeCall(){
		try{
			// init variables
			this.setActualProgress(0);
			this.setMessage("");	
			this.setCallCompleted(false);
			this.setEstablishTemporaryConnectionReceived(false);
			this.setReleaseCallReceived(false);
			this.setupLog4j(this.getAppProfileName());
			appConfigurationData = new AppConfigurationData(this.getAppProfileName());

			// lookup for cap Provider and setup CamelData (IDP && ACR)
			this.ssfCallMBeanImpl = new SsfCallMBeanImpl(appConfigurationData.getCamelConfigurationData(), this.getAppProfileName(), this.getCallDuration() * 10, this.getAcrWaitTime());
		
			this.ssfCallMBeanImpl.addNotificationListener(this, null, null);
			this.ssfCallMBeanImpl.start();
			CAPApplicationContext capAppContext = appConfigurationData.getSimulatorConfigurationData()
			        .getTestCapSsfConfigurationData().getCapApplicationContext().getCAPApplicationContext();

	        SccpAddress origAddress = createLocalAddress();
	        SccpAddress remoteAddress = createRemoteAddress();
	        ssfCallMBeanImpl.performInitialDP(capAppContext, origAddress, remoteAddress);
	    }catch(Throwable e){
	    	logger.log(Level.DEBUG, "Exception generated : ", e);
	     }
	}

	public SccpAddress createLocalAddress() {
        return createCallingPartyAddress();
    }


    public SccpAddress createCallingPartyAddress() {
    	if (appConfigurationData.getSimulatorConfigurationData().getSccpConfigurationData().isRouteOnGtMode()) {
        	return new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, createGlobalTitle(appConfigurationData
                    .getSimulatorConfigurationData().getSccpConfigurationData().getCallingPartyAddressDigits()), 0, appConfigurationData
        			.getSimulatorConfigurationData().getSccpConfigurationData().getLocalSsn());
        } else {
        	return new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, appConfigurationData.getSimulatorConfigurationData()
                    .getSccpConfigurationData().getLocalSpc(), appConfigurationData.getSimulatorConfigurationData()
                    .getSccpConfigurationData().getLocalSsn());
        }
    }

    public SccpAddress createRemoteAddress() {
    	if (appConfigurationData.getSimulatorConfigurationData().getCapConfigurationData().getRemoteAddressDigits() == null
                || appConfigurationData.getSimulatorConfigurationData().getCapConfigurationData().getRemoteAddressDigits().equals("")) {
            return createCalledPartyAddress();
        } else {
            return createCalledPartyAddress(
            		appConfigurationData.getSimulatorConfigurationData().getCapConfigurationData().getRemoteAddressDigits(),
            		appConfigurationData.getSimulatorConfigurationData().getSccpConfigurationData().getRemoteSsn());
        }
    }

    public SccpAddress createCalledPartyAddress() {
        return new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, appConfigurationData.getSimulatorConfigurationData()
                .getSccpConfigurationData().getRemoteSpc(), appConfigurationData.getSimulatorConfigurationData()
                .getSccpConfigurationData().getRemoteSsn());
    }

    public SccpAddress createCalledPartyAddress(String address, int ssn) {
        if (appConfigurationData.getSimulatorConfigurationData().getSccpConfigurationData().isRouteOnGtMode()) {
            return new SccpAddressImpl (RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, createGlobalTitle(address),0,
                    (ssn >= 0 ? ssn : appConfigurationData.getSimulatorConfigurationData().getSccpConfigurationData().getRemoteSsn()));
        } else {
            return createCalledPartyAddress();
        }
    }

    public GlobalTitle createGlobalTitle(String address) {
        GlobalTitle gt = null;
        switch (this.appConfigurationData.getSimulatorConfigurationData().getSccpConfigurationData().getGlobalTitleType().intValue()) {
            case GlobalTitleType.VAL_NOA_ONLY:
                gt = new GlobalTitle0001Impl(address,appConfigurationData.getSimulatorConfigurationData().getSccpConfigurationData()
                        .getNatureOfAddress());
                break;
            case GlobalTitleType.VAL_TT_ONLY:
                gt = new GlobalTitle0010Impl(address,appConfigurationData.getSimulatorConfigurationData().getSccpConfigurationData()
                        .getTranslationType());
                break;
            case GlobalTitleType.VAL_TT_NP_ES:
                gt = new GlobalTitle0011Impl(address,appConfigurationData.getSimulatorConfigurationData().getSccpConfigurationData()
                        .getTranslationType(), null, appConfigurationData.getSimulatorConfigurationData().getSccpConfigurationData()
                        .getNumberingPlan());
                break;
            case GlobalTitleType.VAL_TT_NP_ES_NOA:
                gt = new GlobalTitle0100Impl(address,appConfigurationData.getSimulatorConfigurationData().getSccpConfigurationData()
                        .getTranslationType(), null, appConfigurationData.getSimulatorConfigurationData().getSccpConfigurationData()
                        .getNumberingPlan(), appConfigurationData.getSimulatorConfigurationData().getSccpConfigurationData()
                        .getNatureOfAddress());
                break;
        }
        return gt;
    }

    private void setupLog4j(String appName) {

        //String propFileName = appName + ".log4j.properties";
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
	        } else {
	            BasicConfigurator.configure();
	        }
        // logger.setLevel(Level.TRACE);
        }else{
        	BasicConfigurator.configure();
        }
        logger.debug("log4j configured");
    }

    
    @Override
    public void handleNotification(Notification notification, Object handback) {
    	int s = ssfCallMBeanImpl.getProgressCallDuration();
    	this.setActualProgress(s);
    	//System.out.println("setting progress to : " + s);
    	if (notification.getType().equals("SS7_EVENT-SSF_CALL_MBEAN")&& notification.getMessage().equals("Apply Charging Received :") && !notification.getUserData().equals("")){
        	// do nothing
    		if (s >= 100 && !this.isCallCompleted()) {
        		this.setCallCompleted(true);
        		this.setMessage("Call Completed Successfully - Progress : " + this.getActualProgress() + "%");
        	}        	
        }else if (notification.getType().equals("SS7_EVENT-SSF_CALL_MBEAN")&& notification.getMessage().equals("Release Call Received :") && !notification.getUserData().equals("")){
        	this.setReleaseCallReceived(true);
        	if (!this.isCallCompleted()) { 
        		this.setMessage("RC message received from OCS - Progress : " + this.getActualProgress() + "%");
        	}
        	this.setActualProgress(100); // Setting 100 % to indicate simulation was completed 
        }else if (notification.getType().equals("SS7_EVENT-SSF_CALL_MBEAN")&& notification.getMessage().equals("Establish Temporary Connection Received :") && !notification.getUserData().equals("")){
        	this.setEstablishTemporaryConnectionReceived(true);
        	if (!this.isCallCompleted()) {
        		this.setMessage("ETC message received from OCS - Progress : " + this.getActualProgress() + "%");
        	}
        	this.setActualProgress(100); // Setting 100 % to indicate simulation was completed
        }
    }

	public SsfCallMBeanImpl getSsfCallMBeanImpl() {
		return ssfCallMBeanImpl;
	}

	public void setSsfCallMBeanImpl(SsfCallMBeanImpl ssfCallMBeanImpl) {
		this.ssfCallMBeanImpl = ssfCallMBeanImpl;
	}

	public int getActualProgress() {
		return actualProgress;
	}

	public void setActualProgress(int actualProgress) {
		this.actualProgress = actualProgress;
	}

	public boolean isReleaseCallReceived() {
		return releaseCallReceived;
	}

	public void setReleaseCallReceived(boolean releaseCallReceived) {
		if (releaseCallReceived){
			this.ssfCallMBeanImpl.stop();
		}
		this.releaseCallReceived = releaseCallReceived;
	}

	public boolean isEstablishTemporaryConnectionReceived() {
		return establishTemporaryConnectionReceived;
	}

	public void setEstablishTemporaryConnectionReceived(boolean establishTemporaryConnectionReceived) {
		if (establishTemporaryConnectionReceived){
			this.ssfCallMBeanImpl.stop(); //pending coding for IVR Calls
		}
		this.establishTemporaryConnectionReceived = establishTemporaryConnectionReceived;
	}

	public String getAppProfileName() {
		return appProfileName;
	}

	public void setAppProfileName(String appProfileName) {
		if (appProfileName == "") appProfileName="main";
		this.appProfileName = appProfileName;
	}

	public int getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(int callDuration) {
		this.callDuration = callDuration;
	}

	public int getAcrWaitTime() {
		return acrWaitTime;
	}

	public void setAcrWaitTime(int acrWaitTime) {
		if (acrWaitTime == 0) acrWaitTime = 200;
		this.acrWaitTime = acrWaitTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isCallCompleted() {
		return callCompleted;
	}

	public void setCallCompleted(boolean callCompleted) {
		this.callCompleted = callCompleted;
	}

	
}
