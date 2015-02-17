package org.jdamico.pskcbuilder.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jose Damico
 * Eclipse Public License - v 1.0 (http://www.eclipse.org/legal/epl-v10.html)
 *
 */
public class Commons {
	private static Commons INSTANCE = null;
	public static Commons getInstance(){
		if(INSTANCE == null) INSTANCE = new Commons();
		return INSTANCE;
	}
	private Commons(){}


	public String byteArrayToHexString(byte[] raw) throws UnsupportedEncodingException 
	{
		final byte[] HEX_CHAR_TABLE = {
				(byte)'0', (byte)'1', (byte)'2', (byte)'3',
				(byte)'4', (byte)'5', (byte)'6', (byte)'7',
				(byte)'8', (byte)'9', (byte)'a', (byte)'b',
				(byte)'c', (byte)'d', (byte)'e', (byte)'f'
		};

		byte[] hex = new byte[2 * raw.length];
		int index = 0;

		for (byte b : raw) {
			int v = b & 0xFF;
			hex[index++] = HEX_CHAR_TABLE[v >>> 4];
			hex[index++] = HEX_CHAR_TABLE[v & 0xF];
		}
		return new String(hex, "ASCII");
	}

	public byte[] hexStringToByteArray(String s) throws Exception {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
		return data;
	}

	public void stringToFile(String content, String strFilePath) throws IOException {

		BufferedWriter out = new BufferedWriter(new FileWriter(strFilePath));
		out.write(content);
		out.close();

	}

	public String getStringFromFile(String filePath) throws IOException {
		StringBuffer ret = new StringBuffer();

		BufferedReader in = new BufferedReader(new FileReader(filePath));
		String str;
		while ((str = in.readLine()) != null) {
			ret.append(str+"\n");
		}
		in.close();

		return ret.toString();
	}
	public List<String> getListStringFromFile(String filePath) throws IOException {

		List<String> ret = new ArrayList<String>();

		BufferedReader in = new BufferedReader(new FileReader(filePath));
		String str;
		while ((str = in.readLine()) != null) ret.add(str);
		in.close();

		return ret;
	}
}
