package com.annamakos.socialcare.api.controller;

import com.annamakos.socialcare.api.model.User;
import com.annamakos.socialcare.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class UserController {
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<User>> findAllUsers(){
        List<User> userList =  this.userService.findAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/users/institution/{id}", method = RequestMethod.GET)
    public ResponseEntity<Set<User>> findUsersByInstitution(@PathVariable int id){
        Set<User> users = userService.findUsersByInstitution(id);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/users/alterofficial/role", method = RequestMethod.PUT)
    public ResponseEntity<User> alterUserToOfficial(@RequestParam String username){
        User user = userService.alterUserToOfficial(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/users/alterofficial/institution", method = RequestMethod.PUT)
    public ResponseEntity<User> alterInstitutionForOfficial(@RequestParam String username, @RequestParam String institutionName){
        User user = userService.alterInstitutionForOfficial(username, institutionName);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


//    @PreAuthorize("hasRole('USER')")
//    @RequestMapping(value = "/user/profile", method = RequestMethod.GET)
//    public ResponseBody showMyProfile(){
//        return this.userService.
//    }

}
