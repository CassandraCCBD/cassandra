package org.apache.cassandra.ml;
import weka.classifiers.trees.M5P;
import weka.classifiers.trees.REPTree;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.*;
import weka.core.converters.ArffLoader;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.evaluation.output.prediction.PlainText;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.evaluation.output.prediction.AbstractOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.ArrayUtils;
 
import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;
class DataSet
{
	private static Logger logger = LoggerFactory.getLogger(DataSet.class);
	double []row;
	int size;
	DataSet(int size)
	{
		this.size = size;
		this.row = null;
	}
	
	boolean init(ArrayList<Double> numbers)
	{
		if (numbers.size()==this.size)
		{
			logger.debug("Peace Size was " + numbers.size());
			Double temp[]= numbers.toArray(new Double[size]);
			this.row = ArrayUtils.toPrimitive(temp);
			return true;
		}
		else
		{
			logger.debug("Size was " + numbers.size() + " wanted " + this.size);
			return false;
		}
	}
			
}
	
