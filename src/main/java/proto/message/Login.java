// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: login.proto

package proto.message;

public final class Login {
  private Login() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_proto_message_CSLogin1000_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_proto_message_CSLogin1000_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_proto_message_SCLogin1001_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_proto_message_SCLogin1001_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_proto_message_HeartBeat1_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_proto_message_HeartBeat1_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\013login.proto\022\rproto.message\"0\n\013CSLogin1" +
      "000\022\017\n\007account\030\001 \001(\t\022\020\n\010password\030\002 \001(\t\"." +
      "\n\013SCLogin1001\022\016\n\006status\030\001 \001(\005\022\017\n\007message" +
      "\030\002 \001(\t\"\014\n\nHeartBeat1B\002P\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_proto_message_CSLogin1000_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_proto_message_CSLogin1000_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_proto_message_CSLogin1000_descriptor,
        new java.lang.String[] { "Account", "Password", });
    internal_static_proto_message_SCLogin1001_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_proto_message_SCLogin1001_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_proto_message_SCLogin1001_descriptor,
        new java.lang.String[] { "Status", "Message", });
    internal_static_proto_message_HeartBeat1_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_proto_message_HeartBeat1_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_proto_message_HeartBeat1_descriptor,
        new java.lang.String[] { });
  }

  // @@protoc_insertion_point(outer_class_scope)
}