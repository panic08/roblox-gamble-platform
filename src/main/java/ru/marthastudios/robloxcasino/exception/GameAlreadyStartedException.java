package ru.marthastudios.robloxcasino.exception;

public class GameAlreadyStartedException extends RuntimeException{
    public GameAlreadyStartedException(String exceptionMessage){
        super(exceptionMessage);
    }
}
