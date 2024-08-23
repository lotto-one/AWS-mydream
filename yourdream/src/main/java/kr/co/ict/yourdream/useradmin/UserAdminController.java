package kr.co.ict.yourdream.useradmin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/memadmin")
public class UserAdminController {
    
    @Autowired
    private UserAdminService userAdminService;

    @GetMapping("/with-consultants")
    public List<MemberListDTO> getAllMembersWithConsultants() {
        return userAdminService.getAllMembersWithConsultants();
    }

    @PutMapping("/user-update/{id}")
    public ResponseEntity<Void> updateMember(
        @PathVariable("id") Integer id,
        @RequestBody MemberUpdateDTO memberUpdateDTO) {
        
        userAdminService.updateMember(id, memberUpdateDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/del/{memno}")
    public ResponseEntity<Void> deleteMember(@PathVariable("memno") int memno) {
        userAdminService.deleteMemberByMemno(memno);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
