package org.dkw789.interview.oracle.commands;


import org.dkw789.interview.oracle.dependency.Module;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author donny lam
 */

public class DependCommand implements Command {


    @Override
    public Map<String, Object> execute(List<String> args) {
        String depName = args.get(0);//item1

//Package item1 depends on package item2 (and item3 or any additional packages).

        Module current = Module.getInstance(depName);

        for (String strDependency : args.subList(1, args.size())) {
            Module dependency = Module.getInstance(strDependency);
            current.addDependency(dependency);
            dependency.addDependent(current);
        }
        return Collections.emptyMap();
    }

}
