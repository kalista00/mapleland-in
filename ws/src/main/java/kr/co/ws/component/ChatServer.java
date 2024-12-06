package kr.co.ws.component;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import kr.co.ws.handler.ChatServerHandler;
import kr.co.ws.service.RedisSubscriberService;

@Component
public class ChatServer {

	private final RedisSubscriberService redisSubscriberService;
	private final RedisTemplate<String, String> redisTemplate;

	public ChatServer(RedisSubscriberService redisSubscriberService, RedisTemplate<String, String> redisTemplate) {
		this.redisSubscriberService = redisSubscriberService;
		this.redisTemplate = redisTemplate;
	}

	public void start(int port) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap bootstrap = new ServerBootstrap().group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) {
							CorsConfig corsConfig = CorsConfigBuilder.forAnyOrigin().allowNullOrigin()
									.allowCredentials().build();

							ch.pipeline().addLast(new HttpServerCodec(), new HttpObjectAggregator(65536),
									new CorsHandler(corsConfig), new WebSocketServerProtocolHandler("/ws"),
									new ChatServerHandler(redisSubscriberService, redisTemplate));
						}
					});

			ChannelFuture future = bootstrap.bind(port).sync();
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
