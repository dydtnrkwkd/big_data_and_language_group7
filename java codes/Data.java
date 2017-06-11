package linguistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Data {
	private String name;
	public int[] freq;
	public double[] count;
	public double[] norm;
	public int type;
	public int peak;
	public int length;
	public Data(String n){
		super();
		name=n;
		count=new double[7];
		norm=new double[7];
		freq=new int[7];
		type=0;
		peak=-1;
		length=0;
	}
	private String getName(){
		return name;
	}
	public double[] getCount(){
		return count;
	}
	public void addCount(int i, double v){
		count[i]=v;
	}
	public double[] getNorm(){
		return norm;
	}
	public void addFreq(int i, int v){
		freq[i]=v;
	}
	public int[] getFreq(){
		return freq;
	}
	public void populateNorm(){
		double mx=0;
		for(int i=0; i<7; i++){
			if(count[i]>mx){
				mx=count[i];
			}
		}
		for(int j=0; j<7; j++){
			norm[j]=count[j]/mx;
		}
	}
	public int totalCount(){
		int c=0;
		for(int j=0; j<7; j++){
			c+=freq[j];
		}
		return c;
	}
	public void assignType(){
		double a=(count[0]+count[1])/2;
		double b=(count[2]+count[3]+count[4])/3;
		double c=(count[5]+count[6])/2;
		double mx=Math.max(a, Math.max(b, c));
		double mn=Math.min(a, Math.min(b, c));
		if (mx==a){
			if(mn==b){
				type=312;
			}else{
				type=321;
			}
		}else if(mx==b){
			if(mn==a){
				type=132;
			}else{
				type=231;
			}
		}else{
			if(mn==a){
				type=123;
			}else{
				type=213;
			}
		}
	}
	public int nonzeros(){
		int c=0;
		for(double i:count){
			if(i!=0){
				c++;
			}
		}
		return c;
	}
	public void findPeak(){
		ArrayList<Integer> cand=new ArrayList<>();
		double v=0;
		for(int i=0; i<7;i++){
			v+=count[i];
		}
		v=v/7;
		for(int j=0; j<7; j++){
			if(count[j]>1.75*v){
				cand.add(j);
			}
		}
		if (cand.size()>0){
			peak= Collections.max(cand)+1;
		}else{
			peak= -1;
		}
		
	}

	public static Comparator<Data> CountComparator=new Comparator<Data>(){
	    @Override
	    public int compare(Data o1, Data o2) {
	        return o2.totalCount()-o1.totalCount();
	    }
	};
	public static Comparator<Data> PeakComparator=new Comparator<Data>(){
	    @Override
	    public int compare(Data o1, Data o2) {
	        return o2.peak-o1.peak;
	    }
	};
	
	@Override
	public boolean equals(Object object){
	    if (object != null && object instanceof Data){
	        if(((Data)object).name.equals(name)){
	        	return true;
	        }
	    }
	    return false;
	}
	
	@Override
	public String toString(){
		name+="                     ";
		String result=name.substring(0, 20)+" "+totalCount()+"\t[";
		for(int i=0; i<7; i++){
			result=result+freq[i]+" ";
		}
		result+="]";
		result=result+"\t[";
		for(int i=0; i<7; i++){
			result=result+String.format("%.3f", count[i])+" ";
		}
		result+="]";
		result=result+"\t[";
		for(int i=0; i<7; i++){
			result=result+String.format("%.3f", norm[i])+" ";
		}
		result+="]\t"+type+"\t"+peak;
		return result;
	}

}
