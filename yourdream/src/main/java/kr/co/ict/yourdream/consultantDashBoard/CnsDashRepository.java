package kr.co.ict.yourdream.consultantDashBoard;

import org.springframework.data.jpa.repository.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CnsDashRepository extends JpaRepository<ConsultDashBoard, Integer> {

        int countBy(); // This method will count all entries in the ConsultDashBoard table

        @Query(value = "SELECT ROUND((SELECT COUNT(memno) FROM TBMEMBERCONSULT WHERE USEYN = 'Y') / " +
                        "(SELECT COUNT(cnsno) FROM tbconsult), 2) AS memcns_ave FROM dual", nativeQuery = true)
        float findMemCnsAverage();

        @Query(value = "SELECT ROUND((SELECT SUM(reviewscore) FROM TBINTRES) / " +
                        "(SELECT COUNT(reviewscore) FROM TBINTRES), 1) AS review_ave FROM dual", nativeQuery = true)
        float findReviewAverage();

        @Query(value = "SELECT COUNT(CNSSCORE) FROM tbintres " +
                        "WHERE upddt >= TRUNC(SYSDATE, 'IW') " +
                        "AND upddt < TRUNC(SYSDATE, 'IW') + 7", nativeQuery = true)
        int countCnsScoreThisWeek();

        @Query(value = "SELECT " +
                        "TO_CHAR(upddt, 'Day') AS day_of_week, " +
                        "COUNT(intno) AS cns_apply, " +
                        "COUNT(CNSSCORE) AS cns_complete " +
                        "FROM TBINTRES " +
                        "GROUP BY TO_CHAR(upddt, 'Day') " +
                        "ORDER BY MIN(upddt)", nativeQuery = true)
        List<Map<String, Object>> findCnsStatsByDayOfWeek();

        @Query(value = "SELECT " +
                        "CASE " +
                        "    WHEN categcd = '1' THEN 'IT/개발' " +
                        "    WHEN categcd = '2' THEN '교육' " +
                        "    WHEN categcd = '3' THEN '영업/마케팅' " +
                        "    WHEN categcd = '4' THEN '기획/전략' " +
                        "    WHEN categcd = '5' THEN '경영' " +
                        "END AS categcd, " +
                        "COUNT(*) AS count_per_category " +
                        "FROM TBCONSULT " +
                        "GROUP BY categcd", nativeQuery = true)
        List<Map<String, Object>> findCategoryCounts();

        @Query(value = "SELECT " +
                        "CASE " +
                        "WHEN REVIEWSCORE >= 0 AND REVIEWSCORE < 1 THEN '0~1' " +
                        "WHEN REVIEWSCORE >= 1 AND REVIEWSCORE < 2 THEN '1~2' " +
                        "WHEN REVIEWSCORE >= 2 AND REVIEWSCORE < 3 THEN '2~3' " +
                        "WHEN REVIEWSCORE >= 3 AND REVIEWSCORE < 4 THEN '3~4' " +
                        "WHEN REVIEWSCORE >= 4 AND REVIEWSCORE <= 5 THEN '4~5' " +
                        "END AS score_range, " +
                        "COUNT(*) AS count_per_range " +
                        "FROM TBINTRES " +
                        "WHERE REVIEWSCORE IS NOT NULL " +
                        "GROUP BY CASE " +
                        "WHEN REVIEWSCORE >= 0 AND REVIEWSCORE < 1 THEN '0~1' " +
                        "WHEN REVIEWSCORE >= 1 AND REVIEWSCORE < 2 THEN '1~2' " +
                        "WHEN REVIEWSCORE >= 2 AND REVIEWSCORE < 3 THEN '2~3' " +
                        "WHEN REVIEWSCORE >= 3 AND REVIEWSCORE < 4 THEN '3~4' " +
                        "WHEN REVIEWSCORE >= 4 AND REVIEWSCORE <= 5 THEN '4~5' " +
                        "END " +
                        "ORDER BY score_range", nativeQuery = true)
        List<Map<String, Object>> findReviewScoresByRange();

        @Query(value = "SELECT " +
                        "CASE " +
                        "    WHEN b.CATEGCD='1' THEN 'IT/개발' " +
                        "    WHEN b.CATEGCD='2' THEN '교육' " +
                        "    WHEN b.CATEGCD='3' THEN '영업/마케팅' " +
                        "    WHEN b.CATEGCD='4' THEN '기획/전략' " +
                        "    WHEN b.CATEGCD='5' THEN '경영' " +
                        "END AS categcd_group, " +
                        "CASE " +
                        "    WHEN TO_CHAR(a.REVIEWDT, 'D') = '1' THEN '일요일' " +
                        "    WHEN TO_CHAR(a.REVIEWDT, 'D') = '2' THEN '월요일' " +
                        "    WHEN TO_CHAR(a.REVIEWDT, 'D') = '3' THEN '화요일' " +
                        "    WHEN TO_CHAR(a.REVIEWDT, 'D') = '4' THEN '수요일' " +
                        "    WHEN TO_CHAR(a.REVIEWDT, 'D') = '5' THEN '목요일' " +
                        "    WHEN TO_CHAR(a.REVIEWDT, 'D') = '6' THEN '금요일' " +
                        "    WHEN TO_CHAR(a.REVIEWDT, 'D') = '7' THEN '토요일' " +
                        "END AS weekday, " +
                        "ROUND(AVG(a.REVIEWSCORE), 1) AS ctg_dt_avgscore " +
                        "FROM TBINTRES a " +
                        "JOIN TBMEMBER b ON a.memno = b.memno " +
                        "GROUP BY b.categcd, TO_CHAR(a.REVIEWDT, 'D') " +
                        "ORDER BY TO_CHAR(a.REVIEWDT, 'D')", nativeQuery = true)
        List<Map<String, Object>> findAverageScoreByCategoryAndWeekday();
}