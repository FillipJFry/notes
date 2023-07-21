package com.goit.letscode.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/note")
public class NoteController {

    private static final int MAX_VISIBLE_TITLE_LEN = 50;
    private static final int MAX_VISIBLE_CONTENT_LEN = 200;
    @Autowired
    private NoteService srv;

    @GetMapping("/list")
    public ModelAndView getList() {

        ModelAndView result = new ModelAndView("list");
        List<NoteDTO> notes = srv.listAllForCurrentUser()
                .peek(note -> {
                    String title = note.getTitle();
                    String content = note.getContent();
                    note.setTitle(title.length() <= MAX_VISIBLE_TITLE_LEN ? title :
                                           title.substring(0, MAX_VISIBLE_TITLE_LEN) + "...");
                    note.setContent(content.length() <= MAX_VISIBLE_CONTENT_LEN ? content :
                                           content.substring(0, MAX_VISIBLE_CONTENT_LEN) + "...");
                }).collect(Collectors.toList());
        String header = "Мої нотатки";

        if (!notes.isEmpty()) header += " - " + notes.size() + " шт.";
        result.addObject("notes_header", header);
        result.addObject("notes", notes);
        return result;
    }

    @GetMapping({"/share", "/share/{id}"})
    public ModelAndView share(@PathVariable Optional<String> id) {

        long parsedId;
        try {
            parsedId = Long.parseLong(id.orElse(""));
        } catch (NumberFormatException e) {
            return loadSharePage("Невірне посилання", "");
        }

        NoteDTO note = srv.getById(parsedId);
        if (note == null || note.getAccessType() == AccessType.PRIVATE) {
            return loadSharePage("Ця нотатка не існує або не може бути розшарена", "");
        }
        return loadSharePage(note.getTitle(), note.getContent());
    }

    @GetMapping("/create")
    public ModelAndView create() {

        ModelAndView result = new ModelAndView("edit");
        NoteDTO note = srv.create();
        result.addObject("existing_note", note);
        result.addObject("pageTitle","Створення нотатки");
        result.addObject("header","Створення нотатки");
        return result;
    }

    @PostMapping("/edit")
    public ModelAndView edit(@ModelAttribute NoteDTO note) {

        ModelAndView result = new ModelAndView("edit");
        note = srv.getById(note.getId());
        result.addObject("existing_note", note);
        result.addObject("pageTitle","Редагування нотатки");
        result.addObject("header","Редагування нотатки");
        return result;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute NoteDTO note) {

        try {
            srv.save(note);
        } catch (Exception e) {
            ModelAndView result = new ModelAndView("edit");
            result.addObject("editErrorMsg", e.getMessage());
            result.addObject("existing_note", note);
            return result;
        }
        return new ModelAndView("redirect:/note/list");
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute NoteDTO noteToDel) {

        assert noteToDel != null;
        srv.deleteById(noteToDel.getId());
        return "redirect:/note/list";
    }

    @GetMapping("/pwd")
    public ModelAndView showPwdHash(BCryptPasswordEncoder encoder) {

        ModelAndView result = new ModelAndView("hash");

        String pwdHash = encoder.encode("123");
        result.addObject("pwdHash", pwdHash);
        return result;
    }

    private ModelAndView loadSharePage(String title, String content) {

        ModelAndView result = new ModelAndView("share");
        result.addObject("noteTitle", title);
        result.addObject("noteContent", content);
        return result;
    }
}
