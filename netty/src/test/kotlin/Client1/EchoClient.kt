package Client1
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress

class EchoClient {

    private val host = "127.0.0.1"
    private val port = 9000

    private val logger = LoggerFactory.getLogger(javaClass)

    fun start() {
        val eventLoopGroup = NioEventLoopGroup()

        try {
            val bootstrap = Bootstrap()
            bootstrap.apply {
                group(eventLoopGroup)
                channel(NioSocketChannel::class.java)
                remoteAddress(InetSocketAddress(host, port))
                handler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline().addLast(ClientInboundHandler())
                    }
                })
            }

            logger.info("Client Loading...");
            val channelFuture = bootstrap.connect().sync()
            channelFuture.channel().closeFuture().sync()
            logger.info("Client Complete...");
        } finally {
            eventLoopGroup.shutdownGracefully().sync()
        }
    }
}
