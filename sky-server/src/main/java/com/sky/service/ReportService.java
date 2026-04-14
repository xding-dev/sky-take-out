package com.sky.service;

import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {


    /**
     * 统计指定区间的营业额
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnoverReportVO(LocalDate begin, LocalDate end);

    /**
     * 用户统计接口
     * @param begin
     * @param end
     * @return
     */
    UserReportVO getUSerStatistics(LocalDate begin, LocalDate end);
}
