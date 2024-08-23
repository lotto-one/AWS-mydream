package kr.ict.mydream.personal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ict.mydream.vo.MemberVO;

@Service
public class PersonalService {

    @Autowired
    private PersonalDao personalDao;

    public void makeUpUpdate(MemberVO vo) {
        personalDao.makeUpUpdate(vo);
    }

    public void seasonUpdate(MemberVO vo) {
        personalDao.seasonUpdate(vo);
    }

    public String getgender(MemberVO vo) {
        return personalDao.getgender(vo.getMemno());
    }
}
