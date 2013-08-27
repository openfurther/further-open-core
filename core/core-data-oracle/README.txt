====
    Copyright (C) [2013] [The FURTHeR Project]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

            http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
====

You must install Oracle dependencies to use this module.

+++++++++++++++++++++++++++++

This module requires proprietary Oracle libraries - match the versions with your version of Oracle

1.) Oracle Driver 11.2.0.2.0 - com.oracle:ojdbc6:11.2.0.2.0
 
	Once downloaded, run: 
		mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.2.0 -Dpackaging=jar -Dfile=/path/to/ojdbc6.jar

2.) Oracle XDK 11.2.0.1.0 

	The Java XDK components are included with Oracle Database.
	
	http://docs.oracle.com/cd/E23943_01/appdev.1111/b28394/adx_overview.htm#CJAHCADF
	
	Once obtained, run: 
		mvn install:install-file -DgroupId=com.oracle -DartifactId=xdb -Dversion=11.2.0.1.0 -Dpackaging=jar -Dfile=/path/to/xdb.jar
		mvn install:install-file -DgroupId=com.oracle -DartifactId=xmlparserv2 -Dversion=11.2.0.1.0 -Dpackaging=jar -Dfile=/path/to/xmlparserv2.jar

+++++++++++++++++++++++++++++
		