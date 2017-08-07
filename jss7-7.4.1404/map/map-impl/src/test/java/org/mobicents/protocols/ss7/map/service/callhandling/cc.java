package org.mobicents.protocols.ss7.map.service.callhandling;

public class cc {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CallReferenceNumberTest crnt = new CallReferenceNumberTest();
		try{
			//System.out.println("00".getBytes().length);
			//crnt.testEncode();
			//crnt.testDecode();
			
			//401c413903c1cc
			byte b[] =  new byte[] { (byte)0x40, (byte)0x1c, (byte)0x41, (byte)0x39, (byte)0x03, (byte)0xC1, (byte)0xCC };
			byte a[]= new byte[] {(byte)0, (byte)0};
			for (int i=0;i<=a.length-1;i++){
				System.out.println(a[i]);
			}
			for (int i=0;i<=b.length-1;i++){
				System.out.println(b[i]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
}
