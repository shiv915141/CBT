package com.example.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;


import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class Maincontroller {

    @Autowired
    CredentialRepository credentialRepository;

    @Autowired
    UserdetailRepository userdetailRepository;

    @Autowired
    UsertypelinkRepository usertypelinkRepository;

    @GetMapping("/landing")
    public String getLandingPage() {

        return "landingpage";
    }
    @GetMapping("/usertypelink")
    public String getUserTypelink()
    {
        return "usertype";
    }

    //    @GetMapping("/save")
//    public String saveCredentials() {
//        Credential credential = new Credential();
//        credential.setUsername("Aditya");
//        credential.setPassword("Aditya@123");
//        credentialRepository.save(credential);
//        return "New Credential Saved";
//    }

    @GetMapping("/interimdashboard")
    public String getInterimDashboard() {

        return "interimdashboard";
    }

    @GetMapping("/userdetails")
    public String userDetails()
    {
        return "userdetails";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam("username") String username,
                         @RequestParam("password") String password , Model model) {
        Credential existingCredential = credentialRepository.findByUsername(username);
        if (existingCredential != null) {
            model.addAttribute("error", "Username already taken");
            return "landingpage";
        }

        Credential credential = new Credential();
        credential.setUsername(username);
        credential.setPassword(password);
        credentialRepository.save(credential);

        return "redirect:/interimdashboard";
    }


    @GetMapping("/buyerdashboard")
    public String getBuyersDahsboard() {

        return "buyerdashboard";
    }


    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password, HttpSession session, Model model) {

        Optional<Credential> matchedCredential = credentialRepository.findById(username);
        if (matchedCredential.isPresent()) {
            if (matchedCredential.get().getPassword().equals(password)) {
                session.setAttribute("username", username);

                Optional<Userdetail> userdetail = userdetailRepository.findById(username);
                List<Usertypelink> usertypelinks = usertypelinkRepository.findAll();
                Optional<Usertypelink> usertypelink = usertypelinks.stream().filter(usertypelink1 -> usertypelink1.getUsername().equals(username)).findAny();


                if (usertypelink.isPresent()) {
                    if (usertypelink.get().getType().equals("SELLER")) {

                        return "sellerdashboard";
                    } else if (usertypelink.get().getType().equals("BUYER")) {
                        return "buyerdashboard";
                    } else {
                        return "userdetails";
                    }
                }

                if (userdetail.isPresent()) {
                    model.addAttribute("userdetail", userdetail.get());
                }
                return "sellerdashboard";
            } else {
                model.addAttribute("error", "Invalid password");
                return "landingpage";
            }

        } else {
            model.addAttribute("error", "User not registered");
            return "landingpage";

        }
    }


    @PostMapping("/userdetails")
    public String userDetails(@RequestParam("firstname") String firstname,
                              @RequestParam("lastname") String lastname,
                              @RequestParam("email") String email,
                              @RequestParam("phone") String phone,
                              Model model) {
        Userdetail userdetails = new Userdetail();
        userdetails.setFirstname(firstname);
        userdetails.setLastname(lastname);
        userdetails.setEmail(email);
        userdetails.setPhone(phone);

        userdetailRepository.save(userdetails);

        model.addAttribute("message", "Successfully registered!");

        return "redirect:/usertypelink";
    }


    @PostMapping("/usertypelink")
    public String saveUserTypeLink(@RequestParam("type") String type,
                                   @RequestParam("username") String username, Model model) {

        Usertypelink userTypeLink = new Usertypelink();
        userTypeLink.setType(type);
        userTypeLink.setUsername(username);
        userTypeLink.setId(String.valueOf(Math.floor(Math.random() * 100000)));

        usertypelinkRepository.save(userTypeLink);

        return "redirect:/landing";
    }

}