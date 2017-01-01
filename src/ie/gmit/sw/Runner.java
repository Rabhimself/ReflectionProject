package ie.gmit.sw;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Runner {
	public static void main(String[] args) {
		Map<String, Set<Class>> bigMap = null;
		try {
			bigMap = new JarUnpacker().unpack(args[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Iterator it = bigMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); 
	    }
		
	}
}
