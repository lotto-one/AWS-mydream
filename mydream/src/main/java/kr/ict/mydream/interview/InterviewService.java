package kr.ict.mydream.interview;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ict.mydream.vo.IntDetailVO;
import kr.ict.mydream.vo.IntFeedbkVO;
import kr.ict.mydream.vo.IntResVO;

@Service
public class InterviewService {

    @Autowired
    private InterviewDao interviewDao;

    public String getName(int memno) {
        return interviewDao.getName(memno);
    }

    public Integer getCnsno(int memno) {
        Integer cnsno = interviewDao.getCnsno(memno);
        System.out.println("멤버랑 연결된 컨설턴트번호 =>" + cnsno);
        return cnsno;
    }

    public String getCnsYnN(int memno) {
        Integer cnsno = getCnsno(memno);
        String cnsYnN;
        if (cnsno == null) {
            cnsYnN = "N";
        } else {
            cnsYnN = "Y";
        }
        System.out.println("컨설턴트 있나요?" + cnsYnN);
        return cnsYnN;
    }

    public List<Map> getQuestion(int memno) {
        List<Map> questionlist;
        Integer cnsno;
        try {
            cnsno = getCnsno(memno);
        } catch (Exception e) {
            cnsno = null;
        }

        questionlist = interviewDao.getQuestion(cnsno);
        System.out.println("service에서questionlist" + questionlist);

        return questionlist;
    }

    public String getIntTypeName(int inttypecd) {
        String inttypename = interviewDao.getIntTypeName(inttypecd);
        System.out.println("인성or직무" + inttypename);
        return inttypename;
    }

    public void setIntRes(IntResVO intresvo) {
        interviewDao.setIntRes(intresvo);
    }

    public void setIntDetail(List<IntDetailVO> indetailvoList) {
        interviewDao.setIntDetail(indetailvoList);
    }

    public void setIntFeedbk(IntFeedbkVO intfeedbkvo) {
        interviewDao.setIntFeedbk(intfeedbkvo);
    }

    public Map getResNameTime(int memno) {
        return interviewDao.getResNameTime(memno);
    }
}
