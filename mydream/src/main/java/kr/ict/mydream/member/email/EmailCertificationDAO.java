package kr.ict.mydream.member.email;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailCertificationDAO {
    int countByEmail(String email);
}
