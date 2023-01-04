package com.influxdb;

import java.time.Instant;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

@Measurement(name = "medicion")
public class Medicion {

    public Medicion(Integer id, Instant instante, double valor, String nis, Integer idObis, Integer idMedidor) {
        this.id = id;
        this.instante = instante;
        this.valor = valor;
        this.nis = nis;
        this.idObis = idObis;
        this.idMedidor = idMedidor;
    }

    public Medicion() {
        //empty constructor
    }

    @Column(tag = true)
    private Integer id;

    @Column(timestamp = true)
    private Instant instante;

    @Column
    private double valor;

    @Column
    private String nis;

    @Column
    private Integer idObis;

    @Column
    private Integer idMedidor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getInstante() {
        return instante;
    }

    public void setInstante(Instant instante) {
        this.instante = instante;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public Integer getIdObis() {
        return idObis;
    }

    public void setIdObis(Integer idObis) {
        this.idObis = idObis;
    }

    public Integer getIdMedidor() {
        return idMedidor;
    }

    public void setIdMedidor(Integer idMedidor) {
        this.idMedidor = idMedidor;
    }

}
