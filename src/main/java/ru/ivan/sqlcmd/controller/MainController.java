package ru.ivan.sqlcmd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping(value = "/*")
public class MainController {

    private static final String PAGE_MAIN = "main";
    private static final String FILE_SEPARTOR = File.separator;
    private static final String WEB_SEPARTOR = "/";

    @RequestMapping(value = "", method = {RequestMethod.GET})
    public String main() {

        return PAGE_MAIN;

    }

    @RequestMapping(value = "/images/*", method =
            RequestMethod
                    .GET)
    public void openImage(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String path = request.getServletPath();
        String fileName = path.substring(path.lastIndexOf(WEB_SEPARTOR) + 1);
        OutputStream os = response.getOutputStream();
        String filePath = request.getServletContext().getRealPath("") + "resources" + FILE_SEPARTOR + "images" +
                FILE_SEPARTOR + fileName;
        File imageOnDisk = new File(filePath);
        FileInputStream fis = new FileInputStream(imageOnDisk);
        int bytes;
        while ((bytes = fis.read()) != -1) {
            os.write(bytes);
        }
        fis.close();
    }

}
