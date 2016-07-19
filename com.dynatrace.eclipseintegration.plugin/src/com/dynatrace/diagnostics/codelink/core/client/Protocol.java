/*********************************************
  *  JLoadTrace (c) Dynatrace software GmbH
  *
  * @file: Protocol.java
  * @date: 22.08.2007
  * @author: markus.poechtrager
  *
  */
package com.dynatrace.diagnostics.codelink.core.client;



/**
 * 
 * @author markus.poechtrager
 */
public interface Protocol {
	static final int REQUEST_PING			= 200;
	static final int REQUEST_LOOKUP 		= 250;
	static final int REQUEST_STATUS			= 220;
	
	static final byte PLUGIN_VERSION_OK 		= 0;
	static final byte PLUGIN_VERSION_MISMATCH 	= 1;
	
	static final int RESPONSE_NOT_FOUND 	= 51;
	static final int RESPONSE_FOUND 		= 50;
	
	static final int IDE_ECLIPSE 			= 0;
	
	static final Version PLUGIN_VERSION_31 = new Version(3,1,0);
	
	static class Version implements Comparable<Object> {
		private int major = 0;
		private int minor = 0;
		private int revision = 0;

		/**
		 * @param major
		 * @param minor
		 * @param revision
		 */
		Version(int major, int minor, int revision) {
			this.major = major;
			this.minor = minor;
			this.revision = revision;
		}

		Version(byte[] version) {
			this.major = 	version[0];
			this.minor = 	version[1];
			this.revision = version[2];
		}
		
		public int getMajor() {
			return major;
		}
		public int getMinor() {
			return minor;
		}
		public int getRevision() {
			return revision;
		}
		
		public byte[] toBinary() {
			return new byte[] { (byte)major, (byte)minor, (byte)revision };
		}

		public String toString() {
			return major + "." + minor + "." + revision;
		}

		public int compareTo(Object o) {
			if (!(o instanceof Version)) {
				return 0;
			}
			
			Version v = (Version)o;
			
			int majO = v.major;
			int minO = v.minor;
			int revO = v.revision;
			
			if (major > majO) {
				return 1;
			}
			else if (major == majO) {
				if (minor > minO) {
					return 1;
				}
				else if (minor == minO) {
					if (revision > revO) {
						return 1;
					}
					else if (revision == revO) {
						return 0;
					}
					else {
						return -1;
					}
				}
				else {
					return -1;
				}
			}
			else {
				return -1;
			}
		}
	}
}
