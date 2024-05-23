package com.eincrm.mapper;

import com.eincrm.model.Statics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StaticsMapper {

    List<Statics> getStatisticsCons(@Param("start") String start, @Param("end") String end, @Param("custCode") String custCode);
    List<Statics> getStatisticsConsG(@Param("start") String start, @Param("end") String end,@Param("custCode") String custCode);

    List<Statics> getConsultationResult(@Param("start") String start, @Param("end") String end, @Param("custCode") String custCode);
    List<Statics> getConsultationResultG(@Param("start") String start, @Param("end") String end, @Param("custCode") String custCode);

    List<Statics> getConsultationTime(@Param("start") String start, @Param("end") String end, @Param("custCode") String custCode);
    List<Statics> getConsultationTimeG(@Param("start") String start, @Param("end") String end, @Param("custCode") String custCode);

}
