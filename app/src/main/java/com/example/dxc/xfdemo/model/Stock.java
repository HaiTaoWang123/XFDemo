package com.example.dxc.xfdemo.model;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 3/30/2018-10:02 AM.
 * @Version 1.0
 */

public class Stock {
    private String symbol;//代码
    private String code;//简码
    private String name;//名称
    private String trade;//最新价
    private String pricechange;//涨跌额
    private String changepercent;//涨跌幅
    private String buy;//买入
    private String sell;//卖出
    private String settlement;//昨收
    private String open;//今开
    private String high;//最高
    private String low;//最低
    private String volume;//成交量
    private String amount;//成效额
    private String ticktime;//更新时间
    private String per;//": 4.846,
    private String pb;//": 0.883,
    private String mktcap;//": 3.4195173662505E7,
    private String nmc;//": 3.2740884942335E7,
    private String turnoverratio;//": 0.08281

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public String getPricechange() {
        return pricechange;
    }

    public void setPricechange(String pricechange) {
        this.pricechange = pricechange;
    }

    public String getChangepercent() {
        return changepercent;
    }

    public void setChangepercent(String changepercent) {
        this.changepercent = changepercent;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getSettlement() {
        return settlement;
    }

    public void setSettlement(String settlement) {
        this.settlement = settlement;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTicktime() {
        return ticktime;
    }

    public void setTicktime(String ticktime) {
        this.ticktime = ticktime;
    }

    public String getPer() {
        return per;
    }

    public void setPer(String per) {
        this.per = per;
    }

    public String getPb() {
        return pb;
    }

    public void setPb(String pb) {
        this.pb = pb;
    }

    public String getMktcap() {
        return mktcap;
    }

    public void setMktcap(String mktcap) {
        this.mktcap = mktcap;
    }

    public String getNmc() {
        return nmc;
    }

    public void setNmc(String nmc) {
        this.nmc = nmc;
    }

    public String getTurnoverratio() {
        return turnoverratio;
    }

    public void setTurnoverratio(String turnoverratio) {
        this.turnoverratio = turnoverratio;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", trade='" + trade + '\'' +
                ", pricechange='" + pricechange + '\'' +
                ", changepercent='" + changepercent + '\'' +
                ", buy='" + buy + '\'' +
                ", sell='" + sell + '\'' +
                ", settlement='" + settlement + '\'' +
                ", open='" + open + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", volume='" + volume + '\'' +
                ", amount='" + amount + '\'' +
                ", ticktime='" + ticktime + '\'' +
                ", per='" + per + '\'' +
                ", pb='" + pb + '\'' +
                ", mktcap='" + mktcap + '\'' +
                ", nmc='" + nmc + '\'' +
                ", turnoverratio='" + turnoverratio + '\'' +
                '}';
    }
}