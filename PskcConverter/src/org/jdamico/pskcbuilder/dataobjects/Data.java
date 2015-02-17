package org.jdamico.pskcbuilder.dataobjects;

/**
 * 
 * @author Jose Damico
 * Eclipse Public License - v 1.0 (http://www.eclipse.org/legal/epl-v10.html)
 *
 */
public class Data {
	private Secret secret = null;
	private String timeInterval = null;
	private String counter = null;
	
	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}

	public String getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(String timeInterval) {
		this.timeInterval = timeInterval;
	}

	public Secret getSecret() {
		return secret;
	}

	public void setSecret(Secret secret) {
		this.secret = secret;
	}

	public Data(Secret secret, String timeInterval, String counter) {
		super();
		this.secret = secret;
		this.timeInterval = timeInterval;
		this.counter = counter;
	}
	
	
}
