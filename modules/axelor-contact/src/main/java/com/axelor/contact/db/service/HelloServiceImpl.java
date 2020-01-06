package com.axelor.contact.db.service;

import com.axelor.contact.db.Person;
import com.axelor.contact.db.repo.PersonRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class HelloServiceImpl implements HelloService {
  @Inject PersonRepository personRepo;

  @Override
  @Transactional
  public void onSave(Person person) {
    personRepo.save(personRepo.find(person.getId()));
  }
}
