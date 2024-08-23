package kr.co.ict.yourdream.useradmin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class UserAdminService {

    @Autowired
    private UserAdminRepository userAdminRepository;

    public List<MemberListDTO> getAllMembersWithConsultants() {
        return userAdminRepository.findAllMemberDetailsWithConsultName();
    }


    @Transactional
    public void updateMember(Integer id, MemberUpdateDTO memberUpdateDTO) {
        userAdminRepository.updateMember(
            id,
            memberUpdateDTO.getEmail(),
            memberUpdateDTO.getName(),
            memberUpdateDTO.getNickname(),
            memberUpdateDTO.getGendercd(),
            memberUpdateDTO.getCategcd(),
            memberUpdateDTO.getLoccd(),
            memberUpdateDTO.getPhonenum()
        );
    }

    @Transactional
    public void deleteMemberByMemno(int memno) {
        // 특정 memno를 가진 멤버 삭제
    userAdminRepository.deleteById(memno);
    }
}
