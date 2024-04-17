package com.poly.datn.sd18.service.impl;

import com.poly.datn.sd18.entity.Order;
import com.poly.datn.sd18.entity.OrderDetail;
import com.poly.datn.sd18.model.dto.OrderDetailDTO;
import com.poly.datn.sd18.model.response.OrderDetailResponse;
import com.poly.datn.sd18.repository.OrderDetailRepository;
import com.poly.datn.sd18.repository.OrderRepository;
import com.poly.datn.sd18.repository.ProductDetailRepository;
import com.poly.datn.sd18.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public OrderDetail addOrderDetail(OrderDetailDTO orderDetailDTO) {
        OrderDetail orderDetail = orderDetailDTO.map(new OrderDetail());
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public List<OrderDetailResponse> findOrderDetailByOrderId(Integer orderId) {
        return orderDetailRepository.findOrderDetailByOrderId(orderId);
    }

    @Override
    public List<OrderDetail> findByOrderId(Integer id) {
        return orderDetailRepository.findByOrderId(id);
    }
    private final OrderRepository orderRepository;
    private final ProductDetailRepository productDetailRepository;

    // Phương thức để xóa một chi tiết đơn hàng dựa trên id
    public OrderDetail deleteOrderDetail(int id) {
        OrderDetail obj = orderDetailRepository.findById(id).orElse(null);
        orderDetailRepository.deleteById(id);
        return obj;
    }

    // Phương thức để lấy thông tin chi tiết đơn hàng dựa trên id
    public OrderDetail getById(int id) {
        OrderDetail obj = orderDetailRepository.findById(id).orElse(null);
        return obj;
    }

    // Phương thức để tăng số lượng của một chi tiết đơn hàng dựa trên id và số
    // lượng được chỉ định
    public OrderDetail PlusAmountOrderDetail(int id, int amount) {
        OrderDetail obj = orderDetailRepository.findById(id).orElse(null);
        obj.setQuantity(obj.getQuantity() + amount);
        orderDetailRepository.saveAndFlush(obj);
        return obj;
    }

    // Phương thức để giảm số lượng của một chi tiết đơn hàng dựa trên id và số
    // lượng được chỉ định
    public OrderDetail MinusAmountOrderDetail(int id, int amount) {
        OrderDetail obj = orderDetailRepository.findById(id).orElse(null);
        obj.setQuantity(obj.getQuantity() - amount);
        orderDetailRepository.saveAndFlush(obj);
        return obj;
    }

    // Phương thức để thay đổi trạng thái của một đơn hàng dựa trên id và trạng thái
    // được chỉ định
    public Order ChangeOrderStatus(int id, int status) {
        Order obj = orderRepository.findById(id).orElse(null);
        obj.setStatus(status);
        // Cập nhật các ngày tương ứng với các trạng thái khác nhau
        if (status == 2) {
            obj.setConfirmDate(new Date(System.currentTimeMillis()));
        } else if (status == 3) {
            obj.setShipWaitDate(new Date(System.currentTimeMillis()));
        } else if (status == 4) {
            obj.setShipDate(new Date(System.currentTimeMillis()));
        } else if (status == 5) {
            obj.setSuccessDate(new Date(System.currentTimeMillis()));
        } else if (status == 6) {
            obj.setCancelDate(new Date(System.currentTimeMillis()));
        }
        orderRepository.saveAndFlush(obj);
        return obj;
    }

    // Phương thức để thêm một chi tiết đơn hàng mới
    public OrderDetail add(OrderDetail orderDetail, int productId, int orderId) {
        // Kiểm tra xem chi tiết đơn hàng đã tồn tại chưa
        OrderDetail check = orderDetailRepository.getByProductId(productId, orderId);
        if (check != null) {
            // Nếu đã tồn tại, tăng số lượng của chi tiết đó
            return this.PlusAmountOrderDetail(check.getId(), 1);
        } else {
            // Nếu chưa tồn tại, thêm chi tiết đơn hàng mới vào cơ sở dữ liệu
            Order order = orderRepository.findById(orderId).orElse(null);
            orderDetail.setOrder(order);
            return orderDetailRepository.save(orderDetail);
        }
    }
}
