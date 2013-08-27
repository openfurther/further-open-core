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

/* Sets up test data for core-data unit tests */
INSERT INTO Person (id, name) VALUES (1, 'John Doe');
INSERT INTO Person (id, name) VALUES (2, 'John Doe');
INSERT INTO Person (id, name) VALUES (3, 'John Doe');
INSERT INTO Person (id, name) VALUES (4, 'John Doe');
INSERT INTO Person (id, name) VALUES (5, 'Jane Doe');
INSERT INTO Person (id, name) VALUES (6, 'Jack Doe');
INSERT INTO Person (id, name) VALUES (7, 'Jill Doe');

INSERT INTO Event (id, event, person_id) VALUES (1, 'Event 1', 1)
INSERT INTO Event (id, event, person_id) VALUES (2, 'Event 1', 1)
INSERT INTO Event (id, event, person_id) VALUES (3, 'Event 1', 1)
INSERT INTO Event (id, event, person_id) VALUES (4, 'Event 1', 1)
INSERT INTO Event (id, event, person_id) VALUES (5, 'Event 1', 5)
INSERT INTO Event (id, event, person_id) VALUES (6, 'Event 2', 5)
INSERT INTO Event (id, event, person_id) VALUES (7, 'Event 1', 6)
INSERT INTO Event (id, event, person_id) VALUES (8, 'Event 2', 6)
INSERT INTO Event (id, event, person_id) VALUES (9, 'Event 1', 7)
INSERT INTO Event (id, event, person_id) VALUES (10, 'Event 2', 7)
INSERT INTO Event (id, event, person_id) VALUES (11, 'Event 3', 5)
