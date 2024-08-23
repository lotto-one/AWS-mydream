package kr.co.ict.yourdream.userdash;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tblogin")
public class LoginVOBok {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_logno")
    @SequenceGenerator(name = "seq_logno", sequenceName = "seq_logno", allocationSize = 1)
    @Column
    private int logno;
    @Column
    private int memno;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date logindt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date logoutdt;
    
}
