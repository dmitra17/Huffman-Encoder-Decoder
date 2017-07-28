import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class encoder {
	public static void main(String args[]){
		try{
		final String BINARY_FILE = "encoded.bin";
		final String CODE_TABLE_FILE = "code_table.txt";
		final String INPUT_FILE = args[0];		
		HashMap<Integer,Frequency_Table> freq_table_vector = new HashMap<Integer,Frequency_Table>();
		ArrayList<Integer> input = new ArrayList<Integer>();
		HashMap<Integer,String> code_table_vector = new HashMap<Integer,String>();
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			File file = new File(INPUT_FILE);			
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);			
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				//System.out.println("line= "+line);
				if(line.trim().equals("")){
					continue;
				}				
				int i = Integer.parseInt(line);
				if(i<0 || i>999999){
					throw new NumberFormatException("Input String: "+line);
				}
				input.add(i);
				if(!freq_table_vector.containsKey(i)){		
					freq_table_vector.put(i,new Frequency_Table(i,1));
				}
				else{					
					Frequency_Table freq_tb = freq_table_vector.get(i);					
					int new_freq = freq_tb.get_frequency()+1;
					freq_tb.set_frequency(new_freq);
				}				
			}
		}catch(NumberFormatException e){			
			throw new NumberFormatException("Input must be in range - 0 to 999,999\n"+e.getMessage());
		} 
		catch (IOException e) {
			e.printStackTrace();
		}		
		finally{
			try {
				if(fileReader!=null)				
					fileReader.close();
				if(bufferedReader!=null)
					bufferedReader.close();
			} catch (IOException e) {				
				e.printStackTrace();
			}
		}		
		Frequency_Table huffmanTree = null;
		
		ArrayList<Frequency_Table> freq_table_vector1 = new ArrayList<Frequency_Table>(freq_table_vector.values());
		
		//for(Frequency_Table ft : freq_table_vector1){
			//System.out.println(ft.get_entry()+" : "+ft.get_frequency());
		//}
		//System.out.println("...................");
		freq_table_vector1.add(0,new Frequency_Table(-1,0));
		freq_table_vector1.add(0,new Frequency_Table(-1,0));
		freq_table_vector1.add(0,new Frequency_Table(-1,0));
		
		
		//Building tree starts
		Four_Way_Heap binaryheap = new Four_Way_Heap(freq_table_vector1);
		int initialSize = freq_table_vector1.size()-3;
		int i=0;
		for( i=0; i<(initialSize-1); i++){			
			Frequency_Table min1 = binaryheap.extractMin();
			Frequency_Table min2 = binaryheap.extractMin();
			//System.out.println(min1.get_entry()+":"+min1.get_frequency()+"..."+min2.get_entry()+":"+min2.get_frequency());
			Frequency_Table newelem = new Frequency_Table(-1,min1.get_frequency()+min2.get_frequency());			
			newelem.set_left_child(min1);
			newelem.set_right_child(min2);			
			binaryheap.insert(newelem);
		}		
		huffmanTree =  binaryheap.extractMin();
		//Building tree ends
		
		
				
		if(huffmanTree.get_left_child()==null && huffmanTree.get_right_child()==null){
			code_table_vector.put(huffmanTree.get_entry(), "0");			
		}
		else{
			build_code_table(huffmanTree,code_table_vector, new StringBuilder(""));
		}
		StringBuilder code_table = new StringBuilder();
		Iterator itr = code_table_vector.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry m = (Map.Entry)itr.next();
			code_table = code_table.append(m.getKey()).append(" ").append(m.getValue());
			if(itr.hasNext()){
				code_table = code_table.append("\n");
			}
		}		
		BufferedWriter b_write = null;
		FileWriter f_write = null;
		try {
			f_write = new FileWriter(CODE_TABLE_FILE);
			b_write = new BufferedWriter(f_write);
			b_write.write(code_table.toString().trim());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (b_write != null)
				b_write.close();
				if (f_write != null)
				f_write.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}		
		StringBuilder sb = new StringBuilder();			
		BufferedOutputStream bos = null;	
		try {						
			bos = new BufferedOutputStream(new FileOutputStream(BINARY_FILE));			
			for(int a=0; a<input.size(); a++){
				int j = input.get(a);			
				sb.append(code_table_vector.get(j));
				int currCodeLen = sb.length();					
				if(sb.length()>=8){
					int bp = 0;
					int bytecount=0;
					while(bp < currCodeLen-7){
						bytecount++;
						byte bw1 = 0x00;
						for(int k=0; k<8 && bp+k<sb.length(); k++){
							bw1 = (byte) (bw1 << 1);
							bw1 += sb.charAt(bp+k)=='0'?0x0:0x1;
						}
						bos.write(bw1);
						bp+=8;											
					}
					sb.delete(0,8*bytecount);
				}				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		finally{
			try {
				if(bos!=null){				
					bos.close();
				} 
			}catch (IOException e) {
				e.printStackTrace();
			}			
		}								
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}	
	private static void build_code_table(Frequency_Table huffmanTree, HashMap<Integer,String> code_table_vector, StringBuilder code) {		
		if(huffmanTree==null){
			return;
		}		
		if(huffmanTree.get_left_child()==null && huffmanTree.get_right_child()==null){
			code_table_vector.put(huffmanTree.get_entry(),code.toString());			
		}		
		if(huffmanTree!=null){
			build_code_table(huffmanTree.get_left_child(),code_table_vector,code.append("0"));
			code.deleteCharAt(code.length()-1);
			build_code_table(huffmanTree.get_right_child(),code_table_vector,code.append("1"));
			code.deleteCharAt(code.length()-1);
		}		
	}
	
}
