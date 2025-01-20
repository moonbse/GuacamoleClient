package com;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.GuacamoleSocket;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.apache.guacamole.net.InetGuacamoleSocket;
import org.apache.guacamole.net.SimpleGuacamoleTunnel;
import org.apache.guacamole.protocol.ConfiguredGuacamoleSocket;
import org.apache.guacamole.protocol.GuacamoleConfiguration;
import org.apache.guacamole.servlet.GuacamoleHTTPTunnelServlet;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

public class GuacamoleTunnelServlet extends GuacamoleHTTPTunnelServlet {

    private static final Logger logger = Logger.getLogger(GuacamoleTunnelServlet.class.getName());
    @Override
    protected GuacamoleTunnel doConnect(HttpServletRequest request)
            throws GuacamoleException {
        logger.info("[INFO] Received GET request at /tunnel from " + request.getRemoteAddr());
        // Create our configuration
        GuacamoleConfiguration config = new GuacamoleConfiguration();
        config.setProtocol("ssh");
        config.setParameter("hostname", "ip--address");
        config.setParameter("port", "22");
        config.setParameter("username", "userName");
        config.setParameter("password", "passwordd");
        logger.info("Connecting with config " + config.getParameter("hostname"));
        // Connect to guacd - everything is hard-coded here.

        try {
            // Attempt to create the socket connection
            GuacamoleSocket socket = new ConfiguredGuacamoleSocket(
                    new InetGuacamoleSocket("localhost", 4822),
                    config
            );

            logger.info("[SUCCESS] Guacamole connection established successfully.");
            return new SimpleGuacamoleTunnel(socket);

        } catch (GuacamoleException e) {
            logger.severe("[ERROR] Failed to create Guacamole socket: " + e.getMessage());
            e.printStackTrace();  // Log stack trace for debugging
        } catch (Exception e) {
            logger.severe("[CRITICAL] Unexpected exception while creating Guacamole socket: " + e.getMessage());
            e.printStackTrace();
        }


        // Return a new tunnel which uses the connected socket
        return null;

    }
}