import java.util.ArrayList;


public class Four_Way_Heap {
	private ArrayList<Frequency_Table> v_list;
    public Four_Way_Heap(ArrayList<Frequency_Table> items) {
        this.v_list = items;
        build_heap();
    }
    public void insert(Frequency_Table item) {
        v_list.add(item);
        int i=v_list.size()-1;
        int p=p(i);
        while(p!=i && v_list.get(i).get_frequency()<v_list.get(p).get_frequency() && p>=3) {
            swap(i,p);
            i=p;
            p=p(i);            
        }        
    }
    public void build_heap() {
    	int start=((v_list.size()-4)/4)+3;
        for(int i=start;i>=3;i--) {
            min_heapify(i);
        }        
    }
    public Frequency_Table extractMin() {
        if(v_list.size()==3) {
            throw new IllegalStateException("Min_Heap empty");
        } 
        else if(v_list.size()==4) {
        	Frequency_Table minimum = v_list.remove(3);
            return minimum;
        }
        Frequency_Table minimum = v_list.get(3);
        Frequency_Table lastItem = v_list.remove(v_list.size() - 1);
        v_list.set(3, lastItem);
        min_heapify(3);
        return minimum;
    }
    private void min_heapify(int i) {
    	if(i<3){
    		return;
    	}    	
        int l = l(i);
        int m1 = m1(i);
        int m2 = m2(i);
        int r = r(i);
        int small=-1;
        if(l<=v_list.size()-1 && v_list.get(l).get_frequency()<v_list.get(i).get_frequency()) {
            small=l;
        } else {
            small = i;
        }
        if(m1<=v_list.size()-1 && v_list.get(m1).get_frequency()<v_list.get(small).get_frequency()) {
            small = m1;
        }
        if(m2<=v_list.size()-1 && v_list.get(m2).get_frequency()<v_list.get(small).get_frequency()) {
            small = m2;
        }
        if(r<=v_list.size()-1 && v_list.get(r).get_frequency()<v_list.get(small).get_frequency()) {
            small = r;
        }
        if(small!=i) {
            swap(i, small);
            min_heapify(small);
        }
    }
    public Frequency_Table getMin() {
        return v_list.get(3);
    }
    public boolean is_empty() {
        return v_list.size() == 3;
    }
    private int p(int i) {	
        if ((i-3)%4 == 1) {
            return ((i-3)/4)+3;
        }
        return ((i-4)/4)+3;
    }
    private int l(int i) {
        return i*4-8;
    }    
    private int m1(int i) {
        return i*4-7;
    }    
    private int m2(int i) {
        return i*4-6;
    }    
    private int r(int i) {
        return i*4-5;
    }
    private void swap(int i,int p) {
        Frequency_Table ft = v_list.get(p);
        v_list.set(p, v_list.get(i));
        v_list.set(i, ft);
    }
}