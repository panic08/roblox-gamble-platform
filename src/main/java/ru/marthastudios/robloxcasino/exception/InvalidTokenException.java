package ru.marthastudios.robloxcasino.exception;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String exceptionMessage){
        super(exceptionMessage);
    }
}
