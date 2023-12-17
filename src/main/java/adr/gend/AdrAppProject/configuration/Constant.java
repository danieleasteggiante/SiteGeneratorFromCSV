package adr.gend.AdrAppProject.configuration;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public interface Constant {
    String UPLOAD_PATH = "/home/UPLOAD/";
    Path RESOURCE_PATH = Paths.get( UPLOAD_PATH + "images/");
}
