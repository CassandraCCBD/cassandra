package org.apache.cassandra.ml;
import weka.classifiers.trees.M5P;
import weka.classifiers.trees.REPTree;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.evaluation.output.prediction.PlainText;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.evaluation.output.prediction.AbstractOutput;
import java.util.*;
import java.io.File;

public interface REPInterface
{
	/** Creates and initializes the instance with headers 
	 * needed for both building and testing the tree
	 */
	Instances initInstance();

	/** Creates the Decision tree once the size of the instance has crossed a particular limit
	 *  will be invoked internally by checkSize
	 */
	void buildTree() throws Exception;

	/** Creates a instance of size one, passes it into the REP Tree
	 *  gives the expected value of response time 
	 */
	public double unitTest(ArrayList<Double> row) throws Exception;

	/** Called if the dataset available for the tree is not large enough
	 *  to build the tree. 
	 *  Added to only the training set, we do not keep track of the testing set
	 */
	public void addToDataset(ArrayList<Double> row);

	/** checks if the tree is built or not.
	 *  Overview of Algo -
	 *  1 - check if treeAvailable 
	 *  2 - if yes - unitTest
	 *  	if no - addToDataset
	 */
	public boolean treeAvailable();
	

}
