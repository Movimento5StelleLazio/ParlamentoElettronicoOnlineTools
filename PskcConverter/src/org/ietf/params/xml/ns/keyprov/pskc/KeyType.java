//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.02.10 at 10:02:26 PM CET 
//


package ietf.params.xml.ns.keyprov.pskc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for KeyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="KeyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Issuer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AlgorithmParameters" type="{urn:ietf:params:xml:ns:keyprov:pskc}AlgorithmParametersType" minOccurs="0"/>
 *         &lt;element name="KeyProfileId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="KeyReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FriendlyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Data" type="{urn:ietf:params:xml:ns:keyprov:pskc}KeyDataType" minOccurs="0"/>
 *         &lt;element name="UserId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Policy" type="{urn:ietf:params:xml:ns:keyprov:pskc}PolicyType" minOccurs="0"/>
 *         &lt;element name="Extensions" type="{urn:ietf:params:xml:ns:keyprov:pskc}ExtensionsType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Algorithm" type="{urn:ietf:params:xml:ns:keyprov:pskc}KeyAlgorithmType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KeyType", propOrder = {
    "issuer",
    "algorithmParameters",
    "keyProfileId",
    "keyReference",
    "friendlyName",
    "data",
    "userId",
    "policy",
    "extensions"
})
public class KeyType {

    @XmlElement(name = "Issuer")
    protected String issuer;
    @XmlElement(name = "AlgorithmParameters")
    protected AlgorithmParametersType algorithmParameters;
    @XmlElement(name = "KeyProfileId")
    protected String keyProfileId;
    @XmlElement(name = "KeyReference")
    protected String keyReference;
    @XmlElement(name = "FriendlyName")
    protected String friendlyName;
    @XmlElement(name = "Data")
    protected KeyDataType data;
    @XmlElement(name = "UserId")
    protected String userId;
    @XmlElement(name = "Policy")
    protected PolicyType policy;
    @XmlElement(name = "Extensions")
    protected List<ExtensionsType> extensions;
    @XmlAttribute(name = "Id", required = true)
    protected String id;
    @XmlAttribute(name = "Algorithm")
    protected String algorithm;

    /**
     * Gets the value of the issuer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * Sets the value of the issuer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssuer(String value) {
        this.issuer = value;
    }

    /**
     * Gets the value of the algorithmParameters property.
     * 
     * @return
     *     possible object is
     *     {@link AlgorithmParametersType }
     *     
     */
    public AlgorithmParametersType getAlgorithmParameters() {
        return algorithmParameters;
    }

    /**
     * Sets the value of the algorithmParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link AlgorithmParametersType }
     *     
     */
    public void setAlgorithmParameters(AlgorithmParametersType value) {
        this.algorithmParameters = value;
    }

    /**
     * Gets the value of the keyProfileId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyProfileId() {
        return keyProfileId;
    }

    /**
     * Sets the value of the keyProfileId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyProfileId(String value) {
        this.keyProfileId = value;
    }

    /**
     * Gets the value of the keyReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyReference() {
        return keyReference;
    }

    /**
     * Sets the value of the keyReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyReference(String value) {
        this.keyReference = value;
    }

    /**
     * Gets the value of the friendlyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFriendlyName() {
        return friendlyName;
    }

    /**
     * Sets the value of the friendlyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFriendlyName(String value) {
        this.friendlyName = value;
    }

    /**
     * Gets the value of the data property.
     * 
     * @return
     *     possible object is
     *     {@link KeyDataType }
     *     
     */
    public KeyDataType getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     * 
     * @param value
     *     allowed object is
     *     {@link KeyDataType }
     *     
     */
    public void setData(KeyDataType value) {
        this.data = value;
    }

    /**
     * Gets the value of the userId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserId(String value) {
        this.userId = value;
    }

    /**
     * Gets the value of the policy property.
     * 
     * @return
     *     possible object is
     *     {@link PolicyType }
     *     
     */
    public PolicyType getPolicy() {
        return policy;
    }

    /**
     * Sets the value of the policy property.
     * 
     * @param value
     *     allowed object is
     *     {@link PolicyType }
     *     
     */
    public void setPolicy(PolicyType value) {
        this.policy = value;
    }

    /**
     * Gets the value of the extensions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extensions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExtensions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExtensionsType }
     * 
     * 
     */
    public List<ExtensionsType> getExtensions() {
        if (extensions == null) {
            extensions = new ArrayList<ExtensionsType>();
        }
        return this.extensions;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the algorithm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * Sets the value of the algorithm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlgorithm(String value) {
        this.algorithm = value;
    }

}