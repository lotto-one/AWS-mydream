package kr.co.ict.yourdream.consultantProfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CnsCareerPK implements Serializable {
    private int cnsno;
    private int seqno;
}
