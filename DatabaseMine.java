
public class DatabaseMine implements DatabaseInterface {
	//a table of entries (key,value), both Strings, where the key is the
	//encrypted password and the value is the original password
	
	private int N; // this is a prime number that gives the number of addresses
	
	// these constructors must create your hash tables with enough positions N
	// to hold the entries you will insert; you may experiment with primes N
	public DatabaseMine() {
		// here you pick suitable default N 
		
	}
	
	public DatabaseMine(int N) {
		// here the N is given by the user.
		
	}
	
	@Override
	public String save(String plainPassword, String encryptedPassword) {
		return null;
	}

	@Override
	public String decrypt(String encryptedPassword) {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}
	
	public int hashCode() {
		// function that determines the home address of a key
		//already provided in the class String, apply modulo N, where N is a prime number. Note that N
		//is the table size, so it must be large enough for the amount of keys you wish to insert, also
		//taking into account the loading factor
		return 0;		
	}
	
	public int getN() {
		return N;
	}
	public void setN(int n) {
		N = n;
	}
	
	public int hashFunction(String key) {
		int address = key.hashCode()%N;
		return (address>=0)?address:(address+N);
	}
	
	public void printStatistics() {
		// important statistics must be collected (here or during construction) and
		//printed here: size, number of indexes, load factor, average number of probes
		//and average number of displacements 
		
		System.out.println("*** DatabaseStandard Statistics ***");
		System.out.println("Size is " + "" + " passwords");
		System.out.println("Number of Indexes is " + "");
		System.out.println("Load Factor is " + "");
		System.out.println("Average Number of Probes is " + "");
		System.out.println("Number of displacements (due to collisions) " + "");
		System.out.println("*** End DatabaseStandard Statistics ***");
	}

}
