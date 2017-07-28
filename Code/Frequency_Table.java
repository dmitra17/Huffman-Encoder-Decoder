public class Frequency_Table {
	private int entry;
	private int frequency;
	private Frequency_Table left_child;
	private Frequency_Table right_child;
	
	public Frequency_Table(int entry, int frequency){
		this.entry=entry;
		this.frequency=frequency;
		this.left_child=null;
		this.right_child=null;
	}	
	public Frequency_Table get_left_child() {
		return left_child;
	}
	public void set_left_child(Frequency_Table left_child) {
		this.left_child = left_child;
	}
	public Frequency_Table get_right_child() {
		return right_child;
	}
	public void set_right_child(Frequency_Table right_child) {
		this.right_child = right_child;
	}
	public int get_entry() {
		return entry;
	}
	public void set_entry(int entry) {
		this.entry = entry;
	}
	public int get_frequency() {
		return frequency;
	}
	public void set_frequency(int frequency) {
		this.frequency = frequency;
	}	
	@Override
	public String toString(){
		return (entry+": "+frequency+" Left= "+left_child+" Right= "+right_child);
	}
}
