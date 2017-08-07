package org.mobicents.protocols.ss7.map.service.callhandling;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;

public class cc {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CallReferenceNumberTest crnt = new CallReferenceNumberTest();
		try{
			//System.out.println("00".getBytes().length);
			//crnt.testEncode();
			//crnt.testDecode();
			
			//401c413903c1cc
			//actualizado por CC 
			Calendar now = Calendar.getInstance(); 
			System.out.println(now.get(Calendar.YEAR)); 
			System.out.println(Calendar.getInstance().get(Calendar.MONTH) + 1 );
			System.out.println(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
			System.out.println(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
			System.out.println(Calendar.getInstance().get(Calendar.MINUTE));
			System.out.println(Calendar.getInstance().get(Calendar.SECOND));
            
			
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
}
