package kr.ict.mydream.myconsultant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ict.mydream.vo.ConsultVO;
import kr.ict.mydream.vo.MemberConsultVO;

@Service
public class MyConsultService {

    @Autowired
    private MyConsultDao myConsultDao;

    public MemberConsultVO findCon(int memno) {
        return myConsultDao.findCon(memno);
    }

    public ConsultVO getConsultDetails(int cnsno) {
        return myConsultDao.selectConsultWithCareer(cnsno);
    }
}
