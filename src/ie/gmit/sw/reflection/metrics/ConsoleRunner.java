package ie.gmit.sw.reflection.metrics;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConsoleRunner {
	public static void main(String[] args) throws IOException {

		File f = new File(args[0]);
		JarMetric jm = JarAnalyzer.analyze(f);
		
		List<ClassMetric> classes = jm.getClassMetrics();
		
		System.out.printf("\n\n/////////////////STABILITIES/////////////\n");
		for(ClassMetric cm : classes){
			System.out.println(cm.getClassName());
			System.out.println(cm.getStability());
		}
	}
}
