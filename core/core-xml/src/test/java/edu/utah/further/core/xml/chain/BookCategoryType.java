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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for bookCategoryType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="bookCategoryType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="magazine"/>
 *     &lt;enumeration value="novel"/>
 *     &lt;enumeration value="fiction"/>
 *     &lt;enumeration value="other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "bookCategoryType")
@XmlEnum
public enum BookCategoryType {

    @XmlEnumValue("magazine")
    MAGAZINE("magazine"),
    @XmlEnumValue("novel")
    NOVEL("novel"),
    @XmlEnumValue("fiction")
    FICTION("fiction"),
    @XmlEnumValue("other")
    OTHER("other");
    private final String value;

    BookCategoryType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BookCategoryType fromValue(String v) {
        for (BookCategoryType c: BookCategoryType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
