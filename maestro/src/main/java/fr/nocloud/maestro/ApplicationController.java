package fr.nocloud.maestro;

import java.util.Map;
import java.util.NoSuchElementException;

import fr.nocloud.maestro.model.catalog.Applications;
import fr.nocloud.maestro.services.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    private Applications apps() {

        return applicationService.getApplications();
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    private void refresh() {
        applicationService.refresh();
    }

    @RequestMapping(value = "/{application}/install", method = RequestMethod.POST)
    private void install(@PathVariable String application, @RequestBody(required = false) Map<String, String> parameters) {
        applicationService.install(application, parameters);
    }

    @RequestMapping(value = "/{application}/uninstall", method = RequestMethod.POST)
    private void uninstall(@PathVariable String application) {
        applicationService.uninstall(application);
    }

    @RequestMapping(value = "/{application}/start", method = RequestMethod.POST)
    private void start(@PathVariable String application) {
        applicationService.start(application);
    }

    @RequestMapping(value = "/{application}/stop", method = RequestMethod.POST)
    private void stop(@PathVariable String application) {
        applicationService.stop(application);
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public void notFound() {
    }
}
