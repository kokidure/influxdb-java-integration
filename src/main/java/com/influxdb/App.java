package com.influxdb;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
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
        String token = "HrntqqSRX8ep19Lmu4daHy9o9O7yMx2Oat5xNkN5BJeSXgLXv66aU_1g-rbENJmXbpFPE2Wwn0UivhMN-3Frgg==";
        String bucket = "samr-test";
        String org = "PTI-SAMR";
        
        Connection c = new Connection(url, token, bucket, org);

        //LOG.warn("----- SINGLE POINT WRITE -----");
        Medicion m1 = new Medicion();

        m1.setId(generateRandomNumber(0, 100));
        m1.setInstante(Instant.now());
        m1.setValor(generateRandomNumber(0, 10000));
        m1.setNis(String.valueOf(generateRandomNumber(100000, 200000)));
        m1.setIdObis(generateRandomNumber(1, 10));
        m1.setIdMedidor(generateRandomNumber(1, 10));

        // c.singlePointWrite(m1);

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
        }

        c.multiPointWrite(mList);

        /*
        LOG.warn("----- DETELE POINTS -----");

        OffsetDateTime start = OffsetDateTime.now().minus(1, ChronoUnit.HOURS);
        OffsetDateTime end = OffsetDateTime.now();
        c.deleteRecord(start, end);
        */

        //LOG.warn("----- QUERY DATA -----");
    }

    private static int generateRandomNumber (int min, int max) {
        Random random = new Random();

        return min + random.nextInt(max);
    }
}

