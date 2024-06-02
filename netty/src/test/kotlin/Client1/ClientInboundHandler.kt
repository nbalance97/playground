package Client1

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory

class ClientInboundHandler : SimpleChannelInboundHandler<ByteBuf>() {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun channelActive(ctx: ChannelHandlerContext) {
        logger.info("channelActive")
        // ㅡㅡ ㅋㅋ 이거 Unpooled.copiedBuffer 안써주면 잘 안보내진다..
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello Server".toByteArray(Charsets.UTF_8)))
    }

    override fun channelRead0(channelHandlerContext: ChannelHandlerContext, buf: ByteBuf) {
        logger.info("Client received: ${buf.toString(Charsets.UTF_8)}")
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}
