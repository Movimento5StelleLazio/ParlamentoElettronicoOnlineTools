/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parelon.pskc;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.DestroyFailedException;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.codec.DecoderException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * Class that handles all the operation of both parsing, editing and creating
 * the PKCS XML file.
 *
 * @author Vincenzo Abate
 */
public class XmlManager {

    private String fileName = "", folderOut = "", manufactuer = "", issuer = "", preSharedKeyName = "PARELON ONLUS";
    private String privateKeyFragment1 = "", privateKeyFragment2 = "", publicKey = "";
    private CryptManager crypto;

    public XmlManager(String manufactuer, String issuer) {
        this.manufactuer = manufactuer;
        this.issuer = issuer;
        this.crypto = new CryptManager();
    }

    private boolean validateSerial(String serialNo) {
        /**
         * validate the serial number: the serial must be formed by 9 integer
         * digits
         */
        try {
            if (serialNo.length() != 9) {
                return false;
            }
            return (Long.parseLong(serialNo) > 0);
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public void convertXml(String fileIn, String fileOut) throws FileNotFoundException, SAXException, IOException, InvalidKeyException, Base64DecodingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, UnsupportedEncodingException, ParserConfigurationException, DecoderException, Exception {
        ArrayList<OtpSeed> seeds = new ArrayList<OtpSeed>();

        /**
         * Parserizzazione XML
         */
        FileInputStream fileToConvert = new FileInputStream(fileIn);
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document parsedXml = builder.parse(fileToConvert);
        fileToConvert.close();
        if (parsedXml.hasChildNodes()) {
            int validSerials = 0;
            NodeList keyPackages = parsedXml.getElementsByTagName("KeyPackage");
            for (int i = 0; i < keyPackages.getLength(); i++) {
                Node keyPackageNode = keyPackages.item(i);
                /**
                 * Parse PSKC node structure
                 */
                String serialNo = ((Element) keyPackageNode).getElementsByTagName("SerialNo").item(0).getTextContent();

                if (validateSerial(serialNo)) {
                    validSerials++;

                    OtpSeed validSeed = new OtpSeed();
                    validSeed.setSerialNo(serialNo);
                    validSeed.setCryptoId(String.format("KR-%06d", validSerials));
                    validSeed.setIssuer(issuer);
                    validSeed.setKeyId(String.format("%08d", validSerials));
                    validSeed.setManufacturer(manufactuer);

                    NodeList tempNode = ((Element) keyPackageNode).getElementsByTagName("xenc:CipherValue");
                    if (tempNode != null && tempNode.getLength() != 0) {
                        validSeed.setCipherValue(((Element) tempNode.item(0)).getTextContent());
                    }
                    seeds.add(validSeed);
                }
            }
        }

        /**
         * Creazione del nuovo XML
         */
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        // root elements
        Document doc = docBuilder.newDocument();
        doc.setXmlVersion("1.0");

        Element keyContainer = doc.createElement("KeyContainer");
        doc.appendChild(keyContainer);
        keyContainer.setAttribute("xmlns", "urn:ietf:params:xml:ns:keyprov:pskc");
        keyContainer.setAttribute("xmlns:ds", "http://www.w3.org/2000/09/xmldsig#");
        keyContainer.setAttribute("xmlns:xenc", "http://www.w3.org/2001/04/xmlenc#");
        keyContainer.setAttribute("Version", "1.0");
        keyContainer.setAttribute("ID", "KC0001");

        /**
         * Key Name
         */
        Element encryptionKey = doc.createElement("EncryptionKey");
        keyContainer.appendChild(encryptionKey);
        Element ds_keyName = doc.createElement("ds:KeyName");
        encryptionKey.appendChild(ds_keyName);
        ds_keyName.setTextContent(this.preSharedKeyName);

        /**
         * MAC Method
         */
        Element macMethod = doc.createElement("MACMethod");
        keyContainer.appendChild(macMethod);
        macMethod.setAttribute("Algorithm", "http://www.w3.org/2000/09/xmldsig#hmac-sha1");
        /**
         * Mac Key
         */
        Element macKey = doc.createElement("MACKey");
        macMethod.appendChild(macKey);
        /**
         * EncryptionMethod
         */
        Element encryptionMethod = doc.createElement("xenc:EncryptionMethod");
        macKey.appendChild(encryptionMethod);
        encryptionMethod.setAttribute("Algorithm", "http://www.w3.org/2001/04/xmlenc#aes128-cbc");
        /**
         * xenc:CipherData
         */
        Element xenc_CipherData = doc.createElement("xenc:CipherData");
        macKey.appendChild(xenc_CipherData);
        /**
         * xenc:CipherValue
         */
        Element xenc_CipherValue = doc.createElement("xenc:CipherValue");
        xenc_CipherData.appendChild(xenc_CipherValue);

        /**
         * Passo 1: Criptare la MAC Key
         */
        String macSignature = Base64.encode(crypto.getAesCipheredMacKey());
        xenc_CipherValue.setTextContent(macSignature);

        for (OtpSeed seed : seeds) {
            /**
             * Passo 2: Convertire il dato criptato
             */
            byte[] reCipheredSecretBytes = crypto.convertRsaToAes(seed.getCipherValue());
            seed.setCipherValue(Base64.encode(reCipheredSecretBytes));
            /**
             * Passo 3: Firmare il dato criptato
             */
            byte[] secretSignatureBytes = crypto.signCipheredSecret(reCipheredSecretBytes);
            seed.setValueMac(Base64.encode(secretSignatureBytes));

            /**
             * KeyPackage
             */
            Element keyPackageElement = doc.createElement("KeyPackage");
            keyContainer.appendChild(keyPackageElement);
            /**
             * DeviceInfo
             */
            Element deviceInfoElement = doc.createElement("DeviceInfo");
            keyPackageElement.appendChild(deviceInfoElement);
            /**
             * Manufactuer
             */
            Element manufactuerElement = doc.createElement("Manufactuer");
            manufactuerElement.setTextContent(seed.getManufacturer());
            deviceInfoElement.appendChild(manufactuerElement);
            /**
             * SerialNo
             */
            Element serialNoElement = doc.createElement("SerialNo");
            serialNoElement.setTextContent(seed.getSerialNo());
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
            idElement.setTextContent(seed.getCryptoId());
            cryptoModuleInfoElement.appendChild(idElement);
            /**
             * Key
             */
            Element keyElement = doc.createElement("Key");
            keyPackageElement.appendChild(keyElement);
            keyElement.setAttribute("Id", seed.getKeyId());
            if (seed.getType().equals("TOTP")) {
                keyElement.setAttribute("Algorithm", "urn:ietf:params:xml:ns:keyprov:pskc:totp");
            } else if (seed.getType().equals("HOTP")) {
                keyElement.setAttribute("Algorithm", "urn:ietf:params:xml:ns:keyprov:pskc:hotp");
            } else {
                throw new Exception("OTP type not supported");
            }

            /**
             * Issuer
             */
            Element issuerElement = doc.createElement("Issuer");
            issuerElement.setTextContent(seed.getIssuer());
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
            responseFormatElement.setAttribute("Length", seed.getLength());
            responseFormatElement.setAttribute("Encoding", seed.getEncoding());
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
            encryptedValueElement.appendChild(cipherDataElement);
            /**
             * xenc:CipherValue
             */
            Element cipherValueElement = doc.createElement("xenc:CipherValue");
            cipherValueElement.setTextContent(seed.getCipherValue());
            cipherDataElement.appendChild(cipherValueElement);
            /**
             * ValueMAC
             */
            Element valueMACElement = doc.createElement("ValueMAC");
            valueMACElement.setTextContent(seed.getValueMac());
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
            counterPlainValueElement.setTextContent(seed.getCounter());
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
            timeIntervalPlainValueElement.setTextContent(seed.getTimeInterval());
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
            timePlainValueElement.setTextContent(seed.getTime());
            timeElement.appendChild(timePlainValueElement);
        }

        doc.normalizeDocument();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(doc);
         
        StreamResult result = new StreamResult(new File(fileOut));
        transformer.transform(source, result);
    }

    public String initializeCrypto(byte[] fragment1, byte[] fragment2, byte[] publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, Base64DecodingException, UnsupportedEncodingException, DestroyFailedException, DecoderException {
        return crypto.initAll(fragment1, fragment2, publicKey);
    }

    public String initializeCrypto(byte[] fragment1, byte[] fragment2) throws NoSuchAlgorithmException, InvalidKeySpecException, Base64DecodingException, NoSuchPaddingException, InvalidKeyException, DecoderException {
        crypto.initRsaOperations(fragment1, fragment2);
        crypto.initHmacOperations();
        return crypto.initAesOperations();
    }
}
