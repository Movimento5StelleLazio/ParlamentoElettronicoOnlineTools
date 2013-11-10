package org.jdamico.pskcbuilder.dataobjects;

import org.jdamico.pskcbuilder.utils.Constants;

/**
 * 
 * @author Jose Damico
 * Eclipse Public License - v 1.0 (http://www.eclipse.org/legal/epl-v10.html)
 *
 */
public class AlgorithmParameters {
	
	private ResponseFormat responseFormat = null;
	private int algoType = Constants.ALGO_TYPE_HOTP;
	
	public int getAlgoType() {
		return algoType;
	}

	public void setAlgoType(int algoType) {
		this.algoType = algoType;
	}

	public ResponseFormat getResponseFormat() {
		return responseFormat;
	}

	public void setResponseFormat(ResponseFormat responseFormat) {
		this.responseFormat = responseFormat;
	}

	public AlgorithmParameters(ResponseFormat responseFormat, int algoType) {
		super();
		this.responseFormat = responseFormat;
		this.algoType = algoType;
	}
	
	

}
