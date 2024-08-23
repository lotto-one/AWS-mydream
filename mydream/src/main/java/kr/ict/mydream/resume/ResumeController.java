package kr.ict.mydream.resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.ict.mydream.vo.CareerVO;
import kr.ict.mydream.vo.EducationVO;
import kr.ict.mydream.vo.ResumeVO;
import kr.ict.mydream.vo.SelfIntroVO;

@RestController
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private ObjectMapper objectMapper;

    // private final String filePath;

    private final String filePath =  Paths.get("").toAbsolutePath().toString() + "/uploads";

    @PostMapping("/resumeList")
    public ResponseEntity<?> resumeList(Model model, @RequestBody Map<String, Integer> num) {
        int memno = num.get("memno");
        List list = new ArrayList<ResumeVO>();
        list = resumeService.resumeList(memno);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/resumeDetail")
    public ResponseEntity<?> resumeDetail(@RequestParam("num") int num) {
        List detail = new ArrayList<Map<String, Object>>();
        detail.add(resumeService.resumeDetail(num));
        detail.add(resumeService.resumeDetailEdu(num));
        detail.add(resumeService.resumeDetailCar(num));
        detail.add(resumeService.resumeDetailSelf(num));
        return ResponseEntity.ok().body(detail);
    }

    @PostMapping("/resumeAdd")
    public ResponseEntity<?> resumeAdd(@RequestBody Map<String, Object> data) {
        ResumeVO resumeVO = new ResumeVO();
        EducationVO educationVO = new EducationVO();
        CareerVO careerVO = new CareerVO();
        SelfIntroVO selfIntroVO = new SelfIntroVO();

        // 기본 인적사항 저장
        resumeVO = objectMapper.convertValue(data.get("basic"), ResumeVO.class);
        resumeVO.setMemno((int) data.get("memno"));
        resumeService.resumeAdd(resumeVO);
        System.out.println("인적 저장 완료");
        int rsmno = resumeVO.getRsmno();

        // 학력사항 저장
        List<Map<String, Object>> education = (List<Map<String, Object>>) data.get("education");
        System.out.println(education);
        List<Map<String, Object>> education_res = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> e : education) {
            Map<String, Object> res = new HashMap<>(e);
            res.put("rsmno", rsmno);
            res.remove("td1");
            education_res.add(res);
        }
        for (Map<String, Object> e : education_res) {
            int seqno = (int) educationVO.getSeqno();
            e.put("seqno", seqno);
            System.out.println(e);
            resumeService.resumeAddEdu(e);
        }

        // 경력 저장
        List<Map<String, Object>> career = (List<Map<String, Object>>) data.get("career");
        System.out.println(career);
        List<Map<String, Object>> career_res = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> e : career) {
            Map<String, Object> res = new HashMap<>(e);
            res.put("rsmno", rsmno);
            res.remove("td1");
            career_res.add(res);
        }
        for (Map<String, Object> e : career_res) {
            int seqno = (int) careerVO.getSeqno();
            e.put("seqno", seqno);
            resumeService.resumeAddCar(e);
        }

        // 자소서 저장
        List<Map<String, Object>> selfintro = (List<Map<String, Object>>) data.get("intro");
        System.out.println(selfintro);
        List<Map<String, Object>> selfintro_res = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> e : selfintro) {
            Map<String, Object> res = new HashMap<>(e);
            res.put("rsmno", rsmno);
            res.remove("td1");
            selfintro_res.add(res);
        }
        for (Map<String, Object> e : selfintro_res) {
            int seqno = (int) selfIntroVO.getSeqno();
            e.put("seqno", seqno);
            resumeService.resumeAddSelf(e);
            System.out.println("완료");
        }

        System.out.println(rsmno);
        return ResponseEntity.ok().body(rsmno);
    }

    @PostMapping("/resumeImgUp")
    public void resumeImgUp(@RequestParam("file") MultipartFile mf, @RequestParam Map<String, Object> data,
            HttpServletRequest request) {

        if (mf == null || mf.isEmpty()) {
            System.out.println("파일이 없음");
        }
        String oriFn = mf.getOriginalFilename();
        UUID uuid = UUID.randomUUID();
        String newFn = uuid.toString()+ oriFn;;
        StringBuffer path = new StringBuffer();
        path.append(filePath).append("\\");
        path.append(newFn);
        System.out.println("FullPath :" + path);
        File f = new File(path.toString());
        try {
            mf.transferTo(f);
            data.put("imgname", newFn);
            resumeService.resumeImgUp(data);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/resumeUpdate")
    public void resumeUpdate(@RequestParam("num") int num, @RequestBody Map<String, Object> data) {
        System.out.println("==========================================");
        System.out.println(data.get("basic"));
        System.out.println(data.get("education"));
        System.out.println(data.get("career"));
        System.out.println(data.get("intro"));
        System.out.println("==========================================");
        ResumeVO resumeVO = new ResumeVO();
        EducationVO educationVO = new EducationVO();
        CareerVO careerVO = new CareerVO();
        SelfIntroVO selfIntroVO = new SelfIntroVO();
        int rsmno = num;

        // 인적사항 수정
        resumeService.resumeUpdate((Map<String, Object>) data.get("basic"));

        // 학력 수정
        if (((List<Map<String, Object>>) data.get("education")).size() != 0) {
            List<Map<String, Object>> education = (List<Map<String, Object>>) data.get("education");
            for (Map<String, Object> e : education) {
                if (e.get("seqno") != null) {
                    resumeService.resumeUpdataEdu(e);
                } else {
                    Map<String, Object> education_add = new HashMap<>(e);
                    int seqno = (int) educationVO.getSeqno();
                    education_add.put("rsmno", rsmno);
                    education_add.put("seqno", seqno);
                    education_add.remove("td1");
                    System.out.println(education_add);

                    resumeService.resumeAddEdu(education_add);
                    System.out.println("학력 수정");
                }
            }
        } else {
            System.out.println("학력 변동사항 없음");
        }

        // 경력 수정
        if (((List<Map<String, Object>>) data.get("career")).size() != 0) {
            List<Map<String, Object>> career = (List<Map<String, Object>>) data.get("career");
            for (Map<String, Object> e : career) {
                if (e.get("seqno") != null) {
                    resumeService.resumeUpdataCar(e);
                } else {
                    Map<String, Object> career_add = new HashMap<>(e);
                    int seqno = (int) careerVO.getSeqno();
                    career_add.put("rsmno", rsmno);
                    career_add.put("seqno", seqno);
                    career_add.remove("td1");
                    System.out.println(career_add);

                    resumeService.resumeAddCar(career_add);
                    System.out.println("경력 수정");
                }
            }
        } else {
            System.out.println("경력 변동사항 없음");
        }

        // 자소서 수정
        if (((List<Map<String, Object>>) data.get("intro")).size() != 0) {
            List<Map<String, Object>> selfintro = (List<Map<String, Object>>) data.get("intro");
            for (Map<String, Object> e : selfintro) {
                if (e.get("seqno") != null) {
                    resumeService.resumeUpdataSelf(e);
                } else {
                    Map<String, Object> selfintro_add = new HashMap<>(e);
                    int seqno = (int) careerVO.getSeqno();
                    selfintro_add.put("rsmno", rsmno);
                    selfintro_add.put("seqno", seqno);
                    selfintro_add.remove("td1");
                    System.out.println(selfintro_add);

                    resumeService.resumeAddSelf(selfintro_add);
                    System.out.println("자소서 수정");
                }
            }
        } else {
            System.out.println("자소서 변동사항 없음");
        }
    }

    @Transactional
    @PostMapping("/resumeDelete")
    public void resumeDelete(@RequestBody Map<String, Integer> data) {
        resumeService.resumeDeleteEdu(data);
        resumeService.resumeDeleteCar(data);
        resumeService.resumeDeleteSelf(data);
        resumeService.resumeDelete(data);
    }

    @PostMapping("/resumeDeleteEdu")
    public void resumeDeleteEdu(@RequestBody Map<String, Integer> data) {
        System.out.println("seqno:" + data.get("seqno"));
        System.out.println("rsmno:" + data.get("rsmno"));
        resumeService.resumeDeleteEdu(data);
    }

    @PostMapping("/resumeDeleteCar")
    public void resumeDeleteCar(@RequestBody Map<String, Integer> data) {
        System.out.println("seqno:" + data.get("seqno"));
        System.out.println("rsmno:" + data.get("rsmno"));
        resumeService.resumeDeleteCar(data);
    }

    @PostMapping("/resumeDeleteSelf")
    public void resumeDeleteSelf(@RequestBody Map<String, Integer> data) {
        System.out.println("seqno:" + data.get("seqno"));
        System.out.println("rsmno:" + data.get("rsmno"));
        resumeService.resumeDeleteSelf(data);
    }
}
