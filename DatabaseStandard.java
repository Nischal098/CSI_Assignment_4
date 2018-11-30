import java.util.HashMap;

@SuppressWarnings("serial")
public class DatabaseStandard extends HashMap<String,String> implements DatabaseInterface {
	//A Hash Map
	//The items stored in the hash map are pairs of password and its encrypted version using SHA1.
	//The key is obviously the encrypted password
	private int size;	
	private static final int INITITALSIZE = 1210000;
	
	public DatabaseStandard() {
		super();
		size = 0;
	}

	@Override
	public String save(String plainPassword, String encryptedPassword) {
		// Stores plainPassword and corresponding encryptedPassword in a map.
		// if there was a value associated with this key, it is replaced, 
		// and previous value returned; otherwise, null is returned
		// The key is the encryptedPassword the value is the plainPassword
		
		String replacedString = null;
		
		if (size() == 0) {
			put(encryptedPassword, plainPassword);
		} else {
			if (get(encryptedPassword)!=null) {
				replacedString = get(encryptedPassword);
				put(encryptedPassword, plainPassword);
			}
		}
		size++;
		return replacedString;
	}

	@Override
	public String decrypt(String encryptedPassword) {
		// returns plain password corresponding to encrypted password
		return get(encryptedPassword);
	}

	@Override
	public int size() {
		// returns the number of password pairs stored in the database
		return size;
	}

	public void printStatistics() {
		System.out.println("*** DatabaseStandard Statistics ***");
		System.out.println("Size is " + size() + " passwords");
		System.out.println("Initial Number of Indexes when created " + INITITALSIZE);
		System.out.println("*** End DatabaseStandard Statistics ***");
	}

}
