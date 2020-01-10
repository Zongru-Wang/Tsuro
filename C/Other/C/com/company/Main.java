package com.company;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.io.*;
import java.net.*;


/**
 * Entry point for the application
 */
public class Main {

    public static void main(String[] args) {
	    INamedJsonObjectReader reader = new NamedJsonObjectReader();
        List<INamedJsonObject> objs = null;
        ServerSocket socket = null;
        Socket client = null;
        InputStream in = null;
        PrintWriter out = null;
        Scanner scanner = null;
        try {
            socket = new ServerSocket(8000);
            client = socket.accept();
            in = client.getInputStream();
            scanner = new Scanner(in).useDelimiter("");
            out = new PrintWriter(client.getOutputStream(), true);
            try {
                objs = reader.readAll(scanner);
                Comparator<INamedJsonObject> INamedJsonObjectComparator = Comparator.comparing(INamedJsonObject::getName);
                objs.sort(INamedJsonObjectComparator);
                for (INamedJsonObject obj : objs) {
                    out.println(obj.toString());
                }
            } catch (IllegalArgumentException e) {
                 out.println("Json provided was not valid");
            } catch (Exception e) {
                e.getStackTrace();
            } finally {
                scanner.close();
                in.close();
                out.close();
                client.close();
                socket.close();
            }
        } catch( IOException e ) {
            e.getStackTrace();
        }
    }
}
