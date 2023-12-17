package adr.gend.AdrAppProject.service;

import adr.gend.AdrAppProject.entity.Contact;
import adr.gend.AdrAppProject.entity.Home;
import adr.gend.AdrAppProject.entity.Page;
import adr.gend.AdrAppProject.entity.Person;
import adr.gend.AdrAppProject.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HomeService {
    @Autowired
    PageRepository pageRepository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    HomeRepository homeRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    PostRepository postRepository;
    public List<Page> getMenuParent(){
        Integer personNr  = postRepository.countAllRows();
        Integer postNr  = personRepository.countAllRows();
        List<Page> pageList = pageRepository.findAll().stream().filter(e -> e.getParent().equals("")).collect(Collectors.toList());
        if(personNr > 0) pageList.add(new Page(pageList.size() + 1,"professionisti","","staff",""));
        if(postNr > 0) pageList.add(new Page(pageList.size() + 1,"blog","","blog",""));
        log.info(pageList.toString());
        return pageList;
    }
    public List<Page> getMenuChildren(){
        List<Page> pageList = pageRepository.findAll().stream().filter(e -> !(e.getParent().equals(""))).collect(Collectors.toList());
        log.info(pageList.toString());
        return pageList;
    }
    public Contact getContacts(){
            List<Contact> contact = contactRepository.findAll();
            if (contact.isEmpty()) return new Contact();
            return contact.get(0);
    }
    public List<Home> getHomeSections(){return homeRepository.findAll();}
}
