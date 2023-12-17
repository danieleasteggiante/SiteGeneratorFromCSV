package adr.gend.AdrAppProject.controller;

import adr.gend.AdrAppProject.entity.*;
import adr.gend.AdrAppProject.service.BasicControllerService;
import adr.gend.AdrAppProject.service.CSVParserInterface;
import adr.gend.AdrAppProject.service.DTO.ImageDTO;
import adr.gend.AdrAppProject.service.GenericPageService;
import adr.gend.AdrAppProject.service.HomeService;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.net.MulticastSocket;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static adr.gend.AdrAppProject.configuration.Constant.RESOURCE_PATH;

@Controller
@Data
@Slf4j
public class BasicController {
    Logger logger = LoggerFactory.getLogger(BasicControllerService.class);
    @Autowired
    CSVParserInterface csvParserPerson;
    @Autowired
    BasicControllerService basicControllerService;
    @Autowired
    HomeService homeService;
    @Autowired
    GenericPageService genericPageService;
    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/")
    public String home(Model model, HttpServletResponse response){
        List<Home> homeContent = homeService.getHomeSections();
        homeContent.forEach(item->{
            model.addAttribute(item.getSectionname(),item.getSectioncontent());
        });

        return "index";
    }
    @GetMapping("/page/{name}")
    public String getPage(@PathVariable String name, Model model) {
        Page page = genericPageService.getGenericPage(name);
        if(page == null) return "index";
        model.addAttribute("page",page);
        return "generic";
    }
    @GetMapping("/page/staff")
    public String getStaff(Model model) {
        List<Person> people = genericPageService.getStaffPage();
        model.addAttribute("staff",people);
        return "staff";
    }

    @GetMapping("/page/blog")
    public String getBlog(Model model) {
        List<Post> posts = genericPageService.getBlogContent();
        model.addAttribute("posts",posts);
        return "blog";
    }
    @GetMapping("/page/blog/{slug}")
    public String getPost(@PathVariable String slug,Model model) {
        Page page = genericPageService.getPostFromSlug(slug); //Return page from post, because generic template needs fields from page entity
        if(page == null) return "index";
        model.addAttribute("page",page);
        return "generic";
    }
    @GetMapping("/page/admin-area")
    public String getAdminArea(Model model) {
        return "admin_area";
    }
    @ModelAttribute(name = "menuparent")
    public List<Page> getMenuParent() {
            return homeService.getMenuParent();
    }
    @ModelAttribute(name = "menuchildren")
    public List<Page> getMenuChildren() {
        return homeService.getMenuChildren();
    }
    @ModelAttribute(name = "contacts")
    public Contact getContacts() {
        return homeService.getContacts();
    }
    @GetMapping("/page/upload-csv")
    public String displayUploadForm() {
        return "upload_form";
    }
    @PostMapping("/page/upload")
    public String uploadImage(Model model,
                              @RequestParam("person") MultipartFile persons,
                              @RequestParam("page") MultipartFile pages,
                              @RequestParam("home") MultipartFile home,
                              @RequestParam("blog") MultipartFile blog,
                              @RequestParam("address") String address,
                              @RequestParam("email") String email,
                              @RequestParam("phone") String phone
                              ) throws IOException, CsvValidationException {
        StringBuilder result = new StringBuilder();
        result.append(basicControllerService.savePersonFileFromForm(persons)).append(" ");
        result.append(basicControllerService.savePageFileFromForm(pages)).append(" ");
        result.append(basicControllerService.saveHomeContentFromForm(home)).append(" ");
        result.append(basicControllerService.savePostFromForm(blog)).append(" ");
        result.append(basicControllerService.saveContactInfo(phone,address,email));
        model.addAttribute("msg", result.isEmpty() ? "Sito aggiornato" : result);
        return "upload_form";
    }

    @GetMapping("/page/image-list")
    public List<String> getListOfFiles() throws IOException {
        return basicControllerService.getImageList()
                .stream()
                .map(Image::getName)
                .collect(Collectors.toList());
    }
    @GetMapping("/get-image/{name}")
    public @ResponseBody ResponseEntity<byte[]> getImage(@PathVariable(name = "name")String name) throws IOException, URISyntaxException {
        Path filePath = Paths.get(RESOURCE_PATH + "/" +name);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(getMediatype(name));
        if(filePath.toFile().exists()) return new ResponseEntity<>(Files.readAllBytes(filePath), headers, HttpStatus.OK);
        return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
    }

    private MediaType getMediatype(String fullName) {
        enum ImageType{
            IMAGE_JPEG("jpg","jpeg","ico"),
            IMAGE_PNG("png"),
            IMAGE_GIF("gif");

            String[] extension;
            ImageType(String... extension) {
                this.extension = extension;
            }

            public static ImageType fromExtension(String extension) {
                AtomicReference<ImageType> result = new AtomicReference<>();
                Arrays.stream(ImageType.values()).forEach(e -> {
                    if(Arrays.asList(e.extension).contains(extension))
                        result.set(e);
                });
                return result.get();
            }
        }
        String extension = fullName.split("\\.")[1];
        String validMimeType = convertInValidMimeType(ImageType.fromExtension(extension).toString());
        return MediaType.valueOf(validMimeType);
    }

    private String convertInValidMimeType(String imageType) {
        return imageType.toLowerCase().replace("_","/");
    }

    @GetMapping("/page/upload-images-form")
    public String displayUlpoadImageForm(Model model) throws IOException {
        model.addAttribute("files", getListOfFiles());
        return "upload_image_form";
    }
    @PostMapping("/page/upload-images")
    public RedirectView uploadImage(Model model, @RequestParam("image") MultipartFile[] f) throws IOException, URISyntaxException {
        for (MultipartFile image: f) {
          basicControllerService.saveImages(image);
        }
        model.addAttribute("files", getListOfFiles());
        model.addAttribute("msg","Immagini aggiunte");

        return new RedirectView("/page/upload-images-form");
    }

    @ResponseBody
    @RequestMapping(value="/page/delete-image", consumes={ "application/json"}, produces = { "application/json" })
    public String deleteImage(@RequestBody ImageDTO image) throws IOException {
        return  "{\"result\": \"" +  basicControllerService.deleteImage(image) + "\" }";
    }

    @GetMapping(value="/page/delete-post/{link}")
    public String deletePost(@PathVariable(name = "link")String link)  {
        basicControllerService.deletePost(link);
        return "admin_area";
    }

}
