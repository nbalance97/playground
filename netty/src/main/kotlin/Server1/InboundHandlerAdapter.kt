package Server1

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.slf4j.LoggerFactory

@Sharable
class InboundHandlerAdapter : ChannelInboundHandlerAdapter() {

    private val logger = LoggerFactory.getLogger(javaClass)

    // channelRead() -> Message가 들어올 때마다 호출되는 메소드

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val inbound = msg as ByteBuf
        logger.info("Server received: ${inbound.toString(Charsets.UTF_8)}")
        ctx.write(inbound)
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        logger.info("Read Completed")
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
            .addListener(ChannelFutureListener.CLOSE)
    }

    @Deprecated("Deprecated in Java")
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}
