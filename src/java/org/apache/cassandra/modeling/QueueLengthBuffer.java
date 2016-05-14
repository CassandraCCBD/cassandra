package org.apache.cassandra.modeling;

import java.util.LinkedHashMap;
import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueueLengthBuffer
{
	private static Logger logger = LoggerFactory.getLogger(QueueLengthBuffer.class);
	// stores all the pending tpstats for a given query 
	LinkedHashMap<Object,Object> tpstats;
	LinkedHashMap<Object,Object> jmxTpstats;

	long responseTime;	

	public QueueLengthBuffer()
	{
		tpstats= new LinkedHashMap<Object,Object>();
		jmxTpstats = new LinkedHashMap<Object,Object>();
	}
	public void setResponseTime(long responseTime, int type)
	{
		this.responseTime = responseTime;
		if (type==0) // this is select
		{
			QueueLengths.numNativeRead.decrementAndGet();
		}
		else if (type==-1)
		{}
		else
		{
			QueueLengths.numNativeWrite.decrementAndGet();
		}
	}
	public void getParams(int type, int limit)
	{
		if (type==0)
		{
			tpstats.put("Type", 0);
			tpstats.put("NativeRead",QueueLengths.numNativeRead.incrementAndGet());
			tpstats.put("NativeWrite",QueueLengths.numNativeWrite.get());
		}
		else if (type==-1)
		{
			tpstats.put("Type", 0);
			tpstats.put("NativeRead",QueueLengths.numNativeRead.get());
			tpstats.put("NativeWrite",QueueLengths.numNativeWrite.get());
		}
		else
		{
			tpstats.put("Type", 1);
			tpstats.put("NativeRead",QueueLengths.numNativeRead.get());
			tpstats.put("NativeWrite",QueueLengths.numNativeWrite.incrementAndGet());
		}
		tpstats.put("Limit", limit);
		tpstats.put("numReadStage", QueueLengths.numReadStage.get());
	}

	public void dumpToFile(String path)
	{
		if (path==null)
		{
			if ((int)tpstats.get("Type") ==0)
				path = "/root/metrics/metrics1Read";
			else
				path =  "/root/metrics/metrics1Write";
		}
			try
			{
				PrintWriter writer=new PrintWriter(new BufferedWriter(new FileWriter(path,true)));
				for (Object ob: tpstats.keySet())
					writer.print(tpstats.get(ob) + ",");
				writer.println(responseTime);
				writer.close();
				logger.debug("Succeeded writing tpstats " + path);
			}
			catch (Exception e)
			{
				logger.debug("Writing tpstats failed ", e);
			}
	}
	
}
