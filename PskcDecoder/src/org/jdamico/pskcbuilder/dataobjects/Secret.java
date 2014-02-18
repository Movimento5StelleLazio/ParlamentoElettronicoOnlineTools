package org.jdamico.pskcbuilder.dataobjects;

/**
 * 
 * @author Jose Damico
 * Eclipse Public License - v 1.0 (http://www.eclipse.org/legal/epl-v10.html)
 *
 */
public class Secret {
	private String plainValue = null;

	public String getPlainValue() {
		return plainValue;
	}

	public void setPlainValue(String plainValue) {
		this.plainValue = plainValue;
	}

	public Secret(String plainValue) {
		super();
		this.plainValue = plainValue;
	}
	
	
	
}
