package kr.co.ict.yourdream.userdash;

import java.io.Serializable;
import java.util.Objects;

public class MemberConsultIdBok implements Serializable {

    private int cnsno;
    private int memno;

    public MemberConsultIdBok() {}

    public MemberConsultIdBok(int cnsno, int memno) {
        this.cnsno = cnsno;
        this.memno = memno;
    }

    // Getters, Setters, hashCode, and equals methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberConsultIdBok that = (MemberConsultIdBok) o;
        return cnsno == that.cnsno && memno == that.memno;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cnsno, memno);
    }
    
}
