package kr.co.ict.yourdream.vo;

import java.io.Serializable;
import java.util.Objects;

public class IntResVOId implements Serializable {
    private int intno;
    private int memno;

    // 기본 생성자
    public IntResVOId() {
    }

    // 생성자
    public IntResVOId(int intno, int memno) {
        this.intno = intno;
        this.memno = memno;
    }

    // equals와 hashCode 구현
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        IntResVOId that = (IntResVOId) o;
        return intno == that.intno && memno == that.memno;
    }

    @Override
    public int hashCode() {
        return Objects.hash(intno, memno);
    }
}