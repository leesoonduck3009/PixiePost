package com.example.pixelpost.Model.User;


import com.example.pixelpost.Utils.Exception.PasswordException;
import com.example.pixelpost.Utils.SupportClass.PasswordUtils;
import com.example.pixelpost.Utils.SupportClass.Storage;
import com.example.pixelpost.Utils.SupportClass.ValidateData;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        user.setFriendList(new ArrayList<>());
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
                                DocumentSnapshot ds = taskUser.getResult();
                                User user1 = new User.Builder().setEmail(ds.getString(User.FIELD_EMAIL))
                                                .setAvatarUrl(ds.getString(User.FIELD_AVATAR_URL))
                                        .setId(taskUser.getResult().getId()).setFirstName(ds.getString(User.FIELD_FIRST_NAME))
                                        .setLastName(ds.getString(User.FIELD_LAST_NAME))
                                        .setFriendList(ds.get(User.FIELD_FRIEND_LIST, ArrayList.class))
                                        .setPhoneNumber(ds.getString(User.FIELD_PHONE_NUMBER)).build();
                                listener.onUserOperationCompleted(user1,null);
                            }
                            else
                                listener.onUserOperationCompleted(null, taskUser.getException());
                        }
                );
            }
            else{
                listener.onUserOperationCompleted(null, new Exception("Sai thông tin email hoặc password"));
            }
        });
    }

    @Override
    public void getCurrentUser(OnUserOperationListener listener) {
        initFirebase();
        if(this.user!=null){
            db.collection(User.FIREBASE_COLLECTION_NAME).document(this.user.getUid()).get().addOnCompleteListener(
                    task -> {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot ds = task.getResult();
                            User user1 = new User.Builder().setEmail(ds.getString(User.FIELD_EMAIL))
                                    .setAvatarUrl(ds.getString(User.FIELD_AVATAR_URL))
                                    .setId(task.getResult().getId()).setFirstName(ds.getString(User.FIELD_FIRST_NAME))
                                    .setLastName(ds.getString(User.FIELD_LAST_NAME))
                                    .setFriendList(ds.get(User.FIELD_FRIEND_LIST, ArrayList.class))
                                    .setPhoneNumber(ds.getString(User.FIELD_PHONE_NUMBER)).build();
                            listener.onUserOperationCompleted(user1,null);
                        }
                        else
                        {
                            listener.onUserOperationCompleted(null,task.getException());
                        }
                    }
            );
        }
        else{
            listener.onUserOperationCompleted(null,new Exception("Chưa đăng nhập"));
        }
    }

    @Override
    public void getUserWithID(String id, OnUserOperationListener listener) {
        initFirebase();
        db.collection(User.FIREBASE_COLLECTION_NAME).document(id).get().addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                DocumentSnapshot ds = task.getResult();
                User user1 = new User.Builder()
                        .setAvatarUrl(ds.getString(User.FIELD_AVATAR_URL))
                        .setId(id)
                        .setFriendList((ArrayList<String>) ds.get(User.FIELD_FRIEND_LIST))
                        .setFirstName(ds.getString(User.FIELD_FIRST_NAME))
                        .setLastName(ds.getString(User.FIELD_LAST_NAME))
                        .build();
                listener.onUserOperationCompleted(user1,null);
            }
            else{
                listener.onUserOperationCompleted(null,task.getException());
            }
        });
    }

    @Override
    public void changePassword(User user, String oldPassword, String newPassword, OnUserOperationListener listener) {
        initFirebase();
        String hashNewPassword = PasswordUtils.hashPassword(newPassword);
        db.collection(User.FIREBASE_COLLECTION_NAME).document(user.getId()).get().addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                String password = task.getResult().getString(User.FIELD_PASSWORD);
                if(!PasswordUtils.verifyPassword(oldPassword,password))
                {
                    listener.onUserOperationCompleted(null,new PasswordException());
                }
                else
                {
                    assert hashNewPassword != null;
                    this.user.updatePassword(hashNewPassword).addOnCompleteListener(taskUser->{
                       if(taskUser.isSuccessful())
                       {
                           user.setPassword(hashNewPassword);
                           db.collection(User.FIELD_PASSWORD).document(user.getId()).update(User.FIELD_PASSWORD,hashNewPassword);
                           listener.onUserOperationCompleted(user,null);
                       }
                       else
                           listener.onUserOperationCompleted(null, taskUser.getException());
                    });
                }
            }
            else {
                listener.onUserOperationCompleted(null,task.getException());
            }
        });
    }

    @Override
    public void changeForgotPassword(String email, String newPassword, OnUserOperationListener listener) {
        initFirebase();
        String hashNewPassword = PasswordUtils.hashPassword(newPassword);
        db.collection(User.FIREBASE_COLLECTION_NAME).whereEqualTo(User.FIELD_EMAIL, email).get().addOnCompleteListener(
                taskUser->{
                    if(taskUser.isSuccessful())
                    {
                        QueryDocumentSnapshot user = taskUser.getResult().getDocumentChanges().get(0).getDocument();
                        String id = user.getId();
                        String oldPassword = user.getString(User.FIELD_PASSWORD);
                        assert oldPassword != null;
                        auth.signInWithEmailAndPassword(email,oldPassword).addOnCompleteListener(taskAuth->{
                            if(taskUser.isSuccessful()){
                                FirebaseUser newUser = taskAuth.getResult().getUser();
                                assert hashNewPassword != null;
                                assert newUser != null;
                                newUser.updatePassword(hashNewPassword);
                                db.collection(User.FIREBASE_COLLECTION_NAME).document(id).update(User.FIELD_PASSWORD,hashNewPassword)
                                        .addOnCompleteListener(taskUpdate ->{
                                            if(taskUpdate.isSuccessful())
                                            {
                                                auth.signOut();
                                                listener.onUserOperationCompleted(null,null);
                                            }
                                            else
                                                listener.onUserOperationCompleted(null,taskUpdate.getException());

                                        })
                                        .addOnFailureListener(e->{
                                });
                            }
                        });
                    }
                    else
                    {
                        listener.onUserOperationCompleted(null,taskUser.getException());
                    }
                }
        );
    }

    @Override
    public void logout(User user, OnUserOperationListener listener) {
        initFirebase();
        if(user!=null)
            auth.signOut();
    }

    @Override
    public void uploadAvatar(byte[] avatarImage, User user, OnUserOperationListener listener) {
        initFirebase();
        Storage.UploadImage(avatarImage, user.getId(),User.FIREBASE_COLLECTION_NAME,(url, e) -> {
            if(e!=null)
                listener.onUserOperationCompleted(null,e);
            else{
                user.setAvatarUrl(url);
                db.collection(User.FIREBASE_COLLECTION_NAME).document(user.getId()).update(User.FIELD_AVATAR_URL,url).addOnCompleteListener(
                        task -> {
                            if(task.isSuccessful())
                            {
                                listener.onUserOperationCompleted(user,null);
                            }
                            else
                                listener.onUserOperationCompleted(null,task.getException());
                        }
                );
            }
        });

    }

    @Override
    public void checkEmail(String email, OnCheckingEmailListener listener) {
        initFirebase();
        db.collection(User.FIREBASE_COLLECTION_NAME).whereEqualTo(User.FIELD_EMAIL,email).get().addOnCompleteListener(
                task -> {
                    if(task.isSuccessful())
                    {
                        if(task.getResult().getDocumentChanges().isEmpty())
                            listener.onCheckingEmail(false,null);
                        else
                            listener.onCheckingEmail(true,null);
                    }
                    else{
                        listener.onCheckingEmail(false,task.getException());
                    }
                }
        );
    }

    private void initFirebase()
    {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

}
