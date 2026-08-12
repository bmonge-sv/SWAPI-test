package com.swapi.framework.core;

public final class ResponseCapture {

    private static final ThreadLocal<Integer> STATUS = new ThreadLocal<>();

    private ResponseCapture() {}

    public static void setStatusCode(int code) { STATUS.set(code); }
    public static Integer getStatusCode()       { return STATUS.get(); }
    public static void clear()                  { STATUS.remove(); }
}
