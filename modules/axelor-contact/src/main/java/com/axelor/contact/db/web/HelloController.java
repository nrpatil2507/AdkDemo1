package com.axelor.contact.db.web;

import com.axelor.contact.db.Person;
import com.axelor.contact.db.service.HelloService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class HelloController {

  @Inject HelloService helloService;

  public void setFullName(ActionRequest request, ActionResponse response) {
    Person person = request.getContext().asType(Person.class);
    person.setFullName(person.getFirstName() + " " + person.getLastName());
    // response.setValue("fullName", person.getFirstName() + " " +
    // person.getLastName());
    helloService.onSave(person);
  }
}
