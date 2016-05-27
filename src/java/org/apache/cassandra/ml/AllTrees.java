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
	public static TestREPTree Readstage[];
	public static TestREPTree NonLocalReads[];
	public static TestREPTree LocalMutations[];
	public static TestREPTree NonLocalMutations[];

	static 
	{
		Readstage = new TestREPTree[4];
		NonLocalReads = new TestREPTree[4];
		for (int i=0;i<4;i++)
		{
			//instantiating each 
			Readstage[i] = new TestREPTree(10000, "ReadStage");
			NonLocalReads[i] = new TestREPTree(10000, "NonLocalReads");
		}
	}

}

	

