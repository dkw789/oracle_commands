package org.dkw789.interview.oracle.commands;

import org.dkw789.interview.oracle.dependency.Module;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 *
 * @author donny lam
 */
public class ListCommand implements Command {

    //Lists the names of all currently installed packages.


    @Override
    public Map<String, Object> execute(List<String> args) {
        Map<String, Object> result = new LinkedHashMap<>();
        Module.getInstalled().forEach(m -> result.put(m.getName(), ""));
        return result;
    }

}
