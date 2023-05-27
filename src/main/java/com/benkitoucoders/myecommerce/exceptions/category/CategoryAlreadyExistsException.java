package com.benkitoucoders.myecommerce.exceptions.category;

import com.benkitoucoders.myecommerce.handlers.ApiBasedException;
import org.springframework.http.HttpStatus;

public class CategoryAlreadyExistsException extends ApiBasedException {
    public CategoryAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.CONFLICT;
    }
}
