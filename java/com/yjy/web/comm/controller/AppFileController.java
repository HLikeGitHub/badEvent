package com.yjy.web.comm.controller;

import com.yjy.web.comm.file.fastdfs.FastDFSClient;
import com.yjy.web.comm.result.Result;
import com.yjy.web.comm.result.ResultCode;
import com.yjy.web.comm.utils.JSONUtil;
import com.yjy.web.comm.utils.TextUtil;
import com.yjy.web.comm.utils.crypt.aes.EncryptResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件服务控制器
 * @info AppFileController
 * @author rwx
 * @mail aba121mail@qq.com
 */
@Controller
@RequestMapping(value = "comm/file")
public class AppFileController {

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    /**
     * 上传文件到FastDFS文件服务器
     * @param file
     * @return
     */
    @PostMapping(value = "upload")
    @ResponseBody
    @EncryptResponse
    public String upload(MultipartFile file){
        logger.info("File--->>>upload file="+file);
        //空文件对象
        if(file == null){
            String resultJson = JSONUtil.toJSON(new Result(ResultCode.ERROR_FILE_UPLOAD));
            logger.error(resultJson);
            return resultJson;
        }
        //开始上传
        String url = FastDFSClient.getInstance().upload(file);
        //上传失败
        if(TextUtil.isNullOrEmpty(url)){
            String resultJson = JSONUtil.toJSON(new Result(ResultCode.ERROR_SERVICE_ERR0R));
            logger.error(resultJson);
            return resultJson;
        }
        //上传成功
        Map<String, Object> data = new HashMap<>();
        data.put("file", file.getOriginalFilename());
        data.put("url", url);
        String resultJson = JSONUtil.toJSON(new Result<Map<String, Object>>(ResultCode.SUCCESS, data));
        logger.info(resultJson);
        return resultJson;
    }

    @RequestMapping(value = "show-upload")
    public String showUpload(){
        return "file/show-upload";
    }

    @RequestMapping(value = "delete")
    public String delete(@RequestParam(value="groupName",defaultValue="") String groupName,
                         @RequestParam(value="remoteFileName",defaultValue="") String remoteFileName){
        if(TextUtil.isNullOrEmpty(groupName, remoteFileName)){
            return "params is null or empty";
        }

        try {
            FastDFSClient.getInstance().deleteFile(groupName, remoteFileName);
        }catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }

        return "success";
    }
}