package kr.ict.mydream.personal;

import org.apache.ibatis.annotations.Mapper;

import kr.ict.mydream.vo.MemberVO;

@Mapper
public interface PersonalDao {

    public void makeUpUpdate(MemberVO vo);

    public void seasonUpdate(MemberVO vo);

    public String getgender(int memno);
}
