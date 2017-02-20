package ru.ivan.sqlcmd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping(value = "/console/")
public class ConsoleController {
    @Autowired
    private Service service;

    private String mapping;

    {
        try {
            String[] path = this.getClass().getAnnotation(RequestMapping.class).value();
            if (path != null && path.length == 1) {
                mapping = path[0];
            }
        } catch (Exception e) {
            mapping = "";
        }
    }

    private static final String PAGE_MAIN = "main";
    public static final String LINE_SEPARATOR = System.lineSeparator();


    @RequestMapping(value = PAGE_MAIN, method = RequestMethod.GET)
    public String redirect(HttpSession session) {
        return "redirect:" + mapping;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    
    public String main(Model model,
                       @ModelAttribute("input") String input,
                       @ModelAttribute("commands") String commands,
                       @ModelAttribute("output") String output,
                       HttpSession session, HttpServletRequest request) {
        DatabaseManager manager = getManager(session);
        //model.addAttribute("items", service.getMainMenu(manager));
        String[] commandList=new String[0];
//        if (!"".equals(commands)) {
            commands = commands.concat(input).concat(LINE_SEPARATOR);

        commandList=commands.split(LINE_SEPARATOR);
        model.addAttribute("commandList", commandList);
        model.addAttribute("commands", commands);
        model.addAttribute("input", "");
        model.addAttribute("output", output);
        return PAGE_MAIN;
    }

    private DatabaseManager getManager(HttpSession session) {
        return (DatabaseManager) session.getAttribute("manager");
    }

}
