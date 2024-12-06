package kr.co.ws.service;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RedisSubscriberService implements MessageListener {
	private final Map<String, Set<Channel>> roomChannelsMap = new ConcurrentHashMap<>();
	private final Set<String> subscribedChannels = ConcurrentHashMap.newKeySet();

	@Getter
	private final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	public void addChannel(String roomId, Channel channel) {
		roomId = "room-" + roomId;
		roomChannelsMap.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(channel);
		log.info("채널 추가됨 - {}", roomId);
	}

	public void removeChannel(String roomId, Channel channel) {
		log.info("채널 제거됨 - {}", roomId);
		Set<Channel> channels = roomChannelsMap.get(roomId);
		if (channels != null) {
			channels.remove(channel);
			if (channels.isEmpty()) {
				roomChannelsMap.remove(roomId);
				subscribedChannels.remove(roomId);
			}
		}
	}

	public void addChannel(Channel channel) {
		channels.add(channel);
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String roomId = new String(message.getChannel(), StandardCharsets.UTF_8);
		String messageText = new String(message.getBody(), StandardCharsets.UTF_8);

		log.info("수신된 메시지 [{}]: {}", roomId, messageText);

		Set<Channel> channels = roomChannelsMap.get(roomId);

		log.info("채널 맵 상태: {}", roomChannelsMap);
		log.info("현재 방 ID: {}", roomId);

		if (channels != null) {
			for (Channel channel : channels) {
				if (channel.isActive()) {
					log.info("메시지 전송 - {}", messageText);
					channel.writeAndFlush(new TextWebSocketFrame(messageText));
				} else {
					removeChannel(roomId, channel);
				}
			}
		}
	}
}
