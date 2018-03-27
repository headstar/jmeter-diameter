import groovy.util.logging.Slf4j
import org.jdiameter.api.*
import org.jdiameter.client.impl.StackImpl
import org.jdiameter.client.impl.helpers.XMLConfiguration

import java.util.concurrent.TimeUnit

@Slf4j
class DiameterStack {

    private Stack clientStack;
    private SessionFactory sessionFactory;

    DiameterStack() {
        clientStack = new StackImpl();

        sessionFactory = clientStack.init(createConfiguration("somefile"));
        log.info("Diameter stack initialized");

        Set<org.jdiameter.api.ApplicationId> appIds = stack.getMetaData().getLocalPeer().getCommonApplications();
        log.info("Diameter stack supports " + appIds.size() + " applications");

        // TODO: is this needed?
        Network network = stack.unwrap(Network.class);
        network.addNetworkReqListener(new NetworkReqListener() {
            @Override
            public Answer processRequest(Request request) {
                return null;
            }
        }, this.authAppId);
    }

    void start() {
        clientStack.start();
    }

    void stop() {
        if (clientStack.isActive()) {
            clientStack.stop(10, TimeUnit.SECONDS, DisconnectCause.REBOOTING);
        }
    }

    void destroy() {
        stop();
        clientStack.destroy();
    }

    private Configuration createConfiguration(String configFile) {
        // TODO: groovyfy resource management!
        InputStream is = null;
        try {
            is = getClass().getResourceAsStream(client_cfgFile);
            return new XMLConfiguration(is);
        } finally {
            if(is != null) {
                is.close();
            }
        }
    }
}