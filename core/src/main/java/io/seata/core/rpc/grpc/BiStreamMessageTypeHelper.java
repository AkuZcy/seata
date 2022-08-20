package io.seata.core.rpc.grpc;

import java.util.HashMap;
import java.util.Map;

import io.seata.core.rpc.grpc.generated.GrpcRemoting;

/**
 * @author goodboycoder
 */
public class BiStreamMessageTypeHelper {
    private static final Map<GrpcRemoting.BiStreamMessageType, Class> MESSAGE_TYPE_CLASS_MAP = new HashMap<>();

    private static final Map<Class, GrpcRemoting.BiStreamMessageType> STREAM_MESSAGE_TYPE_MAP = new HashMap<>();
    static {
        MESSAGE_TYPE_CLASS_MAP.put(GrpcRemoting.BiStreamMessageType.TypeBranchCommit, io.seata.serializer.protobuf.generated.BranchCommitRequestProto.class);
        MESSAGE_TYPE_CLASS_MAP.put(GrpcRemoting.BiStreamMessageType.TypeBranchCommitResult, io.seata.serializer.protobuf.generated.BranchCommitResponseProto.class);
        MESSAGE_TYPE_CLASS_MAP.put(GrpcRemoting.BiStreamMessageType.TypeBranchRollback, io.seata.serializer.protobuf.generated.BranchRollbackRequestProto.class);
        MESSAGE_TYPE_CLASS_MAP.put(GrpcRemoting.BiStreamMessageType.TypeBranchRollBackResult, io.seata.serializer.protobuf.generated.BranchRollbackResponseProto.class);
        MESSAGE_TYPE_CLASS_MAP.put(GrpcRemoting.BiStreamMessageType.TYPERegisterRMRequest, io.seata.serializer.protobuf.generated.RegisterRMRequestProto.class);
        MESSAGE_TYPE_CLASS_MAP.put(GrpcRemoting.BiStreamMessageType.TYPERegisterRMResponse, io.seata.serializer.protobuf.generated.RegisterRMResponseProto.class);
        MESSAGE_TYPE_CLASS_MAP.put(GrpcRemoting.BiStreamMessageType.TypeRMUndoLogDelete, io.seata.serializer.protobuf.generated.UndoLogDeleteRequestProto.class);

        STREAM_MESSAGE_TYPE_MAP.put(io.seata.serializer.protobuf.generated.BranchCommitRequestProto.class, GrpcRemoting.BiStreamMessageType.TypeBranchCommit);
        STREAM_MESSAGE_TYPE_MAP.put(io.seata.serializer.protobuf.generated.BranchCommitResponseProto.class, GrpcRemoting.BiStreamMessageType.TypeBranchCommitResult);
        STREAM_MESSAGE_TYPE_MAP.put(io.seata.serializer.protobuf.generated.BranchRollbackRequestProto.class, GrpcRemoting.BiStreamMessageType.TypeBranchRollback);
        STREAM_MESSAGE_TYPE_MAP.put(io.seata.serializer.protobuf.generated.BranchRollbackResponseProto.class, GrpcRemoting.BiStreamMessageType.TypeBranchRollBackResult);
        STREAM_MESSAGE_TYPE_MAP.put(io.seata.serializer.protobuf.generated.RegisterRMRequestProto.class, GrpcRemoting.BiStreamMessageType.TYPERegisterRMRequest);
        STREAM_MESSAGE_TYPE_MAP.put(io.seata.serializer.protobuf.generated.RegisterRMResponseProto.class, GrpcRemoting.BiStreamMessageType.TYPERegisterRMResponse);
        STREAM_MESSAGE_TYPE_MAP.put(io.seata.serializer.protobuf.generated.UndoLogDeleteRequestProto.class, GrpcRemoting.BiStreamMessageType.TypeRMUndoLogDelete);
    }

    public static Class getBiStreamMessageClassType(GrpcRemoting.BiStreamMessageType messageType) {
        return MESSAGE_TYPE_CLASS_MAP.get(messageType);
    }

    public static GrpcRemoting.BiStreamMessageType getBiStreamMessageTypeByClass(Class clazz) {
        return STREAM_MESSAGE_TYPE_MAP.get(clazz);
    }
}
