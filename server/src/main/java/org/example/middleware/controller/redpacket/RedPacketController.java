package org.example.middleware.controller.redpacket;

import org.example.middleware.controller.BookController;
import org.example.middleware.dto.RedPacketDTO;
import org.example.middleware.service.redpacket.IRedPacketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import response.BaseResponse;
import response.StatusCode;

import java.math.BigDecimal;

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

    @RequestMapping(value = prefix+"/rob",method = RequestMethod.GET,consumes  = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse rob(@RequestParam Integer userId,@RequestParam String redId){

        BaseResponse response = new BaseResponse(StatusCode.Success);

        try {
            BigDecimal result = redPacketService.rob(userId, redId);
            if(result!=null){
                response.setData(result);
            }else {
                response = new BaseResponse(StatusCode.Fail.getCode(),"没有了");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }

        return response;
    }
}
