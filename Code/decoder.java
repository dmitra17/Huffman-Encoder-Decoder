import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class decoder {
	public static void main(String args[]){		
		final String BINARY_FILE = args[0];
		final String DECODED_FILE = "decoded.txt";
		final String CODE_TABLE_FILE = args[1];		
		Frequency_Table root_node = new Frequency_Table(-1,0);
		Frequency_Table current_node = null;		
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			File file = new File(CODE_TABLE_FILE);			
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);			
			String line;			
			while ((line = bufferedReader.readLine())!= null) {				
				current_node = root_node;				
				if(line.trim().equals("")){
					continue;
				}				
				String[] linesplit = line.split(" ");				
				String code = linesplit[1];
				String symbol = linesplit[0];				
				for(int i=0; i<code.length(); i++){
					char c = code.charAt(i);
					if(c=='0'){
						if(current_node.get_left_child()==null){
							Frequency_Table newnode;
							if(i==code.length()-1){
								newnode = new Frequency_Table(Integer.parseInt(symbol),0);
							}
							else{
								newnode = new Frequency_Table(-1,0);
							}							
							current_node.set_left_child(newnode);
							current_node = newnode;
						}
						else{
							current_node = current_node.get_left_child();
						}
					}
					else{
						if(current_node.get_right_child()==null){
							Frequency_Table newnode;
							if(i==code.length()-1){
								newnode = new Frequency_Table(Integer.parseInt(symbol),0);
							}
							else{
								newnode = new Frequency_Table(-1,0);
							}
							
							current_node.set_right_child(newnode);
							current_node = newnode;
						}
						else{
							current_node = current_node.get_right_child();
						}
					}
				}												
			}			
		} catch (IOException e) {
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
		FileInputStream fis =null;
		byte[] bytesArray =null;		
		try {
			File file = new File(BINARY_FILE);			
			bytesArray = new byte[(int) file.length()];
			fis = new FileInputStream(file);			
			fis.read(bytesArray);
			fis.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}		
		StringBuilder compressedcode = new StringBuilder("");		
		FileWriter fw = null;
		BufferedWriter bw = null;		
		try {
			fw = new FileWriter(DECODED_FILE);	
			bw = new BufferedWriter(fw);			
			for (byte b : bytesArray) {
				compressedcode.append(Integer.toBinaryString(b & 255 | 256).substring(1));								
				current_node = root_node;
				int codelen = compressedcode.length();
				int deleteupto=-1;
				for(int i=0; i<codelen;i++){
					if(compressedcode.charAt(i)=='0'){
						current_node=current_node.get_left_child();
						if(current_node.get_left_child()==null && current_node.get_right_child()==null){
							bw.write(current_node.get_entry()+"\n");
							deleteupto=i;
							current_node=root_node;
						}
					}
					else{
						current_node=current_node.get_right_child();
						if(current_node.get_left_child()==null && current_node.get_right_child()==null){
							bw.write(current_node.get_entry()+"\n");
							deleteupto=i;
							current_node=root_node;
						}
					}					
				}
				compressedcode.delete(0, deleteupto+1);				
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(bw!=null)
					bw.flush();
				if(fw!=null)
					fw.flush();
				if(bw!=null)
					bw.close();
				if(fw!=null)
					fw.close();
			} catch (IOException e) {				
				e.printStackTrace();
			}
		}		
	}
}
