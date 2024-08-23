package kr.ict.mydream.personal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.ict.mydream.vo.MemberVO;

@RestController
@RequestMapping("/personal")
public class PersonalController {

    // private final static String filePath = Paths.get("").toAbsolutePath().toString() + "/mydream/uploads";

    // private final Path filePath = Paths.get("uploads");
    private final String filePath = Paths.get("").toAbsolutePath().toString() + "/uploads";
    @Autowired
    private PersonalService personalService;

    @PostMapping("/makeUpImgSave")
    public String makeUpImgSave(@RequestParam("imgfile") MultipartFile mf) {
        System.out.println("after_imageSave왔니?");
        System.out.println("filePath : " + filePath);
        UUID uuid = UUID.randomUUID();
        // UUID 객체를 문자열로 변환
        String uuidStr = uuid.toString();
        if (mf == null || mf.isEmpty()) {
            throw new IllegalArgumentException("파일이 전송되지 않았습니다.");
        }
        String oriFn = mf.getOriginalFilename();
        String filename = uuidStr + oriFn;
        StringBuffer path = new StringBuffer();
        path.append(filePath).append("/");
        path.append(filename);
        System.out.println("FullPath :" + path);
        File f = new File(path.toString());
        try {
            mf.transferTo(f);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return filename;
    }

    @PostMapping("/makeUpImgupdate")
    public void makeUpImgupdate(@RequestBody MemberVO vo) {
        System.out.println(vo.getMemno());
        System.out.println(vo.getImgname());
        personalService.makeUpUpdate(vo);
        System.out.println("업데이트 완료");
    }

    @PostMapping("/seasonUpdate")
    public void seasonUpdate(@RequestBody MemberVO vo) {

        personalService.seasonUpdate(vo);
        System.out.println(vo.getSeasoncd() + "업데이트 완료");
    }

    @PostMapping("/genget")
    public String genderget(@RequestBody MemberVO vo) {

        String gender = personalService.getgender(vo);
        return gender;
    }
}
