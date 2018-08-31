package org.dkw789.interview.oracle.commands;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author donny lam
 */

public interface Command {

    Map<String, Object> execute(List<String> args);
}
