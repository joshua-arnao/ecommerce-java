package com.ecommerce.sportscenter.shared.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerErrorResponse {
    private HttpStatus status;
    private String error;
    private String message;
}
