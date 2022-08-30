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
package io.seata.core.rpc.grpc.service;

import com.google.protobuf.Message;
import io.grpc.stub.StreamObserver;
import io.seata.core.protocol.HeartbeatMessage;
import io.seata.core.rpc.SeataChannel;
import io.seata.core.rpc.grpc.GrpcRemotingServer;
import io.seata.core.rpc.grpc.GrpcSeataChannel;
import io.seata.core.rpc.grpc.ProtoTypeConvertHelper;
import io.seata.core.rpc.grpc.generated.HealthCheckServiceGrpc;
import io.seata.core.rpc.processor.RpcMessageHandleContext;
import io.seata.serializer.protobuf.generated.HeartbeatMessageProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.seata.core.rpc.grpc.ContextKeyConstants.CUR_CONNECTION;
import static io.seata.core.rpc.grpc.ContextKeyConstants.CUR_CONNECT_ID;

/**
 * @author goodboycoder
 */
public class HealthCheckService extends HealthCheckServiceGrpc.HealthCheckServiceImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckService.class);

    private final GrpcRemotingServer remotingServer;

    public HealthCheckService(GrpcRemotingServer remotingServer) {
        this.remotingServer = remotingServer;
    }

    @Override
    public void heartbeatChecker(HeartbeatMessageProto request, StreamObserver<HeartbeatMessageProto> responseObserver) {
        HeartbeatMessage requestModel = (HeartbeatMessage) ProtoTypeConvertHelper.convertToModel(request);
        RpcMessageHandleContext handleContext = buildHandleContext(responseObserver);
        handleContext.setMessageReply(response -> {
            if (!(response instanceof HeartbeatMessage)) {
                LOGGER.warn("[GRPC]wrong response type, need {} but actually {}", HeartbeatMessage.class, response.getClass());
                return;
            }
            try {
                responseObserver.onNext((HeartbeatMessageProto) ProtoTypeConvertHelper.convertToProto(response));
                responseObserver.onCompleted();
            } catch (Exception e) {
                LOGGER.warn("[GRPC]fail to send response, req:{}, resp:{}", request, response);
            }
        });
        remotingServer.processMessage(handleContext, requestModel);
    }

    public RpcMessageHandleContext buildHandleContext(StreamObserver<? extends Message> responseObserver) {
        SeataChannel curChannel = new GrpcSeataChannel(CUR_CONNECT_ID.get(), CUR_CONNECTION.get(), responseObserver);
        return new RpcMessageHandleContext(curChannel);
    }
}
