package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;




    /**
     * 统计指定区间的营业额
     * @param begin
     * @param end
     * @return
     */
    public TurnoverReportVO getTurnoverReportVO(LocalDate begin, LocalDate end) {
        //当前集合用于存放从begin 到 end 之间的日期
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);

        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            //查询date日期对应的营业额  营业额：状态为已完成订单的金额统计
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);


            Double turnover = orderMapper.sumByMap(map);
            //如果turnover为null，需要将其转换为0.0
            turnover = turnover == null ? 0.0 : turnover;

            turnoverList.add(turnover);
        }


        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(dateList,","))
                .turnoverList(StringUtils.join(turnoverList,","))
                .build();
    }

    /**
     * 用户统计接口
     * @param begin
     * @param end
     * @return
     */
    public UserReportVO getUSerStatistics(LocalDate begin, LocalDate end) {
        //当前集合用于存放从begin 到 end 之间的日期
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);

        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        //查询每天新增用户数量
        //select count(*) from orders where orderTime> beging and orderTime < end
        List<Integer> newUserList = new ArrayList<>();

        //获取总用户数量
        List<Integer> totalUserList = new ArrayList<>();
        for (LocalDate date : dateList) {
            //查询date日期对应的新增用户数量
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("end", endTime);
            Integer integer1 = userMapper.countUserByMap(map);
            totalUserList.add(integer1);

            Integer integer2 = userMapper.countUserByMap(map);

            map.put("begin", beginTime);

            newUserList.add(integer2);

        }




        return UserReportVO
                .builder()
                .dateList(StringUtils.join(dateList,","))
                .newUserList(StringUtils.join(newUserList,","))
                .totalUserList(StringUtils.join(totalUserList,","))
                .build();
    }
}
