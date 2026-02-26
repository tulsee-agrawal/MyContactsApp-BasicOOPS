package com.profile;

import com.UserManagement.User;
import com.UserManagement.UserException;

public class ProfileService {

    public void updateName(User user, String newName) throws UserException {
        user.setName(newName); 
    }

    public void changePassword(User user, String oldPw, String newPw) throws UserException {
        user.changePassword(oldPw, newPw); 
    }

    public void viewProfile(User user) {
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail().getValue());
    }
}