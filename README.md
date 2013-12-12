## What is JMCloud-ComputeManager?
JMCloud-ComputeManager is a stand-alone GUI application for managing Amazon public cloud.

### These functionalities are:
* Managing Amazon EC2 Instances of all regions in one view
* Managing a group unit consisting of Amazon EC2 Instances in each region
* Administrating security group for a group unit
* Managing keypairs for a group unit
* Operating group(s) or Amazon EC2 instance(s)
* Getting existing instance information very easy (such as Public IP...)
* Easy to Access an instance using SSH client

### In next version, JMCloud-ComputeManager will offer some enhanced functionalities like:
* Managing keypair of each instance
* Cloud App (Automate the installation of an application on VM and manage it)
	- RStudio Server
	- Apache Hadoop 1.0.x (pseudo mode)

## Requirements:
* JAVA JDK 1.7
	- set the JAVA_HOME environment variable and bin path
* the Amazon EC2 API tools
	- download site : http://aws.amazon.com/developertools/351
	- set the EC2_HOME environment variable and bin path
* Amazon AWS account 
	- Web site : http://aws.amazon.com
* Cygwin for Windows OS
	- download site : http://www.cygwin.com/install.html
	- install with openssh package
	- set the CYGWIN_HOME environment variable and bin path

##License	
	Copyright 2013 Jemin Huh
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	    http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.

===========================================================
Please see the [JMCloud-ComputeManager wiki](https://github.com/JM-Lab/JMCloud-ComputeManager/wiki) for information. 
