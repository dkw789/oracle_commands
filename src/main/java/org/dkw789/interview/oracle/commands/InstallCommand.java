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

public class InstallCommand implements Command {
    //Installs item1 and any other packages required by item1.
    @Override
    public Map<String, Object> execute(List<String> args) {
        Map<String, Object> result = new LinkedHashMap<>();
        for (String depName : args) {

            Module dep = Module.getInstance(depName);
            install(dep, result);
        }
        return result;
    }

    private Map<String, Object> install(Module current, Map<String, Object> result) {
        if (!current.isInstalled()) {
            current.setInstalled(true);


            for (Module dependency : current.getDependencies()) {
                if (!dependency.isInstalled()) { //installs dependencies
                    install(dependency, result);
                }

            }
            result.put(current.getName(), "successfully installed");

        } else {
            result.put(current.getName(), "is already installed");

        }
        return result;
    }
}
