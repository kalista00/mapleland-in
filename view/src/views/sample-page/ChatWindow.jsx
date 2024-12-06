import { Box, Button, TextField, Typography } from "@mui/material";
import { useEffect, useRef, useState } from "react";
import { getCookie } from "utils/cookies"; // 유틸리티 함수 import

const ChatWindow = ({ roomId }) => {
  const [channelId, setChannelId] = useState(null);
  const [messages, setMessages] = useState([]);
  const [messageInput, setMessageInput] = useState("");
  const [nickname, setNickname] = useState(""); // nickname 상태 추가
  const socketRef = useRef(null);
  const messagesEndRef = useRef(null);

  useEffect(() => {
    // 컴포넌트가 마운트될 때 nickname을 쿠키에서 가져와 설정
    const nick = getCookie("nickname");
    if (nick) {
      const decodedNick = decodeURIComponent(nick);
      setNickname(decodedNick);
    } else {
      setNickname("Anonymous"); // nickname 쿠키가 없을 경우 기본값 설정
    }
  }, []);

  useEffect(() => {
    const url = import.meta.env.VITE_APP_WS_URL;
    socketRef.current = new WebSocket(`${url}/ws`);
    socketRef.current.onopen = () => {
      console.log("WebSocket 연결이 성공했습니다.");
      const joinMessage = {
        type: "JOIN_ROOM",
        roomId: roomId
      };
      socketRef.current.send(JSON.stringify(joinMessage));
    };

    socketRef.current.onmessage = (event) => {
      const message = event.data;
      console.log("수신한 메시지:", message);
      if (message.startsWith("CHANNEL_ID:")) {
        const id = message.replace("CHANNEL_ID:", "");
        setChannelId(id);
        console.log("Your channel ID:", id);
      } else {
        try {
          const parsedMessage = JSON.parse(message);
          if (parsedMessage.roomId === roomId) {
            setMessages((prevMessages) => [...prevMessages, parsedMessage]);
          }
        } catch (error) {
          console.error("메시지 파싱 오류:", error);
        }
      }
    };

    socketRef.current.onclose = () => {
      console.log("WebSocket 연결이 종료되었습니다.");
    };

    socketRef.current.onerror = (error) => {
      console.error("WebSocket 오류:", error);
    };

    return () => {
      socketRef.current.close();
    };
  }, [roomId]);

  useEffect(() => {
    // 메시지가 업데이트될 때마다 스크롤을 맨 아래로 이동
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const sendMessage = () => {
    if (socketRef.current && socketRef.current.readyState === WebSocket.OPEN) {
      const message = {
        type: "CHAT_MESSAGE",
        roomId: roomId,
        sender: nickname, // 쿠키에서 가져온 nickname 사용
        text: messageInput
      };
      socketRef.current.send(JSON.stringify(message));
      setMessageInput("");
    }
  };

  return (
    <Box>
      {channelId ? <Typography>Your channel ID: {channelId}</Typography> : <Typography>채널 ID를 기다리는 중...</Typography>}
      <Box sx={{ maxHeight: 300, overflowY: "auto", mb: 2 }}>
        {messages.map((message, index) => (
          <Typography key={index} variant="body2" sx={{ mb: 1 }}>
            <strong>{message.sender}:</strong> {message.text}
          </Typography>
        ))}
        {/* 메시지 끝부분에 대한 참조를 위한 요소 추가 */}
        <div ref={messagesEndRef} />
      </Box>
      <Box display="flex">
        <TextField
          variant="outlined"
          fullWidth
          placeholder="메시지를 입력하세요..."
          value={messageInput}
          onChange={(e) => setMessageInput(e.target.value)}
          onKeyPress={(e) => {
            if (e.key === "Enter") sendMessage();
          }}
        />
        <Button variant="contained" color="primary" sx={{ ml: 1 }} onClick={sendMessage} disabled={!messageInput.trim()}>
          전송
        </Button>
      </Box>
    </Box>
  );
};

export default ChatWindow;
