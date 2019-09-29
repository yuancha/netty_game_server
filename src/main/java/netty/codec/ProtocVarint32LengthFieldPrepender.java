package netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.google.protobuf.CodedOutputStream;

/**
 * An encoder that prepends the the Google Protocol Buffers
 * <a href="http://code.google.com/apis/protocolbuffers/docs/encoding.html#varints">Base
 * 128 Varints</a> integer length field.  For example:
 * <pre>
 * BEFORE DECODE (300 bytes)       AFTER DECODE (302 bytes)
 * +---------------+               +--------+---------------+
 * | Protobuf Data |-------------->| Length | Protobuf Data |
 * |  (300 bytes)  |               | 0xAC02 |  (300 bytes)  |
 * +---------------+               +--------+---------------+
 * </pre> *
 *
 * @see CodedOutputStream
 */
@Sharable
public class ProtocVarint32LengthFieldPrepender extends MessageToByteEncoder<ByteBuf> {

    @Override
    protected void encode(
            ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        int bodyLen = msg.readableBytes();
        int headerLen = CodedOutputStream.computeRawVarint32Size(bodyLen);
        out.ensureWritable(ProtocCodecUtil.FIRST_TAG_LENGTH + headerLen + bodyLen);
        out.writeShort(ProtocCodecUtil.FIRST_TAG);
        CodedOutputStream headerOut =
                CodedOutputStream.newInstance(new ByteBufOutputStream(out), headerLen);
        headerOut.writeRawVarint32(bodyLen);
        headerOut.flush();

        out.writeBytes(msg, msg.readerIndex(), bodyLen);
    }
}