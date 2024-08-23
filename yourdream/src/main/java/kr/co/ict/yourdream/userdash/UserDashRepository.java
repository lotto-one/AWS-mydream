package kr.co.ict.yourdream.userdash;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface UserDashRepository extends JpaRepository<MemberVOBoks,Integer>{
    
    @Query("SELECT COUNT(m.memno) FROM MemberVOBoks m")
    long countTotalMembers();

    @Query(value = "SELECT COUNT(*) FROM tblogin WHERE TRUNC(logindt) = TRUNC(:today)", nativeQuery = true)
    long countDailyVisitors(@Param("today") Date today);

    @Query("SELECT COUNT(i.intno) FROM IntResVOBok i")
    long countTotalIntnos();

    @Query("SELECT COUNT(mc) FROM IntResVOBok mc where mc.inttypecd = '2'")
    long countTotal();

    @Query("SELECT new kr.co.ict.yourdream.userdash.DailyMemberCountDTO(m.credt, COUNT(m)) " +
           "FROM MemberVOBoks m " +
           "WHERE m.credt BETWEEN :startDate AND :endDate " +
           "GROUP BY m.credt " +
           "ORDER BY m.credt")
    List<DailyMemberCountDTO> findDailyCounts(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    @Query("SELECT m.categcd as categCd, COUNT(m) as count " +
           "FROM MemberVOBoks m " +
           "GROUP BY m.categcd")
    List<Map<String, Object>> findMemberCountByCategcd();


    @Query("SELECT m.loccd as locCd, COUNT(m) as count " +
           "FROM MemberVOBoks m " +
           "GROUP BY m.loccd")
    List<Map<String, Object>> findMemberCountByLoccd();

    @Query("SELECT new kr.co.ict.yourdream.userdash.IntTypeCountDTO(m.credt, COUNT(m)) " +
           "FROM IntResVOBok m " +
           "WHERE m.inttypecd = '2' AND m.credt BETWEEN :startDate AND :endDate " +
           "GROUP BY m.credt " +
           "ORDER BY m.credt")
    List<IntTypeCountDTO> findDailyIntTypeCounts(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


} 