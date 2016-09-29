package com.onenow.hedgefund.nosqlrest.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil
{
    public static String exceptionToString(Exception ex)
    {
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
}
