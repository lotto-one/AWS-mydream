package kr.co.ict.yourdream.adminConsultant;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.co.ict.yourdream.consultantProfile.CnsCareer;
import kr.co.ict.yourdream.consultantProfile.CnsCareerRepository;
import kr.co.ict.yourdream.consultantProfile.CnsCareerVO;
import kr.co.ict.yourdream.consultantProfile.ConsultProfile;
import kr.co.ict.yourdream.consultantProfile.ConsultVO;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminConsultService {

    @Autowired
    private AdminConsultRepository adminConsultRepository;

    @Autowired
    private CnsCareerRepository cnsCareerRepository;

    @Transactional(readOnly = true)
    public List<ConsultVO> getAllConsultProfiles() {
        return adminConsultRepository.findAllWithCareers().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ConsultVO createConsultProfile(ConsultVO consultVO) {
        ConsultProfile consultProfile = convertToEntity(consultVO);

        Date now = new Date();
        consultProfile.setCredt(now);
        consultProfile.setUpddt(now);

        ConsultProfile savedProfile = adminConsultRepository.save(consultProfile);

        // CnsCareer 엔티티들을 별도로 저장
        if (consultVO.getCnscareerVO() != null) {
            List<CnsCareer> careers = new ArrayList<>();
            for (CnsCareerVO careerVO : consultVO.getCnscareerVO()) {
                CnsCareer career = convertCnsCareerToEntity(careerVO);
                career.setConsultProfile(savedProfile);
                career.setCredt(now);
                career.setUpddt(now);
                careers.add(career);
            }
            cnsCareerRepository.saveAll(careers);
        }

        // 저장된 프로필을 다시 조회하여 연관 엔티티들을 함께 가져옴
        ConsultProfile refreshedProfile = adminConsultRepository.findByIdWithCareers(savedProfile.getCnsno())
                .orElseThrow(() -> new RuntimeException("Failed to retrieve saved profile"));

        return convertToVO(refreshedProfile);
    }

    @Transactional
    public boolean deleteConsultProfile(int cnsno) {
        if (adminConsultRepository.existsById(cnsno)) {
            adminConsultRepository.deleteById(cnsno);
            return true;
        }
        return false;
    }

    @Transactional
    public ConsultVO updateConsultProfile(int cnsno, ConsultVO consultVO) {
        return adminConsultRepository.findById(cnsno)
                .map(existingProfile -> {
                    updateConsultProfileFields(existingProfile, consultVO);

                    Date now = new Date();
                    existingProfile.setUpddt(now);

                    updateCnsCareerList(existingProfile, consultVO.getCnscareerVO());
                    ConsultProfile updatedProfile = adminConsultRepository.save(existingProfile);
                    return convertToVO(updatedProfile);
                })
                .orElseThrow(() -> new RuntimeException("프로필을 찾을 수 없습니다: " + cnsno));
    }

    private void updateConsultProfileFields(ConsultProfile profile, ConsultVO vo) {
        BeanUtils.copyProperties(vo, profile, "cnsno", "cnscareerList", "credt", "upddt");
    }

    private void updateCnsCareerList(ConsultProfile profile, List<CnsCareerVO> careerVOList) {
        // Clear existing careers
        profile.getCnscareerList().clear();

        if (careerVOList != null) {
            Date now = new Date();
            for (CnsCareerVO careerVO : careerVOList) {
                CnsCareer career = convertCnsCareerToEntity(careerVO);
                career.setConsultProfile(profile);
                career.setCredt(now);
                career.setUpddt(now);
                profile.getCnscareerList().add(career);
            }
        }
    }

    private ConsultVO convertToVO(ConsultProfile profile) {
        ConsultVO vo = new ConsultVO();
        BeanUtils.copyProperties(profile, vo);

        // LAZY 로딩된 cnscareerList 초기화
        if (profile.getCnscareerList() != null) {
            Hibernate.initialize(profile.getCnscareerList());
            vo.setCnscareerVO(profile.getCnscareerList().stream()
                    .map(this::convertCnsCareerToVO)
                    .collect(Collectors.toList()));
        } else {
            vo.setCnscareerVO(new ArrayList<>());
        }
        return vo;
    }

    private ConsultProfile convertToEntity(ConsultVO vo) {
        ConsultProfile profile = new ConsultProfile();
        BeanUtils.copyProperties(vo, profile, "cnscareerVO");
        return profile;
    }

    private CnsCareerVO convertCnsCareerToVO(CnsCareer career) {
        CnsCareerVO vo = new CnsCareerVO();
        BeanUtils.copyProperties(career, vo);
        return vo;
    }

    private CnsCareer convertCnsCareerToEntity(CnsCareerVO vo) {
        CnsCareer career = new CnsCareer();
        BeanUtils.copyProperties(vo, career, "consultProfile");
        return career;
    }
}
