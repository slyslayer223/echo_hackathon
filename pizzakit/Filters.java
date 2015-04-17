import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;


public class Filters {
	
	public String filterSizeByList(List<String> args) {
		for (String s : args) {
			if (s.equals("large") || s.equals("medium") || s.equals("small")) {
				return s;
			}
		}
		return null;
	}

	public List<String> filterToppingsByList(List<String> args) throws IOException {
		List<String> toppings = loadToppings();
		List<String> ret = new ArrayList<>();
		for (String s : args) {
			if (toppings.contains(s)) {
				ret.add(s);
			}
		}
		return ret;
	}

	private List<String> loadToppings() throws IOException {

		List<String> ret = new ArrayList<>();
		FileReader fr = new FileReader("toppings.txt");
		BufferedReader br = new BufferedReader(fr);

		String aLine;
		while ( (aLine = br.readLine()) != null ) {
			ret.add(aLine);
		}

		return ret;
	}

}