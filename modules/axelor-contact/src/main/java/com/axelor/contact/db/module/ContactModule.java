package com.axelor.contact.db.module;

import com.axelor.app.AxelorModule;
import com.axelor.contact.db.repo.ContactPersonRepository;
import com.axelor.contact.db.repo.PersonRepository;

public class ContactModule extends AxelorModule {

  @Override
  protected void configure() {
    bind(PersonRepository.class).to(ContactPersonRepository.class);
  }
}
