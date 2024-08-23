package kr.ict.mydream.interview;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.ict.mydream.vo.IntDetailVO;
import kr.ict.mydream.vo.IntFeedbkVO;
import kr.ict.mydream.vo.IntResVO;

@Mapper
public interface InterviewDao {

    public String getName(int memno);

    public Integer getCnsno(int memno);

    public List<Map> getQuestion(Integer cnsno);

    public String getIntTypeName(int inttypecd);

    public void setIntRes(IntResVO intresvo);

    public void setIntDetail(List<IntDetailVO> indetailvoList);

    public void setIntFeedbk(IntFeedbkVO intfeedbkvo);

    public Map getResNameTime(int memno);
}
