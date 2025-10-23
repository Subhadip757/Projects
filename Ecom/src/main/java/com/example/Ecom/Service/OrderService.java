package com.example.Ecom.Service;

import com.example.Ecom.DTO.OrderDTO;
import com.example.Ecom.DTO.OrderItemDTO;
import com.example.Ecom.Model.OrderItem;
import com.example.Ecom.Model.Orders;
import com.example.Ecom.Model.Product;
import com.example.Ecom.Model.User;
import com.example.Ecom.Repo.OrderRepo;
import com.example.Ecom.Repo.ProdRepo;
import com.example.Ecom.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private ProdRepo prodRepository;

    @Autowired
    private OrderRepo orderRepository;

    public OrderDTO placeOrder(Long userId, Map<Long, Integer> productQuantities, double totalAmount){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));

        Orders order = new Orders();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Pending");
        order.setTotalAmount(totalAmount);

        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();

        for(Map.Entry<Long, Integer> entry: productQuantities.entrySet()){
            Product product = prodRepository.findById(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(entry.getValue());

            orderItems.add(orderItem);
            orderItemDTOS.add(new OrderItemDTO(product.getName(), product.getPrice(), entry.getValue()));
        }

        order.setOrderItems(orderItems);
        Orders savedOrders = orderRepository.save(order);
        return new OrderDTO(savedOrders.getId(), savedOrders.getTotalAmount(),
                savedOrders.getStatus(), savedOrders.getOrderDate(), orderItemDTOS);
    }

    public List<OrderDTO> getAllOrders(){
        List<Orders> orders = orderRepository.findAllOrdersWithUsers();

        return orders.stream().map(this::convertToDTO).collect(Collectors.toList());

    }

    private OrderDTO convertToDTO(Orders orders) {
        List<OrderItemDTO> orderItems = orders.getOrderItems().stream()
                .map(item-> new OrderItemDTO(
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity()
                )).collect(Collectors.toList());

        return new OrderDTO(
                orders.getId(),
                orders.getTotalAmount(),
                orders.getStatus(),
                orders.getOrderDate(),
                orders.getUser() != null ? orders.getUser().getName() : "Unknown",
                orders.getUser() != null ? orders.getUser().getEmail() : "Unknown",
                orderItems
        );
    }

    public List<OrderDTO> getOrderByUser(Long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new RuntimeException("user not found");
        }
        User user = userOptional.get();
        List<Orders> ordersList = orderRepository.findByUser(user);
        return ordersList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
