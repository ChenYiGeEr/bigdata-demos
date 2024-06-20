package com.lim.demos.notice.pojo.message;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * FeiShuMsgPostContentItem
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/20 下午4:27
 */
public class FeiShuMsgPostContentItem implements Serializable {

    private String tag = "text";

    private String text;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public FeiShuMsgPostContentItem() {
    }

    public FeiShuMsgPostContentItem(String text) {
        this.text = text;
    }

    public FeiShuMsgPostContentItem(String tag, String text) {
        this.tag = tag;
        this.text = text;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("tag", getTag())
                .append("text", getText())
                .toString();
    }
}