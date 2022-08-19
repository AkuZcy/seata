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
package io.seata.core.rpc.processor.client;

import io.seata.core.protocol.transaction.BranchRollbackRequest;
import io.seata.core.protocol.transaction.BranchRollbackResponse;
import io.seata.core.rpc.RemotingClient;
import io.seata.core.rpc.TransactionMessageHandler;
import io.seata.core.rpc.processor.MessageReply;
import io.seata.core.rpc.processor.RemotingProcessor;
import io.seata.core.rpc.processor.RpcMessageHandleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * process TC do global rollback command.
 * <p>
 * process message type:
 * {@link BranchRollbackRequest}
 *
 * @author zhangchenghui.dev@gmail.com
 * @since 1.3.0
 */
public class RmBranchRollbackProcessor implements RemotingProcessor<BranchRollbackRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RmBranchRollbackProcessor.class);

    private TransactionMessageHandler handler;

    private RemotingClient remotingClient;

    public RmBranchRollbackProcessor(TransactionMessageHandler handler, RemotingClient remotingClient) {
        this.handler = handler;
        this.remotingClient = remotingClient;
    }

    @Override
    public void process(RpcMessageHandleContext ctx, BranchRollbackRequest request) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("rm handle branch rollback process:" + request);
        }
        BranchRollbackResponse branchRollbackResponse = handleBranchRollback(request);
        MessageReply messageReply = ctx.getMessageReply();
        if (null != messageReply) {
            messageReply.reply(branchRollbackResponse);
        }
    }

    private BranchRollbackResponse handleBranchRollback(BranchRollbackRequest branchRollbackRequest) {
        BranchRollbackResponse resultMessage;
        resultMessage = (BranchRollbackResponse) handler.onRequest(branchRollbackRequest, null);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("branch rollback result:" + resultMessage);
        }
        return resultMessage;
    }
}
