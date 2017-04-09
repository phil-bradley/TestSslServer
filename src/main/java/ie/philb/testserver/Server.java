/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ie.philb.testserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.Security;
import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
/**
 *
 * @author philb
 */
public class Server {

    private int port = 1234;
    private SSLServerSocket ss;

    public Server() throws Exception {

        System.setProperty("javax.net.debug", "all");
        
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        
        String keyStoreFileName = "C:\\Users\\philb\\Documents\\NetBeansProjects\\TestServer\\testcert.jks";
        char[] keyStorePassword = "password".toCharArray();

        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(keyStoreFileName), keyStorePassword);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, keyStorePassword);

        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(kmf.getKeyManagers(), null, null);

        ServerSocketFactory ssf = sslcontext.getServerSocketFactory();
        ss = (SSLServerSocket) ssf.createServerSocket(port);

        /**
         * *************************************************
         */
//        ServerSocketFactory ssf = ServerSocketFactory.getDefault();
//        ss = ssf.createServerSocket(port);
        //SSLServerSocketFactory ssfx = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        //this.ss = ssfx.createServerSocket(port);
        //ss = new ServerSocket(port);
    }

    public void run() throws IOException {
        while (true) {
            System.out.println("Waiting for connection");
            SSLSocket s = (SSLSocket) ss.accept();
            System.out.println("Got connection, starting thread");
            new ServerThread(s).start();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Starting server");
        Server m = new Server();
        m.run();
    }
}
