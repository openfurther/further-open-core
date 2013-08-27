//
// Copyright (C) [2013] [The FURTHeR Project]
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//         http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package edu.utah.further.party3.apelon.internal.aspects;

import java.io.Serializable;

public aspect AddSerializableAspect {

	declare parents : com.apelon.dts.client.attribute.DTSPropertyType implements Serializable;
	declare parents : com.apelon.dts.client.attribute.ModifiesItemType implements Serializable;
	declare parents : com.apelon.dts.client.attribute.PropertyValueSize implements Serializable;
	declare parents : com.apelon.dts.client.common.DTSObject implements Serializable;
	declare parents : com.apelon.dts.client.namespace.Authority implements Serializable;
	declare parents : com.apelon.dts.client.namespace.ContentVersion implements Serializable;
	declare parents : com.apelon.dts.client.namespace.Namespace implements Serializable;
	declare parents : com.apelon.dts.client.namespace.NamespaceType implements Serializable;

}
