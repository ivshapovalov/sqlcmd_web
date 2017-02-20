package ru.ivan.sqlcmd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.ivan.sqlcmd.controller.command.*;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.model.PostgreSQLManager;
import ru.ivan.sqlcmd.view.WebConsole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.TreeMap;

@Controller
@RequestMapping(value = "/console/")
public class ConsoleController {

    private String mapping;
    private int historySize = 0;

    private final WebConsole view;
    private final List<Command> consoleCommands = new Help().getCommands();
    private final TreeMap<Integer, String> history = new TreeMap<>();

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
    private static final String PAGE_ERROR = "error";
    public static final String LINE_SEPARATOR = System.lineSeparator();


    public ConsoleController() {

        view = new WebConsole("Input command or 'help' for assistance" + LINE_SEPARATOR);
        DatabaseManager manager = new PostgreSQLManager();
        for (Command command : consoleCommands
                ) {
            AbstractCommand abstractCommand = (AbstractCommand) command;
            abstractCommand.setManager(manager);
            abstractCommand.setView(view);
            if (abstractCommand instanceof History) {
                ((History) abstractCommand).setHistory(history);
            }
        }
    }

    @RequestMapping(value = PAGE_MAIN, method = RequestMethod.GET)
    public String redirect(HttpSession session) {
        return "redirect:" + mapping;
    }

    @RequestMapping(value = "", method = {RequestMethod.POST})
    public String command(Model model,
                          @ModelAttribute("input") String input,
                          @ModelAttribute("commands") String commands,
                          @ModelAttribute("output") String output,
                          HttpSession session, HttpServletRequest request) {
        view.write(input);
        String historyInput = checkHistoryInput(input);
        if (historyInput == null) {
        } else {
            input = historyInput;
        }
        increaseInputHistory(++historySize, input);
        for (Command command : consoleCommands
                ) {

            try {
                if (command.canProcess(input)) {
                    command.process(input);
                    break;
                }
            } catch (ExitException e) {
                throw e;
            } catch (Exception e) {
                printError(e);
                break;
            }
        }
        view.write("====================================================================");
        String[] commandList = new String[0];
        commands = commands.concat(input).concat(LINE_SEPARATOR);
        commandList = commands.split(LINE_SEPARATOR);
        model.addAttribute("commandList", commandList);
        model.addAttribute("commands", commands);
        model.addAttribute("input", "");
        model.addAttribute("output", view.getOutput());
        return PAGE_MAIN;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET})
    public String main(Model model,
                       @ModelAttribute("input") String input,
                       @ModelAttribute("commands") String commands,
                       @ModelAttribute("output") String output,
                       HttpSession session, HttpServletRequest request) {
        DatabaseManager manager = getManager(session);
        //model.addAttribute("items", service.getMainMenu(manager));
        view.setOutput(new StringBuilder("Input command or 'help' for assistance" + LINE_SEPARATOR));
        history.clear();
        String[] commandList = new String[0];
        commands = commands.concat(input).concat(LINE_SEPARATOR);
        commandList = commands.split(LINE_SEPARATOR);
        model.addAttribute("commandList", commandList);
        model.addAttribute("commands", commands);
        model.addAttribute("input", view.getInput().toString());
        model.addAttribute("output", view.getOutput().toString());
        return PAGE_MAIN;

    }

    private DatabaseManager getManager(HttpSession session) {
        return (DatabaseManager) session.getAttribute("manager");
    }

    private void increaseInputHistory(int historySize, String input) {
        history.put(historySize, input);
    }

    private String checkHistoryInput(String input) {
        try {
            int historyIndex = Integer.parseInt(input);
            String newInput = history.get(historyIndex);
            if (newInput != null) {
                view.write(newInput);
                return newInput;
            } else {
                view.write(String.format("Index '%s' does not exist in command history", historyIndex));
            }
        } catch (NumberFormatException e) {
            return input;
        }
        return input;
    }

    private void printError(Exception e) {
        String message = e.getMessage();
        if (e.getCause() != null) {
            message = message + " " + e.getCause().getMessage();
        }
        view.write("Failure cause: " + message);
        view.write("Try again");
    }
}
