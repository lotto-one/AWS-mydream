package kr.co.ict.yourdream.login.api;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CnsCareerId implements Serializable {
    private int cnsno; // 컨설턴트 번호
    private int seqno; // 시퀀스 번호
}
