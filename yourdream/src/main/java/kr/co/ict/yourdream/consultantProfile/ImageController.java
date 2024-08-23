package kr.co.ict.yourdream.consultantProfile;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.nio.file.Files;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/uploads")
public class ImageController {
    /* 🐛🐛🐛 */
    private final Path uploadDir = Paths.get("uploads");

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable("filename") String filename) {
        try {
            Path file = uploadDir.resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(file);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                System.out.println("File not found: " + file.toString());
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("Error serving file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}