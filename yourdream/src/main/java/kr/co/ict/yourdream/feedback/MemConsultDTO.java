package kr.co.ict.yourdream.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemConsultDTO {
    private Integer memno;

    private Integer cnsno;

    private String imgname;

    private String name;
}
