package com.sjm5z.community.controller;

import com.sjm5z.community.dto.FileDTO;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
public class FileController {

    @RequestMapping("/file/upload")
    public @ResponseBody
    FileDTO upload(HttpServletRequest request) {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartHttpServletRequest.getFile("editormd-image-file");
        String path = null;
        try {
            path = ResourceUtils.getURL("classpath:").getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //获取图片姓名
        String filename = file.getOriginalFilename();
        //给图片添加唯一标识
        for(int i = filename.length() - 1; i >= 0; i--){
            if(filename.charAt(i) == '.'){
                String firstName = filename.substring(0,i) + "_" +System.currentTimeMillis();
                String lastName = filename.substring(i, filename.length());
                filename = firstName + lastName;
                break;
            }
        }
        File upload = new File(path + "static/images/upload/" + filename);
        try {
            //将图片上传
            file.transferTo(upload);
            //返回成功状态码
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setUrl("/images/upload/" + filename);
            return fileDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回失败的状态码
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(2);
        fileDTO.setMessage("图片上传失败，请重新上传！");
        return fileDTO;
    }
}
