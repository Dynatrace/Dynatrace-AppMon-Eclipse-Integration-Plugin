package com.dynatrace.diagnostics.codelink.core.client;

import java.io.Serializable;

/**
  * 
  * @author markus.poechtrager
 */
public interface CallDescriptorInterface extends Serializable {
	/**
	  * @return fully qualified class name
	  * @author markus.poechtrager
	 */
	public String getClassName();
	
	/**
	  * @return method name
	  * @author markus.poechtrager
	 */
	public String getMethodName();
	
	/**
	  * @return array of fully qualified parameter types
	  * @author markus.poechtrager
	 */
	public String[] getMethodParameters();
	
}
