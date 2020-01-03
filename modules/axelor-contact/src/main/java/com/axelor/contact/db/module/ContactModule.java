package com.axelor.contact.db.module;

import com.axelor.app.AxelorModule;
import com.axelor.contact.db.repo.ContactPersonRepository;
import com.axelor.contact.db.repo.PersonRepository;
import com.axelor.contact.db.service.HelloService;
import com.axelor.contact.db.service.HelloServiceImpl;

public class ContactModule extends AxelorModule {

  @Override
  protected void configure() {
    bind(PersonRepository.class).to(ContactPersonRepository.class);
    bind(HelloService.class).to(HelloServiceImpl.class);
  }
}
