/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ie.philb.testserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Date;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author philb
 */
public class ServerThread extends Thread {

    protected Socket socket = null;

    public ServerThread(Socket s) {
        this.socket = s;
    }

    @Override
    public void run() {
        InputStream is = null;
        OutputStream os = null;

        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();

            SSLSession session = ((SSLSocket) socket).getSession();

            System.out.println("Peer host is " + session.getPeerHost());
            System.out.println("Cipher is " + session.getCipherSuite());
            System.out.println("Protocol is " + session.getProtocol());
            System.out.println("ID is " + new BigInteger(session.getId()));
            System.out.println("Session created in " + session.getCreationTime());
            System.out.println("Session accessed in " + session.getLastAccessedTime());

            System.out.println("Sending response");
            String msg = "Thank you, the time is " + new Date();
            os.write(msg.getBytes());
            os.flush();
            System.out.println("Sent response -->" + msg + "<--");
        } catch (Exception ex) {

        } finally {
            close(is);
            close(os);
            close(socket);
        }
    }

    private void close(Socket s) {
        if (s == null) {
            return;
        }
        try {
            s.close();
        } catch (Exception ex) {
        }
    }

    private void close(OutputStream os) {
        if (os == null) {
            return;
        }
        try {
            os.close();
        } catch (Exception ex) {
        }
    }

    private void close(InputStream is) {
        if (is == null) {
            return;
        }
        try {
            is.close();
        } catch (Exception ex) {
        }
    }
}
