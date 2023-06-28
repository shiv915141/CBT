package com.example.database;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v0.1")
public class MainRestController {

    @Autowired
    UserdetailRepository userdetailRepository;

    @Autowired
    UsertypelinkRepository usertypelinkRepository;

    @GetMapping
    public List<Usertypelink> getUserTypes(@RequestParam("username") String username)
    {
        return usertypelinkRepository.findByUsername(username);
    }

    @PostMapping("getuserbyemail")
    public ResponseEntity<Userdetail> getUserdetailsByEmail(@RequestParam("email") String email)
    {
        if(userdetailRepository.findByEmail(email).isPresent())
        {
            return new ResponseEntity<>(userdetailRepository.findByEmail(email).get(), HttpStatus.OK);
        }

        else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}