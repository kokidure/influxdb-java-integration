package com.influxdb;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.influxdb.client.DeleteApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.exceptions.InfluxException;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

public class Connection {

    private static final Logger LOG = LoggerFactory.getLogger(Connection.class);

    private String url;
    private String token;
    private String bucket;
    private String org;

    private InfluxDBClient client;

    public Connection(String url, String token, String bucket, String org) {
        this.url = url;
        this.token = token;
        this.bucket = bucket;
        this.org = org;

        client = buildConnection();

    }

    private InfluxDBClient buildConnection() {
        return InfluxDBClientFactory.create(getUrl(), getToken().toCharArray(), getOrg(), getBucket());

    }

    public boolean singlePointWrite() {
        boolean flag = false;

        Point point1 = Point.measurement("sensor").addTag("sensor_id", "TLM0103")
                .addField("location", "Mechanical Room").addField("model_number", "TLM90012Z")
                .time(Instant.now(), WritePrecision.MS);

        try {
            WriteApiBlocking writeApi = client.getWriteApiBlocking();
            writeApi.writePoint(point1);
            flag = true;
        } catch (InfluxException e) {
            LOG.error("Error writing single point", e);
        }

        return flag;
    }

    public boolean singlePointWrite(Medicion m) {
        boolean flag = false;

        if (client == null) {
            client = buildConnection();
        }

        try {
            WriteApiBlocking writeApi = client.getWriteApiBlocking();
            writeApi.writeMeasurement(WritePrecision.MS, m);
            flag = true;
            LOG.info("SINGLE POINT WRITE OK");
        } catch (InfluxException e) {
            LOG.error("Error writing single point", e);
        }

        return flag;
    }

    public boolean multiPointWrite(List<Medicion> mList) {
        boolean flag = false;

        if (client == null) {
            client = buildConnection();
        }

        try {
            WriteApiBlocking writeApi = client.getWriteApiBlocking();

            if (mList != null) {
                writeApi.writeMeasurements(WritePrecision.MS, mList);
                flag = true;
            }
            LOG.info("MULTIPLE POINTS WRITE OK");
        } catch (InfluxException e) {
            LOG.error("Error writing multiple points", e);
        }

        return flag;
    }

    public boolean deleteRecord(OffsetDateTime start, OffsetDateTime end) {
        boolean flag = false;
        DeleteApi deleteApi = client.getDeleteApi();

        try {
            String predicate = "_measurement=\"medicion\"";

            deleteApi.delete(start, end, predicate, getBucket(), getOrg());

            flag = true;
        } catch (InfluxException e) {
            LOG.error("Error deleting points between " + start + " and " + end, e);
        }

        return flag;
    }

    public List<Medicion> queryData() {
        QueryApi queryApi = client.getQueryApi();

        String flux = "from(bucket:\"" + getBucket() + "\") |> range(start:-15m) "
                + "|> filter(fn: (r) => r[\"_measurement\"] == \"medicion\") "
                + "|> sort() |> yield(name: \"sort\")";

        List<FluxTable> table = queryApi.query(flux);
        List<Medicion> result = new LinkedList<>();

        for (FluxTable t : table) {
            List<FluxRecord> records = t.getRecords();

            for (FluxRecord r : records) {
                /*
                 * Medicion m = new Medicion();
                 * 
                 * m.setId((Integer)r.getValueByKey("id"));
                 * m.setIdMedidor((Integer)r.getValueByKey("id_medidor"));
                 * m.setInstante((Instant)r.getValueByKey("instante"));
                 * m.setValor((Double)r.getValueByKey("valor"));
                 * 
                 * result.add(m);
                 */

                LOG.info(r.getValueByKey("id_medidor") + "\t" + r.getValueByKey("valor"));
            }
        }

        return result;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public InfluxDBClient getClient() {
        if (client == null) {
            buildConnection();
        }

        return client;
    }

}
