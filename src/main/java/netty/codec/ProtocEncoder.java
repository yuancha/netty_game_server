package netty.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import com.google.protobuf.MessageLiteOrBuilder;
import router.MessageMapper;

public class ProtocEncoder extends MessageToMessageEncoder<MessageLiteOrBuilder> {
	private MessageMapper mapper;
	
	public ProtocEncoder(MessageMapper mapper) {
		super();
		this.mapper = mapper;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, MessageLiteOrBuilder msg,
			List<Object> out) throws Exception {
		ProtocCodecUtil.encode(ctx, msg, out, mapper);
	}

}
