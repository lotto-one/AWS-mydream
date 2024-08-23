package kr.co.ict.yourdream.vo;

import java.io.Serializable;
import java.util.Objects;

public class MemberConsultVOId implements Serializable {
    private int cnsno;
    private int memno;

    // 기본 생성자
    public MemberConsultVOId() {
    }

    // 생성자
    public MemberConsultVOId(int cnsno, int memno) {
        this.cnsno = cnsno;
        this.memno = memno;
    }

    // equals와 hashCode 구현
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MemberConsultVOId that = (MemberConsultVOId) o;
        return cnsno == that.cnsno && memno == that.memno;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cnsno, memno);
    }
}