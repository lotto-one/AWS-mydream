package kr.co.ict.yourdream.consultantProfile;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.nio.file.StandardCopyOption;
import java.nio.file.Path;

@Service
public class ConsultProfileService {

    @Autowired
    private ConsultProfileRepository consultProfileRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 파일 업로드 경로 설정
    private final Path uploadDir = Paths.get("uploads");

    // 특정 ID로 컨설턴트 프로필을 기본 정보만 조회하는 메서드
    @Transactional(readOnly = true)
    public ConsultVO getBasicConsultProfileById(int cnsno) {
        return consultProfileRepository.findBasicProfileById(cnsno)
                .map(this::convertToVO)
                .orElseThrow(() -> new RuntimeException("프로필을 찾을 수 없습니다: " + cnsno));
    }

    // 특정 ID로 컨설턴트 프로필을 조회하는 메서드 (모든 경력 정보 포함)
    @Transactional(readOnly = true)
    public ConsultVO getConsultProfileById(int cnsno) {
        TypedQuery<ConsultProfile> query = entityManager.createQuery(
                "SELECT cp FROM ConsultProfile cp LEFT JOIN FETCH cp.cnscareerList WHERE cp.cnsno = :cnsno",
                ConsultProfile.class);
        query.setParameter("cnsno", cnsno);
        // query.setHint(QueryHints.HINT_READONLY, true);
        ConsultProfile profile = query.getSingleResult();
        return convertToVO(profile);
    }

    // 기존 컨설턴트 프로필을 업데이트하는 메서드
    @Transactional
    public ConsultVO updateConsultProfile(int cnsno, ConsultVO consultVO) {
        return consultProfileRepository.findById(cnsno)
                .map(existingProfile -> {
                    // 비밀번호가 존재하고 빈 값이 아닌 경우에만 인코딩
                    if (consultVO.getPassword() != null && !consultVO.getPassword().isEmpty()) {
                        String encodedPassword = passwordEncoder.encode(consultVO.getPassword());
                        existingProfile.setPassword(encodedPassword);
                    }
                    updateConsultProfileFields(existingProfile, consultVO);
                    updateCnsCareerList(existingProfile, consultVO.getCnscareerVO());
                    ConsultProfile updatedProfile = consultProfileRepository.save(existingProfile);
                    return convertToVO(updatedProfile);
                })
                .orElseThrow(() -> new RuntimeException("프로필을 찾을 수 없습니다: " + cnsno));
    }

    // 프로필 이미지 파일을 업로드하는 메서드
    @Transactional
    public String uploadProfileImage(MultipartFile mf) throws IOException {
        if (mf == null || mf.isEmpty()) {
            throw new IllegalArgumentException("파일이 전송되지 않았습니다.");
        }

        String filename = UUID.randomUUID().toString().substring(0, 8) + "_" + mf.getOriginalFilename();
        Path targetLocation = uploadDir.resolve(filename);

        try {
            Files.copy(mf.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File successfully saved: " + targetLocation.toString());
        } catch (IOException e) {
            throw new IOException("파일을 저장하는 데 실패했습니다: " + filename, e);
        }

        return filename;
    }

    // 프로필 이미지를 업데이트하는 메서드
    @Transactional
    public void updateProfileImage(int cnsno, String imgname) {
        consultProfileRepository.updateProfileImage(cnsno, imgname);
        System.out.println("프로필 이미지 업데이트 성공. cnsno: " + cnsno + ", imgname: " + imgname);
    }

    // 컨설턴트 프로필의 필드를 업데이트하는 내부 헬퍼 메서드
    // private void updateConsultProfileFields(ConsultProfile profile, ConsultVO vo)
    // {
    // BeanUtils.copyProperties(vo, profile, "cnsno", "cnscareerList");
    // }

    private void updateConsultProfileFields(ConsultProfile profile, ConsultVO vo) {
        // 비밀번호가 제공된 경우에만 인코딩 후 설정
        if (vo.getPassword() != null && !vo.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(vo.getPassword());
            profile.setPassword(encodedPassword);
        }

        // 비밀번호를 제외한 나머지 필드 업데이트
        BeanUtils.copyProperties(vo, profile, "password", "cnsno", "cnscareerList");
    }

    // 컨설턴트 프로필의 경력 리스트를 업데이트하는 내부 헬퍼 메서드
    private void updateCnsCareerList(ConsultProfile profile, List<CnsCareerVO> careerVOList) {
        profile.getCnscareerList().clear();
        if (careerVOList != null) {
            for (CnsCareerVO careerVO : careerVOList) {
                CnsCareer career = convertCnsCareerToEntity(careerVO);
                career.setConsultProfile(profile);
                profile.getCnscareerList().add(career);
            }
        }
    }

    // ConsultProfile 엔티티를 ConsultVO로 변환하는 내부 헬퍼 메서드
    private ConsultVO convertToVO(ConsultProfile profile) {
        ConsultVO vo = new ConsultVO();
        BeanUtils.copyProperties(profile, vo);
        vo.setCnscareerVO(profile.getCnscareerList().stream()
                .map(this::convertCnsCareerToVO)
                .collect(Collectors.toList()));
        return vo;
    }

    // CnsCareer 엔티티를 CnsCareerVO로 변환하는 내부 헬퍼 메서드
    private CnsCareerVO convertCnsCareerToVO(CnsCareer career) {
        CnsCareerVO vo = new CnsCareerVO();
        BeanUtils.copyProperties(career, vo);
        return vo;
    }

    // CnsCareerVO를 CnsCareer 엔티티로 변환하는 내부 헬퍼 메서드
    private CnsCareer convertCnsCareerToEntity(CnsCareerVO vo) {
        CnsCareer career = new CnsCareer();
        BeanUtils.copyProperties(vo, career);
        return career;
    }
}