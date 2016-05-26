package org.apache.cassandra.ml;

import java.util.concurrent.atomic.AtomicInteger;


/** This class keeps track of the number of expected failures 
 * 	vs the number of actual failures.
 * 	Required by the monitoring thread and ReadCallBack.java
 */
public class Failures
{
	public static AtomicInteger decisionTreeDanger = new AtomicInteger(0);
	public static AtomicInteger decisionTreeSafe = new AtomicInteger(0);
	//public static AtomicInteger actualFailures = new AtomicInteger(0);
	public static AtomicInteger totalSuccess = new AtomicInteger(0);
	public static AtomicInteger totalQueries = new AtomicInteger(0);
	//TODO- figure out something to downscale as well
}


