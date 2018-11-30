import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PasswordCracker {
	//This class receives the original passwords in the array list, and is responsible for creating more
	//passwords with augmented rules and insert everything on the given database, which is
	//originally empty
	String currentPassword;
	char[] charCurrentPassword;
	String tempStr;
	
	public PasswordCracker() {
		currentPassword = null;
		charCurrentPassword = null;
		tempStr = null;
	}
	
	public void createDatabase(ArrayList<String> commonPasswords, DatabaseInterface database) {
		// receives list of passwords and populates database with entries consisting
		// of (key,value) pairs where the value is the password and the key is the encrypted password (encrypted using Sha1)

		//1. Capitalize the first letter of each word starting with a letter, e.g. dragon becomes Dragon
		//2. Add the current year to the word, e.g. dragon becomes dragon2018
		//3. Use @ instead of a, e.g. dragon becomes dr@gon
		//4. Use 3 instead of e, e.g. baseball becomes bas3ball
		//5. Use 1 instead of i, e.g. michael becomes m1chael
				
		for(int i = 0; i < commonPasswords.size(); i++) {
			currentPassword = commonPasswords.get(i);
			charCurrentPassword = currentPassword.toCharArray(); //Get a character Array
			try {
				database.save(currentPassword, Sha1.hash(currentPassword));	//Store Initial Password
				
				tempStr = encrpytCapitalize(charCurrentPassword);
				database.save(tempStr,Sha1.hash(tempStr));	//Store Capitalized Passwords
				
				tempStr = encrpytYear(currentPassword);
				database.save(tempStr,Sha1.hash(tempStr));	//Store Added Year Passwords
				
				tempStr = encrpytA(charCurrentPassword);
				database.save(tempStr,Sha1.hash(tempStr));	//Store a to @ Passwords
				
				tempStr = encrpyt3(charCurrentPassword);
				database.save(tempStr,Sha1.hash(tempStr));	//Store e to 3 Passwords
	
				tempStr = encrpyt1(charCurrentPassword);
				database.save(tempStr,Sha1.hash(tempStr));	//Store i to 1 Passwords
				
				//Start Adding Combinations	
				for(int j = 0; i <= 5; j++) {
					
				}				
				} catch (UnsupportedEncodingException e) { e.printStackTrace(); }			
		}		
	}
	
	public String crackPassword(String encryptedPassword, DatabaseInterface database) {
		//uses database to crack encrypted password, returning the original password	
		return database.decrypt(encryptedPassword);
	}
	
	private String encrpytCapitalize(char[] password) {
		//O(1) operation
		password[0] = Character.toUpperCase(password[0]);
		return String.valueOf(password);
	}
	
	private String encrpytYear(String password) {
		//O(1) operation
		LocalDateTime currentTime = LocalDateTime.now();
		int year = currentTime.getYear();
		return (password + Integer.toString(year));
	}
	
	private String encrpytA(char[] password) {
		//O(n) operation
		for(int i = 0; i < password.length; i++){
			if(password[i]=='a') {
				password[i] = '@';
			}
		}
		return String.valueOf(password);
	}
	
	private String encrpyt3(char[] password) {
		//O(n) operation
		for(int i = 0; i < password.length; i++){
			if(password[i]=='e') {
				password[i] = '3';
			}
		}
		return String.valueOf(password);
	}
	
	private String encrpyt1(char[] password) {
		//O(n) operation
		for(int i = 0; i < password.length; i++){
			if(password[i]=='i') {
				password[i] = '1';
			}
		}
		return String.valueOf(password);
	}
	
}
