package com.axelor.project.web;

import com.axelor.meta.CallMethod;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class HelloController {
  @CallMethod
  public void say(ActionRequest request, ActionResponse response) {
    String flash = "hello";
    response.setFlash(flash);
  }
}
