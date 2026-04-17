package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderDetailMapper {

    /**
     * 插入数据
     * @param orderDetails
     */
    void insertBatch(List<OrderDetail> orderDetails);


    /**
     * 根据订单id查询订单明细
     * @param orderId
     * @return
     */
    @Select("select * from order_detail where order_id = #{orderId}")
    List<OrderDetail> getByOrderId(Long orderId);


    /**
     * 查询销量排名top10接口
     * @param begin
     * @param end
     * @return
     */
    List<GoodsSalesDTO> getTopTenOrders(LocalDateTime begin, LocalDateTime end);
}
