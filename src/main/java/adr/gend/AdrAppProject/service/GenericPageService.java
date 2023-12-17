package adr.gend.AdrAppProject.service;

import adr.gend.AdrAppProject.entity.Page;
import adr.gend.AdrAppProject.entity.Person;
import adr.gend.AdrAppProject.entity.Post;
import adr.gend.AdrAppProject.repository.PageRepository;
import adr.gend.AdrAppProject.repository.PersonRepository;
import adr.gend.AdrAppProject.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenericPageService {
    @Autowired
    PageRepository pageRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    PostRepository postRepository;
    public Page getGenericPage(String link){
        return pageRepository.findByLink(link);
    }
    public List<Person> getStaffPage() {
        return personRepository.findAll();
    }

    public List<Post> getBlogContent() { return postRepository.findAll();}

    public Page getPostFromSlug(String slug) {
       return postRepository.findByLink(slug)
               .map(value ->
                       new Page(value.getId(),
                               value.getTitle(),
				"",
                               value.getLink(),
                               value.getContent()))
               .orElse(null);
    }
}
