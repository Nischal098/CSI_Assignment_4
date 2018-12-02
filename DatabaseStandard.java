import java.util.HashMap;

public class DatabaseStandard implements DatabaseInterface {
	//A Hash Map
	//The items stored in the hash map are pairs of password and its encrypted version using SHA1.
	//The key is obviously the encrypted password
	private static final int INITITALSIZE = 179917;
	private HashMap<String,String> hashMap;
	
	public DatabaseStandard() {
		hashMap = new HashMap<>(INITITALSIZE);
	}

	@Override
	public String save(String plainPassword, String encryptedPassword) {
		// Stores plainPassword and corresponding encryptedPassword in a map.
		// if there was a value associated with this key, it is replaced, 
		// and previous value returned; otherwise, null is returned
		// The key is the encryptedPassword the value is the plainPassword
		
		String replacedString = null;	
		replacedString = hashMap.get(encryptedPassword);
		hashMap.put(encryptedPassword, plainPassword);
		return replacedString;
	}

	@Override
	public String decrypt(String encryptedPassword) {
		// returns plain password corresponding to encrypted password
		return hashMap.get(encryptedPassword);
	}

	@Override
	public int size() {
		// returns the number of password pairs stored in the database
		return hashMap.size();
	}

	public void printStatistics() {
		System.out.println("*** DatabaseStandard Statistics ***");
		System.out.println("Size is " + size() + " passwords");
		System.out.println("Initial Number of Indexes when created " + INITITALSIZE);
		System.out.println("*** End DatabaseStandard Statistics ***");
	}

}
