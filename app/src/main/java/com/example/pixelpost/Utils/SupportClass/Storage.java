package com.example.pixelpost.Utils.SupportClass;

import com.example.pixelpost.Model.Post.Post;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Storage {
    public static void UploadImage(byte[] image,String id, String collectionType, OnUploadSuccessListener listener){
        String extension = "jpg";
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference imageRef = firebaseStorage.getReference().child(collectionType).child("/image" + id + "." + extension);
        imageRef.putBytes(image)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        imageRef.getDownloadUrl().addOnSuccessListener(uri ->{
                            listener.onUploadImageSuccess(uri.toString(), null);
                            return;
                        })
                        .addOnFailureListener((e -> {
                            listener.onUploadImageSuccess(null, e);
                            return ;
                        }));
                    }
                    else{
                        listener.onUploadImageSuccess(null, task.getException());
                        return;
                    }
                });
    }
    public interface OnUploadSuccessListener{
        void onUploadImageSuccess(String url, Exception e);
    }
}
