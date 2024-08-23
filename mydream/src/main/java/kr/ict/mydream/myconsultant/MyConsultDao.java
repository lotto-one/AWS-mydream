package kr.ict.mydream.myconsultant;

import org.apache.ibatis.annotations.Mapper;

import kr.ict.mydream.vo.ConsultVO;
import kr.ict.mydream.vo.MemberConsultVO;

@Mapper
public interface MyConsultDao {

    public MemberConsultVO findCon(int memno);

    ConsultVO selectConsultWithCareer(int cnsno);
}
