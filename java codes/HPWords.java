package linguistics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream.Filter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class HPWords {
	public static void main(String args[]) throws IOException {
		String root = "D:\\[자료]\\[KAIST work]\\2017_1_Spring\\Big Data and Language\\r_hp";
		double[][] header = new double[4][7];
		String[] adj = { "JJ", "JJR", "JJS"};
		String[] noun={"NN", "NNS"};
		String[] pprnoun={"NP", "NPS"};
		String[] adv={"RB", "RBR", "RBS" };
		ArrayList<String> filter=new ArrayList<>();
//		filter.addAll(Arrays.asList(adj));
		filter.addAll(Arrays.asList(noun));
//		filter.addAll(Arrays.asList(pprnoun));
//		filter.addAll(Arrays.asList(adv));
		int THRESHOLD = 19;
		ArrayList<Data> result = new ArrayList<Data>();
		for (int i = 1; i < 8; i++) {
			String address = root + i + ".txt";
			try {
				BufferedReader reader = new BufferedReader(new FileReader(address));
				String line;
				int idx = 0;
				int wordCount=0;
				double lengthSum=0;
				while ((line = reader.readLine()) != null) {
					if (line.substring(0, 1).equals("#")) {
						int x = line.indexOf(":");	
						int k = Integer.parseInt(line.substring(x + 1).trim());
						header[idx][i - 1] = k;
					} else {
						int a = line.indexOf("\t");
						int b = line.indexOf("\t", line.indexOf("\t") + 1);
						double c = Double.parseDouble(line.substring(a, b).trim());
						String name = line.substring(b + 1).trim();
						int s = name.indexOf("_");
						String pos = name.substring(s + 1);
						if (filter.contains(pos.toUpperCase()) && s > 0) {
							wordCount++;
							lengthSum+=s;
							Data n = new Data(name);
							int d = result.indexOf(n);
							if (d != -1) {
								Data at = result.get(d);
								at.addCount(i - 1, c / header[1][i - 1] * 100);
								at.addFreq(i - 1, (int) c);
							} else {
								n.addCount(i - 1, c / header[1][i - 1] * 100);
								n.addFreq(i - 1, (int) c);
								result.add(n);
							}
						}
					}
					idx++;
				}
				header[3][i-1]=lengthSum/wordCount;
				reader.close();
			} catch (IOException e) {
			}
		}
		for(Data data:result){
			data.assignType();
			data.findPeak();
		}
		int cnt = 0;
		BufferedWriter bw = null;
		FileWriter fw = null;
		int total=0;
		try {
			String base="D:\\[자료]\\[KAIST work]\\2017_1_Spring\\Big Data and Language\\";
			fw = new FileWriter(base+"result_"+filter.get(0)+".txt");
			bw = new BufferedWriter(fw);
			for (int i = 0; i < header.length; i++) {
				for (int j = 0; j < header[0].length; j++) {
					System.out.print(header[i][j] + " ");
					bw.write(header[i][j] + " ");
					if(i==1){
						total+=header[i][j];
					}
				}
				System.out.println("");
				bw.newLine();
			}
			bw.write(total+"");
			bw.newLine();
			bw.write("Index\tWord\t\tTotal\tCount\t\t\t\tP\tercentage\t\t\t\t\tNormalized");
			bw.newLine();
			System.out.println("Index\tWord\t\tTotal\tCount\t\t\t\t\tPercentage\t\t\t\t\tNormalized");
			Collections.sort(result, Data.CountComparator);
			for (int k = 0; k < result.size(); k++) {
				int r = result.get(k).totalCount();
				int c=result.get(k).nonzeros();
				if (r > THRESHOLD && c==7) {
					cnt++;
					result.get(k).populateNorm();
					System.out.println(cnt + " " + result.get(k));
					bw.write(cnt + " " + result.get(k));
					bw.newLine();
				}
			}
			bw.close();
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
