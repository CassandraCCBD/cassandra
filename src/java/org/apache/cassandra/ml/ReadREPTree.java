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
public class ReadREPTree implements REPInterface
{
	private static Logger logger = LoggerFactory.getLogger(TestREPTree.class);
	REPTree obj;
	Instances structure;	
	final int MAX_SIZE;
	boolean built;
	boolean started;
	Semaphore sem = new Semaphore(1, true);
	public Instances initInstance()
	{
		// we create a ArrayList of attributes
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		// we add 5 columns to this list of attributes
		for (int i=0;i<29;i++)
		{
			attributes.add(new Attribute("Col"+i));
		}
		//we now initialize an instance with these attributes
		Instances structure = new Instances("test_instance", attributes,0); 
		structure.setClassIndex(structure.numAttributes() - 1);
		return structure;
	}

	ReadREPTree(int MAX_SIZE)
	{
		logger.debug("INIT of Tree");
		this.MAX_SIZE = MAX_SIZE;
		this.structure = initInstance();
		started=false;
		logger.debug("Structure is " + structure);
	}
/*		// we dump a bunch of data into this
		for (int j=0;j<100000;j++)
		{
			DataSet set = new DataSet();
			set.init();
			structure.add(new DenseInstance(1.0,set.row));
		}
		System.out.println("After adding");
//		System.out.println(structure);
		/** We don't want to do the decision tree until we can build the instance properly */
	public void buildTree()
	{
		try
		{	
		if (started==true)
			return;
		started=true;
		sem.acquire();
		structure.setClassIndex(structure.numAttributes() - 1);
		obj = new REPTree();
		//obj.buildClassifier(train);
		obj.buildClassifier(structure);
		//we check this tree out in a file
		try
		{
			String path = "/root/metrics/Trees";
			PrintWriter writer=new PrintWriter(new BufferedWriter(new FileWriter(path,true)));
			writer.println(structure);
			writer.println("*******************************");
			writer.println(obj);
			writer.println("*******************************");
			writer.println("*******************************");
			writer.close();
		}
		catch(Exception e)
		{
			logger.debug("Couldn't write to file");
		}
		System.out.println(obj);
		System.out.println(" *******************************");
		Evaluation eval = new Evaluation(structure);
		StringBuffer preds = new StringBuffer();
		PlainText plainText = new PlainText();
		plainText.setHeader(structure);
		plainText.setBuffer(preds);
		eval.evaluateModel(obj,structure,plainText);
		this.built=true;
		sem.release();
		}
		catch (Exception e)
		{

		}
		//System.out.println(preds.toString());
	}		
	/*	System.out.println(" *******************************");
		weka.core.Range attr=null;

		eval = new Evaluation(structure);
		//AbstractOutput p=null;
		StringBuffer predy = new StringBuffer();
		eval.crossValidateModel(obj,structure,10,new Random(1),plainText,attr,true);
		//eval.evaluateModel(obj, test);
		System.out.println(eval.toSummaryString());
		//System.out.println(eval.toClassDetailsString());
	
		System.out.println(" *******************************");
		System.out.println(plainText.toString());
		//System.out.println(eval.toMatrixString());
		System.out.println(" *******************************");
		
		double[] prediction=obj.distributionForInstance(structure.get(0));
		for(int i=0;i<prediction.length;i++)
			System.out.println(" Probs of class "+structure.classAttribute().value(i)+" : "+Double.toString(prediction[i]));
		unitTest();*/
	
	/** This function only tests for a single unit or row */
	public double unitTest(ArrayList<Double> row) 
	{
		try
		{
		logger.debug("Unit testing");
		Instances struct = initInstance();
		DataSet set = new DataSet(row.size());
		set.init(row);
		struct.add(new DenseInstance(1.0,set.row));
		System.out.println(struct);
		StringBuffer preds = new StringBuffer();
		PlainText plainText = new PlainText();
		plainText.setHeader(struct);
		plainText.setBuffer(preds);
		Evaluation eval = new Evaluation(struct);
		//AbstractOutput p=null;
		eval.evaluateModel(obj, struct);
		//eval.crossValidateModel(obj,struct,1,new Random(1),plainText,null,true);
	/*	System.out.println(" *******************************");
		System.out.println("predictions");
		System.out.println(eval.predictions().get(0).actual());
		System.out.println(eval.predictions().get(0).predicted());*/
		return eval.predictions().get(0).predicted();
		}
		catch(Exception e)
		{
			return -1;
		}
				
	}		

	public void addToDataset(ArrayList<Double> row)
	{	
		// we add to the instance 
		//logger.debug("in addToDataset, row is " + row + " structure " + structure);
		DataSet set = new DataSet(row.size());
		boolean success= set.init(row);
		if (success)
			structure.add(new DenseInstance(1.0,set.row));
		//checks if size of dataset is above 'n', calls buildTree
		checkSize();
		logger.debug("Added successfully, size is " + this.structure.size());
			
	}

	void checkSize()
	{
		try
		{
		if (!built && this.structure.size()>MAX_SIZE )
				buildTree();
		}
		catch(Exception e)
		{

		}
	}



	public boolean treeAvailable()
	{
		return built;
	}	


}

