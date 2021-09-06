package org.example.middleware.dto;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class RedPacketDTO {
    private Integer userId;
    @NotNull
    private Integer total;
    @NotNull
    private     Integer amount;
}
