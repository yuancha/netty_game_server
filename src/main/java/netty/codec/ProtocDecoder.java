package netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import router.MessageMapper;

import java.util.List;

public class ProtocDecoder extends MessageToMessageDecoder<ByteBuf> {

	private MessageMapper mapper;
	
	public ProtocDecoder(MessageMapper mapper) {
		super();
		this.mapper = mapper;
	}


	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg,
			List<Object> out) throws Exception {
		ProtocCodecUtil.decode(ctx, msg, out, mapper);
	}

}
