package com.example.ayen.dto.response;

public class ApiResponse<T> {
    private int code;
    private T data;

    public ApiResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    // Getter & Setter
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}