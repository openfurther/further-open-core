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
package edu.utah.further.dts.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.apelon.dts.client.attribute.DTSQualifier;
import com.apelon.dts.client.attribute.QualifierType;
import com.apelon.dts.client.attribute.QualifiesItemType;

public class UDTSQualifierTest
{

	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void testDTSQualifier()
	{
		QualifiesItemType qit1 = QualifiesItemType.CONCEPT_ASSOCIATION;
		QualifiesItemType qit2 = QualifiesItemType.CONCEPT_ASSOCIATION;
		
		QualifierType type1 = new QualifierType("Specific Specimen", 2, "Q2", 32769, qit1);
		QualifierType type2 = new QualifierType("Specific Specimen", 2, "Q2", 32769, qit2);
		
		DTSQualifier q1 = new DTSQualifier(type1, "119297000", false);
		DTSQualifier q2 = new DTSQualifier(type2, "119297000", false);
		
		assertEquals(2, q1.getQualifierType().getId());
		assertEquals(32769, q1.getQualifierType().getNamespaceId());
		assertEquals("Specific Specimen", q1.getQualifierType().getName());
		assertEquals("Q2", q1.getQualifierType().getCode());

		assertEquals(2, q2.getQualifierType().getId());
		assertEquals(32769, q2.getQualifierType().getNamespaceId());
		assertEquals("Specific Specimen", q2.getQualifierType().getName());
		assertEquals("Q2", q2.getQualifierType().getCode());
		
		assertTrue(qit1.equals(qit2));
		assertTrue(qit1.hashCode() == qit2.hashCode());
		
		assertTrue(type1.equals(type2));
		assertTrue(type1.hashCode() == type2.hashCode());
		
		assertTrue(q1.equals(q2));
		assertTrue(q1.hashCode() == q2.hashCode());
		
		assertTrue(q1.getQualifierType().equals(q2.getQualifierType()));
		assertTrue(q1.getQualifierType().hashCode() == q2.getQualifierType().hashCode());
		
		assertTrue((q1.getQualifierType().getId() ^ q1.getValue().hashCode()) == (q2.getQualifierType().getId() ^ q2.getValue().hashCode()));
		assertTrue(q1.getQualifierType().hashCode() == q2.getQualifierType().hashCode());
		
		assertTrue(q1.getValue().equals(q2.getValue()));
		assertTrue(q1.getValue().hashCode() == q2.getValue().hashCode());
		
		int h1 = hash(q1.hashCode());

		int h2 = hash(q2.hashCode());

		assertTrue(h1 == h2);
	}
	
	private int hash(int h) {
		int y = h;
        y ^= (y >>> 20) ^ (y >>> 12);
        return y ^ (y >>> 7) ^ (y >>> 4);

	}

}