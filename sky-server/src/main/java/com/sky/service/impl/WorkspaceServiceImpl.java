package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    /**
     * 查询今日运营数据
     *
     * @return
     */
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        //计算当天营业额 select sum(amount) from orders where
        Map map = new HashMap();
        map.put("begin", begin);
        map.put("end", end);

        //查询今天的所有订单
        Integer  totalCount = orderMapper.countByMap(map);

        //查询新增用户数量 user表中
        Integer newUSers = userMapper.countUserByMap(map);

        map.put("status", Orders.COMPLETED);

        //查询今日营业额
        Double turnover = orderMapper.sumByMap(map);
        turnover = turnover == null? 0.0 : turnover;


        //查询今天为已完成的订单 即有效订单数
        Integer validOrderCount = orderMapper.countByMap(map);

        double unitPrice = 0.0;
        double orderCompletionRate = 0.0;
        //计算订单完成率
        if (totalCount != 0 && validOrderCount != 0) {
            //平均客单价： 营业额 / 有效订单数
            unitPrice = turnover / validOrderCount;
            //订单完成率 有效订单数/ 总订单数量
            orderCompletionRate = validOrderCount.doubleValue() / totalCount;

        }

        return BusinessDataVO
                .builder()
                .newUsers(newUSers)
                .orderCompletionRate(orderCompletionRate)
                .turnover(turnover)
                .unitPrice(unitPrice)
                .validOrderCount(validOrderCount)
                .build();
    }

    /**
     * 查询套餐总览
     * @return
     */
    public DishOverViewVO getMeal() {
        Map map = new HashMap();
        //查看起售套餐
        map.put("status", StatusConstant.ENABLE);
        Integer sold = setmealMapper.countByMap(map);

        //查看停售套餐
        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = setmealMapper.countByMap(map);
        return DishOverViewVO
                .builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 查询菜品总览
     * @return
     */
    public DishOverViewVO getDish() {
        Map map = new HashMap();
        //查看起售套餐
        map.put("status", StatusConstant.ENABLE);
        Integer sold = dishMapper.countByMap(map);

        //查看停售套餐
        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = dishMapper.countByMap(map);
        return DishOverViewVO
                .builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 查询订单管理数据
     * @return
     */
    public OrderOverViewVO overviewOrders(LocalDateTime begin, LocalDateTime end) {
        Map map = new HashMap();
        map.put("begin", begin);
        map.put("end", end);
        //查询全部订单
        Integer  allOrders = orderMapper.countByMap(map);
        //查询已取消订单数量
        map.put("status",Orders.CANCELLED);
        Integer cancelledOrders =  orderMapper.countByMap(map);

        //查询完成订单数量
        map.put("status",Orders.COMPLETED);
        Integer completedOrders =  orderMapper.countByMap(map);

        //查询待派送订单数量
        map.put("status",Orders.CONFIRMED);
        Integer deliveredOrders =  orderMapper.countByMap(map);

        //查询待接单订单数量
        map.put("status",Orders.TO_BE_CONFIRMED);
        Integer waitingOrders =  orderMapper.countByMap(map);
        return OrderOverViewVO
                .builder()
                .allOrders(allOrders)
                .cancelledOrders(cancelledOrders)
                .completedOrders(completedOrders)
                .deliveredOrders(deliveredOrders)
                .waitingOrders(waitingOrders)
                .build();
    }
}
