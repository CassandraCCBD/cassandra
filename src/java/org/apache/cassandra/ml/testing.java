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
import java.io.File;
import java.util.*;
public class testing{
	public static void main(String[] args)throws Exception{
		DataSource loader = new DataSource(args[0]);
		//loader.setFile(new File(args[0]));
		Instances structure = loader.getDataSet();
		structure.setClassIndex(structure.numAttributes()-1);
		/*int trainSize = (int) Math.round(structure.numInstances() * 60/100);
		int testSize =  structure.numInstances() - trainSize;
		Instances train = new Instances(structure, 0, trainSize);
		Instances test = new Instances(structure, trainSize, testSize);*/
		REPTree obj = new REPTree();
		//obj.buildClassifier(train);
		obj.buildClassifier(structure);
		System.out.println(obj);
		System.out.println(" *******************************");
		Evaluation eval = new Evaluation(structure);
		StringBuffer preds = new StringBuffer();
		PlainText plainText = new PlainText();
		plainText.setHeader(structure);
		plainText.setBuffer(preds);
		eval.evaluateModel(obj,structure,plainText);
		System.out.println(preds.toString());
		
		System.out.println(" *******************************");
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
	}
}
