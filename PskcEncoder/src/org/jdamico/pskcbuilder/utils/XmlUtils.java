package org.jdamico.pskcbuilder.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.jdamico.pskcbuilder.dataobjects.KeyContainer;
import org.xml.sax.SAXException;

/**
 * 
 * @author Jose Damico
 * Eclipse Public License - v 1.0 (http://www.eclipse.org/legal/epl-v10.html)
 *
 */
public class XmlUtils {
	
	private static XmlUtils INSTANCE = null;
	public static XmlUtils getInstance(){
		if(INSTANCE == null) INSTANCE = new XmlUtils();
		return INSTANCE;
	}
	private XmlUtils(){}
	
	public boolean isDocValid(String xml) {

		boolean ret = true;
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		File schemaLocation = new File(Constants.XSD_PATH);

		if(schemaLocation.exists()){
			Schema schema = null;
			try {
				schema = factory.newSchema(schemaLocation);
			} catch (SAXException e) {
				ret = false;
				e.printStackTrace();
			}
			Validator validator = schema.newValidator();
			Source source = new StreamSource(new StringReader(xml));
			try {
				validator.validate(source);
			} catch (SAXException e) {
				ret = false;
				e.printStackTrace();
			} catch (IOException e) {
				ret = false;
				e.printStackTrace();
			}
		}else{
			ret = false;
		}

		return ret;

	}
	public String Obj2XmlStr(KeyContainer kc) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<KeyContainer Version=\"1.0\" xmlns=\"urn:ietf:params:xml:ns:keyprov:pskc\">\n");
		
		for (int i = 0; i < kc.getKeyPackageList().size(); i++) {
			
			sb.append("<KeyPackage>\n");
			sb.append("<DeviceInfo>\n");
			sb.append("<Manufacturer>"+kc.getKeyPackageList().get(i).getDeviceInfo().getManufacturer()+"</Manufacturer>\n");
			sb.append("<SerialNo>"+kc.getKeyPackageList().get(i).getDeviceInfo().getSerialNo()+"</SerialNo>\n");
			sb.append("</DeviceInfo>\n");
			sb.append("<Key Id=\""+(i+1)+"\" Algorithm=\""+kc.getKeyPackageList().get(i).getKey().getAlgorithm()+"\">\n");
			sb.append("<Issuer>"+kc.getKeyPackageList().get(i).getKey().getIssuer()+"</Issuer>\n");
			sb.append("<AlgorithmParameters>\n");
			sb.append("<ResponseFormat Length=\""+kc.getKeyPackageList().get(i).getKey().getAlgorithmParameters().getResponseFormat().getLength()+"\" Encoding=\""+kc.getKeyPackageList().get(i).getKey().getAlgorithmParameters().getResponseFormat().getEncoding()+"\"/>\n");
			sb.append("</AlgorithmParameters>\n");
			sb.append("<Data>\n");
			sb.append("<Secret><PlainValue>"+kc.getKeyPackageList().get(i).getKey().getData().getSecret().getPlainValue()+"</PlainValue></Secret>\n");
			
			String sAlgoType = kc.getKeyPackageList().get(i).getKey().getAlgorithmParameters().getAlgoType() == Constants.ALGO_TYPE_HOTP ? "Counter" : "Time";
			
			sb.append("<"+sAlgoType+"><PlainValue>"+kc.getKeyPackageList().get(i).getKey().getData().getCounter()+"</PlainValue></"+sAlgoType+">\n");
			sb.append("<TimeInterval><PlainValue>"+kc.getKeyPackageList().get(i).getKey().getData().getTimeInterval()+"</PlainValue></TimeInterval>\n");
					//"<TimeDrift><PlainValue>"+kc.getKeyPackageList().get(i).getKey().getData().getTimeInterval()+"</PlainValue></TimeDrift>\n" +
			sb.append("</Data>\n");
			sb.append("</Key>\n");
			sb.append("</KeyPackage>\n");
		}
		
		sb.append("</KeyContainer>");
		
		   
		
		return sb.toString();
	}

}
