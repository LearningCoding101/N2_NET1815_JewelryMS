package com.project.JewelryMS.model.Image;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImgbbResponse {

    private Data data;
    private boolean success;
    private int status;

    // Getters and setters
    @lombok.Data
    public static class Data {
        private String id;
        private String title;
        @JsonProperty("url_viewer")
        private String urlViewer;
        private String url;
        @JsonProperty("display_url")
        private String displayUrl;
        private int width;
        private int height;
        private int size;
        private long time;
        private int expiration;
        private Image image;
        private Thumb thumb;
        private Medium medium;
        @JsonProperty("delete_url")
        private String deleteUrl;

        // Getters and setters
        @lombok.Data
        public static class Image {
            private String filename;
            private String name;
            private String mime;
            private String extension;
            private String url;

            // Getters and setters
        }
        @lombok.Data
        public static class Thumb {
            private String filename;
            private String name;
            private String mime;
            private String extension;
            private String url;

            // Getters and setters
        }
        @lombok.Data
        public static class Medium {
            private String filename;
            private String name;
            private String mime;
            private String extension;
            private String url;

            // Getters and setters
        }

        // Getters and setters
    }
}
