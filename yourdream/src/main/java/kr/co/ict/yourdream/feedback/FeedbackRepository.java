package kr.co.ict.yourdream.feedback;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import kr.co.ict.yourdream.vo.IntResVO;
import kr.co.ict.yourdream.vo.IntResVOId;

@Repository
@Transactional
public interface FeedbackRepository extends JpaRepository<IntResVO, IntResVOId> {

        @Query("SELECT new kr.co.ict.yourdream.feedback.FeedbackDTO(i.memno, i.intno, m.name, i.inttypecd, i.upddt) " +
                        "FROM IntResVO i JOIN i.memberVO m " +
                        "WHERE i.memno = :memno AND i.inttypecd = '2'")
        List<FeedbackDTO> findByMemnoAndInttypecd(@Param("memno") Integer memno);

        @Query("SELECT new kr.co.ict.yourdream.feedback.MemConsultDTO(m.memno, i.cnsno, m.imgname, m.name) FROM MemberConsultVO i join i.memberVO m WHERE i.memno=m.memno and i.cnsno=:cnsno AND i.useyn='Y' ORDER BY m.memno ASC")
        List<MemConsultDTO> findByMemnoAndUseyn(@Param("cnsno") Integer cnsno);

        @Query("SELECT new kr.co.ict.yourdream.feedback.FeedbackDetail_InfoDTO(a.name, b.credt) FROM MemberVO a join IntResVO b on a.memno = b.memno WHERE b.intno=:intno AND b.inttypecd='2'")
        FeedbackDetail_InfoDTO fInfoDTO(@Param("intno") Integer intno);

        @Query("SELECT new kr.co.ict.yourdream.feedback.FeedbackDetail_InFeedbkDTO(a.intno, a.efeed1, a.efeed2, a.pfeed1, a.pfeed2, a.vfeed1, a.vfeed2, a.sttfeed1, a.sttfeed2, a.upddt) "
                        +
                        " FROM IntFeedbkVO a where a.intno=:intno ORDER BY feedno asc")
        FeedbackDetail_InFeedbkDTO fResEvalDTO(@Param("intno") Integer intno);

        @Query("SELECT new kr.co.ict.yourdream.feedback.FeedbackDetail_IntResDTO(a.cnsfeedbk, a.upddt) FROM IntResVO a WHERE a.intno=:intno")
        FeedbackDetail_IntResDTO fResConEvalDTO(@Param("intno") Integer intno);

        @Query("SELECT new kr.co.ict.yourdream.feedback.FeedbackDetail_IntDetailDTO(a.question, a.answer, a.vhertz, a.vjitter, a.vspeed, a.aifeedbk, a.escore, a.pscore) FROM IntDetailVO a WHERE a.intno=:intno")
        List<FeedbackDetail_IntDetailDTO> fEscoreDTO(@Param("intno") Integer intno);

        @Query("SELECT new kr.co.ict.yourdream.feedback.FeedbackDetail_ConsultEvalDTO(a.qcnsfeedbk, a.upddt) FROM ConsultevalVO a WHERE a.intno=:intno ORDER BY a.intno, a.qno asc")
        List<FeedbackDetail_ConsultEvalDTO> fQconEvalDTO(@Param("intno") Integer intno);

        @Modifying
        @Transactional
        @Query("UPDATE ConsultevalVO a SET a.qcnsfeedbk=:qcnsfeedbk where intno=:intno and qno=:qno")
        void fQfeedUpdate(@Param("qcnsfeedbk") String qcnsfeedbk, @Param("intno") Integer intno,
                        @Param("qno") Integer qno);

        @Modifying
        @Transactional
        @Query("update IntResVO a set a.cnsfeedbk=:cnsfeedbk where intno=:intno")
        void fCnsfeedbkUpdate(@Param("cnsfeedbk") String cnsfeedbk, @Param("intno") Integer intno);

        @Query("SELECT new kr.co.ict.yourdream.feedback.ResumeInfoDTO(r.rsmno, r.title, r.name, r.imgname, r.addr, r.birthymd, r.email, r.mphonenum, r.hphonenum) FROM ResumeVO r WHERE  r.memberVO.memno = :memno AND r.rsmno = (SELECT "
                        +
                        "MAX(r2.rsmno) FROM ResumeVO r2 WHERE  r2.memberVO.memno = :memno)")
        ResumeInfoDTO resumeInfo(@Param("memno") Integer memno);

        @Query("SELECT new kr.co.ict.yourdream.feedback.ResumeEduDTO(r.seqno, r.entymd, r.gradeymd, r.schoolname, r.major, r.gradeuateyn, r.gradescore) from EducationVO r where r.rsmno=:rsmno")
        List<ResumeEduDTO> resumeEdu(@Param("rsmno") Integer rsmno);

        @Query("SELECT new kr.co.ict.yourdream.feedback.ResumeCarDTO(r.seqno, r.entymd, r.quitymd, r.compname, r.jobclass, r.maintask, r.jobposition) from CareerVO r where r.rsmno=:rsmno")
        List<ResumeCarDTO> resumeCar(@Param("rsmno") Integer rsmno);

        @Query("SELECT new kr.co.ict.yourdream.feedback.ResumeSelfDTO(r.seqno, r.title, r.content) from SelfIntroVO r where r.rsmno=:rsmno")
        List<ResumeSelfDTO> resumeSelf(@Param("rsmno") Integer rsmno);

}