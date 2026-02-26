package com.UserManagement;

public abstract class User {
	private final Email email;
	private  String pwHash;
	private  String name;
	public User(Email email, String pwHash, String name) {
		this.email=email;
		this.pwHash=pwHash;
		this.name=name;
	}
	public Email getEmail() {
		return email;
	}
	public String getPwHash() {
		return pwHash;
	}
	public String getName() {
		return name;
	}


	//UC-03
	    public void setName(String newName) throws UserException {
	        if (newName == null || newName.trim().isEmpty()) {
	            throw new UserException("Name cannot be empty.");
	        }
	        this.name = newName.trim();
	    }
	    public void changePassword(String oldPassword, String newPassword) throws UserException {
	        if (oldPassword == null || newPassword == null) {
	            throw new UserException("Passwords cannot be null.");
	        }

	        String oldHash = PasswordHashing.hash(oldPassword);

	        if (!oldHash.equals(this.pwHash)) {
	                   throw new UserException("Old password is incorrect.");
	               }
	               PasswordValidation.validate(newPassword);

	               this.pwHash = PasswordHashing.hash(newPassword);
	           }

@Override
    public String toString() {
        return getClass().getSimpleName() +
                " {email=" + email.getValue() + ", name=" + name + "}";
    }

}