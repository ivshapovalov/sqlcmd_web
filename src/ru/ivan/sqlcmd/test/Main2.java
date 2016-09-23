package ru.ivan.sqlcmd.test;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by oleksandr.baglai on 08.09.2015.
 * Thanks to http://blog.marcnuri.com/blog/defaul/2007/03/27/Java-App-for-PostgreSQL-scheduled-backups-using-pg-dump-Windows-only
 */
public class Main2 {

    public static void main(String[] args) {
        try {
            //We initialize some variables that well be using
            Runtime r = Runtime.getRuntime();
            //Path to the place we store our backups
            String rutaCT = "C:\\PostgreSQL\\BACKUP\\";
            //PostgreSQL variables
            String IP = "localhost";
            String user = "postgres";
            String dbase = "sqlcmd";
            String password = "postgres";
            Process p;
            ProcessBuilder pb;
            //We build a string with todayвs date (This will be the backupвs filename)
            TimeZone zonah = TimeZone.getTimeZone("GMT+1");
            Calendar Calendario = GregorianCalendar.getInstance(zonah, new Locale("es"));
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            StringBuffer date = new StringBuffer();
            date.append(df.format(Calendario.getTime()));

            File file = new File(rutaCT);
            /*We test if the path to our programs exists*/
            if (file.exists()) {
                 /*We then test if the file weвre going to generate exists too. If it exists we will delete it*/
                StringBuffer fechafile = new StringBuffer();
                fechafile.append(rutaCT);
                fechafile.append(date.toString());
                fechafile.append(".backup");
                File ficherofile = new File(fechafile.toString());
                //Probamos a ver si existe ese Гєltimo dato
                if (ficherofile.exists()) {
                    //Lo Borramos
                    ficherofile.delete();
                }

                pb = new ProcessBuilder("C:\\Program Files\\PostgreSQL\\9.5\\bin\\"+"pg_dump.exe", "-f", fechafile.toString(), "-F", "c", "-Z", "9", "-v", "-o", "-h", IP, "-U", user, dbase);
                pb.environment().put("PGPASSWORD", password);
                pb.redirectErrorStream(true);
                p = pb.start();
                try {
                    InputStream is = p.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is,"CP1251");
                    BufferedReader br = new BufferedReader(isr);
                    String ll;
                    while ((ll = br.readLine()) != null) {
                        System.out.println(ll);
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }

        } catch (IOException x) {
            System.err.println("Could not invoke browser, command=");
            System.err.println("Caught: " + x.getMessage());
        }
    }

}