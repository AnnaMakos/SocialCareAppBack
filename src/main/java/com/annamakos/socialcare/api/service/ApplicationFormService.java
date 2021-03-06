package com.annamakos.socialcare.api.service;

import com.amazonaws.services.codedeploy.model.ApplicationDoesNotExistException;
import com.annamakos.socialcare.api.dto.ApplicationFormBasicDTO;
import com.annamakos.socialcare.api.dto.ApplicationFormDTO;
import com.annamakos.socialcare.api.model.*;
import com.annamakos.socialcare.api.repository.ApplicationFormRepository;
import com.annamakos.socialcare.api.repository.ChildFormRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class ApplicationFormService {

    private ApplicationFormRepository applicationFormRepository;
    private ChildFormRepository childFormRepository;
    private UserService userService;

    public ApplicationFormService(ApplicationFormRepository applicationFormRepository, ChildFormRepository childFormRepository, UserService userService) {
        this.applicationFormRepository = applicationFormRepository;
        this.childFormRepository = childFormRepository;
        this.userService = userService;
    }

    public List<ApplicationFormDTO> findAll() {
        List<ApplicationForm> forms = applicationFormRepository.findAll();
        List<ApplicationFormDTO> formsDTO = new ArrayList<>();
        forms.forEach(form -> formsDTO.add(new ApplicationFormDTO(form)));
        return formsDTO;
    }

    public List<ApplicationFormDTO> findAllByApplicantUsername(String name) {
        List<ApplicationForm> forms = applicationFormRepository.findAllByApplicantUsername(name);
        List<ApplicationFormDTO> formsDTO = new ArrayList<>();
        forms.forEach(form -> {
            formsDTO.add((new ApplicationFormDTO(form)));
        });
        return formsDTO;
    }

    public List<ApplicationFormDTO> findAllByOfficialUsername(String username) {
        List<ApplicationForm> forms = this.applicationFormRepository.findAllByOfficialUsername(username);
        List<ApplicationFormDTO> formsDTO = new ArrayList<>();
        forms.forEach(form -> {
            formsDTO.add(new ApplicationFormDTO(form));
        });
        return formsDTO;
    }

    public ApplicationForm findById(long id) {
        ApplicationForm appForm = applicationFormRepository.findById(id).orElseThrow(() ->
                new ApplicationDoesNotExistException("ApplicationForm does not exist"));
        return appForm;
    }

    public ApplicationFormDTO findDTOById(long id) {
        ApplicationForm appForm = applicationFormRepository.findById(id).orElseThrow(() ->
                new ApplicationDoesNotExistException("ApplicationForm does not exist"));
        ApplicationFormDTO appFormDTO = new ApplicationFormDTO(appForm);
        return appFormDTO;
    }

    public ApplicationFormDTO addApplicationFormWithoutChildren(ApplicationFormBasicDTO appDTO) {
        String comment;
        if (appDTO.getComments() == null) {
            comment = "";
        } else {
            comment = appDTO.getComments();
        }

        User applicant = this.userService.findByUsername2(appDTO.getUsername());

        ApplicationForm app = new ApplicationForm(
                appDTO.getApplicationStatus(),
                appDTO.getMaritalStatus(),
                appDTO.getCitizenship(),
                appDTO.getPhone(),
                comment,
                applicant
        );
        applicationFormRepository.save(app);


        return new ApplicationFormDTO(app);
    }

    public ApplicationFormDTO addOfficialToForm(String officialUsername, long formId) {
        ApplicationForm app = this.findById(formId);
        User official = userService.findByUsername2(officialUsername);
        app.setOfficial(official);
        applicationFormRepository.save(app);
        return new ApplicationFormDTO(app);
    }

    public ApplicationFormDTO alterApplicationStatus(String status, long id) {
        ApplicationForm appForm = this.findById(id);
        appForm.setApplicationStatus(status);
        this.applicationFormRepository.save(appForm);
        ApplicationFormDTO appFormDTO = new ApplicationFormDTO(appForm);
        return appFormDTO;
    }

}
