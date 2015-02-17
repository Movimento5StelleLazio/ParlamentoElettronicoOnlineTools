package org.jdamico.pskcbuilder.dataobjects;

/**
 * 
 * @author Jose Damico
 * Eclipse Public License - v 1.0 (http://www.eclipse.org/legal/epl-v10.html)
 *
 */
public class KeyPackage {
	
	private DeviceInfo deviceInfo = null;
	private Key key = null;
	
	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public KeyPackage(DeviceInfo deviceInfo, Key key) {
		super();
		this.deviceInfo = deviceInfo;
		this.key = key;
	}
	
	
	

	
	
}
