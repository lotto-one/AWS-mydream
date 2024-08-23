package kr.co.ict.yourdream.login.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Service
public class ConsultLoginService {
    
    @Autowired
    private ConsultLoginRepository consultLoginRepository;

    @Autowired
    private CnsCareerRepositoryBok cnsCareerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    // 비밀번호 암호화
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    // 회원 등록
    public void registerConsultant(ConsultVO99 consul) {
        consultLoginRepository.save(consul);
    }

    // 이메일로 회원 조회
    public ConsultVO99 findByEmail(String email) {
        return consultLoginRepository.findByEmail(email);
    }

    // 비밀번호 검증
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public void updatePassword(Integer cnsno, String rawPassword) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        ConsultVO99 consultant = consultLoginRepository.findById(cnsno).orElseThrow(() -> new RuntimeException("Consultant not found"));
        consultant.setPassword(encodedPassword);
        consultLoginRepository.save(consultant);
    }

    @Transactional
public void registerConsultant2(ConsultDTO consultDTO) {
    // ConsultVO99 엔티티 생성 및 기본값 설정
    ConsultVO99 consultVO99 = new ConsultVO99();
    consultVO99.setName(consultDTO.getName());
    consultVO99.setGendercd(consultDTO.getGendercd());
    consultVO99.setCategcd(consultDTO.getCategcd());
    consultVO99.setEmail(consultDTO.getEmail());
    consultVO99.setPhonenum(consultDTO.getPhonenum());
    consultVO99.setBirthymd(consultDTO.getBirthymd());
    consultVO99.setIntroduce(consultDTO.getIntroduce());
    consultVO99.setImgname(consultDTO.getImgname());
    consultVO99.setCnscareer(consultDTO.getCnscareer());
    consultVO99.setCnsproject(consultDTO.getCnsproject());

    // 기본값 비밀번호를 인코딩하여 설정
    String defaultPassword = "11"; // 기본값 비밀번호
    consultVO99.setPassword(encodePassword(defaultPassword));
    consultVO99.setRolecd(consultDTO.getRolecd());

    // ConsultVO99 엔티티 저장
    ConsultVO99 savedConsult = consultLoginRepository.save(consultVO99);

    // CnsCareerVO99 엔티티 생성 및 저장
    for (CareerDTObok careerDTO : consultDTO.getCnscareer_vo()) {
        CnsCareerVO99 cnsCareerVO99 = new CnsCareerVO99();
        cnsCareerVO99.setCnsno(savedConsult.getCnsno());
        cnsCareerVO99.setTerm(careerDTO.getTerm() != null ? careerDTO.getTerm() : ""); // 기본값 설정
        cnsCareerVO99.setContent(careerDTO.getContent() != null ? careerDTO.getContent() : ""); // 기본값 설정
        cnsCareerVO99.setDetail(careerDTO.getDetail() != null ? careerDTO.getDetail() : ""); // 기본값 설정
        cnsCareerVO99.setCareerdiv(careerDTO.getCareerdiv() != null ? careerDTO.getCareerdiv() : ""); // 기본값 설정

        // seqno는 자동 계산
        Integer maxSeqno = (Integer) entityManager.createQuery(
            "SELECT COALESCE(MAX(c.seqno), 0) FROM CnsCareerVO99 c WHERE c.cnsno = :cnsno")
            .setParameter("cnsno", savedConsult.getCnsno())
            .getSingleResult();
        cnsCareerVO99.setSeqno(maxSeqno + 1);

        // CnsCareerVO99 엔티티 저장
        cnsCareerRepository.save(cnsCareerVO99);
    }
}

    
}
