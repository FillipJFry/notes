package com.goit.letscode.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.List;

@Controller
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteService srv;

    @GetMapping("/list")
    public ModelAndView getList() {

        ModelAndView result = new ModelAndView("list");
        List<NoteDTO> notes = srv.listAllForCurrentUser();
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
        return result;
    }

    @PostMapping("/edit")
    public ModelAndView edit(@ModelAttribute NoteDTO note) {

        ModelAndView result = new ModelAndView("edit");
        note = srv.getById(note.getId());
        result.addObject("existing_note", note);
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
