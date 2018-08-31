package org.dkw789.interview.oracle;

import org.dkw789.interview.oracle.dependency.Module;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.dkw789.interview.oracle.dependency.Module.getInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 *
 * @author donny lam
 */

public class CommandTest {

    @Before
    public void reset() {

        Module.getAll().clear();
    }

    @Test
    public void testInstall() throws Exception {
        String input = "DEPEND TELNET TCPIP NETCARD\n" +
                "DEPEND TCPIP NETCARD\n" +
                "DEPEND DNS TCPIP NETCARD\n" +
                "DEPEND BROWSER TCPIP HTML\n" +
                "INSTALL NETCARD\n" +
                "INSTALL TELNET\n" +
                "INSTALL foo\n" +
                "END";

        Main app = new Main(new ByteArrayInputStream(input.getBytes()), new PrintStream(new ByteArrayOutputStream()));
        app.process();


        Set<Module> expected = new HashSet<>(asList(getInstance("NETCARD"), getInstance("TCPIP"), getInstance("TELNET"), getInstance("foo")));
        assertEquals(expected, Module.getInstalled());

    }

    @Test
    public void testListCommand() throws Exception {


    }

    @Test
    public void testDependCommand() throws Exception {
        String input = "DEPEND TELNET TCPIP NETCARD\n" +
                "DEPEND TCPIP NETCARD\n" +
                "DEPEND DNS TCPIP NETCARD\n" +
                "DEPEND BROWSER TCPIP HTML\n" +
                "END";
        Main app = new Main(new ByteArrayInputStream(input.getBytes()), new PrintStream(new ByteArrayOutputStream()));
        app.process();


        Module module = getInstance("TELNET");

        assertEquals(2, module.getDependencies().size());
        assertTrue(module.getDependencies().containsAll(asList(getInstance("TCPIP"), getInstance("NETCARD"))));

        module = getInstance("TCPIP");
        assertEquals(1, module.getDependencies().size());
        assertTrue(module.getDependencies().iterator().next().equals(getInstance("NETCARD")));

        module = getInstance("NETCARD");
        assertEquals(0, module.getDependencies().size());


        module = getInstance("DNS");
        assertEquals(2, module.getDependencies().size());
        assertTrue(module.getDependencies().containsAll(asList(getInstance("TCPIP"), getInstance("NETCARD"))));

        module = getInstance("BROWSER");
        assertEquals(2, module.getDependencies().size());
        assertTrue(module.getDependencies().containsAll(asList(getInstance("TCPIP"), getInstance("HTML"))));
    }

    @Test
    public void testRemoveCommand() throws Exception {
        String input = "DEPEND TELNET TCPIP NETCARD\n" +
                "DEPEND TCPIP NETCARD\n" +
                "DEPEND DNS TCPIP NETCARD\n" +
                "DEPEND BROWSER TCPIP HTML\n" +
                "INSTALL NETCARD\n" +
                "INSTALL TELNET\n" +
                "INSTALL foo\n" +
                "REMOVE NETCARD\n" +
                "INSTALL BROWSER\n" +
                "INSTALL DNS\n" +
                "END";

        Main app = new Main(new ByteArrayInputStream(input.getBytes()), new PrintStream(new ByteArrayOutputStream()));
        app.process();


        List<Module> result = Module.getAll().stream().filter(Module::isInstalled).collect(toList());
        assertEquals(7, result.size());
        assertTrue(result.containsAll(asList(getInstance("NETCARD"), getInstance("TCPIP"), getInstance("TELNET"),
                getInstance("foo"), getInstance("BROWSER"), getInstance("HTML"), getInstance("DNS"))));
    }

    @Test
    public void testBigRemoveCommand() throws Exception {
        String input = "DEPEND TELNET TCPIP NETCARD\n" +
                "DEPEND TCPIP NETCARD\n" +
                "DEPEND DNS TCPIP NETCARD\n" +
                "DEPEND BROWSER TCPIP HTML\n" +
                "INSTALL NETCARD\n" +
                "INSTALL TELNET\n" +
                "INSTALL foo\n" +
                "REMOVE NETCARD\n" +
                "INSTALL BROWSER\n" +
                "INSTALL DNS\n" +
                "LIST\n" +
                "REMOVE TELNET\n" +
                "REMOVE NETCARD\n" +
                "REMOVE DNS\n" +
                "REMOVE NETCARD\n" +
                "INSTALL NETCARD\n" +
                "REMOVE TCPIP\n" +
                "REMOVE BROWSER\n" +
                "REMOVE TCPIP\n" +
                "END";
        Main app = new Main(new ByteArrayInputStream(input.getBytes()), new PrintStream(new ByteArrayOutputStream()));
        app.process();
        assertEquals(new HashSet<>(asList(getInstance("NETCARD"), getInstance("foo"))), Module.getInstalled());
    }

    @Test
    public void testStringOutput() throws Exception {
        String input = "DEPEND TELNET TCPIP NETCARD\n" +
                "DEPEND TCPIP NETCARD\n" +
                "DEPEND DNS TCPIP NETCARD\n" +
                "DEPEND BROWSER TCPIP HTML\n" +
                "INSTALL NETCARD\n" +
                "NETCARD successfully installed\n" +
                "INSTALL TELNET\n" +
                "TCPIP successfully installed\n" +
                "TELNET successfully installed INSTALL foo foo successfully installed\n" +
                "REMOVE NETCARD\n" +
                "NETCARD is still needed\n" +
                "INSTALL BROWSER\n" +
                "HTML successfully installed\n" +
                "BROWSER successfully installed\n" +
                "INSTALL DNS\n" +
                "DNS successfully installed\n" +
                "LIST\n" +
                "HTML BROWSER DNS NETCARD foo TCPIP TELNET\n" +
                "REMOVE TELNET\n" +
                "TELNET successfully removed\n" +
                "REMOVE NETCARD\n" +
                "NETCARD is still needed\n" +
                "REMOVE DNS\n" +
                "DNS successfully removed\n" +
                "REMOVE NETCARD\n" +
                "NETCARD is still needed\n" +
                "INSTALL NETCARD\n" +
                "NETCARD is already installed\n" +
                "REMOVE TCPIP\n" +
                "TCPIP is still needed\n" +
                "REMOVE BROWSER\n" +
                "BROWSER successfully removed\n" +
                "HTML is no longer needed\n" +
                "HTML successfully removed\n" +
                "TCPIP is no longer needed\n" +
                "TCPIP successfully removed\n" +
                "REMOVE TCPIP\n" +
                "TCPIP is not installed\n" +
                "LIST\n" +
                "NETCARD foo\n" +
                "END";


        ByteArrayOutputStream out_stream = new ByteArrayOutputStream();

        Main app = new Main(new ByteArrayInputStream(input.getBytes()), new PrintStream(out_stream));
        app.process();
        String result = out_stream.toString();
        String[] lines = result.split("\n");
        Arrays.stream(lines).forEach(System.out::println);
        assertEquals(50, lines.length);

    }
}

