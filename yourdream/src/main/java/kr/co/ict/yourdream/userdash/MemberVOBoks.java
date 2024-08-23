package kr.co.ict.yourdream.userdash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tbmember")
public class MemberVOBoks {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_memno")
    @SequenceGenerator(name = "seq_memno", sequenceName = "seq_memno", allocationSize = 1)
    @Column
    private int memno;

    @Column
    private String name;
    
    @Column
    private String gendercd;
    
    @Column
    private String nickname;

    @Column
    private String categcd;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String phonenum;

    @Column
    private String birthymd;

    @Column
    private String loccd;

    @Column
    private String seasoncd;

    @Column
    private String entymd;

    @Column
    private String quitymd;

    @Column
    private String imgname;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date credt;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date upddt;

    @Column
    private String rolecd;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IntResVOBok> interests;
}
