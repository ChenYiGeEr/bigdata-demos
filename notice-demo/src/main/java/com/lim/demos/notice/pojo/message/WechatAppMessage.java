/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lim.demos.notice.pojo.message;

import java.util.Map;

public class WechatAppMessage {

    /** 指定接收消息的成员，成员ID列表（多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为"@all"，则向该企业应用的全部成员发送 */
    private String touser;

    /** 消息类型，此时固定为：text */
    private String msgtype;

    /** 企业应用的id，整型。企业内部开发，可在应用的设置页面查看；第三方服务商，可通过接口 获取企业授权信息 获取该参数值 */
    private Integer agentid;

    private Map<String, String> text;

    private Map<String, String> markdown;

    /** 表示是否是保密消息，0表示可对外分享，1表示不能分享且内容显示水印，默认为0 */
    private Integer safe;

    /** 表示是否开启id转译，0表示否，1表示是，默认0。仅第三方应用需要用到，企业自建应用可以忽略。 */
    private Integer enable_id_trans;

    /** 表示是否开启重复消息检查，0表示否，1表示是，默认0 */
    private Integer enable_duplicate_check;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public Integer getAgentid() {
        return agentid;
    }

    public void setAgentid(Integer agentid) {
        this.agentid = agentid;
    }

    public Map<String, String> getText() {
        return text;
    }

    public void setText(Map<String, String> text) {
        this.text = text;
    }

    public Integer getSafe() {
        return safe;
    }

    public void setSafe(Integer safe) {
        this.safe = safe;
    }

    public Integer getEnable_id_trans() {
        return enable_id_trans;
    }

    public void setEnable_id_trans(Integer enable_id_trans) {
        this.enable_id_trans = enable_id_trans;
    }

    public Integer getEnable_duplicate_check() {
        return enable_duplicate_check;
    }

    public void setEnable_duplicate_check(Integer enable_duplicate_check) {
        this.enable_duplicate_check = enable_duplicate_check;
    }

    public Map<String, String> getMarkdown() {
        return markdown;
    }

    public void setMarkdown(Map<String, String> markdown) {
        this.markdown = markdown;
    }

    public WechatAppMessage() {
    }

    public WechatAppMessage(String touser, String msgtype, Integer agentid, Map<String, String> contentMap,
                            Integer safe, Integer enableIdTrans, Integer enableDuplicateCheck) {
        this.touser = touser;
        this.msgtype = msgtype;
        this.agentid = agentid;
        this.markdown = contentMap;
        this.safe = safe;
        this.enable_id_trans = enableIdTrans;
        this.enable_duplicate_check = enableDuplicateCheck;
    }
}
