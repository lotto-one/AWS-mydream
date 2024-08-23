package kr.ict.mydream.resume;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ict.mydream.vo.CareerVO;
import kr.ict.mydream.vo.EducationVO;
import kr.ict.mydream.vo.ResumeVO;
import kr.ict.mydream.vo.SelfIntroVO;

@Service
public class ResumeService {
    @Autowired
    private ResumeDao resumeDao;

    public List<ResumeVO> resumeList(int num) {
        return resumeDao.resumeList(num);
    }

    public ResumeVO resumeDetail(int num) {
        return resumeDao.resumeDetail(num);
    }

    public List<EducationVO> resumeDetailEdu(int num) {
        return resumeDao.resumeDetailEdu(num);
    }

    public List<CareerVO> resumeDetailCar(int num) {
        return resumeDao.resumeDetailCar(num);
    }

    public List<SelfIntroVO> resumeDetailSelf(int num) {
        return resumeDao.resumeDetailSelf(num);
    }

    public void resumeAdd(ResumeVO vo) {
        resumeDao.resumeAdd(vo);
    }

    public void resumeAddEdu(Map<String, Object> edu) {
        resumeDao.resumeAddEdu(edu);
    }

    public void resumeAddCar(Map<String, Object> car) {
        resumeDao.resumeAddCar(car);
    }

    public void resumeAddSelf(Map<String, Object> self) {
        resumeDao.resumeAddSelf(self);
    }

    public ResumeVO resumeUpload(ResumeVO vo) {
        return resumeDao.resumeUpload(vo);
    }

    public void resumeImgUp(Map<String, Object> data) {
        resumeDao.resumeImgUp(data);
    }

    public void resumeUpdate(Map<String, Object> basic) {
        resumeDao.resumeUpdate(basic);
    }

    public void resumeUpdataEdu(Map<String, Object> edu) {
        resumeDao.resumeUpdateEdu(edu);
    }

    public void resumeUpdataCar(Map<String, Object> car) {
        resumeDao.resumeUpdateCar(car);
    }

    public void resumeUpdataSelf(Map<String, Object> self) {
        resumeDao.resumeUpdateSelf(self);
    }

    public void resumeDelete(Map<String, Integer> rsmno) {
        resumeDao.resumeDelete(rsmno);
    }

    public void resumeDelEdu(int rsmno) {
        resumeDao.resumeDelEdu(rsmno);
    }

    public void resumeDelCar(int rsmno) {
        resumeDao.resumeDelCar(rsmno);
    }

    public void resumeDelSelf(int rsmno) {
        resumeDao.resumeDelSelf(rsmno);
    }

    public void resumeDeleteEdu(Map<String, Integer> num) {
        resumeDao.resumeDeleteEdu(num);
    }

    public void resumeDeleteCar(Map<String, Integer> num) {
        resumeDao.resumeDeleteCar(num);
    }

    public void resumeDeleteSelf(Map<String, Integer> num) {
        resumeDao.resumeDeleteSelf(num);
    }
}
