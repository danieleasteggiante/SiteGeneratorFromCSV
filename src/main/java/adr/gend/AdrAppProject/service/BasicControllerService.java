package adr.gend.AdrAppProject.service;


import adr.gend.AdrAppProject.entity.Contact;
import adr.gend.AdrAppProject.entity.Image;
import adr.gend.AdrAppProject.entity.Post;
import adr.gend.AdrAppProject.repository.*;
import adr.gend.AdrAppProject.service.DTO.ImageDTO;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static adr.gend.AdrAppProject.configuration.Constant.RESOURCE_PATH;
import static adr.gend.AdrAppProject.configuration.Constant.UPLOAD_PATH;

@Slf4j
@Service
@Transactional
public class BasicControllerService {
    @Autowired
    @Qualifier("page")
    CSVParserInterface csvParserPage;
    @Autowired
    @Qualifier("home")
    CSVParserInterface csvParserHome;
    @Autowired
    @Qualifier("person")
    CSVParserInterface csvParserPerson;
    @Autowired
    @Qualifier("blog")
    CSVParserInterface csvParserPost;
    @Autowired
    PageRepository pageRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    HomeRepository homeRepository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostCRUDRepository postCRUDRepository;
    @Autowired
    ImageCRUDRepository imageCRUDRepository;
    public String savePageFileFromForm(MultipartFile file) throws IOException, CsvValidationException {
        if (file.isEmpty()) return "File Pages non inserito";
        Path fileNameAndPath = Paths.get(UPLOAD_PATH, file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        File f = new File(fileNameAndPath.toUri());
        if(!f.exists()) throw new RuntimeException("File uploaded not exist");
        String result= "";

        pageRepository.deleteAll();
        if(pageRepository.saveAll(csvParserPage.parse(f).getValue()).isEmpty()) result = "Errore in fase di salvataggio PAGES, il file è impostato correttamente o forse è vuoto?";
        return result;
    }

    private void createFolderIfNotExist(String ...folders) {
        Arrays.stream(folders).forEach(f->{
            File directory = new File(f);
            log.info(directory.getAbsolutePath());
            if (!directory.exists()) directory.mkdir();
        });
    }

    public String savePersonFileFromForm(MultipartFile file) throws IOException, CsvValidationException {
        if(file.isEmpty()) return "File Person non inserito";
        createFolderIfNotExist(UPLOAD_PATH, RESOURCE_PATH.toString());
        Path fileNameAndPath = Paths.get(UPLOAD_PATH, file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        File f = new File(fileNameAndPath.toUri());
        String result = "";

        if(!f.exists()) throw new RuntimeException("File uploaded not exist");

        personRepository.deleteAll();
        if(personRepository.saveAll(csvParserPerson.parse(f).getValue()).isEmpty()) result = "Errore in fase di salvataggio STAFF, il file è impostato correttamente o forse è vuoto?";
        return result;
    }

    public String saveHomeContentFromForm(MultipartFile file) throws IOException, CsvValidationException {
        if(file.isEmpty()) return "File Home non inserito";

        Path fileNameAndPath = Paths.get(UPLOAD_PATH, file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        File f = new File(fileNameAndPath.toUri());

        if(!f.exists()) throw new RuntimeException("File uploaded not exist");
        String result = "";

        homeRepository.deleteAll();
        if(homeRepository.saveAll(csvParserHome.parse(f).getValue()).isEmpty()) result = "Errore in fase di salvataggio HOME, il file è impostato correttamente o forse è vuoto?";
        return result;
    }

    public String savePostFromForm(MultipartFile file) throws IOException, CsvValidationException {
        if(file.isEmpty()) return "File Post non inserito";

        Path fileNameAndPath = Paths.get(UPLOAD_PATH, file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        File f = new File(fileNameAndPath.toUri());

        if(!f.exists()) throw new RuntimeException("File uploaded not exist");
        String result = "";

        List<Post> postList = csvParserPost.parse(f).getValue();
        try{
        postList.stream()
                .filter(e->!postRepository.existsById(e.getId()))
                .forEach(e->postRepository.save(e));
        }
        catch (Exception e){
            result = "Errore durante il salvataggio dei post, forse c'è qualche errore?";
        }

        return result;
    }
    /* TODO:
        - sistemare caricamento immagini linux
        - https e controllo per PWA
    */

    public void saveImages(MultipartFile multipartFile) throws IOException, URISyntaxException {
        createFolderIfNotExist(UPLOAD_PATH, RESOURCE_PATH.toString());
        if(multipartFile.isEmpty()) return;
        Path filePath = Paths.get(RESOURCE_PATH + "/"+multipartFile.getOriginalFilename());
        Files.write(filePath, multipartFile.getBytes());
        Image image = Image.builder().name(multipartFile.getOriginalFilename())
                .link("/get-image/" +  multipartFile.getOriginalFilename())
                .path(filePath.toString()).build();
        imageRepository.save(image);
    }

    public String saveContactInfo(String phone, String address, String email)  {
        if (phone.equals("") && address.equals("") && email.equals("")) return "I contatti non sono stati inseriti";

        phone = phone.replace("<a!>","<br>");
        address = address.replace("<a!>","<br>");
        email = email.replace("<a!>","<br>");

        contactRepository.deleteAll();
        contactRepository.save(new Contact(1,address, email,phone));
        return "";
    }

    public String deleteImage(ImageDTO image) throws IOException {
        Path path = Paths.get(RESOURCE_PATH.toString() ,image.getImage());
        File imageToDelete = new File(path.toString());
        imageCRUDRepository.deleteByName(image.getImage());
        if(imageToDelete.delete()) return "deleted";
        return "error while deleting image";
    }

    public List<Image> getImageList() {
        return imageRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public void deletePost(String link) {
        Optional<Post> post = postRepository.findByLink(link);
        post.ifPresent(p->postCRUDRepository.deleteByLink(p.getLink()));
    }
}

