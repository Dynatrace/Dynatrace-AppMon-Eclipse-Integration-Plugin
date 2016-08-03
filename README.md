<img src="/img/logo/eclipse.png" width="300" />


# Dynatrace Eclipse Integration Plugin [![Build Status](https://travis-ci.org/Dynatrace/Dynatrace-Eclipse-Integration-Plugin.svg?branch=master)](https://travis-ci.org/Dynatrace/Dynatrace-Eclipse-Integration-Plugin) 

Download Latest Release: https://github.com/Dynatrace/Dynatrace-Eclipse-Integration-Plugin/releases/download/6.3.4/Dynatrace_Eclipse_Integration_6.3.4.2020.zip
([Manual Installation](#manual))

The Dynatrace Eclipse Integration Plugin enable you to:
* launch applications with an injected Dynatrace Agent directly from Eclipse
* retrieve & display the key architectural metrics (such as number of SQL queries, external API calls, exceptions and log messages) from your tests
* perform look-ups of sources files and methods from applications under diagnosis in Dynatrace Application Monitoring

#### Table of Contents

* [Installation](#installation)  
 * [Prerequisites](#prerequisites)  
* [Configuration](#configuration)
 * [Global Settings](#global)
 * [Run Configurations](#run_configurations)
* [Use the Eclipse Integration Plugin](#use)
 * [Launcher](#launcher)
 * [Test Result](#test_result)
 * [Source Code Look-up](#source_code)
*  [Problems? Questions? Suggestions?](#feedback)
* [Additional Resources](#resources)
  * [Dynatrace AppMon Documentation](#doc)
  * [Recorded Webinar](#webinar)
  * [Blogs](#blogs)


<a name="installation"/>
## Installation

<a name="prerequisites"/>
### Prerequisites

* Dynatrace Application Monitoring Version: 6.1, 6.2, 6.3
* Eclipse Version: Juno, Kepler, Luna, Mars

Find further information in the [Dynatrace community](https://community.dynatrace.com/community/display/DL/Dynatrace+Eclipse+Integration+Plugin).

* Download the [latest plugin](https://github.com/Dynatrace/Dynatrace-Eclipse-Integration-Plugin/releases/download/6.3.4/Dynatrace_Eclipse_Integration_6.3.4.2020.zip)
* unzip the archive
* in Eclipse, click on "Help" / "Install New Software..."
* Add.., Local...
* select the extracted CodeLink63 directory

![add site](/img/conf/add_site.png)

<a name="configuration"/>
## Configuration

<a name="global"/>
### Global Settings

The global settings for the plugin are located under Window / Preferences

![global settings](/img/conf/global_settings.png)

<a name="run_configurations"/>
### Run Configurations 

![run configurations](/img/conf/run_with_appmon_configuration.png) 

![edit run configurations](/img/conf/run_with_appmon_configuration_2.png) 

<a name="use"/>
## Use the Eclipse Integration Plugin

<a name="launcher"/>
### Launcher

The lancher will run applications with an injected Dynatrace Agent using the agent name and additional parameters defined in the run configuration:

![edit run configurations](/img/use/launcher.png) 

The launcher support the following launch mode:
* Java application
* JUnit test
* Equinox OSGi application
* Eclipse workbench application

The agent will output debug information into the console:

![edit run configurations](/img/use/launcher_console.png) 

<a name="test_result"/>
### Test Result

When lauching JUnit tests with the Dynatrace launcher, the plug-in will automatically register a test run to the Dynatrace Server. The results and key architectural metrics are automatically retrieved and displayed in the *Test Result* tab.

![edit run configurations](/img/use/test_result.png) 

The test results are also visible in the Dynatrace Client for further drill-down to the PurePath level.

![edit run configurations](/img/use/dynatrace_client_test_automation.png) 

<a name="source_code"/>
### Source Code Look-up

The Dynatrace Client enables you to analyze PurePaths down to the individual methods that have been instrumented in the context of the captured transaction. When you identify a problematic method either in the PurePath view or in the Methods view of the Dynatrace Client, you can use the Source Code Lookup to jump to the source code line if you have the Eclipse project open.

![edit run configurations](/img/use/source_lookup.png) 

<a name="feedback"/>
## Problems? Questions? Suggestions?

* [Eclipse Integration Plugin FAQ / Troubleshooting Guide](FAQ.md)
* Post any problems, questions or suggestions to the Dynatrace Community's [Application Monitoring & UEM Forum](https://answers.dynatrace.com/spaces/146/index.html).


<a name="resources"/>
## Additional Resources

<a name="doc"/>
### Dynatrace AppMon Documentation

- [Continuous Delivery & Test Automation](https://community.dynatrace.com/community/pages/viewpage.action?pageId=215161284)
- [Capture Performance Data from Tests](https://community.dynatrace.com/community/display/DOCDT63/Capture+Performance+Data+from+Tests)
- [Integrate Dynatrace in Continous Integration Builds](https://community.dynatrace.com/community/display/DOCDT63/Integrate+Dynatrace+in+Continuous+Integration+Builds)

<a name="webinar"/>
### Recorded Webinar

- [Online Perf Clinic - Eclipse and Jenkins Integration](https://youtu.be/p4Vh6BWlPjg)
- [Online Perf Clinic - Metrics-Driven Continuous Delivery with Dynatrace Test Automation](https://youtu.be/TXPSDpy7unw)

<a name="blogs"/>
### Blogs

- [Continuous Performance Validation in Continuous Integration Environments](http://apmblog.dynatrace.com/2013/11/27/continuous-performance-validation-in-continuous-integration-environments/)
- [Software Quality Metrics for your Continuous Delivery Pipeline – Part I](http://apmblog.dynatrace.com/2014/03/13/software-quality-metrics-for-your-continuous-delivery-pipeline-part-i/)
- [Software Quality Metrics for your Continuous Delivery Pipeline – Part II: Database](http://apmblog.dynatrace.com/2014/04/23/database-access-quality-metrics-for-your-continuous-delivery-pipeline/)
- [Software Quality Metrics for your Continuous Delivery Pipeline – Part III – Logging](http://apmblog.dynatrace.com/2014/06/17/software-quality-metrics-for-your-continuous-delivery-pipeline-part-iii-logging/)


