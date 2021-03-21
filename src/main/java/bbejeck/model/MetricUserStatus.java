package bbejeck.model;

import lombok.Data;

import java.util.Date;

public class MetricUserStatus {

    private String metric;
    private String uid;
    private String expression;
    private Date createTime;

    private Double price;



    public static MetricUserStatus fromMetricAndMetricUser(Metric m,MetricUser mu){
        MetricUserStatus mus = new MetricUserStatus();
        if (m != null) {
            mus.setMetric(m.getMetric());
            mus.setPrice(m.getPrice());
            mus.setCreateTime(m.getCreateTime());
        }
        if (mu != null) {
            mus.setExpression(mu.getExpression());
            mus.setUid(mu.getUid());
        }
        return mus;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
