package netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.List;

import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import router.MessageMapper;

public class ProtocCodecUtil {
	private static Logger logger = LoggerFactory.getLogger(ProtocCodecUtil.class.getName());
	private static final boolean HAS_PARSER;
	/**
	 * 消息分隔符
	 */
	public static final short FIRST_TAG = 127;
	/**
	 * 消息分割占用符字节数
	 */
	public static final int FIRST_TAG_LENGTH = 2;
	/**
	 * 消息号占用字节数
	 */
	private static final int MSG_TYPE_LENGTH = 4;
	static {
		boolean hasParser = false;
		try {
			// MessageLite.getParsetForType() is not available until protobuf
			// 2.5.0.
			MessageLite.class.getDeclaredMethod("getParserForType");
			hasParser = true;
		} catch (Throwable t) {
			// Ignore
		}

		HAS_PARSER = hasParser;
	}

	public static void encode(ChannelHandlerContext ctx, MessageLiteOrBuilder msg, List<Object> out, MessageMapper mapper) throws Exception {
		if (msg instanceof MessageLite) {
			int msgType = mapper.getTypeByMsg(((MessageLite) msg));
			final byte[] result = toBytes(msgType, ((MessageLite) msg));
			out.add(Unpooled.wrappedBuffer(result));
			return;
		}
		if (msg instanceof MessageLite.Builder) {
			MessageLite msgLite = ((MessageLite.Builder) msg).build();
			int msgType = mapper.getTypeByMsg(msgLite);
			final byte[] result = toBytes(msgType, msgLite);
			out.add(Unpooled.wrappedBuffer(result));
		}
	}

	private static byte[] toBytes(int msgType,
			MessageLite msg) throws IOException {
		int serializedSize = msg.getSerializedSize();
		final byte[] result = new byte[serializedSize + MSG_TYPE_LENGTH];
//		result[0] = (byte) (msgType >>> 8);
//		result[1] = (byte) msgType;
		result[0] = (byte) (msgType >>> 24);
		result[1] = (byte) (msgType >>> 16);
		result[2] = (byte) (msgType >>> 8);
		result[3] = (byte) msgType;
		final CodedOutputStream output = CodedOutputStream.newInstance(result, MSG_TYPE_LENGTH, serializedSize);
		msg.writeTo(output);
		return result;
	}
	
	public static void decode(ChannelHandlerContext ctx, ByteBuf msg,
			List<Object> out, MessageMapper mapper) throws Exception {
		int msgType = msg.readInt();
		MessageLite prototype = mapper.getMsgByType(msgType);
		decode(ctx, msg, out, prototype);
	}
	
	public static void decode(ChannelHandlerContext ctx, ByteBuf msg,
			List<Object> out, MessageLite prototype) throws Exception {
		decode(ctx, msg, out, prototype, null);
	}
	
	public static MessageLite getMsg(MessageMapper mapper, short msgId, ByteString b) {
		MessageLite prototype = mapper.getMsgByType(msgId);
		if(prototype == null) {
			logger.debug("Not found msgId=" + msgId);
			return null;
		}
		byte[] array = b.toByteArray();
		MessageLite result = null;
		try {
			result = prototype.getParserForType().parseFrom(array, 0, array.length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static void decode(ChannelHandlerContext ctx, ByteBuf msg,
			List<Object> out, MessageLite prototype,
			ExtensionRegistryLite extensionRegistry) throws Exception {
		final byte[] array;
		final int offset;
		final int length = msg.readableBytes();
		if (msg.hasArray()) {
			array = msg.array();
			offset = msg.arrayOffset() + msg.readerIndex();
		} else {
			array = new byte[length];
			msg.getBytes(msg.readerIndex(), array, 0, length);
			offset = 0;
		}

		if (extensionRegistry == null) {
			if (HAS_PARSER) {
				out.add(prototype.getParserForType().parseFrom(array, offset, length));
			} else {
				out.add(prototype.newBuilderForType().mergeFrom(array, offset, length).build());
			}
		} else {
			if (HAS_PARSER) {
				out.add(prototype.getParserForType().parseFrom(array, offset, length, extensionRegistry));
			} else {
				out.add(prototype.newBuilderForType()
						.mergeFrom(array, offset, length, extensionRegistry)
						.build());
			}
		}
	}
}
