package com.axelor.csv.adapter;

import com.axelor.data.adapter.Adapter;
import java.util.Map;
import java.util.regex.Pattern;

public class BooleanDataAdapter extends Adapter {

  private Pattern pattern;

  @Override
  public Object adapt(Object value, Map<String, Object> context) {

    if (pattern == null) {

      String mypattern = this.get("mypattern", "(T|true|1)");
      pattern = Pattern.compile(mypattern, Pattern.CASE_INSENSITIVE);
    }
    return pattern.matcher((String) value).matches();
  }
}
