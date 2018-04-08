package com.example.dxc.xfdemo.model;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 3/30/2018-10:02 AM.
 * @Version 1.0
 */

public class StockMDL {
    private String symbol;//代码
    private String code;//简码
    private String name;//名称
    private double trade;//最新价
    private double pricechange;//涨跌额
    private double changepercent;//涨跌幅
    private double buy;//买入
    private double sell;//卖出
    private double settlement;//昨收
    private double open;//今开
    private double high;//最高
    private double low;//最低
    private double volume;//成交量
    private double amount;//成效额
    private String ticktime;//更新时间
    private double per;//本益比（普通股每股市场价格/普通股每股每年盈利）
    private double pb;//平均市净率（平均市净率=股价/账面价值。其中，账面价值=总资产-无形资产-负债-优先股权益）
    private double mktcap;//总市值（单位：万）
    private double nmc;//流通市值（单位：万）
    private double turnoverratio;//换手率

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

    public double getTrade() {
        return trade;
    }

    public void setTrade(double trade) {
        this.trade = trade;
    }

    public double getPricechange() {
        return pricechange;
    }

    public void setPricechange(double pricechange) {
        this.pricechange = pricechange;
    }

    public double getChangepercent() {
        return changepercent;
    }

    public void setChangepercent(double changepercent) {
        this.changepercent = changepercent;
    }

    public double getBuy() {
        return buy;
    }

    public void setBuy(double buy) {
        this.buy = buy;
    }

    public double getSell() {
        return sell;
    }

    public void setSell(double sell) {
        this.sell = sell;
    }

    public double getSettlement() {
        return settlement;
    }

    public void setSettlement(double settlement) {
        this.settlement = settlement;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTicktime() {
        return ticktime;
    }

    public void setTicktime(String ticktime) {
        this.ticktime = ticktime;
    }

    public double getPer() {
        return per;
    }

    public void setPer(double per) {
        this.per = per;
    }

    public double getPb() {
        return pb;
    }

    public void setPb(double pb) {
        this.pb = pb;
    }

    public double getMktcap() {
        return mktcap;
    }

    public void setMktcap(double mktcap) {
        this.mktcap = mktcap;
    }

    public double getNmc() {
        return nmc;
    }

    public void setNmc(double nmc) {
        this.nmc = nmc;
    }

    public double getTurnoverratio() {
        return turnoverratio;
    }

    public void setTurnoverratio(double turnoverratio) {
        this.turnoverratio = turnoverratio;
    }
}