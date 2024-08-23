package kr.co.ict.yourdream.adminConsultant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import kr.co.ict.yourdream.consultantProfile.ConsultVO;

import java.util.List;

@RestController
@RequestMapping("/admin/adminConsult")
public class AdminConsultController {

    @Autowired
    private AdminConsultService adminConsultService;

    // 모든 컨설턴트 프로필 조회
    @GetMapping
    public List<ConsultVO> getAllConsultProfiles() {
        return adminConsultService.getAllConsultProfiles();
    }

    // 새 컨설턴트 프로필 생성(컨설턴트 추가)
    @PostMapping("/addConsultant")
    public ResponseEntity<ConsultVO> createConsultProfile(@RequestBody ConsultVO consultVO) {
        ConsultVO createdProfile = adminConsultService.createConsultProfile(consultVO);
        return ResponseEntity.ok(createdProfile);
    }

    // 컨설턴트 프로필 삭제
    @DeleteMapping("/{cnsno}/delConsultant")
    public ResponseEntity<Void> deleteConsultProfile(@PathVariable("cnsno") int cnsno) {
        boolean deleted = adminConsultService.deleteConsultProfile(cnsno);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // 컨설턴트 프로필 정보 업데이트
    @PostMapping("/{cnsno}/updateConsultant")
    public ResponseEntity<ConsultVO> updateConsultProfile(@PathVariable("cnsno") int cnsno,
            @RequestBody ConsultVO consultVO) {
        try {
            ConsultVO updatedProfileVO = adminConsultService.updateConsultProfile(cnsno, consultVO);
            return ResponseEntity.ok(updatedProfileVO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
