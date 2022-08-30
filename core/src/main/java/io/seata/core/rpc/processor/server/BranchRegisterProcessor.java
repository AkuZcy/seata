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

import io.seata.core.protocol.transaction.BranchRegisterRequest;
import io.seata.core.protocol.transaction.BranchRegisterResponse;
import io.seata.core.rpc.RpcContext;
import io.seata.core.rpc.SeataChannelServerManager;
import io.seata.core.rpc.TransactionMessageHandler;
import io.seata.core.rpc.processor.RpcMessageHandleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author goodboycoder
 */
public class BranchRegisterProcessor extends BaseServerOnRequestProcessor<BranchRegisterRequest, BranchRegisterResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BranchRegisterProcessor.class);

    public BranchRegisterProcessor(TransactionMessageHandler transactionMessageHandler) {
        super(transactionMessageHandler);
    }

    @Override
    protected BranchRegisterResponse onRequestMessage(RpcMessageHandleContext ctx, BranchRegisterRequest branchRegisterRequest) {
        RpcContext rpcContext = SeataChannelServerManager.getContextFromIdentified(ctx.channel());
        return (BranchRegisterResponse) transactionMessageHandler.onRequest(branchRegisterRequest, rpcContext);
    }
}
