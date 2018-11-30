import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PasswordCracker {
	//This class receives the original passwords in the array list, and is responsible for creating more
	//passwords with augmented rules and insert everything on the given database, which is
	//originally empty
	private String currentPassword;
	private char[] charCurrentPassword;
	private String tempStrCapitalize, tempStrYear, tempStrA, tempStr3, tempStr1;
	private char[] tempcharCurrentPasswordA, tempcharCurrentPassword3, tempcharCurrentPassword1;
	private List<char[]> tempCurrentPasswordA, tempCurrentPassword3, tempCurrentPassword1;
	private ArrayList<String> currentPassAStr, currentPass3Str, currentPass1Str;
	private LocalDateTime currentTime;
	private int year;
	private boolean containsA, containsE, containsI;
	
	public PasswordCracker() {
		currentPassword = null;
		charCurrentPassword = null;
	
		tempCurrentPasswordA = new ArrayList<char[]>();
		tempCurrentPassword3 = new ArrayList<char[]>();
		tempCurrentPassword1 = new ArrayList<char[]>();
		
		currentPassAStr = new ArrayList<String>();
		currentPass3Str = new ArrayList<String>();
		currentPass1Str = new ArrayList<String>();
		
		restartPassword();
		
		currentTime = LocalDateTime.now();
		year = currentTime.getYear();
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
			restartPassword();	
			clearArrayLists();
			currentPassword = commonPasswords.get(i);
			charCurrentPassword = currentPassword.toCharArray(); 			//Get a character Array
			tempcharCurrentPasswordA = currentPassword.toCharArray();
			tempcharCurrentPassword3 = currentPassword.toCharArray();
			tempcharCurrentPassword1 = currentPassword.toCharArray();
			try {
				database.save(currentPassword, Sha1.hash(currentPassword));	//Store Initial Password
				try {
					Integer.parseInt(currentPassword); 						//Check if password is all Integer
					tempStrYear = encrpytYear(currentPassword);
					database.save(tempStrYear,Sha1.hash(tempStrYear));			//Store Added Year Passwords				
				} catch (NumberFormatException e) {
					for(int  j = 0; j < charCurrentPassword.length; j++){ 	
						if(charCurrentPassword[j]=='a') {
							tempcharCurrentPasswordA[j] = '@';
							tempCurrentPasswordA.add(tempcharCurrentPasswordA.clone());
							containsA = true;
						}
						if(charCurrentPassword[j]=='e') {
							tempcharCurrentPassword3[j] = '3';
							tempCurrentPassword3.add(tempcharCurrentPassword3.clone());
							containsE = true;
						}
						if(charCurrentPassword[j]=='i') {
							tempcharCurrentPassword1[j] = '1';
							tempCurrentPassword1.add(tempcharCurrentPassword1.clone());
							containsI = true;
						}
					}	
					
					if (containsA) {
						for(char[] charSequence : tempCurrentPasswordA) {
							currentPassAStr.add(String.valueOf(charSequence));
							for(int x = 0; x < charSequence.length; x++) {
								if (charSequence[x] == '@') {
									charSequence[x] = 'a';
									if (!String.valueOf(charSequence).equals(currentPassword))
										currentPassAStr.add(String.valueOf(charSequence));							
									charSequence[x] = '@';
								}
							}
						}	
					}
					if (containsE) {
						for(char[] charSequence : tempCurrentPassword3) {
							currentPass3Str.add(String.valueOf(charSequence));
							for(int x = 0; x < charSequence.length; x++) {
								if (charSequence[x] == '3') {
									charSequence[x] = 'e';
									if (!String.valueOf(charSequence).equals(currentPassword))
										currentPassAStr.add(String.valueOf(charSequence));
									charSequence[x] = '3';
								}
							}
						}	
					}
					if (containsI) {
						for(char[] charSequence : tempCurrentPassword1) {
							currentPass1Str.add(String.valueOf(charSequence));
							for(int x = 0; x < charSequence.length; x++) {
								if (charSequence[x] == '1') {
									charSequence[x] = 'i';
									if (!String.valueOf(charSequence).equals(currentPassword))
										currentPassAStr.add(String.valueOf(charSequence));
									charSequence[x] = '1';
								}
							}
						}
					}
					
					if (containsA || containsE || containsI) 
						checkAllAor3or1(currentPassAStr, currentPass3Str, currentPass1Str,
								Character.isDigit(charCurrentPassword[0]), database); 
					
					
					if (Character.isDigit(charCurrentPassword[0])) { 		//If first letter is a digit, only encypt Year
						tempStrYear = encrpytYear(currentPassword);
						database.save(tempStrYear,Sha1.hash(tempStrYear));	//Store Added Year Passwords										
					} else {												//If not, Store Capitalized and Year
						tempStrYear = encrpytYear(currentPassword);
						database.save(tempStrYear,Sha1.hash(tempStrYear));	//Store Added Year Passwords
						
						tempStrCapitalize = encrpytCapitalize(charCurrentPassword);
						database.save(tempStrCapitalize,Sha1.hash(tempStrCapitalize));	//Store Capitalized Passwords
						
						tempStrYear = encrpytYear(String.valueOf(charCurrentPassword));	//Store Capitalized and Year
						database.save(tempStrYear,Sha1.hash(tempStrYear));
					}
				}	
			} catch (UnsupportedEncodingException e) { e.printStackTrace(); }			
		}		
	}
	
	private void restartPassword() {
		tempStrCapitalize = null;
		tempStrYear = null;
		tempStrA = null;
		tempStr3 = null;
		tempStr1 = null;
		containsA = false;
		containsE = false;
		containsI = false;
		tempcharCurrentPasswordA = null;
		tempcharCurrentPassword3 = null;
		tempcharCurrentPassword1 = null;
	}
	
	private void clearArrayLists() {
		tempCurrentPasswordA.clear();
		tempCurrentPassword3.clear();
		tempCurrentPassword1.clear();
		currentPassAStr.clear();
		currentPass3Str.clear();
		currentPass1Str.clear();
	}

	public String crackPassword(String encryptedPassword, DatabaseInterface database) {
		//uses database to crack encrypted password, returning the original password	
		return database.decrypt(encryptedPassword);
	}
	
	private void checkAllAor3or1(ArrayList<String> a, ArrayList<String> e, ArrayList<String> i,
			boolean isLetter1Digit, DatabaseInterface database) {
		//Get and save every combination of 		
		char[] tmp;
		for(String passA : a) {
			saveA31(passA, isLetter1Digit, database);
			//if(passA.startsWith("b@")) System.out.println(passA);
			
			tmp = passA.toCharArray();
			saveA31(replaceLetters(tmp, 'e', '3'), isLetter1Digit, database);			
			saveA31(replaceLetters(tmp, 'i', '1'), isLetter1Digit, database);
			
			tmp = passA.toCharArray();
			saveA31(replaceLetters(tmp, 'i', '1'), isLetter1Digit, database);
		}
		for(String passE : e) {
			saveA31(passE, isLetter1Digit, database);
			
			tmp = passE.toCharArray();
			saveA31(replaceLetters(tmp, 'i', '1'), isLetter1Digit, database);
			saveA31(replaceLetters(tmp, 'a', '1'), isLetter1Digit, database);
			
			tmp = passE.toCharArray();
			saveA31(replaceLetters(tmp, 'a', '1'), isLetter1Digit, database);
		}
		for(String passI : i) {
			saveA31(passI, isLetter1Digit, database);
			
			tmp = passI.toCharArray();
			saveA31(replaceLetters(tmp, 'i', '1'), isLetter1Digit, database);
			saveA31(replaceLetters(tmp, 'e', '3'), isLetter1Digit, database);
			
			tmp = passI.toCharArray();
			saveA31(replaceLetters(tmp, 'e', '3'), isLetter1Digit, database);
		}
	}
	
	private void saveA31(String pass, boolean isLetter1Digit, DatabaseInterface database) {
		//add encrypt word to database
		try {
			if(isLetter1Digit) {
				database.save(pass,Sha1.hash(pass));
				database.save(encrpytYear(pass),Sha1.hash(encrpytYear(pass)));
			} else {
				database.save(pass,Sha1.hash(pass));
				database.save(encrpytYear(pass),Sha1.hash(encrpytYear(pass)));
				String tmpString = encrpytCapitalize(pass.toCharArray());
				database.save(tmpString,Sha1.hash(tmpString));
				database.save(encrpytYear(tmpString),Sha1.hash(encrpytYear(tmpString)));
			}
		} catch (UnsupportedEncodingException e1) {e1.printStackTrace(); }
	}
	
	private String encrpytYear(String password) {
		//O(1) operation
		return (password + Integer.toString(year));
	}
	
	private String encrpytCapitalize(char[] password) {
		//O(1) operation
		password[0] = Character.toUpperCase(password[0]);
		return String.valueOf(password);
	}
	
	private String replaceLetters(char[] pass, char replaceThis, char ReplaceWith) {
		for(int k = 0; k < pass.length; k ++) {
			if(pass[k] == replaceThis)
				pass[k] = ReplaceWith;
		}
		return String.valueOf(pass);
	}
}
