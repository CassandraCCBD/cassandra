package org.apache.cassandra.ml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
/** This class monitors the expected failures and when it goes above a
 *  certain threshold it scales itself up
 */
public class Monitoring implements Runnable
{
	private static Logger logger = LoggerFactory.getLogger(Monitoring.class);
	double threshold;	
	int numcores;
	public void run()
	{
		numcores=4;
		while (true)
		{
			// get the ratio for failures vs success
			try
			{
				logger.debug("trying things now");
				double dangerRatio = Failures.decisionTreeDanger.get()/Failures.totalQueries.get();
				double safeRatio = Failures.decisionTreeSafe.get()/Failures.totalQueries.get();
				if (dangerRatio>=this.threshold)
				{
					// here we need to send some message, do some upscaling etc
					// also need to clear the counters here
					// we execute "cgset -r cpuset='stringVar' coreTest" 
					logger.debug("Upping it");
					String stringVar = getmoreCores();
					String cmd = "cgset -r cpuset.cpus=" + getmoreCores() + " coreTest";
					Process proc = Runtime.getRuntime().exec(cmd);
					Failures.decisionTreeDanger.set(0);
					Failures.decisionTreeSafe.set(0);
					Failures.totalQueries.set(0);
				}
				else if (safeRatio>=0.4)
				{
					// here we downscale the entire thing 
					logger.debug("Downing it");
					String cmd = "cgset -r cpuset.cpus=" + getlessCores() + " coreTest";
					String[] env = {"PATH=/bin:/usr/bin/"};
					Process proc = Runtime.getRuntime().exec(cmd, env);
					Failures.decisionTreeDanger.set(0);
					Failures.decisionTreeSafe.set(0);
					Failures.totalQueries.set(0);
				}
				else
				{
					logger.debug("Safe Ratio - " + safeRatio + " DangerRatio " + dangerRatio);
				}
				Thread.sleep(1000);	
			}
			catch (Exception e)
			{
				try
				{
					logger.debug("monitoring issue, maybe workload hasn't started yet");
					Thread.sleep(1000);	
				}
				catch (Exception e1)
				{}
			}
		}
	}
	
	public String getmoreCores()
	{
		if ((numcores++)==0)
			return "0-1";
		else if ((numcores++)==1)
			return "0-2";
		else 
		{
			numcores++;
			return "0-3";
		}
	}

	public String getlessCores()
	{
		if ((numcores--)==2)
			return "0-1";
		else if ((numcores--)==3)
			return "0-2";
		else 
		{
			--numcores;
			return "0";
		}
	}
	public void dumpToFile(double failureRatio, double accuracy)
	{
		String path="/root/metrics/monitor";
		try
		{ 
			PrintWriter writer=new PrintWriter(new BufferedWriter(new FileWriter(path,true)));
			writer.println("ratio - " + failureRatio + " accuracy - " + accuracy);
			writer.close();
		}
		catch (Exception e)
		{
			logger.debug("error writing to monitoring file");
		}
	}

}
