package kr.ict.mydream.resume;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.ict.mydream.vo.CareerVO;
import kr.ict.mydream.vo.EducationVO;
import kr.ict.mydream.vo.ResumeVO;
import kr.ict.mydream.vo.SelfIntroVO;

@Mapper
public interface ResumeDao {
    public List<ResumeVO> resumeList(int num); // 이력서 리스트

    public ResumeVO resumeDetail(int num); // 이력서 상세보기(기본)

    public List<EducationVO> resumeDetailEdu(int num); // 이력서 상세보기(학력)

    public List<CareerVO> resumeDetailCar(int num); // 이력서 상세보기(경력)

    public List<SelfIntroVO> resumeDetailSelf(int num); // 이력서 상세보기(자소서)

    public void resumeAdd(ResumeVO vo); // 이력서 추가

    public void resumeAddEdu(Map<String, Object> edu); // 이력서 추가(학력)

    public void resumeAddCar(Map<String, Object> car); // 이력서 추가(경력)

    public void resumeAddSelf(Map<String, Object> self); // 이력서 추가(자소서)

    public void resumeImgUp(Map<String, Object> data); // 이력서 사진 추가

    public ResumeVO resumeUpload(ResumeVO vo); // 이력서 업로드

    public void resumeUpdate(Map<String, Object> basic); // 이력서 수정

    public void resumeUpdateEdu(Map<String, Object> edu); // 이력서 수정(학력)

    public void resumeUpdateCar(Map<String, Object> car); // 이력서 수정(경력)

    public void resumeUpdateSelf(Map<String, Object> self); // 이력서 수정(자소서)

    public void resumeDelete(Map<String, Integer> rsmno); // 이력서 삭제

    public void resumeDelEdu(int rsmno);

    public void resumeDelCar(int rsmno);

    public void resumeDelSelf(int rsmno);

    public void resumeDeleteEdu(Map<String, Integer> num); // 이력서 삭제(학력)

    public void resumeDeleteCar(Map<String, Integer> num); // 이력서 삭제(경력)

    public void resumeDeleteSelf(Map<String, Integer> num); // 이력서 삭제(자소서)

}
