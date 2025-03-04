package ru.ivannovr;

import ru.ivannovr.gui.menu.LoginPanel;
import ru.ivannovr.utils.DatabaseManager;

import javax.swing.*;
import java.net.URL;
import java.util.jar.Manifest;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
        String dbUrl = null;
        String dbUser = null;
        String dbPassword = null;

        for (String arg : args) {
            if (arg.startsWith("--db-url=")) {
                dbUrl = arg.substring("--db-url=".length());
            } else if (arg.startsWith("--db-user=")) {
                dbUser = arg.substring("--db-user=".length());
            } else if (arg.startsWith("--db-password=")) {
                dbPassword = arg.substring("--db-password=".length());
            }
        }

        if (dbUrl == null || dbUser == null || dbPassword == null) {
            String jarName = getJarName();
            String jarVersion = getJarVersion();
            System.err.println("Missing required arguments. Usage: java -jar " +
                    jarName + (jarVersion != null ? "-" + jarVersion : "") + ".jar " +
                    "--db-url=<url> --db-user=<user> --db-password=<password>");
            System.exit(1);
        }

        DatabaseManager dbManager = new DatabaseManager(dbUrl, dbUser, dbPassword);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Java Game Box");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setSize(600, 600);
            frame.setLocationRelativeTo(null);

            URL iconURL = App.class.getResource("/icon.png");
            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                frame.setIconImage(icon.getImage());
            } else {
                System.err.println("Icon resource not found: /icon.png");
            }

            frame.setContentPane(new LoginPanel(frame, dbManager));
            frame.setVisible(true);
        });
    }

    private static String getJarName() {
        URL resource = App.class.getResource("/" + App.class.getName().replace('.', '/') + ".class");
        if (resource == null) {
            return "JavaGameBox";
        }
        String classJar = resource.toString();
        if (classJar.startsWith("jar:")) {
            try {
                String manifestPath = classJar.substring(0, classJar.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF";
                URL manifestURL = new URL(manifestPath);
                Manifest manifest = new Manifest(manifestURL.openStream());
                String title = manifest.getMainAttributes().getValue("Implementation-Title");
                return title != null ? title : "JavaGameBox";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "JavaGameBox";
    }

    private static String getJarVersion() {
        URL resource = App.class.getResource("/" + App.class.getName().replace('.', '/') + ".class");
        if (resource == null) {
            return null;
        }
        String classJar = resource.toString();
        if (classJar.startsWith("jar:")) {
            try {
                String manifestPath = classJar.substring(0, classJar.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF";
                URL manifestURL = new URL(manifestPath);
                Manifest manifest = new Manifest(manifestURL.openStream());
                return manifest.getMainAttributes().getValue("Implementation-Version");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}