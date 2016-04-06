package org.apache.cassandra.modeling;

import java.util.concurrent.atomic.AtomicInteger;
public class ReadStageMapping {
	// keeps track of the id of the newest request
	private static AtomicInteger id = new AtomicInteger(0);
	//TODO: Add hashmaps to store the readstage response time and store 
	//	the "getThroughCache" times
	//	Any further crunchin of these numbers can be done within the database
	
	public static int getAndIncrementId()
	{
		return id.incrementAndGet();
	}
}

