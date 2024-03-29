package com.example.pixelpost.Model.User;


import com.example.pixelpost.Utils.SupportClass.PasswordUtils;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class UserModel implements IUserModel{
    //region Singleton define
    private static UserModel instance;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private UserModel() {}

    // Static method to get the singleton instance
    public static UserModel getInstance() {
        if (instance == null) {
            synchronized (UserModel.class) {
                if (instance == null) {
                    instance = new UserModel();
                }
            }
        }
        return instance;
    }
    //endregion

    @Override
    public void createUser(User user, OnCreateUserListener listener) {
        initFirebase();

        user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                user.setId(Objects.requireNonNull(task.getResult().getUser()).getUid());
                db.collection(User.FIREBASE_COLLECTION_NAME).document(task.getResult().getUser().getUid()).set(user).addOnCompleteListener(taskUser -> {
                    if(taskUser.isSuccessful())
                    {
                        listener.onCreateUserComplete(user,false, taskUser.getException());

                    }
                    else
                        listener.onCreateUserComplete(null, false, taskUser.getException());
                });
            }
            else
            {
                listener.onCreateUserComplete(null, task.getException() instanceof FirebaseAuthUserCollisionException,task.getException());
            }
        });

    }

    @Override
    public void updateUser(User user, OnUserOperationListener listener) {
        initFirebase();
        HashMap<String, Object> newUser = new HashMap<>();
        newUser.put(User.FIELD_FIRST_NAME, user.getFirstName());
        newUser.put(User.FIELD_LAST_NAME, user.getLastName());
        if(user.getAvatarUrl()!=null && !user.getAvatarUrl().isEmpty())
        {
            newUser.put(User.FIELD_AVATAR_URL,user.getAvatarUrl());
        }
        db.collection(User.FIREBASE_COLLECTION_NAME).document(user.getId()).update(newUser).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                listener.onUserOperationCompleted(user,null);
            }
            else{
                listener.onUserOperationCompleted(null,task.getException());
            }
        });

    }

    @Override
    public void login(User user, OnUserOperationListener listener) {
        initFirebase();
        user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
        auth.signInWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                db.collection(User.FIREBASE_COLLECTION_NAME).document(task.getResult().getUser().getUid()).get().addOnCompleteListener(
                        taskUser->{
                            if(taskUser.isSuccessful())
                            {
                                listener.onUserOperationCompleted(taskUser.getResult().toObject(User.class),null);
                            }
                            else
                                listener.onUserOperationCompleted(null, taskUser.getException());
                        }
                );
            }
            else{
                listener.onUserOperationCompleted(null, task.getException());
            }
        });
    }

    @Override
    public void changePassword(User user, String oldPassword, String newPassword, OnUserOperationListener listener) {
        User user
    }

    @Override
    public void changeForgotPassword(String email, String newPassword, OnUserOperationListener listener) {

    }

    @Override
    public void logout(User user, OnUserOperationListener listener) {

    }

    @Override
    public void uploadAvatar(byte[] avatarImage, User user, OnUserOperationListener listener) {

    }

    private void initFirebase()
    {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

}
