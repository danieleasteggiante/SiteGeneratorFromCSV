package adr.gend.AdrAppProject.service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingUtility {
    private String string;

    public ParsingUtility(String string) {
        this.string = string;
    }

    public String formatPage() {
        return parseImages()
                .parseBold()
                .parseAccapo()
                .parseLink()
                .getString();
    }

    public String getString() {
        return string;
    }

    public ParsingUtility parseAccapo() {
        Matcher imagesMatcher = Pattern.compile("(<a![^<>]*>)")
                .matcher(string);
        while (imagesMatcher.find()) {
            String el = imagesMatcher.group();
            String res = el.replace("<a!>","<br>");
            string = string.replace(el,res);
        }
        return this;
    }

    public ParsingUtility parseBold() {
        Matcher imagesMatcher = Pattern.compile("(<b!_[^<>]*>)")
                .matcher(string);
        while (imagesMatcher.find()) {
            String el = imagesMatcher.group();
            String res = el
                    .replace(">","</strong>")
                    .replace("<b!_","<strong>");
            string = string.replace(el,res);
        }
        return this;
    }

    public ParsingUtility parseLink() {
        Matcher imagesMatcher = Pattern.compile("(<l!_[^<>]*>)")
                .matcher(string);
        while (imagesMatcher.find()) {
            String el = imagesMatcher.group();
            String link = getElementstFromLinkMetatag(el).get(0);
            String text = getElementstFromLinkMetatag(el).get(1);
            string = string.replace(el,"<a href='" + link +"' target='_blank'>" + text + "</a>");
        }
        return this;
    }

    private static List<String> getElementstFromLinkMetatag(String el) {
        return Arrays.stream(el
                .replace("<l!_","")
                .replace(">","")
                .split(",")).toList();
    }

    public ParsingUtility parseImages() {
        Matcher imagesMatcher = Pattern.compile("(<i!_[^<>]*>)")
                .matcher(string);
        while (imagesMatcher.find()) {
            String el = imagesMatcher.group();
            StringBuilder html = new StringBuilder();
            html.append("<div class='row'>");
            List<String> images = getImagesFromString(el);
            images.forEach(i->{
                html.append("<div class='col-"
                        + getColumnSize(images.size())
                        + "' /> <img class='img-fluid m-2' src='/get-image/"
                        + i +"'/></div>");
            });
            html.append("</div>");
            string = string.replace(el,html);
        }
        return this;
    }

    public static String getColumnSize(int size) {
        int maxSize = 12;
        return String.valueOf(maxSize/size);
    }

    public static List<String> getImagesFromString(String el) {
        return Arrays.stream(el
                .replace("<i!_","")
                .replace(">","")
                .split(",")).toList();
    }
}
