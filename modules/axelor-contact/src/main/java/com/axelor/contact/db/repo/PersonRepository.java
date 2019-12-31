package com.axelor.contact.db.repo;

import com.axelor.contact.db.Person;
import java.util.Map;

public class PersonRepository extends AbstractPersonRepository {
  @Override
  public Map<String, Object> populate(Map<String, Object> json, Map<String, Object> context) {
    if (!context.containsKey("json-enhance")) {
      return json;
    }
    try {
      Long id = (Long) json.get("id");
      Person contact = find(id);
      json.put("address", contact.getAddresses().get(0));
      json.put("hasImage", contact.getImage() != null);
    } catch (Exception e) {
    }

    return json;
  }
}
