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

import io.seata.core.protocol.transaction.BranchCommitRequest;
import io.seata.core.protocol.transaction.BranchCommitResponse;
import io.seata.core.rpc.RemotingClient;
import io.seata.core.rpc.TransactionMessageHandler;
import io.seata.core.rpc.processor.MessageReply;
import io.seata.core.rpc.processor.RemotingProcessor;
import io.seata.core.rpc.processor.RpcMessageHandleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * process TC global commit command.
 * <p>
 * process message type:
 * {@link BranchCommitRequest}
 *
 * @author zhangchenghui.dev@gmail.com
 * @since 1.3.0
 */
public class RmBranchCommitProcessor implements RemotingProcessor<BranchCommitRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RmBranchCommitProcessor.class);

    private TransactionMessageHandler handler;

    private RemotingClient remotingClient;

    public RmBranchCommitProcessor(TransactionMessageHandler handler, RemotingClient remotingClient) {
        this.handler = handler;
        this.remotingClient = remotingClient;
    }

    @Override
    public void process(RpcMessageHandleContext ctx, BranchCommitRequest request) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("rm client handle branch commit process:" + request);
        }
        BranchCommitResponse branchCommitResponse = handleBranchCommit(request);
        MessageReply messageReply = ctx.getMessageReply();
        if (null != messageReply) {
            messageReply.reply(branchCommitResponse);
        }
    }

    private BranchCommitResponse handleBranchCommit(BranchCommitRequest branchCommitRequest) {
        BranchCommitResponse resultMessage;
        resultMessage = (BranchCommitResponse) handler.onRequest(branchCommitRequest, null);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("branch commit result:" + resultMessage);
        }
        return resultMessage;
    }
}
