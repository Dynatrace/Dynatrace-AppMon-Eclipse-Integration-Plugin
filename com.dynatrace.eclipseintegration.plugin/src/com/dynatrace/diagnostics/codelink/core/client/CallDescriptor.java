package com.dynatrace.diagnostics.codelink.core.client;

import java.io.UnsupportedEncodingException;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;


public class CallDescriptor implements CallDescriptorInterface {

	private static final long serialVersionUID = 1298117461710246473L;
	private String className;
	private String methodName;
	private String[] methodArgs;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String method) {
		this.methodName = method;
	}

	public String[] getMethodParameters() {
		return methodArgs;
	}
	
	public void setMethodArguments(String[] args) {
		this.methodArgs = args;
	}

	
/// static methods for parsing
	
	public static CallDescriptorInterface parse(byte[] input) {
		try {
			return parse(new String(input, "utf-8"));
		}
		catch (UnsupportedEncodingException e) {
			LogHelper.logWarning("Unsupported encoding " + e + "[ErrorLocation-40]");
		}
		return parse(new String(input));
	}
	
	
	private static CallDescriptorInterface parse(String utf) {
		String[] data = utf.split(";", 3);
		CallDescriptor ret = new CallDescriptor();
		ret.setClassName(processType(data[0]));
		ret.setMethodName(data[1]);
		String argString = data[2];
		if (argString != null && argString.length() > 0) {
			String[] args = argString.split(",", -1);
			for (int i=0; i<args.length; i++) {
				args[i] = processType(args[i]);
			}
			ret.setMethodArguments(args);
		}
		return ret;
	}
	

	private static String processType(String type) {
		// we assume a '$' indicates a nested/inner class
		// for nested/inner classes to be found by eclipse 
		// search engine they need to be separated by '.'
		return type.replace('$', '.');
	}


	public String toString() {
		StringBuffer sb = new StringBuffer();

		if (className != null && className.length() > 0) {
			sb.append(className);
		}
		sb.append(";");
		if (methodName != null && methodName.length() > 0) {
			sb.append(methodName);
		}
		sb.append(";");
		if (methodArgs != null && methodArgs.length > 0) {
			sb.append(methodArgs[0]);
			for (int i=1; i<methodArgs.length; i++) {
				sb.append(",");
				sb.append(methodArgs[i]);
			}
		}
		
		return sb.toString();
	}
}
