<img src="/img/logo/eclipse.png" width="300" />


# Dynatrace Eclipse Integration Plugin


The Dynatrace Eclipse Integration Plugin enable you to:
* launch applications with an injected Dynatrace Agent directly from Eclipse
* retrieve & display the key architectural metrics (such as number of SQL queries, external API calls, exceptions and log messages) from your tests
* perform look-ups of sources files and methods from applications under diagnosis in Dynatrace Application Monitoring</td>
</tr>
</table>

## Installation

### Prerequisites

* Dynatrace Application Monitoring Version: 6.1, 6.2, 6.3
* Eclipse Version: Juno, Kepler, Luna, Mars

Find further information in the [Dynatrace community](https://community.dynatrace.com/community/display/DL/Dynatrace+Eclipse+Integration+Plugin).

### From the Eclipse Repository

* in Eclipse, click on "Help" / "Install New Software..."

![install new software](/img/conf/install_new_software.png)
* click on Add... and enter the repository URL: http://www.dynatrace.com/eclipseintegration

![add repository](/img/conf/add_repository.png)

* select the plugin and click on Next >

![select plugin](/img/conf/install_local.png)
* accept the license agreement
* restart Eclipse


### Manual Installation

The process is the same as the manual installation (see below). 

* Download the plugin from the [Dynatrace community](https://community.dynatrace.com/community/display/DL/Dynatrace+Eclipse+Integration+Plugin)
* in Eclipse, click on "Help" / "Install New Software..."
* click on Archive... and select the downloaded file

![add site](/img/conf/add_site.png)


## Configuration

### Global Settings

The global settings for the plugin are located under Window / Preferences

![global settings](/img/conf/global_settings.png)

### Run Configurations 

![run configurations](/img/conf/run_with_appmon_configuration.png) 

![edit run configurations](/img/conf/run_with_appmon_configuration_2.png) 

## Use

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

### Test Result

When lauching JUnit tests, the plug-in will automatically register a test run to the Dynatrace Server. The results and key architectural metrics are automatically retrieved and displayed in the *Test Result* tab.

![edit run configurations](/img/use/test_result.png) 

### Code Look-up

## Problems? Questions? Suggestions?

* [Eclipse Integration Plugin FAQ / Troubleshooting Guide](FAQ.md)
* Post any problems, questions or suggestions to the Dynatrace Community's [Application Monitoring & UEM Forum](https://answers.dynatrace.com/spaces/146/index.html).
