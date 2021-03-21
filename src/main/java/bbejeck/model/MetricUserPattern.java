package bbejeck.model;

import lombok.Data;
import org.apache.commons.lang.math.RandomUtils;

import java.util.Date;


public class MetricUserPattern {
    //指标 - 指标编码，指标名称，指标单位（可选）
    private String metric;
    //用户 - 用户UID，用户邮箱，用户密码
    private String uid;
    //指标值
    private Double price;
    //匹配级别（ok：未匹配,crit：匹配）
    private String level;
    //匹配描述（虚拟货币-比特币-5分钟之内价格 已经大于 5 美元）
    private String content;
    //指标生成时间
    private Date createTime;

    
    /**
     * 完整的通知
     * 标题：虚拟货币-比特币-5分钟之内价格 已经大于 5 美元
     * 内容：尊敬的客户，您关注的虚拟货币-比特币-5分钟之内价格 已经大于 5 美元，数据生成时间为2021/03/19T00:00:00
     *
     * 指标编码：大类-产品编码-关注属性-聚合方式-窗口，产品编码可以为变长
     */
    public static MetricUserPattern fromMetricUserStatus(MetricUserStatus mus){
        MetricUserPattern mup = new MetricUserPattern();

        if (mus != null) {
            mup.setMetric(mus.getMetric());
            mup.setCreateTime(mus.getCreateTime());
            mup.setUid(mus.getUid());
            mup.setPrice(mus.getPrice());
            //TODO
            int mock = RandomUtils.nextInt(10);
            if (mock < 3) {
                mup.setLevel("crit");
                mup.setContent("命中规则啦");
            }else{
                mup.setLevel("ok");
                mup.setContent(null);
            }


        }

        return mup;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
