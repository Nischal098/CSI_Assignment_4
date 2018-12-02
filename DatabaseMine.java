
import java.util.*;

public class DatabaseMine implements DatabaseInterface {
	//a table of entries (key,value), both Strings, where the key is the
	//encrypted password and the value is the original password
		
	private int N; // this is a prime number that gives the number of addresses
	private int sizePasswords, probes;
	private ArrayList<MyHashEntry<String,String>> table;
	private final static int INITIALSIZE = 179917;
	
	// These constructors must create your hash tables with enough positions N
	// to hold the entries you will insert; you may experiment with primes N
	public DatabaseMine() {
		// here you pick suitable default N 
		N = INITIALSIZE;
		sizePasswords = 0;
		table = new ArrayList<>(INITIALSIZE);
		setupTable();
	}
	
	public DatabaseMine(int N) {
		// here the N is given by the user.
		this.N = N;
		sizePasswords = 0;
		table = new ArrayList<>(N);
		setupTable();
	}
	
	@Override
	public String save(String plainPassword, String encryptedPassword) {
		probes++;
		int hashValue = hashFunction(encryptedPassword);
		int existedHashVal = contains(encryptedPassword);
		if (existedHashVal != -1) {
			MyHashEntry<String, String> entryVal = table.get(existedHashVal);
			String returnStr = entryVal.getValue();
			entryVal.setValue(plainPassword);
			table.set(existedHashVal, entryVal);
			return returnStr;
		} else {
			for(int i = 0; i < N; i++) {
				int hashProbed = (hashValue + i)%N;
				MyHashEntry<String, String> entryVal = table.get(hashProbed);
				if (entryVal == null) {
					//Empty Spot Found
					table.set(hashProbed, new MyHashEntry<String, String>(encryptedPassword, plainPassword));
					sizePasswords++;
					return null;
				}
				if (entryVal.getKey() == null) {
					//Special Case where Key is null
					table.set(hashProbed, new MyHashEntry<String, String>(encryptedPassword, plainPassword));
					sizePasswords++;
					return null;
				}
			}
		}
		return null;
	}

	@Override
	public String decrypt(String encryptedPassword) {
		int hashValue = hashFunction(encryptedPassword);
		for(int i = 0; i < N; i++) {
			int hashProbed = (hashValue + i)%N;
			MyHashEntry<String, String> entryVal = table.get(hashProbed);
			if (entryVal == null) return null;						
			if (entryVal.getKey() == null) continue;				
			if (entryVal.getKey().equals(encryptedPassword)) {
				return entryVal.getValue();
			}
		}		
		return null;
	}

	@Override
	public int size() {
		return sizePasswords;
	}
	
	public int contains(String key) {
		int hashValue = hashFunction(key);
		for(int i = 0; i < N; i++) {
			int hashProbed = (hashValue + i)%N;
			MyHashEntry<String, String> entryVal = table.get(hashProbed);
			if (entryVal == null) return -1;						
			if (entryVal.getKey() == null) continue;				
			if (entryVal.getKey().equals(key)) {
				return hashProbed;
			}
		}		
		return -1;
	}
	
	private void setupTable() {
		for (int i = 0; i <= N; i++)
			table.add(null);
	}
	
	public int getN() {
		return N;
	}
	
	public int hashFunction(String key) {
		int address = key.hashCode()%N;
		return (address>=0)?address:(address+N);
	}
	
	public float getLoadingFactor() {
		return (float)sizePasswords/(float)N;
	}
	
	public float getProbes() {
		return probes/sizePasswords;
	}
	
	public int getDisplacements() {
		return probes;
	}
	
	public void printStatistics() {
		// important statistics must be collected (here or during construction) and
		//printed here: size, number of indexes, load factor, average number of probes
		//and average number of displacements 
		
		System.out.println("*** DatabaseStandard Statistics ***");
		System.out.println("Size is " + size() + " passwords");
		System.out.println("Number of Indexes is " + getN());
		System.out.println("Load Factor is " + getLoadingFactor());
		System.out.println("Average Number of Probes is " + getProbes());
		System.out.println("Number of displacements (due to collisions) " + getDisplacements());
		System.out.println("*** End DatabaseStandard Statistics ***");
	}
	
	public class MyHashEntry<K, V> implements Map.Entry<K, V> {
	    private final K key;
	    private V value;

	    public MyHashEntry(K key, V value) {
	        this.key = key;
	        this.value = value;
	    }

	    @Override
	    public K getKey() {
	        return key;
	    }

	    @Override
	    public V getValue() {
	        return value;
	    }

	    @Override
	    public V setValue(V value) {
	        V old = this.value;
	        this.value = value;
	        return old;
	    }
	}

}
