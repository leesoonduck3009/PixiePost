package com.example.pixelpost.Contract.Activity;

public interface ISignUp01ActivityContract {
    interface Presenter{
        void checkEmail(String email);
    }
    interface View{
        void isLegitEmail();
        void isNotLegitEmail();
        void checkEmailFailed(Exception e);
    }
}
