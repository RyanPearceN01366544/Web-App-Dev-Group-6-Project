package com.Group6.WebAppDevGroupProject.Service;

import com.Group6.WebAppDevGroupProject.Models.MenuItem;
import com.Group6.WebAppDevGroupProject.Models.Order;
import com.Group6.WebAppDevGroupProject.Repository.MenuRepository;
import com.Group6.WebAppDevGroupProject.Repository.OrderRepository;
import com.Group6.WebAppDevGroupProject.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private MenuRepository menuRepository;


    public Order getOrderById(long id_) {
        return orderRepo.findById(id_).orElse(null);
    }
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }
    public List<Order> getAllActiveOrders() {
        return orderRepo.findAllActive();
    }
    public List<Order> getAllOrdersFromUser(long id_) {
        if (userRepo.findById(id_).isPresent()) {
            return orderRepo.findAllByUser(id_);
        }
        return null;
    }
    public List<Order> getAllActiveOrdersFromUser(long id_) {
        if (userRepo.findById(id_).isPresent()) {
            return orderRepo.findAllActiveByUser(id_);
        }
        return null;
    }
    public List<MenuItem> getMenuItemsFromOrder(long id_) {
        Optional<Order> ord_ = orderRepo.findById(id_);
        if (ord_.isPresent()) {
            if (!ord_.get().getOrder_items().equals("[]")) {
                List<MenuItem> menuItems = getAllMenuItemsFromOrder(ord_.get());
                return menuItems;
            }
        }
        return null;
    }
    public Order saveOrder(Order ord_) {
        handleStocks(ord_);
        return orderRepo.save(ord_);
    }
    public Order updateOrder(long id_, Order ord_) {
        return orderRepo.findById(id_).map(order -> {
            order.setOrder_items(ord_.getOrder_items());
            order.setOrder_requested(ord_.getOrder_requested());
            order.setOrder_time(ord_.getOrder_time());
            order.setOrder_status(ord_.getOrder_status());
            order.setOrder_items(getMenuItemsStringFromOrder(getMenuItemsMapFromOrder(ord_)));
            handleStocks(order, ord_);
            return orderRepo.save(order);
        }).orElse(null);
    }
    public void deleteOrder(long id_) {
        if (orderRepo.findById(id_).isPresent()) {
            if (!new Date().after(orderRepo.findById(id_).get().getOrder_time())) {
                for (Map.Entry<Long, Integer> item_ : getMenuItemsMapFromOrder(orderRepo.findById(id_).get()).entrySet()) {
                    menuRepository.findById(item_.getKey()).map((itemR_) -> {
                        itemR_.setStock(itemR_.getStock() + item_.getValue()); // Adding onto the stock since the order is canceled!
                        return itemR_;
                    });
                }
                orderRepo.deleteById(id_);
            }
        }
    }
    public void deleteOrder(long id_, boolean isAdmin_) {
        if (orderRepo.findById(id_).isPresent()) {
            if (!new Date().after(orderRepo.findById(id_).get().getOrder_time()) || isAdmin_) {
                for (Map.Entry<Long, Integer> item_ : getMenuItemsMapFromOrder(orderRepo.findById(id_).get()).entrySet()) {
                    menuRepository.findById(item_.getKey()).map((itemR_) -> {
                        itemR_.setStock(itemR_.getStock() + item_.getValue()); // Adding onto the stock since the order is canceled!
                        return itemR_;
                    });
                }
                orderRepo.deleteById(id_);
            }
        }
    }

    // -- HANDLERS --
    private Map<Long, Integer> getMenuItemsMapFromOrder(Order ord_) {
        List<String> orderItemsIdsStr = Arrays.stream(ord_.getOrder_items().replaceAll("[\\[\\]]", "").split(",")).toList();
        Map<Long, Integer> orderItems = new HashMap<>();
        for (String text_ : orderItemsIdsStr){
            String[] pair_ = text_.split(":"); // -> 0 is key, 1 is value (Basically, key is menuItem ID and value is Stock.)
            orderItems.put(Long.parseLong(pair_[0]), Integer.parseInt(pair_[1]));
        }
        return orderItems;
    }
    private String getMenuItemsStringFromOrder(Map<Long, Integer> values_) {
        StringBuilder sb_ = new StringBuilder();
        sb_.append("[");
        boolean first_ = true; // -- Just to make sure each one has a ',' except the first...
        for (Map.Entry<Long, Integer> pair_ : values_.entrySet()) {
            if (!first_) {
                sb_.append(", ");
            }
            sb_.append(pair_.getKey()).append(": ").append(pair_.getValue());
        }
        sb_.append("]");
        return sb_.toString();
    }
    private List<MenuItem> getAllMenuItemsFromOrder(Order ord_){
        List<String> orderItemsIdsStr = Arrays.stream(ord_.getOrder_items().replaceAll("[\\[\\]]", "").split(",")).toList();
        List<MenuItem> menuItems = new ArrayList<>();
        for (String text_ : orderItemsIdsStr){
            String[] pair_ = text_.split(":"); // -> 0 is key, 1 is value (Basically, key is menuItem ID and value is Stock.)
            menuItems.add(menuRepository.findById(Long.parseLong(pair_[0])).orElse(null));
        }
        return menuItems;
    }

    // -- Subtracting/Adding Stocks (Basically, Handlers for Stocks)
    private void handleStocks(Order ord_) {
        for (Map.Entry<Long, Integer> itemData_ : getMenuItemsMapFromOrder(ord_).entrySet()) {
            if (menuRepository.findById(itemData_.getKey()).isPresent()) {
                MenuItem item_ = menuRepository.findById(itemData_.getKey()).get();
                if (item_.getStock() - itemData_.getValue() >= 0) {
                    menuRepository.findById(itemData_.getKey()).map((itemR_) -> {
                        itemR_.setStock(itemR_.getStock() - itemData_.getValue());
                        return itemR_;
                    });
                }
            }
        }
    }
    private void handleStocks(Order oldOrd_, Order newOrd_) {
        Map<Long, Integer> oldItemData_ = getMenuItemsMapFromOrder(oldOrd_);
        for (Map.Entry<Long, Integer> newItemData_ : getMenuItemsMapFromOrder(newOrd_).entrySet()) {
            if (oldItemData_.containsKey(newItemData_.getKey()) && oldItemData_.get(newItemData_.getKey()) - newItemData_.getValue() >= 0)
            {
                menuRepository.findById(newItemData_.getKey()).map((itemR_) -> {
                    // This should turn negative if more is requested and positive is less is requested.
                    // Example: Old Value: 2, New Value: 1 -> 2 - 1 = +1, Example #2: Old Value: 1, New Value: 2 -> 1 - 2 = -1
                    itemR_.setStock(itemR_.getStock() + (oldItemData_.get(newItemData_.getKey()) - newItemData_.getValue()));
                    return itemR_;
                });
            }
            else if (!oldItemData_.containsKey(newItemData_.getKey())) {
                menuRepository.findById(newItemData_.getKey()).map((itemR_) -> {
                    itemR_.setStock(itemR_.getStock() - newItemData_.getValue());
                    return itemR_;
                });
            }
        }
    }
}
