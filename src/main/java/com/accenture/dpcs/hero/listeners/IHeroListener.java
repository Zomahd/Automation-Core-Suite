package com.accenture.dpcs.hero.listeners;

import org.testng.ITestNGListener;

public abstract class IHeroListener {

    protected static final String CLASS_MUST_IMPLEMENT = "the class must implement";
    protected static final String CLASS_NOT_FOUND= "the class was not found";
    protected static final String CLASS_NO_PARAM_CONSTRUCTOR = "the class must have a no parameter constructor";
    protected static final String CLASS_CANNOT_BE_ACCESSED= "the class can not be accessed";
    protected static final String ERROR_INST_CLASS = "error instantiating the class";
    protected static final String INVOCATION_TARGET_EXCEPTION = "invocation target exception";
    protected static final String INVALID_LISTENER = "invalid listener";
    protected static final String ERROR_WHILE_EXEC = "error while executing";

    protected abstract void addListeners(String config);
    protected abstract void instanceError(String msg, String name);
    protected abstract void instanceError(String msg, String name, Throwable throwable);
    protected abstract void methodError(String method, ITestNGListener listener, Throwable throwable);

}
