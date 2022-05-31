package com.accenture.dpcs.hero.exceptions;

/**
 * Created by d.arguedas.calderon on 11/11/2016.
 */
public class UnknownBrowserException extends Exception {

    private static final long serialVersionUID = 1L;

    public UnknownBrowserException(String message)  {
        super(message);
    }
}
