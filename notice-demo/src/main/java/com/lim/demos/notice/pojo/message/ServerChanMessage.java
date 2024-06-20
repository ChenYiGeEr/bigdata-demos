package com.lim.demos.notice.pojo.message;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * ServerChanMessage
 * <p>server酱消息对象</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/19 下午5:18
 */
public class ServerChanMessage {

    /** 消息标题，必填。最大长度为 32 。 */
    private String title;

    /** 消息内容，选填。支持 Markdown语法 ，最大长度为 32KB ,消息卡片截取前 30 显示。 */
    private String desp;

    /** 消息卡片内容，选填。最大长度64。如果不指定，将自动从desp中截取生成。 */
    private String shorts;

    /** 是否隐藏调用IP，选填。如果不指定，则显示；为1则隐藏。 */
    private Integer noip;

    /**
     * 动态指定本次推送使用的消息通道，选填。如不指定，则使用网站上的消息通道页面设置的通道。支持最多两个通道，多个通道值用竖线|隔开。
     * 测试号=0
     * 企业微信群机器人=1
     * 钉钉群机器人=2
     * 飞书群机器人=3
     * Bark iOS=8
     * 方糖服务号=9
     * PushDeer=18
     * 企业微信应用消息=66
     * 自定义=88
     * 官方Android版·β=98
     * */
    private String channel;

    /** 消息抄送的openid，选填。只支持测试号和企业微信应用消息通道。测试号的 openid 从测试号页面获得 ，多个 openid 用 , 隔开。企业微信应用消息通道的 openid 参数，内容为接收人在企业微信中的 UID（可在消息通道页面配置好该通道后通过链接查看） , 多个人请 | 隔开，即可发给特定人/多个人。不填则发送给通道配置页面的接收人。 */
    private String openid;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public String getShorts() {
        return shorts;
    }

    public void setShorts(String shorts) {
        this.shorts = shorts;
    }

    public Integer getNoip() {
        return noip;
    }

    public void setNoip(Integer noip) {
        this.noip = noip;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public ServerChanMessage(){}

    public ServerChanMessage(String title, String desp, String shorts, Integer noip, String channel, String openid) {
        this.title = title;
        this.desp = desp;
        this.shorts = shorts;
        this.noip = noip;
        this.channel = channel;
        this.openid = openid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("title", getTitle())
                .append("desp", getDesp())
                .append("shorts", getShorts())
                .append("noip", getNoip())
                .append("channel", getChannel())
                .append("openid", getOpenid())
                .toString();
    }
}
