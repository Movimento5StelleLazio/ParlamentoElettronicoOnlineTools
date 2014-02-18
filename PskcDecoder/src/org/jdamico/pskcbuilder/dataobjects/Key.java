package org.jdamico.pskcbuilder.dataobjects;

/**
 * 
 * @author Jose Damico
 * Eclipse Public License - v 1.0 (http://www.eclipse.org/legal/epl-v10.html)
 *
 */
public class Key {

	private String id = null;
	private String algorithm = null;
	private AlgorithmParameters algorithmParameters = null;
	private String issuer = null;
	private Data data = null;
	
	public AlgorithmParameters getAlgorithmParameters() {
		return algorithmParameters;
	}
	public void setAlgorithmParameters(AlgorithmParameters algorithmParameters) {
		this.algorithmParameters = algorithmParameters;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	
	public Key(String id, String algorithm, String issuer, Data data, AlgorithmParameters algorithmParameters) {
		super();
		this.id = id;
		this.algorithm = algorithm;
		this.issuer = issuer;
		this.data = data;
		this.algorithmParameters = algorithmParameters;
	}
	
	
}
