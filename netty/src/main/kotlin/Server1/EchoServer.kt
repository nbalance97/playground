package Server1

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress

class EchoServer {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val port: Int = 9000

    fun start() {
        logger.info("Server Loading...")
        val inboundHandlerAdapter = InboundHandlerAdapter()
        val nioEventLoopGroup = NioEventLoopGroup()

        try {
            val bootstrap = ServerBootstrap()
            bootstrap.apply {
                group(nioEventLoopGroup)
                channel(NioServerSocketChannel::class.java)
                localAddress(InetSocketAddress(port))
                childHandler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline().addLast(inboundHandlerAdapter)
                    }
                })
            }

            val channelFuture = bootstrap.bind().sync()
            logger.info("Server started at port $port")
            channelFuture.channel().closeFuture().sync()
            logger.info("channel end completed")
        } finally {
            nioEventLoopGroup.shutdownGracefully().sync()
        }
    }
}
