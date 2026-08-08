package nihvostain.wordle_backend.common.managment;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

@Configuration
public class JMXConfig {

    private final RequestCounter requestCounter;

    public JMXConfig(RequestCounter requestCounter) {
        this.requestCounter = requestCounter;
    }

    @PostConstruct
    public void registerMBean() throws Exception {

        MBeanServer server = ManagementFactory.getPlatformMBeanServer();

        ObjectName name = new ObjectName("wordle.monitoring:type=RequestCounter");

        if (!server.isRegistered(name)) {
            server.registerMBean(requestCounter, name);
        }

    }
}