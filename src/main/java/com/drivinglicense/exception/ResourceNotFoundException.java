package com.drivinglicense.exception;

public class ResourceNotFoundException extends RuntimeException{
    ResourceNotFoundException(String message){
        super(message);
    }

    ResourceNotFoundException(String entityName,Object id){
        super(entityName + "not found with id: " + id);
    }

}
