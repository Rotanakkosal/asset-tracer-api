package com.kshrd.asset_tracer_api.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BodyResponse<T> {
    private Boolean success;
    private Timestamp date;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T payload;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T totalData;


    public static  <T> ResponseEntity<?> getBodyResponse(T payload) {
        BodyResponse<T> response = BodyResponse.<T>builder()
                .success(true)
                .date(new  Timestamp(System.currentTimeMillis()))
                .payload(payload)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public static  <T> ResponseEntity<?> getBodyResponse(T payload, T totalPage) {
        BodyResponse<T> response = BodyResponse.<T>builder()
                .success(true)
                .date(new  Timestamp(System.currentTimeMillis()))
                .payload(payload)
                .totalData(totalPage)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
