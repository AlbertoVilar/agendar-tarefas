package com.vilardev.Daily;

import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class DailyApplication {

    public static void main(String[] args) throws SQLException {
        // Start H2 TCP server early so the datasource can connect to jdbc:h2:tcp://localhost/mem:dailydb
        Server tcpServer = null;
        try {
            tcpServer = Server.createTcpServer("-tcp", "-tcpPort", "9092", "-tcpDaemon").start();
            System.out.println("H2 TCP server started on port 9092");
        } catch (SQLException ex) {
            System.err.println("Failed to start H2 TCP server: " + ex.getMessage());
            // proceed; if TCP server is already running, the app may still connect
        }

        Server finalTcpServer = tcpServer;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (finalTcpServer != null) {
                finalTcpServer.stop();
                System.out.println("H2 TCP server stopped");
            }
        }));

        SpringApplication.run(DailyApplication.class, args);
    }

}
