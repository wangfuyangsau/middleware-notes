package org.example.middleware.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DeadInfo {
    private Integer id;
    private String msg;

    public DeadInfo(){

    }
    public DeadInfo(Integer id, String msg) {
        this.id = id;
        this.msg = msg;
    }

}
