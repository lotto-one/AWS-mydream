import React, { useState, useEffect, useRef } from "react";
import "./OTO_detail.css";
import { useLocation } from "react-router-dom";
import axios from "axios";

interface Message {
  chatdiv: string;
  name: string;
  profileImage: string;
  content: string;
  chatdt: string;
}

const OTO_detail: React.FC = () => {
  const [ws, setWs] = useState<WebSocket | null>(null);
  const [newMessage, setNewMessage] = useState("");
  const [messages, setMessages] = useState<Message[]>([]);
  const [roomnum, setRoomnum] = useState<number | null>(null);
  const [UIMG, setUIMG] = useState("default.png");
  const [unick, setUnick] = useState("유저이름");
  const [CIMG, setCIMG] = useState("default.png");
  const [cname, setCname] = useState("컨턴턴트 이름");
  const chatContainerRef = useRef<HTMLDivElement>(null);
  const [cnsno] = useState(localStorage.getItem("cnsno"));
  // const [chtno] = useState(2);
  const location = useLocation();

  // location.state에서 chtno 값 추출
  const { chtno } = location.state || {}; // location.state가 없을 경우를 대비해 기본값 {}를 사용

  const chattype = (type: string) => (type === "1" ? "user" : "consultant");

  const chatdetail = async () => {
    const memnoData = new FormData();
    console.log(
      `${process.env.REACT_APP_BACK_END_URL}/consultant/chat/chatdetail?chtno=${chtno}`
    );
    try {
      const res = await axios.get(
        `${process.env.REACT_APP_BACK_END_URL}/consultant/chat/chatdetail?chtno=${chtno}`,
        {
          headers: { "Content-Type": "application/json" },
        }
      );
      setMessages(res.data);
      console.log(messages);
      await chatprofile(memnoData);
      scrollToEnd();
    } catch (error) {
      console.error("Error fetching chat details:", error);
    }
  };

  const handleImageError = (
    event: React.SyntheticEvent<HTMLImageElement, Event>
  ) => {
    event.currentTarget.src = "/img/admin_img/noimg.png"; // 대체 이미지 경로
  };

  const chatprofile = async (memnoData: FormData) => {
    try {
      const res = await axios.get(
        `${process.env.REACT_APP_BACK_END_URL}/consultant/chat/profile?chtno=${chtno}`,
        {
          headers: { "Content-Type": "application/json" },
        }
      );
      setUIMG(res.data.uimg);
      console.log("res.data.UIMG" + res.data.uimg);
      setCIMG(res.data.cimg);
      setUnick(res.data.unick);
      setCname(res.data.cname);
      console.log(res.data);
      setMessages((prevMessages) =>
        prevMessages.map((message) => ({
          ...message,
          name: message.chatdiv === "1" ? "user" : "consulte",
          profileImage:
            message.chatdiv === "1"
              ? `http://localhost:80/mydream/${res.data.uimg}`
              : `http://localhost:81/yourdream/uploads/${res.data.cimg}`,
        }))
      );
    } catch (error) {
      console.error("Error fetching chat profile:", error);
    }
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = ("0" + (date.getMonth() + 1)).slice(-2);
    const day = ("0" + date.getDate()).slice(-2);
    const hours = ("0" + date.getHours()).slice(-2);
    const minutes = ("0" + date.getMinutes()).slice(-2);

    return `${year}-${month}-${day} ${hours}:${minutes}`;
  };

  const connect = () => {
    const chatmsg = `${cnsno}-${chtno}`;
    console.log(process.env.VUE_APP_Web_Socket_URL);

    const socket = new WebSocket(
      `${process.env.REACT_APP_Web_Socket_URL}/ws/chat`,
      chatmsg
    );
    socket.onmessage = (event) => onMessage(event);
    socket.onopen = () => onOpen();
    socket.onerror = (error) => onError(error);
    socket.onclose = () => onClose();
    setWs(socket);
    //chatdetail();
  };

  const sendMessage = () => {
    if (newMessage.trim() !== "") {
      const messageObject = {
        chtno: `${chtno}`,
        content: newMessage,
        chatdiv: "2",
      };
      ws?.send(JSON.stringify(messageObject));
      setMessages((prevMessages) => [
        ...prevMessages,

        {
          chatdiv: "2",
          name: "consultant",
          profileImage: `http://localhost:81/yourdream/uploads/${CIMG}`,
          content: newMessage,
          chatdt: getCurrentDateTime(),
        },
      ]);

      setNewMessage("");
    }
    scrollToEnd();
  };

  const onMessage = (event: MessageEvent) => {
    // if (!roomnum) {
    //   setRoomnum(parseInt(event.data, 10));
    //   return;
    // }
    console.log("onMessage작동");
    console.log("event.data" + event.data);
    const message: Message = JSON.parse(event.data);
    console.log("message" + message);
    setMessages((prevMessages) => [
      ...prevMessages,
      {
        chatdiv: "1",
        name: "user",
        profileImage: `http://localhost:80/mydream/${UIMG}`,
        content: message.content,
        chatdt: getCurrentDateTime(),
      },
    ]);
    scrollToEnd();
  };

  const getCurrentDateTime = () => {
    const now = new Date();
    const day = now.getDate().toString().padStart(2, "0");
    const month = (now.getMonth() + 1).toString().padStart(2, "0");
    const year = now.getFullYear();
    const hours = now.getHours().toString().padStart(2, "0");
    const minutes = now.getMinutes().toString().padStart(2, "0");
    return `${year}-${month}-${day} ${hours}:${minutes}`;
  };

  const onOpen = () => {
    console.log("Connected to the WebSocket server.");
  };

  const onError = (error: Event) => {
    console.error("WebSocket error:", error);
  };

  const onClose = () => {
    console.log("Disconnected from the WebSocket server.");
  };

  useEffect(() => {
    connect();
    chatdetail();
    return () => {
      ws?.close();
    };
  }, []);
  const scrollToEnd = () => {
    if (chatContainerRef.current) {
      chatContainerRef.current.scrollTop =
        chatContainerRef.current.scrollHeight;
    }
  };

  return (
    <div className="container">
      <div className="OTO-con-1">
        <div className="OTO-con-2">
          <div className="OTO-con-3">
            <h3 style={{ fontWeight: "bold" }}>면접자 1:1 상담</h3>
          </div>
        </div>
      </div>
      <div className="OTO-notice">
        📢 답변 시간은 컨설턴트 상황에 따라 다르게 소요되며 욕설 및 비방을 한
        사용자는 경고 조치 없이 불이익을 받을 수 있습니다.
      </div>
      <div className="OTO-con">
        <div className="interviewRes-counsel">
          <div className="interviewRes-imgbox">
            <img
              src={`http://localhost:80/mydream/${UIMG}`}
              alt="noimage"
              className="interviewRes-img"
              onError={handleImageError}
            />
          </div>
          <div className="interviewRes-text">
            <h5 className="interviewRes-h5">면접자 : {unick}</h5>
          </div>
        </div>
        <div className="chat-container scrollable-div" ref={chatContainerRef}>
          {messages.map((message, index) => (
            <div
              key={index}
              className={`chat-message ` + chattype(message.chatdiv)}
            >
              <img
                src={
                  // `${process.env.REACT_APP_VUE_FRONT_END_URL}` +
                  message.profileImage
                }
                alt={message.name}
                className="profile-image"
                onError={handleImageError}
              />
              <div className="message-info">
                <div className={`message-text ` + chattype(message.chatdiv)}>
                  {message.content}
                </div>
                <div className={"message-time " + chattype(message.chatdiv)}>
                  {formatDate(message.chatdt)}
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
      <div className="OTO-userWrite">
        <input
          value={newMessage}
          onChange={(e) => setNewMessage(e.target.value)}
          onKeyUp={(e) => e.key === "Enter" && sendMessage()}
          type="text"
          className="user-input"
          placeholder="메시지를 입력하세요..."
        />
        <button onClick={sendMessage} className="send-button">
          전송
        </button>
      </div>
    </div>
  );
};

export default OTO_detail;
