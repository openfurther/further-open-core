--
-- Copyright (C) [2013] [The FURTHeR Project]
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--         http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

-- Hibernate generated query - list mode
select
this_.id as id1_2_, this_.name as name1_2_, event1_.id as id2_0_, event1_.event as event2_0_,
event1_.person_id as person3_2_0_, complexper4_.id as id1_1_, complexper4_.name as name1_1_

from Person this_
inner join Event event1_ on this_.id=event1_.person_id
left outer join Person complexper4_ on event1_.person_id=complexper4_.id where event1_.event like ?

-- ============================================================

-- Hibernate generated query - scroll mode
select this_.id as id1_2_, this_.name as name1_2_, event1_.id as id2_0_, event1_.event as event2_0_,
event1_.person_id as person3_2_0_, complexper4_.id as id1_1_, complexper4_.name as name1_1_

from Person this_
inner join Event event1_ on this_.id=event1_.person_id
left outer join Person complexper4_ on event1_.person_id=complexper4_.id where event1_.event like ?
