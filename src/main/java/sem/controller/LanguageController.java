package sem.controller;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/language", produces = MediaType.APPLICATION_JSON_VALUE)
public class LanguageController {
	@GetMapping
    public Locale getLanguage() {
        return LocaleContextHolder.getLocale();
    }

}
