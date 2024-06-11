package ru.mts.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PhoneNumber implements Serializable {
    private String phoneNumber;

    public PhoneNumber() {
    }
}