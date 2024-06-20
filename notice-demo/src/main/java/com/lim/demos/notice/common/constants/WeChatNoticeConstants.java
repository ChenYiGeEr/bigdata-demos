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

package com.lim.demos.notice.common.constants;

public final class WeChatNoticeConstants {

    public static final String MARKDOWN_QUOTE = ">";

    public static final String MARKDOWN_ENTER = "\n";

    public static final String CHARSET = "UTF-8";

    public static final String WE_CHAT_PUSH_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token={token}";

    public static final String WE_CHAT_APP_CHAT_PUSH_URL = "https://qyapi.weixin.qq.com/cgi-bin/appchat/send?access_token={token}";

    public static final String WE_CHAT_TOKEN_URL =
            "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={corpId}&corpsecret={secret}";

    public static final String WE_CHAT_CONTENT_KEY = "content";

    public static final String WE_CHAT_MESSAGE_TYPE_TEXT = "text";

    public static final Integer WE_CHAT_MESSAGE_SAFE_PUBLICITY = 0;

    public static final Integer WE_CHAT_MESSAGE_SAFE_PRIVACY = 1;

    public static final Integer WE_CHAT_ENABLE_ID_TRANS = 0;

    public static final Integer WE_CHAT_DUPLICATE_CHECK_INTERVAL_ZERO = 0;

    private WeChatNoticeConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
