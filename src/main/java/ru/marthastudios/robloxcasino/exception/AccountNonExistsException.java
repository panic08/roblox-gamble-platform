package ru.marthastudios.robloxcasino.exception;

public class AccountNonExistsException extends RuntimeException{
    public AccountNonExistsException(String exceptionMessage){
        super(exceptionMessage);
    }
}
