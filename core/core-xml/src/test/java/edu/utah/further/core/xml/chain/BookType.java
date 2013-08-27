/**
 * Copyright (C) [2013] [The FURTHeR Project]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.05.20 at 10:19:07 AM MDT 
//


package edu.utah.further.core.xml.chain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;

import edu.utah.further.core.api.collections.CollectionUtil;


/**
 * <p>Java class for bookType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="bookType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ISBN" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="price" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="authors">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="authorName" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="promotion">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="Discount" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="None" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="publicationDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="bookCategory">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NCName">
 *               &lt;enumeration value="magazine"/>
 *               &lt;enumeration value="novel"/>
 *               &lt;enumeration value="fiction"/>
 *               &lt;enumeration value="other"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="itemId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bookType", propOrder = {
    "name",
    "isbn",
    "price",
    "authors",
    "description",
    "promotion",
    "publicationDate",
    "bookCategory"
})
public class BookType {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(name = "ISBN")
    protected long isbn;
    @XmlElement(required = true)
    protected String price;
    @XmlElement(required = true)
    protected BookType.Authors authors;
    protected String description;
    @XmlElement(required = true)
    protected BookType.Promotion promotion;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar publicationDate;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String bookCategory;
    @XmlAttribute
    protected String itemId;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(final String value) {
        this.name = value;
    }

    /**
     * Gets the value of the isbn property.
     * 
     */
    public long getISBN() {
        return isbn;
    }

    /**
     * Sets the value of the isbn property.
     * 
     */
    public void setISBN(final long value) {
        this.isbn = value;
    }

    /**
     * Gets the value of the price property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrice() {
        return price;
    }

    /**
     * Sets the value of the price property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrice(final String value) {
        this.price = value;
    }

    /**
     * Gets the value of the authors property.
     * 
     * @return
     *     possible object is
     *     {@link BookType.Authors }
     *     
     */
    public BookType.Authors getAuthors() {
        return authors;
    }

    /**
     * Sets the value of the authors property.
     * 
     * @param value
     *     allowed object is
     *     {@link BookType.Authors }
     *     
     */
    public void setAuthors(final BookType.Authors value) {
        this.authors = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(final String value) {
        this.description = value;
    }

    /**
     * Gets the value of the promotion property.
     * 
     * @return
     *     possible object is
     *     {@link BookType.Promotion }
     *     
     */
    public BookType.Promotion getPromotion() {
        return promotion;
    }

    /**
     * Sets the value of the promotion property.
     * 
     * @param value
     *     allowed object is
     *     {@link BookType.Promotion }
     *     
     */
    public void setPromotion(final BookType.Promotion value) {
        this.promotion = value;
    }

    /**
     * Gets the value of the publicationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPublicationDate() {
        return publicationDate;
    }

    /**
     * Sets the value of the publicationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPublicationDate(final XMLGregorianCalendar value) {
        this.publicationDate = value;
    }

    /**
     * Gets the value of the bookCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBookCategory() {
        return bookCategory;
    }

    /**
     * Sets the value of the bookCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBookCategory(final String value) {
        this.bookCategory = value;
    }

    /**
     * Gets the value of the itemId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * Sets the value of the itemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemId(final String value) {
        this.itemId = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="authorName" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "authorName"
    })
    public static class Authors {

        @XmlElement(required = true)
        protected List<String> authorName;

        /**
         * Gets the value of the authorName property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <code>set</code> method for the authorName property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAuthorName().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getAuthorName() {
            if (authorName == null) {
                authorName = CollectionUtil.newList();
            }
            return this.authorName;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="Discount" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="None" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "discount",
        "none"
    })
    public static class Promotion {

        @XmlElement(name = "Discount")
        protected String discount;
        @XmlElement(name = "None")
        protected String none;

        /**
         * Gets the value of the discount property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDiscount() {
            return discount;
        }

        /**
         * Sets the value of the discount property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDiscount(final String value) {
            this.discount = value;
        }

        /**
         * Gets the value of the none property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNone() {
            return none;
        }

        /**
         * Sets the value of the none property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNone(final String value) {
            this.none = value;
        }

    }

}
