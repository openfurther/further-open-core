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

create or replace PROCEDURE GET_PHYSICAL_QUERY
  (
    p_sys_refcur OUT SYS_REFCURSOR,
    p_query_id     NUMBER,
    p_namespace_id NUMBER )
AS
BEGIN
  IF ( p_namespace_id = 32776 ) THEN -- UUEDW
    BUILD_UUEDW_QUERY2( p_query_id );
  END IF;
  OPEN p_sys_refcur FOR SELECT * FROM query_nmspc WHERE query_id = p_query_id AND namespace_id = p_namespace_id;
END GET_PHYSICAL_QUERY;
