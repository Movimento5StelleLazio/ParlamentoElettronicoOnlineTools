package org.jdamico.pskcbuilder.dataobjects;

/**
 * 
 * @author Jose Damico
 * Eclipse Public License - v 1.0 (http://www.eclipse.org/legal/epl-v10.html)
 *
 */
public class DeviceInfo {
	
	private String manufacturer = null;
	private String serialNo = null;
	
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public DeviceInfo(String manufacturer, String serialNo) {
		super();
		this.manufacturer = manufacturer;
		this.serialNo = serialNo;
	}
	
	

}
