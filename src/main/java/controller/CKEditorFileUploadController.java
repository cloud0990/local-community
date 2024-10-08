package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class CKEditorFileUploadController {
	
	@Autowired
	ServletContext application;
	
	@RequestMapping(value="/ckeditorImageUpload.do", method=RequestMethod.POST)
	public void ckeditorImageUpload(
			HttpServletRequest request, 
			HttpServletResponse response, 
			@RequestParam MultipartFile upload) throws     Exception {
			
        PrintWriter printWriter = null;

        try{
 
            String fileName = upload.getOriginalFilename(); 
            
            String web_path = "/resources/upload/";
            
            
            String abs_path = application.getRealPath(web_path);
            File f = new File(abs_path,fileName);
            if(f.exists()){
            	 long time = System.currentTimeMillis();
            	 int index = fileName.lastIndexOf('.');
            	 String f_name = fileName.substring(0,index);
            	 String f_ext  = fileName.substring(index);
            	 
            	 fileName = String.format("%s_%d%s", f_name,time,f_ext);
            	 f = new File(abs_path,fileName);
            }
            upload.transferTo(f);
 
            String callback = request.getParameter("CKEditorFuncNum");
            
            response.setContentType("text/html;charset=utf-8");
            printWriter = response.getWriter();
            
            String url = request.getRequestURL().toString().replaceAll("/ckeditorImageUpload.do", "");
            String fileUrl = url + web_path + fileName;

            printWriter.println("{\"filename\" : \"" + fileName + "\", \"uploaded\" : 1, \"url\":\"" + fileUrl + "\"}");
            printWriter.flush();

        }catch(IOException e){
            e.printStackTrace();
        } finally {
            try {
                
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

	@RequestMapping(value="/ckeditorImageUpload2.do", method=RequestMethod.POST)
	public void ckeditorImageUpload2(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile upload) throws Exception {

		OutputStream out = null;
        PrintWriter printWriter = null;
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
 
        try{
            String fileName = upload.getOriginalFilename();
            byte[] bytes = upload.getBytes();
            String web_path = "/resources/upload/";
            String abs_path = application.getRealPath(web_path);

            File f = new File(abs_path,fileName);
            if(f.exists()){
            	long time = System.currentTimeMillis();
            	int index = fileName.lastIndexOf('.');
            	String f_name = fileName.substring(0,index);
            	String f_ext  = fileName.substring(index);

            	fileName = String.format("%s_%d%s", f_name,time,f_ext);
            	f = new File(abs_path,fileName);
            }
            
            out = new FileOutputStream(f);
            out.write(bytes);
            
            String callback = request.getParameter("CKEditorFuncNum");
 
            printWriter = response.getWriter();
            
            String url = request.getRequestURL().toString().replaceAll("/ckeditorImageUpload.do", "");

            String fileUrl = url + web_path + fileName;
 
            printWriter.println("{\"filename\" : \"" + fileName + "\", \"uploaded\" : 1, \"url\":\"" + fileUrl + "\"}");
            printWriter.flush();
 
        }catch(IOException e){
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
}