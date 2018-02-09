package com.vanxd.stock.vo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private Integer id;
    private Double mount = 10000D;
    private Integer hold = 0;
    private Double buyPrice = 0D;
    private LocalDate buyDate;

    /** ** */

    private List<Long> hit = new ArrayList<>();

    public Game() {
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", mount=" + mount +
                ", hold=" + hold +
                ", buyPrice=" + buyPrice +
                ", buyDate=" + buyDate +
                '}';
    }

    public LocalDate getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(LocalDate buyDate) {
        this.buyDate = buyDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Long> getHit() {
        return hit;
    }

    public void setHit(List<Long> hit) {
        this.hit = hit;
    }

    public Double getMount() {
        return mount;
    }

    public void setMount(Double mount) {
        this.mount = mount;
    }

    public Integer getHold() {
        return hold;
    }

    public void setHold(Integer hold) {
        this.hold = hold;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }
}
