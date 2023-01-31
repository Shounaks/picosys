package com.example.picosys.exceptions;

import lombok.Getter;

@Getter
public class PartnerCompServiceException extends RuntimeException {
    private long id = 1L;
    public PartnerCompServiceException(String message) {
        super(message);
    }
    public PartnerCompServiceException(Long id,String message) {
        super(message);
        this.id = id;
    }

}