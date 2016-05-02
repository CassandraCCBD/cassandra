package org.apache.cassandra.modeling;


import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.io.*;

import org.apache.cassandra.metrics.*;
import org.apache.cassandra.tools.*; 

import com.google.common.collect.Multimap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/** This class provides foreground and background job metrics whenever 
  * the method is invoked
  */
public class QueueLengths {
	private static Logger logger = LoggerFactory.getLogger(QueueLengths.class);
	// an atomic integer that keeps track of the number of threads using the SSTables
	// we have a bunch of atomic integers which keep track of the active requests in native tp and readstage tp
	public static AtomicInteger numNativeRead= new AtomicInteger(0);
	public static AtomicInteger numNativeScan= new AtomicInteger(0);
	public static AtomicInteger numNativeWrite= new AtomicInteger(0);
	
	public static AtomicInteger numReadStage = new AtomicInteger();
	public static AtomicInteger numRecord = new AtomicInteger(0);
	private static NodeProbe probe;
	static	
	{
		try
		{
			probe = new NodeProbe("10.10.1.69");
		}
		catch (Exception e)
		{
			logger.debug("Opening JMX failed");
		}
	}
	// foreground activity
	public static QueueLengthBuffer foregroundActivity(QueueLengthBuffer buff)
	{
		// we get the number of active and  pending tasks in the threadpools and dump it to a file
		try
		{
			long  pending, total;
			int active;
			Multimap<String, String> threadPools = probe.getThreadPools(); 
			for (Map.Entry<String, String> tpool : threadPools.entries())                                                           
			{
				pending = (long)probe.getThreadPoolMetric(tpool.getKey(), tpool.getValue(), "PendingTasks");
				active = (int)probe.getThreadPoolMetric(tpool.getKey(), tpool.getValue(), "ActiveTasks");
				total = pending+active;
				buff.tpstats.put(tpool.getValue(),total);
			}     
		}
		catch(Exception e)
		{
			logger.debug("writing metrics to file failed", e);
		}
		return buff;
	}
}
		


