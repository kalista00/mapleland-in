package kr.co.ws.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import kr.co.ws.service.RedisSubscriberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ChatServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	private final RedisSubscriberService redisSubscriberService;
	private final RedisTemplate<String, String> redisTemplate;

	private ObjectMapper om = new ObjectMapper();
	private AttributeKey<String> ROOM_ID_ATTR = AttributeKey.valueOf("roomId");

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		redisSubscriberService.addChannel(ctx.channel());
		log.info("### test connected - {}", ctx.channel().id().asLongText());

		ctx.channel().writeAndFlush(new TextWebSocketFrame("CHANNEL_ID:" + ctx.channel().id().asLongText()));
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
		String message = msg.text();
		try {
			Map<String, Object> messageMap = om.readValue(message, Map.class);

			String type = (String) messageMap.get("type");
			if ("JOIN_ROOM".equals(type)) {
				String roomId = (String) messageMap.get("roomId");
				if (roomId != null) {
					ctx.channel().attr(ROOM_ID_ATTR).set(roomId);
					redisSubscriberService.addChannel(roomId, ctx.channel());

					Long userCount = redisTemplate.opsForValue().increment("room:" + roomId + ":userCount");
					log.info("User joined room {}: current user count is {}", roomId, userCount);

					Map<String, Object> countMessageMap = new HashMap<>();
					countMessageMap.put("type", "USER_COUNT");
					countMessageMap.put("roomId", roomId);
					countMessageMap.put("userCount", userCount);
					String countMessageJson = om.writeValueAsString(countMessageMap);
					redisTemplate.convertAndSend("room-" + roomId, countMessageJson);
				}
			} else if ("CHAT_MESSAGE".equals(type)) {
				String roomId = ctx.channel().attr(ROOM_ID_ATTR).get();
				if (roomId != null) {
					messageMap.put("roomId", roomId);
					String updatedMessage = om.writeValueAsString(messageMap);
					redisTemplate.convertAndSend("room-" + roomId, updatedMessage);
				} else {
					log.warn("No roomId set for channel {}", ctx.channel().id().asLongText());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) {
		log.info("channel removed - {}", ctx.channel().id().asLongText());
		String roomId = ctx.channel().attr(ROOM_ID_ATTR).get();
		if (roomId != null) {
			redisSubscriberService.removeChannel(roomId, ctx.channel());

			Long userCount = redisTemplate.opsForValue().decrement("room:" + roomId + ":userCount");
			log.info("User left room {}: current user count is {}", roomId, userCount);

			Map<String, Object> messageMap = new HashMap<>();
			messageMap.put("type", "USER_COUNT");
			messageMap.put("roomId", roomId);
			messageMap.put("userCount", userCount);
			try {
				String messageJson = om.writeValueAsString(messageMap);
				redisTemplate.convertAndSend("room-" + roomId, messageJson);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
