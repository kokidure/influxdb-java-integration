package com.influxdb;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App 
{
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main( String[] args )
    {
        LOG.info("##### INFLUX/JAVA integration #####");

        String url = "http://192.168.33.10:8086";
        //String token = "HrntqqSRX8ep19Lmu4daHy9o9O7yMx2Oat5xNkN5BJeSXgLXv66aU_1g-rbENJmXbpFPE2Wwn0UivhMN-3Frgg==";
        String token = "o3X7R83l7zN1-7jXtxbqJ9NgleFXfFEyYbvlHEcFESXDniqVLNkE-cjptaq1lzEONAS_jqbpBNSo0cVrhiY95Q==";
        String bucket = "samr-test";
        String org = "PTI-SAMR";
        
        Connection conexion = new Connection(url, token, bucket, org);

        /* 
        LOG.warn("----- SINGLE POINT WRITE -----");
        Medicion m1 = new Medicion();

        m1.setId(7);
        m1.setInstante(Instant.now());
        m1.setValor(5.75);
        m1.setNis("AABCD5478");
        m1.setIdObis(11);
        m1.setIdMedidor(1);

        conexion.singlePointWrite(m1);
 */

        /* 
        LOG.warn("----- MULTIPLE POINTS WRITE -----");

        List<Medicion> mList = new LinkedList<>();

        for (int i = 0; i < 1000; i++) {
            Medicion m = new Medicion(generateRandomNumber(1, 5),
                                        Instant.now(),
                                        generateRandomNumber(0, 10),
                                        String.valueOf(generateRandomNumber(100000, 200000)),
                                        generateRandomNumber(1, 10),
                                        generateRandomNumber(1, 5));
            
            mList.add(m);
            try {
                System.out.print(".");
                Thread.sleep(10);
            } catch (InterruptedException e) {
                LOG.error("INTERRUPTED EXCEPTION",e);
            }
        }
        
        System.out.println("\n");
        conexion.multiPointWrite(mList);
         */

         
        LOG.warn("----- DETELE POINTS -----");

        OffsetDateTime start = OffsetDateTime.now().minus(30, ChronoUnit.MINUTES);
        OffsetDateTime end = OffsetDateTime.now();
        conexion.deleteRecord(start, end);
       

/* 
        LOG.warn("----- QUERY DATA -----");

        conexion.queryData(); */
    }

    private static int generateRandomNumber (int min, int max) {
        Random random = new Random();

        return min + random.nextInt(max);
    }
}