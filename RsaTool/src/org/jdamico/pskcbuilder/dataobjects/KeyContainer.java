package org.jdamico.pskcbuilder.dataobjects;

import java.util.List;

/**
 * 
 * @author Jose Damico
 * Eclipse Public License - v 1.0 (http://www.eclipse.org/legal/epl-v10.html)
 *
 */
public class KeyContainer {

	private List<KeyPackage> keyPackageList = null;

	public List<KeyPackage> getKeyPackageList() {
		return keyPackageList;
	}

	public void setKeyPackageList(List<KeyPackage> keyPackageList) {
		this.keyPackageList = keyPackageList;
	}
	
	
	
}
