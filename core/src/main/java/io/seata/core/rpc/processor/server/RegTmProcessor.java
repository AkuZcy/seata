/*
 *  Copyright 1999-2019 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.seata.core.rpc.processor.server;

import io.seata.common.loader.EnhancedServiceLoader;
import io.seata.common.util.NetUtil;
import io.seata.core.protocol.RegisterTMRequest;
import io.seata.core.protocol.RegisterTMResponse;
import io.seata.core.protocol.Version;
import io.seata.core.rpc.RegisterCheckAuthHandler;
import io.seata.core.rpc.RemotingServer;
import io.seata.core.rpc.SeataChannelServerManager;
import io.seata.core.rpc.processor.MessageReply;
import io.seata.core.rpc.processor.RemotingProcessor;
import io.seata.core.rpc.processor.RpcMessageHandleContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * process TM client registry message.
 * <p>
 * process message type:
 * {@link RegisterTMRequest}
 *
 * @author zhangchenghui.dev@gmail.com
 * @since 1.3.0
 */
public class RegTmProcessor implements RemotingProcessor<RegisterTMRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegTmProcessor.class);

    private RemotingServer remotingServer;

    private RegisterCheckAuthHandler checkAuthHandler;

    public RegTmProcessor(RemotingServer remotingServer) {
        this.remotingServer = remotingServer;
        this.checkAuthHandler = EnhancedServiceLoader.load(RegisterCheckAuthHandler.class);
    }

    @Override
    public void process(RpcMessageHandleContext ctx, RegisterTMRequest rpcMessage) throws Exception {
        RegisterTMResponse registerTMResponse = onRegTmMessage(ctx, rpcMessage);
        MessageReply messageReply = ctx.getMessageReply();
        if (null != messageReply) {
            messageReply.reply(registerTMResponse);
        }
    }

    private RegisterTMResponse onRegTmMessage(RpcMessageHandleContext ctx, RegisterTMRequest message) {
        String ipAndPort = NetUtil.toStringAddress(ctx.channel().remoteAddress());
        Version.putChannelVersion(ctx.channel().remoteAddress(), message.getVersion());
        boolean isSuccess = false;
        String errorInfo = StringUtils.EMPTY;
        try {
            if (null == checkAuthHandler || checkAuthHandler.regTransactionManagerCheckAuth(message)) {
                SeataChannelServerManager.registerTMChannel(message, ctx.channel());
                Version.putChannelVersion(ctx.channel().remoteAddress(), message.getVersion());
                isSuccess = true;
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("TM checkAuth for client:{},vgroup:{},applicationId:{} is OK",
                        ipAndPort, message.getTransactionServiceGroup(), message.getApplicationId());
                }
            } else {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn("TM checkAuth for client:{},vgroup:{},applicationId:{} is FAIL",
                            ipAndPort, message.getTransactionServiceGroup(), message.getApplicationId());
                }
            }
        } catch (Exception exx) {
            isSuccess = false;
            errorInfo = exx.getMessage();
            LOGGER.error("TM register fail, error message:{}", errorInfo);
        }
        RegisterTMResponse response = new RegisterTMResponse(isSuccess);
        if (StringUtils.isNotEmpty(errorInfo)) {
            response.setMsg(errorInfo);
        }
        if (isSuccess && LOGGER.isInfoEnabled()) {
            LOGGER.info("TM register success,message:{},channel:{},client version:{}", message, ctx.channel(),
                message.getVersion());
        }
        return response;
    }

}
