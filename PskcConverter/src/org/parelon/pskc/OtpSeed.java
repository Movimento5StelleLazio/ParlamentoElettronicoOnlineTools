/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parelon.pskc;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Vicnenzo Abate
 */
public class OtpSeed {

    private String manufacturer, serialNo, cryptoId, keyId, issuer, length, encoding, type, cipherValue, valueMac, counter, time, timeInterval;

    public OtpSeed() {
        this.manufacturer = "";
        this.serialNo = "";
        this.cryptoId = "";
        this.keyId = "";
        this.issuer = "";
        this.length = "6";
        this.encoding = "DECIMAL";
        this.type = "TOTP";
        this.cipherValue = "";
        this.valueMac = "";
        this.counter = "0";
        this.time = "0";
        this.timeInterval = "60";
    }

    public Element getKeyPackage(Document doc) throws ParserConfigurationException, Exception {
        /**
         * KeyPackage
         */
        Element keyPackageElement = doc.createElement("KeyPackage");
        doc.appendChild(keyPackageElement);
        /**
         * DeviceInfo
         */
        Element deviceInfoElement = doc.createElement("DeviceInfo");
        keyPackageElement.appendChild(deviceInfoElement);
        /**
         * Manufactuer
         */
        Element manufactuerElement = doc.createElement("Manufactuer");
        manufactuerElement.setTextContent(this.manufacturer);
        deviceInfoElement.appendChild(manufactuerElement);
        /**
         * SerialNo
         */
        Element serialNoElement = doc.createElement("SerialNo");
        serialNoElement.setTextContent(this.serialNo);
        deviceInfoElement.appendChild(serialNoElement);
        /**
         * CryptoModuleInfo
         */
        Element cryptoModuleInfoElement = doc.createElement("CryptoModuleInfo");
        keyPackageElement.appendChild(cryptoModuleInfoElement);
        /**
         * ID
         */
        Element idElement = doc.createElement("ID");
        idElement.setTextContent(this.cryptoId);
        cryptoModuleInfoElement.appendChild(idElement);
        /**
         * Key
         */
        Element keyElement = doc.createElement("Key");
        keyPackageElement.appendChild(keyElement);
        keyElement.setAttribute("Id", this.keyId);
        if (this.type.equals("TOTP"))
            keyElement.setAttribute("Algorithm", "urn:ietf:params:xml:ns:keyprov:pskc:totp");
        else if (this.type.equals("HOTP"))
            keyElement.setAttribute("Algorithm", "urn:ietf:params:xml:ns:keyprov:pskc:hotp");
        else
            throw new Exception("OTP type not supported");
        
        /**
         * Issuer
         */
        Element issuerElement = doc.createElement("Issuer");
        issuerElement.setTextContent(this.issuer);
        keyElement.appendChild(issuerElement);
        /**
         * AlgorithmParameters
         */
        Element algorithmParametersElement = doc.createElement("AlgorithmParameters");
        keyElement.appendChild(algorithmParametersElement);
        /**
         * ResponseFormat
         */
        Element responseFormatElement = doc.createElement("ResponseFormat");
        algorithmParametersElement.appendChild(responseFormatElement);
        responseFormatElement.setAttribute("Length", this.length);
        responseFormatElement.setAttribute("Encoding", this.encoding);
        /**
         * Data
         */
        Element dataElement = doc.createElement("Data");
        keyElement.appendChild(dataElement);
        /**
         * Secret
         */
        Element secretElement = doc.createElement("Secret");
        dataElement.appendChild(secretElement);
        /**
         * EncryptedValue
         */
        Element encryptedValueElement = doc.createElement("EncryptedValue");
        secretElement.appendChild(encryptedValueElement);
        /**
         * xenc:EncryptionMethod
         */
        Element encryptionMethodElement = doc.createElement("xenc:EncryptionMethod");
        encryptedValueElement.appendChild(encryptionMethodElement);
        encryptionMethodElement.setAttribute("Algorithm", "http://www.w3.org/2001/04/xmlenc#aes128-cbc");
        /**
         * xenc:CipherData
         */
        Element cipherDataElement = doc.createElement("xenc:CipherData");
        encryptionMethodElement.appendChild(cipherDataElement);
        /**
         * xenc:CipherValue
         */
        Element cipherValueElement = doc.createElement("xenc:CipherValue");
        cipherValueElement.setTextContent(this.cipherValue);
        cipherDataElement.appendChild(cipherValueElement);
        /**
         * ValueMAC
         */
        Element valueMACElement = doc.createElement("ValueMAC");
        valueMACElement.setTextContent(this.valueMac);
        secretElement.appendChild(valueMACElement);
        /**
         * Counter
         */
        Element counterElement = doc.createElement("Counter");
        dataElement.appendChild(counterElement);
        /**
         * PlainValue
         */
        Element counterPlainValueElement = doc.createElement("PlainValue");
        counterPlainValueElement.setTextContent(this.counter);
        counterElement.appendChild(counterPlainValueElement);
        /**
         * TimeInterval
         */
        Element timeIntervalElement = doc.createElement("TimeInterval");
        dataElement.appendChild(timeIntervalElement);
        /**
         * PlainValue
         */
        Element timeIntervalPlainValueElement = doc.createElement("PlainValue");
        timeIntervalPlainValueElement.setTextContent(this.timeInterval);
        timeIntervalElement.appendChild(timeIntervalPlainValueElement);
        /**
         * Time
         */
        Element timeElement = doc.createElement("Time");
        dataElement.appendChild(timeElement);
        /**
         * PlainValue
         */
        Element timePlainValueElement = doc.createElement("PlainValue");
        timePlainValueElement.setTextContent(this.time);
        timeElement.appendChild(timePlainValueElement);
        
        return keyPackageElement;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public void setCryptoId(String cryptoId) {
        this.cryptoId = cryptoId;
    }

    public String getCryptoId() {
        return this.cryptoId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getKeyId() {
        return this.keyId;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getIssuer() {
        return this.issuer;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getLength() {
        return this.length;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setCipherValue(String cipherValue) {
        this.cipherValue = cipherValue;
    }

    public String getCipherValue() {
        return this.cipherValue;
    }

    public void setValueMac(String valueMac) {
        this.valueMac = valueMac;
    }

    public String getValueMac() {
        return this.valueMac;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getCounter() {
        return this.counter;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return this.time;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getTimeInterval() {
        return this.timeInterval;
    }
}
