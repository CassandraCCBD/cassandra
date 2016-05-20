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

/** This class contains all the decision trees that will
 *  be accessed.
 *  1 - Readstage 
 *  2 - NonLocalReads
 *  3 - LocalMutations
 *  4 - NonLocalMutations
 */
public class AllTrees
{
	public static TestREPTree Readstage = new TestREPTree(29);
	public static TestREPTree NonLocalReads = new TestREPTree(30);
	public static TestREPTree LocalMutations = new TestREPTree(29);
	public static TestREPTree NonLocalMutations = new TestREPTree(30);

}

	

