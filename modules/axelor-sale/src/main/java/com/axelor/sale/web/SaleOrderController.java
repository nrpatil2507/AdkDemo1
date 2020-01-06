package com.axelor.sale.web;

import com.axelor.common.ObjectUtils;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.sale.db.Order;
import com.axelor.sale.db.OrderLine;
import com.axelor.sale.db.Tax;
import java.util.List;

public class SaleOrderController {

  public void setTotalAmount(ActionRequest request, ActionResponse response) {

    OrderLine orderline = request.getContext().asType(OrderLine.class);
    orderline.getTotalQuantity();

    float value = orderline.getPrice().floatValue() * orderline.getTotalQuantity();
    float taxValue = orderline.getTaxes().getTaxRate().floatValue() * value / 100;
    float totalAmount = value + taxValue;

    response.setValue("TotalAmount", totalAmount);
  }

  public void setAmount(ActionRequest request, ActionResponse response) {
    Order order = request.getContext().asType(Order.class);
    float taxValue = 0;
    float totalVal = 0;
    float value = 0;
    int qty = 0;
    List<OrderLine> orderLineList = order.getItems();
    if (!ObjectUtils.isEmpty(orderLineList)) {
      for (OrderLine orderLine : orderLineList) {
        value = value + orderLine.getPrice().floatValue() * orderLine.getTotalQuantity();
        qty = qty + orderLine.getTotalQuantity();

        Tax tax = orderLine.getTaxes();
        taxValue = tax.getTaxRate().floatValue() * value / 100;
        totalVal = value + taxValue;
      }
    }
    response.setValue("taxAmount", taxValue);
    response.setValue("totalAmount", totalVal);
    response.setValue("totalQty", qty);
  }

  public void chekstatus(ActionRequest request, ActionResponse response) {

    Order order = request.getContext().asType(Order.class);
    String act = request.getAction();

    if (act.equals("action.order.status.method.onclick.change.statusdraft")) {
      response.setAttr("status", "value", "DRAFT");

    } else if (act.equals("action.order.status.method.onclick.change.statusopen")) {
      response.setAttr("status", "value", "OPEN");

    } else if (act.equals("action.order.status.method.onclick.change.statusclose")) {
      response.setAttr("status", "value", "CLOSED");
    } else {
      response.setAttr("status", "value", "CANCELED");
    }
  }
}
