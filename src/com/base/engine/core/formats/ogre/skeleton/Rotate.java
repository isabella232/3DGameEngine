//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.11.01 at 02:28:31 PM CET 
//


package com.base.engine.core.formats.ogre.skeleton;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "axis"
})
@XmlRootElement(name = "rotate")
public class Rotate {

    @XmlAttribute(name = "angle", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String angle;
    @XmlElement(required = true)
    protected Axis axis;

    /**
     * Gets the value of the angle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAngle() {
        return angle;
    }

    /**
     * Sets the value of the angle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAngle(String value) {
        this.angle = value;
    }

    /**
     * Gets the value of the axis property.
     * 
     * @return
     *     possible object is
     *     {@link Axis }
     *     
     */
    public Axis getAxis() {
        return axis;
    }

    /**
     * Sets the value of the axis property.
     * 
     * @param value
     *     allowed object is
     *     {@link Axis }
     *     
     */
    public void setAxis(Axis value) {
        this.axis = value;
    }

}
