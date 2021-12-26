package com.yjy.web.comm.result;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultUtil {

    /**
     * 表单验证，返回形式Result
     * @param br
     * @return
     */
    public static Result hasErrors(BindingResult br) {
        if (br.hasErrors()) {
            List<FieldError> fieldErrors = br.getFieldErrors();
            List<String> messagge;
            Map<String, List<String>> map = new HashMap<>();
            for (FieldError fieldError : fieldErrors) {
                if (!map.containsKey(fieldError.getField())) {
                    messagge = new ArrayList<>();
                } else {
                    messagge = map.get(fieldError.getField());
                }
                messagge.add(fieldError.getDefaultMessage());
                map.put(fieldError.getField(), messagge);
            }
            return new Result(ResultCode.ERROR_CHECK, map);
        }
        return new Result(ResultCode.SUCCESS, null);
    }
}