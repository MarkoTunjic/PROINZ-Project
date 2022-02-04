package hr.fer.proinz.skilletcooking.payload.Response;

import lombok.Getter;
import lombok.Setter;

//this annotation generates getters
@Getter

// this annotation generates setters
@Setter

/** A class that represents a HTTP message reepsonse */
public class MessageResponse {

    /** An attribute that contains the message to be sent */
    private String message;

    /** A constructor that creates a message response from the given message */
    public MessageResponse(String message) {
        this.message = message;
    }
}
