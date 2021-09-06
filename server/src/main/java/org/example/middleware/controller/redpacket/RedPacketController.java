package org.example.middleware.controller.redpacket;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.example.middleware.controller.BookController;
import org.example.middleware.dto.RedPacketDTO;
import org.example.middleware.service.redis.redpacket.IRedPacketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import response.BaseResponse;
import response.StatusCode;

import javax.swing.plaf.PanelUI;

@RestController
public class RedPacketController {
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    private static final String  prefix = "red/packet";
    @Autowired

    private  IRedPacketService redPacketService;
    @RequestMapping(value = prefix+"/hand/out",method = RequestMethod.POST,consumes  = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse handOut(@Validated @RequestBody RedPacketDTO dto, BindingResult result){
        if(result.hasErrors()){
            return  new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);

        try {
            String redId = redPacketService.handOut(dto);
            response.setData(redId);
        } catch (Exception e) {
            e.printStackTrace();
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }

        return response;
    }
}
